package compiler;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Stack;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Parser {

    private int expStart;
    private int expEnd;
    private final LexicalAnalyzer lexicalAnalyzer;
    private int iterator;
    private Token token;
    private final ArrayList<String> idList;
    private int listCount;
    private String regA;
    private int tMax = 0;
    private int l = 1;
    private String exp;
    private boolean append;
    private final HashSet<String> varList;

    public Parser(String fileName) {
        this.lexicalAnalyzer = new LexicalAnalyzer(fileName);
        this.lexicalAnalyzer.analyze();
        this.iterator = 0;
        this.idList = new ArrayList<>();
        this.listCount = 0;
        this.append = false;
        this.regA = "";
        this.varList = new HashSet<>();
    }

    public void parse() {
        boolean found = false;
        token = lexicalAnalyzer.getTokens().get(iterator);
        if (token.type.equals(1)) {                                             // Handling "PROGRAM"
            token = lexicalAnalyzer.getTokens().get(++iterator);
            if (token.type.equals(16)) {                                        // Handling Program Name
                write(token.content, "START", "000000");
                write("", "EXTREF", "XREAD,XWRITE,LPUSH,LPOP");
                write("", "STL", "RETADR");
                write("", "J", "{EXADDR}");
                token = lexicalAnalyzer.getTokens().get(++iterator);
                if (token.type.equals(2)) {                                     // Handling "VAR"
                    token = lexicalAnalyzer.getTokens().get(++iterator);
                    if (parseIdList()) {                                        // Adding ID List code to .asm File
                        idList.forEach((id) -> {
                            write(id, "RESW", "1");
                            varList.add(id);
                        });
                        idList.clear();
                        listCount = 0;
                        if (token.type.equals(3)) {
                            if (parseStmtList()) {
                                for (int i = 1; i <= tMax; i++) {
                                    write("", "RESW", "T" + (i));
                                }
                                if (token.type.equals(5)) {
                                    write("", "END", "");
                                    found = true;
                                }
                            }
                        }
                    }
                }
            }
        }
        if (!found) {
            Error();
        }
    }
    
    private boolean parseStmtList() {
        boolean found = true;
        do {
            token = lexicalAnalyzer.getTokens().get(++iterator);
            if (!parseStmt()) {
                found = false;
            }
        } while (token.type.equals(11));

        return found;
    }

    private boolean parseStmt() {
        boolean found = false;
        if (parseRead()) {
            write("", "+JSUB", "XREAD");
            write("", "WORD", String.valueOf(listCount));
            idList.forEach((id) -> {
                if (!varList.contains(id)) {
                    Error();
                }
                write("", "WORD", id);
            });
            idList.clear();
            listCount = 0;
            found = true;
        } else if (parseWrite()) {
            write("", "+JSUB", "XWRITE");
            write("", "WORD", String.valueOf(listCount));
            idList.forEach((id) -> {
                if (!varList.contains(id)) {
                    Error();
                }
                write("", "WORD", id);
            });
            idList.clear();
            listCount = 0;
            found = true;
        } else if (parseAssign()) {
            expEnd = iterator;
            exp = "";
            for (int i = expStart; i < expEnd; i++) {
                exp += lexicalAnalyzer.getTokens().get(i).content + " ";
            }
            genExpCode(infixToPostfix(exp.split(" ")).split(" "));
            write("", "STA", lexicalAnalyzer.getTokens().get(expStart - 2).content);
            regA = lexicalAnalyzer.getTokens().get(expStart - 2).content;
            found = true;
        } else if (parseFor()) {
            found = true;
        } else if (token.type.equals(5)) {
            found = true;
        } else if(token.type.equals(11)) {
        	found = true;
        }
        
        return found;
    }

    // parsing Read/Write expressions
    private boolean parseWrite() {
        boolean found = false;
        if (token.type.equals(8)) {
            token = lexicalAnalyzer.getTokens().get(++iterator);
            if (token.type.equals(14)) {
                token = lexicalAnalyzer.getTokens().get(++iterator);
                if (parseIdList()) {
                    if (token.type.equals(15)) {
                        found = true;
                        token = lexicalAnalyzer.getTokens().get(++iterator);
                    }
                }
            }
        }
        return found;
    }

    private boolean parseRead() {
        boolean found = false;
        if (token.type.equals(7)) {
            token = lexicalAnalyzer.getTokens().get(++iterator);
            if (token.type.equals(14)) {
                token = lexicalAnalyzer.getTokens().get(++iterator);
                if (parseIdList()) {
                    if (token.type.equals(15)) {
                        found = true;
                        token = lexicalAnalyzer.getTokens().get(++iterator);    // Token should be at ";" or "END"
                    }
                }
            }
        }
        return found;
    }

    private boolean parseIdList() {
        boolean found = false;
        if (token.type.equals(16)) {
            found = true;
            idList.add(token.content); // Add first ID to list
            listCount++; // Increment List Count
            token = lexicalAnalyzer.getTokens().get(++iterator);
            while (token.content.equals(",") && found) {
                token = lexicalAnalyzer.getTokens().get(++iterator);
                if (token.type.equals(16)) {
                    idList.add(token.content); // Add first ID to list
                    listCount++; // Increment List Count
                    token = lexicalAnalyzer.getTokens().get(++iterator);
                } else {
                    found = false;
                }
            }
        }
        return found;
    }

    // Expression Validation
    private boolean parseAssign() {
        boolean found = false;
        if (token.type.equals(16) && varList.contains(token.content)) {
            token = lexicalAnalyzer.getTokens().get(++iterator);
            if (token.type.equals(12)) {
                token = lexicalAnalyzer.getTokens().get(++iterator);
                expStart = iterator;
                if (parseExp()) {
                    found = true;
                }
            }
        }
        return found;
    }

    private boolean parseExp() {
        boolean found = false;
        if (parseTerm()) {
            found = true;
            while (token.type.equals(13) && found) {                            // Plus sign
                token = lexicalAnalyzer.getTokens().get(++iterator);
                if (!parseTerm()) {
                    found = false;
                }
            }
        }
        return found;
    }

    private boolean parseTerm() {
        boolean found = false;
        if (parseFactor()) {
            found = true;
            while (token.type.equals(17) && found) {
                token = lexicalAnalyzer.getTokens().get(++iterator);
                if (!parseFactor()) {
                    found = false;
                }
            }
        }
        return found;
    }

    private boolean parseFactor() {
        boolean found = false;
        if (token.type.equals(16) && varList.contains(token.content)) {
            found = true;
            token = lexicalAnalyzer.getTokens().get(++iterator);
        } else if (token.type.equals(14)) {                                     // Open Bracket
            token = lexicalAnalyzer.getTokens().get(++iterator);
            if (parseExp()) {
                if (token.type.equals(15)) {                                    // Closed Bracket
                    found = true;
                    token = lexicalAnalyzer.getTokens().get(++iterator);
                }
            }
        }
        return found;
    }

    // Expression Code
    private void genExpCode(String[] exp) {
        // Generates Code For the Evaluation of the mathematical expression
        String temp = null;
        int t = 1;
        int usedT = 0;
        Stack<String> stack = new Stack<>();
        for (String c : exp) {
            // If the scanned character is an operand (number here),
            // push it to the stack.
            if (c.matches("\\w+")) {
                stack.push(c);
            } //  If the scanned character is an operator, pop two
            // elements from stack apply the operator
            else {
                String val1 = stack.pop();
                String val2 = stack.pop();

                switch (c) {
                    case "+":
                        if (val1.equals(regA)) {
                            write("", "ADD", val2);
                            stack.push("T" + (t));
                            regA = "T" + (t);
                            temp = "T" + (t++);
                        } else if (val2.equals(regA)) {
                            write("", "ADD", val1);
                            stack.push("T" + (t));
                            regA = "T" + (t);
                            temp = "T" + (t++);
                        } else {
                            if (temp != null) {
                                write("", "STA", temp);
                                temp = null;
                                usedT++;
                            }
                            write("", "LDA", val2);
                            write("", "ADD", val1);
                            stack.push("T" + (t));
                            regA = "T" + (t);
                            temp = "T" + (t++);
                        }
                        break;

                    case "*":
                        if (val1.equals(regA)) {
                            write("", "MUL", val2);
                            stack.push("T" + (t));
                            regA = "T" + (t);
                            temp = "T" + (t++);
                        } else if (val2.equals(regA)) {
                            write("", "MUL", val1);
                            stack.push("T" + (t));
                            regA = "T" + (t);
                            temp = "T" + (t++);
                        } else {
                            if (temp != null) {
                                write("", "STA", temp);
                                temp = null;
                                usedT++;
                            }
                            write("", "LDA", val2);
                            write("", "MUL", val1);
                            stack.push("T" + (t));
                            regA = "T" + (t);
                            temp = "T" + (t++);
                        }
                        break;
                }
            }
        }
        if (usedT > tMax) {
            tMax = usedT;
        }
        if (!stack.peek().equals(regA)) {
            write("", "LDA", stack.peek());
            regA = stack.pop();
        }
    }

    private String infixToPostfix(String[] exp) {

        String result = "";
        Stack<String> stack = new Stack<>();

        for (String c : exp) {
            if (c.matches("\\w+")) {
                result += c + " ";
            } else if (c.equals("(")) {
                stack.push(c);
            } else if (c.equals(")")) {
                while (!stack.isEmpty() && !stack.peek().equals("(")) {
                    result += stack.pop() + " ";
                }

                if (!stack.isEmpty() && !stack.peek().equals("(")) {
                    return "Invalid Expression";
                } else {
                    stack.pop();
                }
            } else {
                while (!stack.isEmpty() && Prec(c.charAt(0)) <= Prec(stack.peek().charAt(0))) {
                    result += stack.pop() + " ";
                }
                stack.push(c);
            }
        }
        while (!stack.isEmpty()) {
            result += stack.pop() + " ";
        }
        return result;
    }

    private int Prec(char ch) {
        switch (ch) {
            case '+':
                return 1;
            case '*':
                return 2;
        }
        return -1;
    }

    // Tools
    private void write(String label, String mnemonic, String operand) {
        try {
            BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(lexicalAnalyzer.getFileName().replaceFirst(".pas", ".asm"), append));
            bufferedWriter.write(String.format("%6s \t %6s \t %-20s\n", label, mnemonic, operand));
            bufferedWriter.close();
        } catch (IOException ex) {
            Logger.getLogger(Parser.class.getName()).log(Level.SEVERE, null, ex);
        }
        append = true;
    }

    private void Error() {
        System.out.println("syntax error: Around Token #" + (iterator));
        File file = new File(lexicalAnalyzer.getFileName().replaceFirst("txt", "asm"));
        if (file.exists()) {
            //file.delete();
        }
        System.exit(0);
    }

    private boolean parseFor() {
        boolean found = false;
        if (token.type.equals(6)) {
            token = lexicalAnalyzer.getTokens().get(++iterator);
            if (parseAssign()) {
                expEnd = iterator;
                int startValBegin = expStart;
                int StartValEnd = expEnd;
                if (token.type.equals(9)) {
                    token = lexicalAnalyzer.getTokens().get(++iterator);
                    expStart = iterator;
                    if (parseExp()) {
                        expEnd = iterator;
                        exp = "";
                        for (int i = expStart; i < expEnd; i++) {
                            exp += lexicalAnalyzer.getTokens().get(i).content + " ";
                        }
                        genExpCode(infixToPostfix(exp.split(" ")).split(" "));
                        write("", "+JSUB", "LPUSH");
                        write("", "RMO", "A,L");

                        exp = "";
                        for (int i = startValBegin; i < StartValEnd; i++) {
                            exp += lexicalAnalyzer.getTokens().get(i).content + " ";
                        }
                        genExpCode(infixToPostfix(exp.split(" ")).split(" "));

                        String assignVar = lexicalAnalyzer.getTokens().get(startValBegin - 2).content;
                        // Assign Loop Number to a String
                        String loopNum = "LOOP" + (l++);
                        String nextLoop = "LOOP" + (l++);

                        // Write first line of loop with Labeled at l
                        write(loopNum, "STA", assignVar);
                        regA = assignVar;
                        write("", "CMPR", "A, L");
                        // Make condition to jump to line labeled at l+1 
                        write("", "JLT", nextLoop);
                        if (token.type.equals(10)) {
                            token = lexicalAnalyzer.getTokens().get(++iterator);
                            if (token.type.equals(3)) {
                                if (parseStmtList()) {
                                    if (token.type.equals(4)) {
                                        write("", "LDA", assignVar);
                                        write("", "ADD", "#1");
                                        regA = null;
                                        write("", "J", loopNum);
                                        write(nextLoop, "+JSUB", "LPOP");
                                        token = lexicalAnalyzer.getTokens().get(++iterator);
                                        found = true;
                                    }
                                }
                            } else if (parseStmt()) {
                                write("", "LDA", assignVar);
                                write("", "ADD", "#1");
                                regA = null;
                                write("", "J", loopNum);
                                write(nextLoop, "+JSUB", "LPOP");
                                found = true;
                            }
                        }
                    }
                }
            }
        }
        return found;
    }

}

