import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

public class DataAnalyzer {

	private String line = null;
	private ArrayList<String> tempStoredLines = new ArrayList<String>();

	// METHODS FOR CONDUCTING ANALYSIS
	// variables for generalAnalysis
	private ArrayList<String> permGeneralLines = new ArrayList<String>();
	private Map<String, Integer> genMap = new TreeMap<String, Integer>();

	// variables for trumpAnalysis
	private ArrayList<String> permTrumpLines = new ArrayList<String>();
	private Map<String, Integer> trumpMap = new TreeMap<String, Integer>();

	// used in analysis methods
	/**
	 * Reads each character in a line and organizes it in an ArrayList
	 * If it is a character from a - z When a word ends the program 
	 * condenses the ArrayList into a string.
	 * @param readLine
	 */
	public void wordIdentifier(String readLine, ArrayList<String> permList) {
		/*
		 * reads one character at a time and checks if it is a value a-z. if it is it
		 * will store the character in an ArrayList. When anything else is detected, the
		 * temporary ArrayList is consolidated (into a word) into a permanent ArrayList
		 */
		for (int i = 0; i < readLine.length(); i++) {
			String charString = Character.toString(readLine.charAt(i));
			String lowerCharString = charString.toLowerCase();
			if (lowerCharString.matches("[a-z]")) {
				// if (stringLineLower.matches(".*[a-z].*")) {
				tempStoredLines.add(lowerCharString);
			} else {
				this.arrayListCondenser(permList);
			} // else
		} // for loop
		this.arrayListCondenser(permList);
	}// wordIdentifier

	// used in wordIdentifier
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

	// used in readFile
	/**
	 * Counts the # of times a word occurs in data. Creates HashMap of the data. A
	 * HashMap is a collection class that stores key (the actual word) and value (#
	 * of times it appears) pairs
	 */
	public void wordFrequency(ArrayList<String> permList, Map<String, Integer> map) {
		Set<String> unique = new HashSet<String>(permList);
		for (String key : unique) {
			map.put(key, Collections.frequency(permList, key));
		} // for loop
		Map<String, Integer> sortedMap = sortByValue(map);
		printMap(sortedMap);
	}// wordFrequency

	// used in wordFrequency
	/**
	 * Sorts map by value
	 * @param unsortMap - unsorted version of the map that is passed to the method
	 * @return sortedMap - sorted version of the map that is passed to the method
	 */
	private static Map<String, Integer> sortByValue(Map<String, Integer> unsortMap) {
		// convert map to list of map
		List<Map.Entry<String, Integer>> list = new LinkedList<Map.Entry<String, Integer>>(unsortMap.entrySet());

		// sort list with collections.sort() using a custom comparator
		Collections.sort(list, new Comparator<Map.Entry<String, Integer>>() {
			public int compare(Map.Entry<String, Integer> o1, Map.Entry<String, Integer> o2) {
				return (o1.getValue()).compareTo(o2.getValue());
			}// compare
		});// Collections.sort

		// loop through sorted list and put it in a new insertion order Map LinkedHash
		// Map
		Map<String, Integer> sortedMap = new LinkedHashMap<String, Integer>();
		for (Map.Entry<String, Integer> entry : list) {
			sortedMap.put(entry.getKey(), entry.getValue());
		}
		return sortedMap;
	}

	// used in wordFrequency
	/**
	 * Orders treeMap by the integer i.e. the # of times it occurs in the file
	 * 
	 * @throws IOException
	 */
	private void printMap(Map<String, Integer> map) {
		Set s = map.entrySet();
		Iterator it = s.iterator();
		FileWriter writer;
		BufferedWriter bufferedWriter;
		while (it.hasNext()) {
			Map.Entry entry = (Map.Entry) it.next();
			String key = (String) entry.getKey();
			Integer value = (Integer) entry.getValue();
			System.out.println(key + " => " + value);
			/*
			 * writes data to separate text file
			 */
			this.textFileWriter(key, value);
		} // while loop
		System.out.println("========================");
	}// printMap

	// used in printMap
	/**
	 * writes all initialized analyses to a text file
	 * @param key - key of the analyzed map
	 * @param value - value associated with the key of the analyzed map
	 */
	public void textFileWriter(String key, Integer value) {
		FileWriter writer;
		BufferedWriter bufferedWriter;
		try {
			writer = new FileWriter("Analyses.txt", true);
			bufferedWriter = new BufferedWriter(writer);
			bufferedWriter.write(key + " => " + value + "\n");
			bufferedWriter.close();
		} catch (IOException e) {
			e.printStackTrace();
		} // catch
	}//textFileWriter
	
	// ANALYSIS METHODS
	/**
	 * reads the file and identifies all words in file
	 * 
	 * @param fileToRead
	 *            - the file that is being analyzed
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
			} // while loop
			this.wordFrequency(permGeneralLines, genMap);
			bufferedReader.close();
		} catch (FileNotFoundException ex) {
			System.out.println("Unable to open file '" + fileToRead + "'");
		} catch (IOException ex) {
			System.out.println("Error reading file '" + fileToRead + "'");
		} // catch
	}// generalAnalysis

	/**
	 * reads the file and identifies all words in the file's lines that contain
	 * trump
	 * 
	 * @param fileToRead
	 *            - the file that is being analyzed
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
				} // if
			} // while loop
			this.wordFrequency(permTrumpLines, trumpMap);
			bufferedReader.close();
		} catch (FileNotFoundException ex) {
			System.out.println("Unable to open file '" + fileToRead + "'");
		} catch (IOException ex) {
			System.out.println("Error reading file '" + fileToRead + "'");
		} // catch
	}// trumpAnalysis

}// DataAnalyzer Class
