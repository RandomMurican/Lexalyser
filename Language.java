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
		lexemes.clear();							// Delete any data from previous reads
		int line = 1, start = 1;

		while(inputScanner.hasNextLine() && !wasError) {
			String lexemeString = "", str = inputScanner.nextLine();
			for(int i = 0; i <= str.length(); i++) {
				//System.out.println(line + "|" + i + " lexStr: \"" + lexemeString + "\", " + wasError);
				if(lexemeString.equals("/") && str.charAt(i) == '/') {
					i = str.length();
					lexemeString = "";
				} else if(i < str.length() && str.charAt(i) != ' ' && str.charAt(i) != '\t') {
					if(	str.charAt(i) == '(' || str.charAt(i) == ')') {
						if(lexemeString != "") {
							if(!addLexeme(lexemeString, line, start))
								i = str.length();
							else {}
							lexemeString = "";
						} else{}
						if(!wasError) {
								if(!addLexeme(String.valueOf(str.charAt(i)), line, start))
									i = str.length();
								else {}
								lexemeString = "";
						}
					} else {
						lexemeString += str.charAt(i);
						if(lexemeString.length() == 1) {
							start = i;
						}
					}
				} else if(lexemeString != "") {
					if(!addLexeme(lexemeString, line, start))
						i = str.length();
					else {}
					lexemeString = "";
				}
			}
			line++;
		}
		inputScanner.close();
	}
	
	public boolean addLexeme(String lexeme, int line, int pos) {
		lexemes.add(new Lexeme(lexeme, kinds, line, pos));
		if(!lexemes.get(lexemes.size()-1).getSuccess())	// Checking to see if it was NOT a lexeme
			wasError = true;
		return !wasError;
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
