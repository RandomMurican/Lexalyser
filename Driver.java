package com.randommurican.Lexalyser;

import java.io.File;
import java.io.FileNotFoundException;

public class Driver {
	public static void main(String[] args) {
		boolean error = false;
		MiniLang lang = new MiniLang(); //Declare a new language
		
		//	Add in all of the types of lexemes as
		//	defined by the grammar
		lang.addKind("false", "false");
		lang.addKind("true", "true");
		lang.addKind("lessThan", "<");
		lang.addKind("equal", "=");
		lang.addKind("plus", "\\+\\B");
		lang.addKind("minus", "\\-\\B");
		lang.addKind("multiply", "\\*\\B");
		lang.addKind("divide", "\\/\\B");
		lang.addKind("or", "or");
		lang.addKind("and", "and");
		lang.addKind("not", "not");
		lang.addKind("openParentheses", "\\(\\B");
		lang.addKind("closeParentheses", "\\)\\B");
		lang.addKind("ID", "[a-zA-Z]([a-zA-Z]|[0-9]|(_))*", true);
		lang.addKind("NUM", "[0-9]+", true);
		
		//load in the file to be parsed through command line
		try {
			error = lang.parse(new File("test.txt"));
		} catch (FileNotFoundException e) {
			System.out.println("File not found");
			error = true;
		}
		
		// print out the lexemes
		if(!error) {
			do {
			System.out.println(lang.position() + ", " + lang.kind() + ", " + lang.value() );
			} while ( lang.next() != null );
		}
	}
}
