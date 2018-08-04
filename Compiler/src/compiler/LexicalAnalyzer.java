package compiler;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class LexicalAnalyzer {

    private final String fileName;
    private String code;
    private String line;
    final private String regex = "(PROGRAM|VAR|BEGIN|END\\.|END|FOR|READ|WRITE|TO|DO|,|;|:=|\\+|\\*|\\(|\\)|[A-Za-z_][A-Za-z_0-9]*)";
    private final HashMap<String, Integer> tokenList;
    private final ArrayList<Token> tokens;

    public LexicalAnalyzer(String fileName) {
    	this.fileName = fileName;
        this.code = "";

        this.tokenList = new HashMap<>();
        this.tokens = new ArrayList<>();

        tokenList.put("PROGRAM", 1);
        tokenList.put("VAR", 2);
        tokenList.put("BEGIN", 3);
        tokenList.put("END", 4);
        tokenList.put("END.", 5);
        tokenList.put("FOR", 6);
        tokenList.put("READ", 7);
        tokenList.put("WRITE", 8);
        tokenList.put("TO", 9);
        tokenList.put("DO", 10);
        tokenList.put(";", 11);
        tokenList.put(":=", 12);
        tokenList.put("+", 13);
        tokenList.put("(", 14);
        tokenList.put(")", 15);
        tokenList.put("id", 16);
        tokenList.put("*", 17);
        tokenList.put(",", 18);
    }
    
    public void analyze() {
        readFile();
        tokenize();
        printTokens();
    }

    private void readFile() {
        // Reads file contents into a string in memory

        BufferedReader bufferedReader = null;
        try {
            bufferedReader = new BufferedReader(new FileReader(fileName));
            while ((line = bufferedReader.readLine()) != null) {
                code = code.concat("\n" + line);
            }
        } catch (FileNotFoundException ex) {
            Logger.getLogger(LexicalAnalyzer.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(LexicalAnalyzer.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                bufferedReader.close();
            } catch (IOException ex) {
                Logger.getLogger(LexicalAnalyzer.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    private void tokenize() {

        Pattern pattern = Pattern.compile(regex, Pattern.MULTILINE);
        Matcher matcher = pattern.matcher(code);
        String token;
        while (matcher.find()) {
            token = matcher.group(1);
            if (tokenList.containsKey(token)) {
                tokens.add(new Token(token, tokenList.get(token)));
            } else {
                tokens.add(new Token(token, tokenList.get("id")));
            }
        }
    }

    private void printTokens() {
        System.out.printf("%10s \t %-10s \t %-10s\n","Token #", "Token Type", "Token");
        System.out.printf("--------------------------------------------\n");
        for (int i = 0; i < tokens.size(); i++) {
            System.out.printf("%-10s| \t %-10s| \t %-10s|\n", i, tokens.get(i).type, tokens.get(i).content);
        }
        System.out.printf("--------------------------------------------\n");
        System.out.print("\n\n\n");
        System.out.println("Assembly Code:");
    }

    public ArrayList<Token> getTokens() {
        return this.tokens;
    }

    public String getFileName() {
        return fileName;
    }

}

