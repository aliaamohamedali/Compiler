package compiler;

import java.util.Scanner;

public class Main {
	
    public static void main(String[] args) {
        // TODO code application logic here
    	// Parser parser = new Parser(args[0]);
    	Parser parser;
    	Scanner scanner = new Scanner(System.in);
    	String fileName;
    	int choice=1;
    	
    	while(true) {
    		System.out.println("Please Choose file to parse, 7 to parse a new File or 8 to exit");
    		System.out.println("1. EXP.pas");
    		System.out.println("2. EXP2.pas");
    		System.out.println("3. hourstomins.pas");
    		System.out.println("4. LOOPs.pas");
    		System.out.println("5. SUM.pas");
    		System.out.println("6. SUM_LOOP.pas");
    		
    		choice = scanner.nextInt();
    		
    		switch(choice) {
    		
    		case 1:
    			fileName = "EXP.pas";
    			parser = new Parser(fileName);
    			parser.parse();
    			break;
    		case 2:
    			fileName = "EXP2.pas";
    			parser = new Parser(fileName);
    			parser.parse();
    			break;
    		case 3:
    			fileName = "hourstomins.pas";
    			parser = new Parser(fileName);
    			parser.parse();
    			break;
    		case 4:
    			fileName = "LOOPs.pas";
    			parser = new Parser(fileName);
    			parser.parse();
    			break;
    		case 5:
    			fileName = "SUM.pas";
    			parser = new Parser(fileName);
    			parser.parse();
    			break;
    		case 6:
    			fileName = "SUM_LOOP.pas";
    			parser = new Parser(fileName);
    			parser.parse();
    			break;
    		case 7:
    			System.out.println("Please Enter complete path of File");
    			fileName = scanner.next();
    			parser = new Parser(fileName);
    			parser.parse();
    			break;
    		case 8:
    			System.exit(1);
    			break;	
    		
    		
    		
    		
    		
    		
    		}
    	}
    	
    }

}
