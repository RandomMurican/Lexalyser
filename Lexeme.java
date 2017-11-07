package com.randommurican.Lexalyser;

import java.util.List;
import java.util.regex.Matcher;

public class Lexeme {
	private String kind;
	private int line;
	private int pos;
	private String value;
	private boolean success;
	
	Lexeme(String lexemeString, List<Kind> patterns, int line, int pos) {
		this.line = line; // line of the lexeme
		this.pos = pos; // position of the lexeme
		success = false; // Checking for errors
		boolean hasValue = false; // We assume false and change it when we know true
		
		/*
		 * Here we cycle through all the known patterns checking each one
		 * until we have a match. 
		 */
		for(int i = 0; i < patterns.size(); i++) {
			Matcher patternMatcher = patterns.get(i).getPattern().matcher(lexemeString);
	        if(patternMatcher.matches()) { 						// When we have a match
	        	this.kind = patterns.get(i).getKind();		// We pull the name of the kind
	        	if(patterns.get(i).hasValue()) {			// Check if we need a value
	        		this.value = lexemeString;				// Add the value if true
	        		hasValue = true;						// And mark down that it has a value
	        	}
	        	i = patterns.size();						// Then we skip the rest of the kinds
	        	success = true;								// and lazily inform the program there were no errors
	        }
		} if(!success)
			System.out.println("Unexpected character at Line: " + line + " char: " + pos); // This is printed if we didn't find a match
		
		if(hasValue)
			this.value = lexemeString;
	}
	
	public boolean getSuccess() {return success;}
	public String getKind() {return kind;}
	public boolean hasValue() {
		if(value.equals(null))
			return false;
		return true;
	}
	public String getValue() {return value;}
	public String getPos() {return line + ", " + pos;}
	
	public void print() {
		if(!value.equals(null)) //Printed with value
			System.out.print("<" + line + "-" + pos + ", " + kind + ", \"" + value + "\"> ");
		else		// Printed without value
			System.out.print("<" + line + "-" + pos + ", " + kind + "> ");
	}
	
}
