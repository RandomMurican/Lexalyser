package com.randommurican.Lexalyser;

import java.io.File;
import java.io.FileNotFoundException;

public class Driver {
	public static void main(String[] args) {
		Language lang = new Language();
		try {
			System.out.println("------Test1------- should pass");
			lang.parse(new File("Test1.txt"));
			lang.print();
			System.out.println("------Test2------ should fail line 6");
			lang.parse(new File("Test2.txt"));
			lang.print();
			System.out.println("------Test3------ should fail line 9");
			lang.parse(new File("Test3.txt"));
			lang.print();
			System.out.println("------Test4------ should fail line 4 and 8");
			lang.parse(new File("Test4.txt"));
			lang.print();
			System.out.println("------Test5------- should fail line 2");
			lang.parse(new File("Test5.txt"));
			lang.print();
			System.out.println("------Test6------- blank file");
			lang.parse(new File("Test6.txt"));
			lang.print();
		} catch (FileNotFoundException e) {
			System.out.println("Could not find a text file");
		}
	}
}
