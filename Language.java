package com.randommurican.Lexalyser;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Language {
	private List<Lexeme> lexemes;
	private List<Kind> kinds; // List of patterns we look for
	private boolean wasError;
	private int currentLexeme;

	/**
	 * Declaration of the language that turns the grammar file into
	 * regEx statements
	 * 
	 * @param grammar
	 * @throws FileNotFoundException
	 */
	Language() {
		lexemes = new ArrayList<Lexeme>();
		kinds = new ArrayList<Kind>();
		wasError = false;
		currentLexeme = 0;

		// Hardcoding the grammar for simplicity
			kinds.add(new Kind("lessThan", "<", false));
			kinds.add(new Kind("equal", "=", false));
			kinds.add(new Kind("plus", "\\+", false));
			kinds.add(new Kind("minus", "-", false));
			kinds.add(new Kind("multiply", "\\*", false));
			kinds.add(new Kind("divide", "/", false));
			kinds.add(new Kind("or", "or", false));
			kinds.add(new Kind("and", "and", false));
			kinds.add(new Kind("not", "not", false));
			kinds.add(new Kind("openParentheses", "\\(", false));
			kinds.add(new Kind("closeParentheses", "\\)", false));
			kinds.add(new Kind("true", "(true)", false));
			kinds.add(new Kind("false", "(false)", false));
			kinds.add(new Kind("ID", "[a-zA-Z]([a-zA-Z]|[0-9]|(_))*", true));
			kinds.add(new Kind("NUM", "[0-9]+", true));
	}

	/**
	 * Takes input and turns it into a list of lexemes
	 * 
	 * @param input
	 * @throws FileNotFoundException
	 */
	public void parse(File input) throws FileNotFoundException {
		wasError = false;
		Scanner inputScanner = new Scanner(input);	// Start reading the input
		lexemes.clear();					// Delete any data from previous reads
		int line = 1, start = 0;			// For error reporting

		while(inputScanner.hasNextLine() && !wasError) {
			String lexemeString = "", str = inputScanner.nextLine();
			for(int i = 0; i <= str.length(); i++) {
				if(lexemeString.equals("//")) {		// If a comment is marked
					i = str.length();
					lexemeString = "";
				}
				if(i < str.length() && str.charAt(i) != ' ' && str.charAt(i) != '\t') {	// If the next char isn't white space
					lexemeString += str.charAt(i);
					if(lexemeString.length() == 1) {									// and it was the first for the current lexeme
						start = i;
					}
				}
				else if(lexemeString != "") {	// We found white space or the end of a line and have a lexeme
					lexemes.add(new Lexeme(lexemeString, kinds, line, start));
					lexemeString = "";
					if(!lexemes.get(lexemes.size()-1).getSuccess()) {	// Check to see if it was NOT a lexeme
						wasError = true;
						i = str.length();								// Kill the process since there was an error
					}
				}
			}
			line++;
		}
		inputScanner.close();
	}

	/**
	 * Outputs the lexemes in order in the terminal
	 */
	public void print() {
		if(!wasError && lexemes.size() > 0) {
			do {
				if(!(value() == null))
					System.out.println(position() + ", " + kind() + ", " + value());
				else
					System.out.println(position() + ", " + kind());
			} while ( next() != null );
		}
	}

	/*
	 * Project Requirements
	 */

	public Lexeme next() {
		Lexeme temp;
		if(currentLexeme < lexemes.size() - 1) {
			currentLexeme++;
			temp = lexemes.get(currentLexeme);
			return temp;
		} else
			return null;

	}

	public String kind() {
		String temp = lexemes.get(currentLexeme).getKind();
		return temp;
	}

	public String value() {
		return lexemes.get(currentLexeme).getValue();
	}

	public String position() {
		String temp = lexemes.get(currentLexeme).getPos();
		return temp;
	}

	public int getCurrentLexeme() {return currentLexeme;}

}
