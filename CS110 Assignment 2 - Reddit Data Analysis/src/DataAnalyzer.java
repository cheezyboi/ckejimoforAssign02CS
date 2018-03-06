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
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeMap;

public class DataAnalyzer {

	private String line = null;
	private ArrayList<String> tempStoredLines = new ArrayList<String>();

	// METHODS FOR CONDUCTING ANALYSIS
	// variables for postGeneralAnalysis
	private ArrayList<String> permPostGeneralLines = new ArrayList<String>();
	private Map<String,Integer> genPostMap = new TreeMap<String,Integer>();
	
	// variables for authorGeneralAnalysis
	private ArrayList<String> permAuthorGeneralLines = new ArrayList<String>();
	private Map<String,Integer> genAuthorMap = new TreeMap<String,Integer>();

	// variables for trumpAnalysis
	private ArrayList<String> permTrumpLines = new ArrayList<String>();
	private Map<String,Integer> trumpMap = new TreeMap<String,Integer>();
	
	// variables for authorAnalysis
	private ArrayList<String> permAuthorLines = new ArrayList<String>();
	private Map<Integer,String> authorMap = new TreeMap<Integer,String>();
	private ArrayList<String> drjarnsLines = new ArrayList<String>();

	
	// used in analysis methods
	/**
	 * Reads each character in a line and organizes it in an ArrayList
	 * If it is a character from a - z When a word ends the program
	 * condenses the ArrayList into a string.
	 * @param readLine
	 */
	private void wordIdentifier(String readLine, ArrayList<String> permList) {
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

	// used in analyses
	/**
	 * Counts the # of times a word occurs in data. Creates HashMap of the data. 
	 * A HashMap is a collection class that stores key (the actual word) and value 
	 * (# of times it appears) pairs
	 */
	private void wordFrequency(ArrayList<String> permList, Map<String, Integer> map) {
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
		while (it.hasNext()) {
			Map.Entry entry = (Map.Entry) it.next();
			String key = (String) entry.getKey();
			Integer value = (Integer) entry.getValue();
			System.out.println(key + " => " + value);
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
	private void textFileWriter(String key, Integer value) {
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

	//--------------------------------------------------------
	//--------------------------------------------------------
	// ANALYSIS METHODS
	/**
	 * reads the file and identifies all words in the posts file
	 * this also works for the authors file but I kept them separate
	 * because I wanted them funneled into different variables
	 * @param fileToRead - the file that is being analyzed
	 */
	public void generalPostAnalysis(String fileToRead) {
		System.out.println("Ready to read file.");
		line = null;
		try {
			FileReader myFileReader = new FileReader(fileToRead);
			System.out.println("I was able to open your file!");
			BufferedReader bufferedReader = new BufferedReader(myFileReader);
			while ((line = bufferedReader.readLine()) != null) {
				line.toLowerCase();
				this.wordIdentifier(line, permPostGeneralLines);
			} // while loop
			this.wordFrequency(permPostGeneralLines, genPostMap);
			bufferedReader.close();
		} catch (FileNotFoundException ex) {
			System.out.println("Unable to open file '" + fileToRead + "'");
		} catch (IOException ex) {
			System.out.println("Error reading file '" + fileToRead + "'");
		} // catch
	}// generalPostAnalysis

	// looks at whole file
	/**
	 * reads the file and identifies all words in the authors file
	 * this also works for the posts file but I kept them separate
	 * because I wanted them funneled into different variables
	 * @param fileToRead
	 *            - the file that is being analyzed
	 */
	public void generalAuthorAnalysis(String fileToRead) {
		System.out.println("Ready to read file.");
		line = null;
		try {
			FileReader myFileReader = new FileReader(fileToRead);
			System.out.println("I was able to open your file!");
			BufferedReader bufferedReader = new BufferedReader(myFileReader);
			while ((line = bufferedReader.readLine()) != null) {
				line.toLowerCase();
				this.wordIdentifier(line, permAuthorGeneralLines);
			} // while loop
			this.wordFrequency(permAuthorGeneralLines, genAuthorMap);
			bufferedReader.close();
		} catch (FileNotFoundException ex) {
			System.out.println("Unable to open file '" + fileToRead + "'");
		} catch (IOException ex) {
			System.out.println("Error reading file '" + fileToRead + "'");
		} // catch
	}// generalAuthorAnalysis
	
	// used in drjarns
	/**
	 * stores all file lines in arrayList. used to identify author's posts.
	 * @param fileToRead - the file that is being analyzed
	 */
	public void generalPostStorage(String fileToRead) {
		System.out.println("Ready to read file.");
		line = null;
		try {
			FileReader myFileReader = new FileReader(fileToRead);
			System.out.println("I was able to open your file!");
			BufferedReader bufferedReader = new BufferedReader(myFileReader);
			while ((line = bufferedReader.readLine()) != null) {
				permPostGeneralLines.add(line);
			} // while loop
			bufferedReader.close();
		} catch (FileNotFoundException ex) {
			System.out.println("Unable to open file '" + fileToRead + "'");
		} catch (IOException ex) {
			System.out.println("Error reading file '" + fileToRead + "'");
		} // catch
	}// generalPostAnalysis
	

	// looks at only lines containing 'trump'
	/**
	 * read file and identifies words in the file's lines that contain trump
	 * this is a narrower version of the general analysis. finds 
	 * what words are associated with bringing up trump
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
	

	// looks at only lines containing 'trump'
	/**
	 * Author drjarns posted the most. these are his posts.
	 */
	public void drjarnsAnalysis(String posts, String authors) {
		this.generalPostStorage(posts);
		System.out.println("Ready to read file.");
		line = null;
		int nameCounter = 0;
		try {
			FileReader myFileReader = new FileReader(authors);
			System.out.println("I was able to open your file!");
			BufferedReader bufferedReader = new BufferedReader(myFileReader);
			while ((line = bufferedReader.readLine()) != null) {
				line.toLowerCase();
				nameCounter++;
				if (line.contains("drjarns")) {
					drjarnsLines.add(this.permPostGeneralLines.get(nameCounter));
				} // if
			} // while loop
			System.out.println("-----------------------");
			for (String str : drjarnsLines) {
				System.out.println(str);
			}
			System.out.println(drjarnsLines.size());
			bufferedReader.close();
		} catch (FileNotFoundException ex) {
			System.out.println("Unable to open file '" + authors + "'");
		} catch (IOException ex) {
			System.out.println("Error reading file '" + authors + "'");
		} // catch
	}// drjarnsAnalysis

	// attempting to index the redditAuthor.txt
	public void authorAnalysis(String fileToRead) {
		System.out.println("Ready to read file.");
		line = null;
		try {
			FileReader myFileReader = new FileReader(fileToRead);
			System.out.println("I was able to open your file!");
			BufferedReader bufferedReader = new BufferedReader(myFileReader);
			while ((line = bufferedReader.readLine()) != null) {
				line.toLowerCase();
				this.wordIdentifier(line, permAuthorLines);
			} // while loop
			this.authorIndex(permAuthorLines, authorMap);
			bufferedReader.close();
		} catch (FileNotFoundException ex) {
			System.out.println("Unable to open file '" + fileToRead + "'");
		} catch (IOException ex) {
			System.out.println("Error reading file '" + fileToRead + "'");
		} // catch
	}// authorAnalysis

}// DataAnalyzer Class
