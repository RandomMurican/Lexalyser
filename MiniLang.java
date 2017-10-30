package com.randommurican.Lexalyser;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class MiniLang {
		private List<Lexeme> lex;
		private List<Patstern> kinds;
		private boolean kill; // Lazy way of error stopping, bad ej
		private int currentLexeme;
	
	MiniLang(){
		lex = new ArrayList<Lexeme>();		// List of any lexemes we find
		kinds = new ArrayList<Patstern>();	// List of patterns we look for
		kill = false;
		currentLexeme = 0;
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
	
	
	public boolean parse(File input) throws FileNotFoundException {
		Scanner scan = new Scanner(input);
		lex.clear();
		int line = 0;
		int start = 0;
		
		
		while(scan.hasNextLine() && !kill) {
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
					lex.add(new Lexeme(lexemeString, kinds, line, start));	// submit the string and repeat
					lexemeString = "";										// Reset the string
					if(!lex.get(lex.size()-1).getSuccess()) {				// Check to see if it was NOT a lexeme
						kill = true;
						i = str.length();										// Kill the process since there was an error
					}
				}
			}
			line++;
			
		}
		scan.close();
		return kill;
	}
	
	public void print() { // Print all the lexemes we have on new lines
		if(!kill)
			for(int i = 0; i < lex.size(); i++) {
				lex.get(i).print();
				System.out.println("");
			}
	}
	
	/*
	 * Project Requirements
	 */
	

	
	public Lexeme next() {
		Lexeme temp;
		if(currentLexeme < lex.size() - 1) {
			currentLexeme++;
			temp = lex.get(currentLexeme);
			return temp;
		} else
			return null;
	
	}
	
	public String kind() {
		String temp = lex.get(currentLexeme).getKind();
		return temp;
	}
	
	public String value() {
		if(lex.get(currentLexeme).hasValue()) {
			String temp = lex.get(currentLexeme).getValue();
			return  temp;
		} else {return null;}
	}
	
	public String position() {
		String temp = lex.get(currentLexeme).getPos();
		return temp;
	}
	
}
