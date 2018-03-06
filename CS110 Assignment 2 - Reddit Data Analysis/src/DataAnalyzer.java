import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

public class DataAnalyzer {

	private String line = null;
	private ArrayList<String> tempStoredLines = new ArrayList<String>();

	//Variables for general analysis
	private ArrayList<String> permGeneralLines = new ArrayList<String>();
	private Map<String, Integer> genMap = new TreeMap<String, Integer>();

	//Variables for TrumpAnalysis
	private ArrayList<String> permTrumpLines = new ArrayList<String>();
	private Map<String, Integer> trumpMap = new TreeMap<String, Integer>();

	//used in analysis methods
	/**
	 * Reads each character in a line and organizes it in an ArrayList.
	 * If it is a character from a - z When a word ends the program
	 * condenses the ArrayList into a string.
	 * @param readLine
	 */
	public void wordIdentifier(String readLine,ArrayList<String> permList) {
		/*
		 * reads one character at a time and checks if it is a value a-z.
		 * if it is it will store the character in an ArrayList. When
		 * anything else is detected, the temporary ArrayList is consolidated
		 * (into a word) into a permanent ArrayList
		 */
		for (int i = 0; i < readLine.length(); i++) {
			String charString = Character.toString(readLine.charAt(i));
			String lowerCharString = charString.toLowerCase();
			if (lowerCharString.matches("[a-z]")) {
			//if (stringLineLower.matches(".*[a-z].*")) {
				tempStoredLines.add(lowerCharString);
			} else {
				this.arrayListCondenser(permList);
			}//else
		}//for loop
		this.arrayListCondenser(permList);
	}//wordIdentifier

	//used in wordIdentifier
	/**
	 * Condenses the array of characters into string -> store in new ArrayList
	 */
	private void arrayListCondenser(ArrayList<String> permList) {
		StringBuilder sb = new StringBuilder();
		for (String s : tempStoredLines) {
			sb.append(s);
		}
		permList.add(sb.toString());
		tempStoredLines.clear();
	}

	//used in readFile
	/**
	 * Counts the # of times a word occurs in data. Creates HashMap of the data.
	 * A HashMap is a collection class that stores key (the actual word) and value
	 * (# of times it appears) pairs
	 */
	public void wordFrequency(ArrayList<String> permList,Map<String, Integer> map) {
		Set<String> unique = new HashSet<String>(permList);
		for (String key : unique) {
			map.put(key, Collections.frequency(permList, key));
		}//for loop
		printMap(map);
	}//wordFrequency

	//used in wordFrequency
	/**
	 * Orders treeMap by the integer i.e. the # of times it occurs in the file
	 */
	 private static void printMap(Map<String, Integer> map) {
			 Set s = map.entrySet();
			 Iterator it = s.iterator();
			 while ( it.hasNext() ) {
					Map.Entry entry = (Map.Entry) it.next();
					String key = (String) entry.getKey();
					Integer value = (Integer) entry.getValue();
					System.out.println(key + " => " + value);
			 }//while loop
			 System.out.println("========================");
	 }//printMap

	/**
	 * reads the file and identifies all words in file
	 * @param fileToRead - the file that is being analyzed
	 */
	public void generalAnalysis(String fileToRead) {
		System.out.println("Ready to read file.");
		line = null;
		try {
			FileReader myFileReader = new FileReader(fileToRead);
			System.out.println("I was able to open your file!");
			BufferedReader bufferedReader = new BufferedReader(myFileReader);
			while ((line = bufferedReader.readLine()) != null) {
				line.toLowerCase();
				this.wordIdentifier(line, permGeneralLines);
			}//while loop
			this.wordFrequency(permGeneralLines, genMap);
			bufferedReader.close();
		} catch (FileNotFoundException ex) {
			System.out.println("Unable to open file '" + fileToRead + "'");
		} catch (IOException ex) {
			System.out.println("Error reading file '" + fileToRead + "'");
		}//catch
	}//generalAnalysis


	/**
	 * reads the file and identifies all words in the file's lines that contain
	 * trump
	 * @param fileToRead - the file that is being analyzed
	 */
	public void trumpAnalysis(String fileToRead) {
		System.out.println("Ready to read file.");
		line = null;
		try {
			FileReader myFileReader = new FileReader(fileToRead);
			System.out.println("I was able to open your file!");
			BufferedReader bufferedReader = new BufferedReader(myFileReader);
			while ((line = bufferedReader.readLine()) != null) {
				line.toLowerCase();
				if (line.contains("trump")) {
					this.wordIdentifier(line, permTrumpLines);
				}//if
			}//while loop
			this.wordFrequency(permTrumpLines, trumpMap);
			bufferedReader.close();
		} catch (FileNotFoundException ex) {
			System.out.println("Unable to open file '" + fileToRead + "'");
		} catch (IOException ex) {
			System.out.println("Error reading file '" + fileToRead + "'");
		}//catch
	}//trumpAnalysis

	//not in use
	private void writeList() {
	// The FileWriter constructor throws IOException, which must be caught.
        PrintWriter writer;
		try {
			System.out.println("wrote a file");
			writer = new PrintWriter("the-file-name.txt", "UTF-8");
			writer.println("The first line");
			writer.println("The second line");
			writer.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }

}//DataAnalyzer Class
