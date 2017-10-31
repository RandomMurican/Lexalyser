package com.randommurican.Lexalyser;

import java.io.File;
import java.io.FileNotFoundException;

public class Driver {
	public static void main(String[] args) {
		MiniLang lang;
		try {
			lang = new MiniLang(new File("Grammar.txt"));
			lang.parse(new File("test.txt"));
			lang.print();
		} catch (FileNotFoundException e1) {
			System.out.println("Could not find a text file");
		}
	}
}
