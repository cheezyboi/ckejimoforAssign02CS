import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

public class DataAnalyzer {

	String userName;
	float accountAge;
	int counter = 0;
	ArrayList<String> tempStoredLines = new ArrayList<String>();
	ArrayList<String> permStoredLines = new ArrayList<String>();
	Map<Integer,String> treeMap = new TreeMap<Integer, String>();


	/**
	 * Reads each character in a line and organizes it in an ArrayList.
	 * If it is a character from a - z When a word ends the program
	 * condenses the ArrayList into a string.
	 * @param readLine
	 */
	public void wordIdentifier(String readLine) {
		/*
		 * reads one character at a time and checks if it is a value a-z.
		 * if it is it will store the character in an ArrayList. When
		 * anything else is detected, the temporary ArrayList is consolidated
		 * (into a word) into a permanent ArrayList
		 */
		for (int i = 0; i < readLine.length(); i++) {
			String stringLine = Character.toString(readLine.charAt(i));
			if (stringLine.matches(".*[a-z].*")) {
				tempStoredLines.add(stringLine);
			} else {
				StringBuilder sb = new StringBuilder();
				for (String s : tempStoredLines) {
					sb.append(s);
				}
				permStoredLines.add(sb.toString());
				tempStoredLines.clear();
			}
		}
		this.arrayListCondenser();
	}

	// used in wordIdentifier
	/**
	 * Condenses the array of characters into string -> store in new ArrayList
	 */
	public void arrayListCondenser() {
		StringBuilder sb = new StringBuilder();
		for (String s : tempStoredLines) {
			sb.append(s);
		}
		permStoredLines.add(sb.toString());
		tempStoredLines.clear();
	}

	// used in readFile
	/**
	 * Counts the # of times a word occurs in data. Creates HashMap of the data.
	 * A HashMap is a collection class that stores key (the actual word) and value
	 * (# of times it appears) pairs
	 */
	public void wordFrequency() {
		/*
		 * initializes a HashSet from the values in permStoredLines
		 * stores each key in the ArrayList with a unique value
		 * (a.k.a. HashCode)
		 */
		Set<String> unique = new HashSet<String>(permStoredLines);
		/*
		 * initializes a HashMap using the word as the information Key and
		 * the frequency that the work appears as the value
		 */
		for (String key : unique) {
			treeMap.put(Collections.frequency(permStoredLines, key), key);
		}//for loop
		printMap(treeMap);
	}//wordFrequency

	/**
	 * Orders treeMap by the integer i.e. the # of times it occurs in the file
	 */
	public static void printMap(Map<Integer, String> map) {
	    Set s = map.entrySet();
	    Iterator it = s.iterator();
	    while ( it.hasNext() ) {
	       Map.Entry entry = (Map.Entry) it.next();
	       Integer key = (Integer) entry.getKey();
	       String value = (String) entry.getValue();
	       System.out.println(key + " => " + value);
	    }//while loop
	    System.out.println("========================");
	}//printMap

	public void readFile(String fileToRead) {
		System.out.println("Ready to read file.");
		String line = null;
		try {
			FileReader myFileReader = new FileReader(fileToRead);
			System.out.println("I was able to open your file!");
			BufferedReader bufferedReader = new BufferedReader(myFileReader);
			while ((line = bufferedReader.readLine()) != null) {
				this.wordIdentifier(line);
			}//while loop
			this.wordFrequency();
			System.out.println(permStoredLines.size());
			bufferedReader.close();
		} catch (FileNotFoundException ex) {
			System.out.println("Unable to open file '" + fileToRead + "'");
		} catch (IOException ex) {
			System.out.println("Error reading file '" + fileToRead + "'");
		}
	}//readFile

	public void analysis2() {
		
	}
}//DataAnalyzer Class
