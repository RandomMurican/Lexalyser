package com.randommurican.Lexalyser;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class MiniLang {
		private List<Lexeme> lexemes;
		private List<Patstern> kinds;
		private boolean wasError;
		private int currentLexeme;
	
	MiniLang(File grammar) throws FileNotFoundException {
		lexemes = new ArrayList<Lexeme>();
		kinds = new ArrayList<Patstern>();	// List of patterns we look for
		wasError = false;
		currentLexeme = 0;
		
		Scanner grammarScanner = new Scanner(grammar);
		while(grammarScanner.hasNextLine() && !wasError) {
			String name = "", pattern = "", scannedLine = "";
			boolean hasValue = false;
			scannedLine = grammarScanner.nextLine();
			if(scannedLine.startsWith("Pattern: ")) {
				name = scannedLine.substring(9, scannedLine.length());
				pattern = grammarScanner.nextLine().trim();
				if(grammarScanner.nextLine().trim() == "true")
					hasValue = true;
			} else {wasError = true;}
			kinds.add(new Patstern(name,pattern, hasValue));
		}
		grammarScanner.close();
	}
	
	// Pass info to Patstern where it's reformatted for use
	public void addKind(String name, String pattern) {
		kinds.add(new Patstern(name, pattern));
	}
	
	// Pass info to Patstern where it's reformatted for use
	public void addKind(String name, String pattern, boolean bool) {
		kinds.add(new Patstern(name, pattern, bool));
	}
	
	// Just in case, I added a way to remove a kind
	public boolean removeKind(String name) {
		for(int i = 0; i < kinds.size(); i++) {
			if(kinds.get(i).getKind() == name) {
				kinds.remove(i);
				return true;
			}
		}
		return false;
	}
	
	public int getKindCount() {return kinds.size();} // Just a simple count for processing purposes
	
	
	public void parse(File input) throws FileNotFoundException {
		Scanner scan = new Scanner(input);
		lexemes.clear();
		int line = 0;
		int start = 0;
		
		
		while(scan.hasNextLine() && !wasError) {
			String lexemeString = "";
			String str = scan.nextLine();
			for(int i = 0; i <= str.length(); i++) {
				if(lexemeString.equals("//")) { // In case of comment, ignore it and skip to the last char to load next line
					i = str.length();
					lexemeString = "";
				}
				if(i < str.length() && str.charAt(i) != ' ' && str.charAt(i) != '\t') {	// Looking for non-blanks
					lexemeString += str.charAt(i);				// Add the current char to the string
					if(lexemeString.length() == 1) {			// If it's the first to be added
						start = i;									// Note the position for the lexeme
					}
				}
				else if(lexemeString != "") { 								// When we reach the end or a blank is found and we have a string
					lexemes.add(new Lexeme(lexemeString, kinds, line, start));	// submit the string and repeat
					lexemeString = "";										// Reset the string
					if(!lexemes.get(lexemes.size()-1).getSuccess()) {				// Check to see if it was NOT a lexeme
						wasError = true;
						i = str.length();										// Kill the process since there was an error
					}
				}
			}
			line++;
			
		}
		scan.close();
	}
	
	public void print() { // Print all the lexemes we have on new lines
		if(!wasError && lexemes.size() > 0) {
			do {
				System.out.println(position() + ", " + kind() + ", " + value() );
			} while ( next() != null );
		}  /*
			for(int i = 0; i < lex.size(); i++) {
				lex.get(i).print();
				System.out.println("");
			} */
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
