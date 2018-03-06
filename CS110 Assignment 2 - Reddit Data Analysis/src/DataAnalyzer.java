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
//HELLO
public class DataAnalyzer {

	private String line = null;
	private ArrayList<String> tempStoredLines = new ArrayList<String>();
	private int nameCounter = 0;

	// ============================================
	// ============================================
	// ============================================

	// variables for postGeneralAnalysis
	private ArrayList<String> permPostGeneralLines = new ArrayList<String>();
	private Map<String,Integer> genPostMap = new TreeMap<String,Integer>();

	// variables for authorGeneralAnalysis
	private ArrayList<String> permAuthorGeneralLines = new ArrayList<String>();
	private Map<String,Integer> genAuthorMap = new TreeMap<String,Integer>();

	// variables for trumpAnalysis
	private ArrayList<String> permTrumpLines = new ArrayList<String>();
	private Map<String,Integer> trumpMap = new TreeMap<String,Integer>();

	// variables for specificAuthorAnalysis
	private ArrayList<String> permAuthorLines = new ArrayList<String>();
	private ArrayList<String> permAuthorLinesID = new ArrayList<String>();
	private Map<String,Integer> authorMap = new TreeMap<String,Integer>();

	// variables for specificWordAnalysis
	private ArrayList<String> permWordLines = new ArrayList<String>();
	private ArrayList<String> permWordLinesID = new ArrayList<String>();
	private Map<String,Integer> wordMap = new TreeMap<String,Integer>();

	//variable for drjarnsAnalysis
	private ArrayList<String> drjarnsLines = new ArrayList<String>();

	// ============================================
	// ============================================
	// Backend methods used for counting, identifying, and organizing
	// ============================================
	// ============================================

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
			//this.textFileWriter(key, value);
		} // while loop
		System.out.println("============================================");
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

	// ============================================
	// ============================================
	// Methods used to conduct actual analyses on given data
	// ============================================
	// ============================================

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
			System.out.println("============================================");
			System.out.println("=============OVERALL WORD COUNT=============");
			System.out.println("============================================");
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
			System.out.println("============================================");
			System.out.println("==========OVERALL AUTHOR POST COUNT=========");
			System.out.println("============================================");
			this.wordFrequency(permAuthorGeneralLines, genAuthorMap);
			bufferedReader.close();
		} catch (FileNotFoundException ex) {
			System.out.println("Unable to open file '" + fileToRead + "'");
		} catch (IOException ex) {
			System.out.println("Error reading file '" + fileToRead + "'");
		} // catch
	}// generalAuthorAnalysis

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
			System.out.println("============================================");
			System.out.println("========SUCCESSFULLY INDEXED POSTS==========");
			System.out.println("============================================");
			bufferedReader.close();
		} catch (FileNotFoundException ex) {
			System.out.println("Unable to open file '" + fileToRead + "'");
		} catch (IOException ex) {
			System.out.println("Error reading file '" + fileToRead + "'");
		} // catch
	}// generalPostAnalysis

	// ============================================
	// ============================================
	// Data specific methods run to find meaning in the  general results
	// ============================================
	// ============================================

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
		nameCounter = 0;
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

	// ============================================
	// ============================================
	// Generalized methods to find info on specific word or author
	// ============================================
	// ============================================

	/**
	 * displays the word content/count and all the posts from a specifc user
	 * @param posts - data file containing posts
	 * @param authors - data file containing authors
	 * @param specificAuthor - String containing the characters that make up
	 * the name of the user you are searching for e.g. 'drjarns'
	 */
	// attempting to index the redditAuthor.txt
	public void specificAuthorAnalysis(String posts, String authors, String specificAuthor) {
		this.generalPostStorage(posts);
		System.out.println("Ready to read file.");
		line = null;
		nameCounter = 0;
		try {
			FileReader myFileReader = new FileReader(authors);
			System.out.println("I was able to open your file!");
			BufferedReader bufferedReader = new BufferedReader(myFileReader);
			while ((line = bufferedReader.readLine()) != null) {
				line.toLowerCase();
				nameCounter++;
				if (line.contains(specificAuthor)) {
					permAuthorLines.add(this.permPostGeneralLines.get(nameCounter));
					this.wordIdentifier(this.permPostGeneralLines.get(nameCounter), permAuthorLinesID);
				} // if
			} // while loop
			System.out.println("============================================");
			System.out.println("================WORD COUNT==================");
			System.out.println("============================================");
			this.wordFrequency(permAuthorLinesID, authorMap);
			System.out.println("================USER POSTS==================");
			System.out.println("============================================");
			for (String str : permAuthorLines) {
				System.out.println(str);
			} // for loop
			System.out.println("============================================");
			System.out.println("=========NUMBER OF " + specificAuthor + "'S POSTS=========");
			System.out.println("============================================");
			System.out.println(permAuthorLines.size());
			bufferedReader.close();
		} catch (FileNotFoundException ex) {
			System.out.println("Unable to open file '" + authors + "'");
		} catch (IOException ex) {
			System.out.println("Error reading file '" + authors + "'");
		} // catch
	}// specificAuthorAnalysis

	/**
	 * displays the word content/count and all the posts containing a specifc word
	 * @param posts - data file containing posts
	 * @param specificWord - String containing the characters that make up
	 * the name of the user you are searching for e.g. 'trump'
	 */
	public void specificWordAnalysis(String posts, String specificWord) {
		this.generalPostStorage(posts);
		System.out.println("Ready to read file.");
		line = null;
		nameCounter = 0;
		try {
			FileReader myFileReader = new FileReader(posts);
			System.out.println("I was able to open your file!");
			BufferedReader bufferedReader = new BufferedReader(myFileReader);
			while ((line = bufferedReader.readLine()) != null) {
				line.toLowerCase();
				nameCounter++;
				if (line.contains(specificWord)) {
					permWordLines.add(this.permPostGeneralLines.get(nameCounter));
					this.wordIdentifier(this.permPostGeneralLines.get(nameCounter), permWordLinesID);
				} // if
			} // while loop
			System.out.println("============================================");
			System.out.println("================WORD COUNT==================");
			System.out.println("============================================");
			this.wordFrequency(permWordLinesID, wordMap);
			System.out.println("==========POSTS CONTAINING '" + specificWord + "'==========");
			System.out.println("============================================");
			for (String str : permWordLines) {
				System.out.println(str);
			} // for loop
			System.out.println("============================================");
			System.out.println("======NUMBER OF POSTS CONTAINING '" + specificWord + "'====");
			System.out.println("============================================");
			System.out.println(permWordLines.size());
			bufferedReader.close();
		} catch (FileNotFoundException ex) {
			System.out.println("Unable to open file '" + posts + "'");
		} catch (IOException ex) {
			System.out.println("Error reading file '" + posts + "'");
		} // catch
	}// authorAnalysis

}// DataAnalyzer Class
