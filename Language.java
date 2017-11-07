package com.randommurican.Lexalyser;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Language {
	private List<Lexeme> lexemes;
	private List<Patstern> kinds; // List of patterns we look for
	private boolean wasError;
	private int currentLexeme;

	/**
	 * Declaration of the language that turns the grammar file into
	 * regEx statements
	 * 
	 * @param grammar
	 * @throws FileNotFoundException
	 */
	Language(File grammar) throws FileNotFoundException {
		lexemes = new ArrayList<Lexeme>();
		kinds = new ArrayList<Patstern>();
		wasError = false;
		currentLexeme = 0;

		Scanner grammarScanner = new Scanner(grammar);
		while(grammarScanner.hasNextLine() && !wasError) {
			String name = "", pattern = "", scannedLine = "";
			boolean hasValue = false;
			scannedLine = grammarScanner.nextLine();
			if(scannedLine.startsWith("Pattern: ")) {
				name = scannedLine.substring(9, scannedLine.length()).trim();
				pattern = grammarScanner.nextLine().trim();
				if(grammarScanner.nextLine().trim() == "true")
					hasValue = true;
			} else {wasError = true;}
			kinds.add(new Patstern(name, pattern, hasValue));
		}
		grammarScanner.close();
	}

	/**
	 * Takes programmer input and turns it into a list of lexemes
	 * 
	 * @param input
	 * @throws FileNotFoundException
	 */
	public void parse(File input) throws FileNotFoundException {
		Scanner inputScanner = new Scanner(input);	// Start reading the input
		lexemes.clear();					// Delete any data from previous reads
		int line = 0, start = 0;			// For error reporting

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
				System.out.println(position() + ", " + kind() + ", " + value() );
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
		if(lexemes.get(currentLexeme).hasValue()) {
			String temp = lexemes.get(currentLexeme).getValue();
			return  temp;
		} else {return null;}
	}

	public String position() {
		String temp = lexemes.get(currentLexeme).getPos();
		return temp;
	}

	public int getCurrentLexeme() {return currentLexeme;}

}
