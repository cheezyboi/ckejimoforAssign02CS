import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;
import java.util.TreeMap;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;
import com.google.common.collect.MultimapBuilder;

public class DataAnalyzer {
	// ==========================================================================
	// ==========================================================================
	// Variables used in data analysis
	// ============================================
	// ============================================
	private String line = null;
	private ArrayList<String> tempStoredLines = new ArrayList<String>();
	private int nameCounter = 0;
	public String decisionMaker = null;
	private ArrayList<String> permLines = new ArrayList<String>();
	private ArrayList<String> permLinesID = new ArrayList<String>();
	private ArrayList<String> storagePost = new ArrayList<String>();
	private ArrayList<String> storageAuthor = new ArrayList<String>();
	private ArrayList<String> storageAuthorFiltered = new ArrayList<String>();
	private Map<String, Integer> map = new TreeMap<String, Integer>();
	private Multimap<String, String> mapByPost = ArrayListMultimap.create();

	// ==========================================================================
	// ==========================================================================
	// Backend methods used for counting, identifying, and organizing
	// ============================================
	// ============================================
	/**
	 * stores post file lines in arrayList
	 * 
	 * @param fileToRead
	 *            - the file that is being analyzed
	 */
	private void generalPostStorage(String fileToRead) {
		storagePost.clear();
		line = null;
		try {
			FileReader myFileReader = new FileReader(fileToRead);
			BufferedReader bufferedReader = new BufferedReader(myFileReader);
			while ((line = bufferedReader.readLine()) != null) {
				storagePost.add(line);
			} // while loop
				// this.commentTemplate("SUCCESSFULLY INDEXED POSTS");
			bufferedReader.close();
		} catch (FileNotFoundException ex) {
			System.out.println("Unable to open file '" + fileToRead + "'");
		} catch (IOException ex) {
			System.out.println("Error reading file '" + fileToRead + "'");
		} // catch
	}// generalPostStorage

	/**
	 * stores author file lines in arrayList
	 * 
	 * @param fileToRead
	 *            - the file that is being analyzed
	 */
	private void generalAuthorStorage(String author) {
		storageAuthor.clear();
		line = null;
		try {
			FileReader myFileReader = new FileReader(author);
			BufferedReader bufferedReader = new BufferedReader(myFileReader);
			while ((line = bufferedReader.readLine()) != null) {
				storageAuthor.add(line);
			} // while loop
				// this.commentTemplate("SUCCESSFULLY INDEXED AUTHORS");
			bufferedReader.close();
		} catch (FileNotFoundException ex) {
			System.out.println("Unable to open file '" + author + "'");
		} catch (IOException ex) {
			System.out.println("Error reading file '" + author + "'");
		} // catch
	}// generalAuthorStorage

	/**
	 * Reads each character in a line and organizes it in an ArrayList If it is a
	 * character from a - z When a word ends the program condenses the ArrayList
	 * into a string.
	 * 
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

	/**
	 * Counts the # of times a word occurs in data. Creates HashMap of the data. A
	 * HashMap is a collection class that stores key (the actual word) and value (#
	 * of times it appears) pairs
	 */
	private void wordFrequency(ArrayList<String> permList, Map<String, Integer> map) {
		Set<String> unique = new HashSet<String>(permList);
		for (String key : unique) {
			if (key.equalsIgnoreCase("") == false) {
				map.put(key, Collections.frequency(permList, key));
			}
		} // for loop

	}// wordFrequency

	/**
	 * Sorts wordFrequency map by value
	 * 
	 * @param unsortMap
	 *            - unsorted version of the map that is passed to the method
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

	/**
	 * Orders wordFrequency treeMap by the integer i.e. the # of times it occurs in
	 * the file
	 *
	 * @throws IOException
	 */
	@SuppressWarnings("rawtypes")
	private void printMap(Map<String, Integer> map) {
		Set s = map.entrySet();
		Iterator it = s.iterator();
		while (it.hasNext()) {
			Map.Entry entry = (Map.Entry) it.next();
			String key = (String) entry.getKey();
			Integer value = (Integer) entry.getValue();
			System.out.println(key + " => " + value);
			// this.textFileWriter(key, value);
		} // while loop
	}// printMap

	/**
	 * Filters wordFrequency by removing filler words and words that occur below a
	 * user-defined frequency
	 * 
	 * @param permList
	 *            - arrayList of lines that are being analyzed
	 * @param map
	 *            - map where frequency values are being stored with their
	 *            associated word
	 * @param number
	 *            - user defined frequency of word occurance
	 */
	private void wordFrequencyFiltered(ArrayList<String> permList, Map<String, Integer> map, int number) {
		Set<String> unique = new HashSet<String>(permList);
		for (String key : unique) {
			if (key.equalsIgnoreCase("this") == false && key.equalsIgnoreCase("the") == false
					&& key.equalsIgnoreCase("and") == false && key.equalsIgnoreCase("of") == false
					&& key.equalsIgnoreCase("a") == false && key.equalsIgnoreCase("for") == false
					&& key.equalsIgnoreCase("is") == false && key.equalsIgnoreCase("at") == false
					&& key.equalsIgnoreCase("on") == false && key.equalsIgnoreCase("it") == false
					&& key.equalsIgnoreCase("are") == false && key.equalsIgnoreCase("be") == false
					&& key.equalsIgnoreCase("that") == false && key.equalsIgnoreCase("in") == false
					&& key.equalsIgnoreCase("was") == false && key.equalsIgnoreCase("has") == false
					&& key.equalsIgnoreCase("as") == false && key.equalsIgnoreCase("so") == false
					&& key.equalsIgnoreCase("not") == false && key.equalsIgnoreCase("") == false
					&& key.equalsIgnoreCase("as") == false && key.equalsIgnoreCase("so") == false
					&& key.equalsIgnoreCase("an") == false && key.equalsIgnoreCase("its") == false
					&& key.equalsIgnoreCase("to") == false && key.equalsIgnoreCase("up") == false
					&& Collections.frequency(permList, key) == number) {
				map.put(key, Collections.frequency(permList, key));
			} else if (key.equalsIgnoreCase("this") == false && key.equalsIgnoreCase("the") == false
					&& key.equalsIgnoreCase("and") == false && key.equalsIgnoreCase("of") == false
					&& key.equalsIgnoreCase("a") == false && key.equalsIgnoreCase("for") == false
					&& key.equalsIgnoreCase("is") == false && key.equalsIgnoreCase("at") == false
					&& key.equalsIgnoreCase("on") == false && key.equalsIgnoreCase("it") == false
					&& key.equalsIgnoreCase("are") == false && key.equalsIgnoreCase("be") == false
					&& key.equalsIgnoreCase("that") == false && key.equalsIgnoreCase("in") == false
					&& key.equalsIgnoreCase("was") == false && key.equalsIgnoreCase("has") == false
					&& key.equalsIgnoreCase("as") == false && key.equalsIgnoreCase("so") == false
					&& key.equalsIgnoreCase("not") == false && key.equalsIgnoreCase("") == false
					&& key.equalsIgnoreCase("as") == false && key.equalsIgnoreCase("so") == false
					&& key.equalsIgnoreCase("an") == false && key.equalsIgnoreCase("its") == false
					&& key.equalsIgnoreCase("to") == false && key.equalsIgnoreCase("up") == false
					&& Collections.frequency(permList, key) >= number && number >= 5) {
				map.put(key, Collections.frequency(permList, key));
			} // else if
		} // for loop
	}// wordFrequency

	/**
	 * returns an arrayList of all of the authors that have posted only once
	 * 
	 * @param author
	 */
	public void authorsWOnePost(String author) {
		this.generalAuthorStorage(author);
		this.authorStorageFilter();
		System.out.println(storageAuthorFiltered);
	} // authorsWOnePost
		// ==========================================================================
		// ==========================================================================
		// methods for calculating percents
		// ============================================
		// ============================================

	/**
	 * returns a float value for the total number of users
	 * 
	 * @param permList
	 *            - index of authors
	 * @param map
	 *            - stores the post if it is from a user that posted the specified
	 *            amount of times
	 * @param postNumber
	 *            - number of posts that an author posted
	 * @param noInterations
	 *            - how many segments you want to break the data up into
	 * @return
	 */
	private float authorFrequency(ArrayList<String> permList, Map<String, Integer> map) {
		Set<String> unique = new HashSet<String>(permList);
		for (String key : unique) {
			if (key.equalsIgnoreCase("") == false) {
				map.put(key, Collections.frequency(permList, key));
			} // if
		} // for loop
		return map.size();
	} // authorFrequency

	/**
	 * returns a float value for the number of users that post 'x' times
	 * 
	 * @param permList
	 *            - index of authors
	 * @param map
	 *            - stores the post if it is from a user that posted the specified
	 *            amount of times
	 * @param postNumber
	 *            - number of posts that an author posted
	 * @param noInterations
	 *            - how many segments you want to break the data up into
	 * @return
	 */
	private float authorFrequencyFiltered(ArrayList<String> permList, Map<String, Integer> map, int postNumber,
			int noInterations) {
		map.clear();
		Set<String> unique = new HashSet<String>(permList);
		for (String key : unique) {
			if (postNumber < noInterations) {
				if (key.equalsIgnoreCase("") == false && Collections.frequency(permList, key) == postNumber) {
					map.put(key, Collections.frequency(permList, key));
				} // if frequency = postnubmer
			} else {
				if (key.equalsIgnoreCase("") == false && Collections.frequency(permList, key) >= postNumber) {
					map.put(key, Collections.frequency(permList, key));
				} // if frequency > postnubmer
			} // else
		} // for loop
		return map.size();
	} // authorFrequencyFiltered

	/**
	 * filters the author storage method for authors that have only posted once
	 */
	private void authorStorageFilter() {
		storageAuthorFiltered.clear();
		Set<String> unique = new HashSet<String>(storageAuthor);
		for (String key : unique) {
			if (key.equalsIgnoreCase("") == false && Collections.frequency(storageAuthor, key) == 1) {
				storageAuthorFiltered.add(key);
			} // if frequency = postnubmer
		} // for loop
		this.commentTemplate("AUTHOR POSTS FILTERED");
	}

	/**
	 * filters the author storage method for authors that posted > 20 times
	 */
	private void authorStorageFilter20() {
		storageAuthorFiltered.clear();
		Set<String> unique = new HashSet<String>(storageAuthor);
		for (String key : unique) {
			if (key.equalsIgnoreCase("") == false && Collections.frequency(storageAuthor, key) >= 20) {
				storageAuthorFiltered.add(key);
			} // if frequency = postnubmer
		} // for loop
		this.commentTemplate("AUTHOR POSTS FILTERED");
	}

	/**
	 * returns boolean of whether or not the string matches any of the items in the
	 * array
	 * 
	 * @param inputString
	 *            - the line in the file being analyzed
	 * @param items
	 *            - the array that the line is being analyzed against
	 * @return boolean of whether or not the string matches any of the items in the
	 *         array
	 */
	private boolean containsItemFromArray(String inputString, String[] items) {
		// Convert the array of String items as a Stream
		// For each element of the Stream call inputString.contains(element)
		// If you have any match returns true, false otherwise
		return Arrays.stream(items).anyMatch(inputString::contains);
	}

	// ==========================================================================
	// ==========================================================================
	// Methods used to conduct actual analyses on given data
	// ============================================
	// ============================================
	/**
	 * reads the file and identifies all words in the posts file this also works for
	 * the authors file but I kept them separate because I wanted them funneled into
	 * different variables
	 * 
	 * @param fileToRead
	 *            - the file that is being analyzed
	 */
	private void generalPostAnalysis(String fileToRead) {
		this.titleTemplate("Overall post analysis", "shows the frequency of ALL the words as they occur in the file");
		map.clear();
		permLines.clear();
		System.out.println("Ready to read file.");
		line = null;
		try {
			FileReader myFileReader = new FileReader(fileToRead);
			System.out.println("I was able to open your file!");
			BufferedReader bufferedReader = new BufferedReader(myFileReader);
			while ((line = bufferedReader.readLine()) != null) {
				line.toLowerCase();
				this.wordIdentifier(line, permLines);
			} // while loop
			bufferedReader.close();
		} catch (FileNotFoundException ex) {
			System.out.println("Unable to open file '" + fileToRead + "'");
		} catch (IOException ex) {
			System.out.println("Error reading file '" + fileToRead + "'");
		} // catch
		this.commentTemplate("OVERALL WORD COUNT");
		this.wordFrequency(permLines, map);
		Map<String, Integer> sortedMap = sortByValue(map);
		printMap(sortedMap);
	}// generalPostAnalysis

	/**
	 * generalPostAnalysis filtered to remove filler words and words that appear
	 * under 50 times
	 * 
	 * @param fileToRead
	 *            - file being analyzed
	 */
	private void generalPostAnalysisFiltered(String fileToRead) {
		this.titleTemplate("Overall post analysis filtered",
				"if frequency is above 450, shows the frequency of the words as they occur in the file");
		map.clear();
		permLines.clear();
		line = null;
		try {
			FileReader myFileReader = new FileReader(fileToRead);
			BufferedReader bufferedReader = new BufferedReader(myFileReader);
			while ((line = bufferedReader.readLine()) != null) {
				line.toLowerCase();
				this.wordIdentifier(line, permLines);
			} // while loop

			bufferedReader.close();
		} catch (FileNotFoundException ex) {
			System.out.println("Unable to open file '" + fileToRead + "'");
		} catch (IOException ex) {
			System.out.println("Error reading file '" + fileToRead + "'");
		} // catch
		this.commentTemplate("OVERALL WORD COUNT");
		this.wordFrequencyFiltered(permLines, map, 450);
		Map<String, Integer> sortedMap = sortByValue(map);
		printMap(sortedMap);
	}// generalPostAnalysisFiltered

	/**
	 * shows which authors posted the most in order from biggest to smallest
	 * 
	 * @param fileToRead
	 *            - the file that is being analyzed
	 */
	private void generalAuthorAnalysis(String fileToRead) {
		this.titleTemplate("Overall author analysis", "shows ALL usernames of authors and how many times they posted");
		map.clear();
		permLines.clear();
		System.out.println("Ready to read file.");
		line = null;
		try {
			FileReader myFileReader = new FileReader(fileToRead);
			System.out.println("I was able to open your file!");
			BufferedReader bufferedReader = new BufferedReader(myFileReader);
			while ((line = bufferedReader.readLine()) != null) {
				line.toLowerCase();
				this.wordIdentifier(line, permLines);
			} // while loop
			bufferedReader.close();
		} catch (FileNotFoundException ex) {
			System.out.println("Unable to open file '" + fileToRead + "'");
		} catch (IOException ex) {
			System.out.println("Error reading file '" + fileToRead + "'");
		} // catch
		this.commentTemplate("OVERALL AUTHOR POST COUNT");
		this.wordFrequency(permLines, map);
		Map<String, Integer> sortedMap = sortByValue(map);
		printMap(sortedMap);
	}// generalAuthorAnalysis

	/**
	 * shows which authors posted the most in order from biggest to smallest
	 * 
	 * @param fileToRead
	 *            - the file that is being analyzed
	 */
	private void generalAuthorAnalysisFiltered(String fileToRead) {
		this.titleTemplate("Overall author analysis filtered",
				"shows usernames ow authors who posted more than 20 times");
		map.clear();
		permLines.clear();
		line = null;
		try {
			FileReader myFileReader = new FileReader(fileToRead);
			BufferedReader bufferedReader = new BufferedReader(myFileReader);
			while ((line = bufferedReader.readLine()) != null) {
				line.toLowerCase();
				this.wordIdentifier(line, permLines);
			} // while loop
			bufferedReader.close();
		} catch (FileNotFoundException ex) {
			System.out.println("Unable to open file '" + fileToRead + "'");
		} catch (IOException ex) {
			System.out.println("Error reading file '" + fileToRead + "'");
		} // catch
		this.commentTemplate("OVERALL AUTHOR POST COUNT");
		this.wordFrequencyFiltered(permLines, map, 20);
		Map<String, Integer> sortedMap = sortByValue(map);
		printMap(sortedMap);
	}// generalAuthorAnalysis

	/**
	 * analysis that returns percent values for the number of users that post 'x'
	 * times
	 * 
	 * @param author
	 *            - text file that is being analyzed
	 */
	public void authorPercentAnalysis(String author) {
		this.titleTemplate("Author Analysis", "returns percent values for the number of users that post 'x' times");
		map.clear();
		this.generalAuthorStorage(author);
		line = null;
		float noOfAuthors = 0;
		float noOfAuthorsFiltered = 0;
		float percent = 0;
		float b = 0;
		int noInterations = 5;
		noOfAuthors = this.authorFrequency(storageAuthor, map);
		for (int i = 1; i <= noInterations; i++) {
			noOfAuthorsFiltered = this.authorFrequencyFiltered(storageAuthor, map, i, noInterations);
			percent = noOfAuthorsFiltered / noOfAuthors * 100;
			if (i == 1)
				System.out.printf("%-1.2f%% of authors posted %d time\n", percent, i);
			else if (i < noInterations) {
				b = noOfAuthorsFiltered + b;
				System.out.printf("%-1.2f%% of authors posted %d times\n", percent, i);
			} else if (i == noInterations) {
				b = noOfAuthorsFiltered + b;
				System.out.printf("%-1.2f%% of authors posted %d or more times\n", percent, i);
			}
		} // for loop
		float percentAffectedPosts = b / noOfAuthors * 100;
		System.out.printf("\nTotal of %-1.2f%% of users posted more than once\n", percentAffectedPosts);
	} // authorPercentAnalysis

	/**
	 * analysis of the posts of the authors that only posted once
	 * 
	 * @param post
	 *            - posts text file
	 * @param author
	 *            - authors text file
	 */
	private void onePostAuthorAnalysis(String post, String author) {
		this.titleTemplate("Analysis on authors who only posted once",
				"returns the most prevalant words, with above 200 occurrences, written by users who only posted once");
		permLines.clear();
		permLinesID.clear();
		map.clear();
		line = null;
		nameCounter = 0;
		this.generalPostStorage(post);
		this.generalAuthorStorage(author);
		this.authorStorageFilter();
		String[] a1Array = storageAuthorFiltered.toArray(new String[storageAuthorFiltered.size()]);
		try {
			FileReader myFileReader = new FileReader(author);
			BufferedReader bufferedReader = new BufferedReader(myFileReader);
			while ((line = bufferedReader.readLine()) != null) {
				line.toLowerCase();
				nameCounter++;
				if (this.containsItemFromArray(line, a1Array) == true) {
					permLines.add(this.storagePost.get(nameCounter - 1));
					this.wordIdentifier(this.storagePost.get(nameCounter - 1), permLinesID);
				}
			} // while loop
			bufferedReader.close();
		} catch (FileNotFoundException ex) {
			System.out.println("Unable to open file '" + author + "'");
		} catch (IOException ex) {
			System.out.println("Error reading file '" + author + "'");
		} // catch
		this.commentTemplate("WORD COUNT");
		this.wordFrequency(permLinesID, map);
		Map<String, Integer> sortedMap = sortByValue(map);
		printMap(sortedMap);
		this.commentTemplate("NUMBER OF POSTS FROM USERS WHO ONLY POSTED ONCE");
		System.out.println(permLines.size());
		System.out.println("=============================================================\n");
	} // onePostAuthorAnalysis

	/**
	 * analysis of the posts of the authors that only posted once
	 * 
	 * @param post
	 *            - posts text file
	 * @param author
	 *            - authors text file
	 */
	private void onePostAuthorAnalysisFiltered(String post, String author) {
		this.titleTemplate("Analysis on authors who only posted once",
				"returns the most prevalant words, with above 200 occurrences, written by users who only posted once");
		permLines.clear();
		permLinesID.clear();
		map.clear();
		line = null;
		nameCounter = 0;
		this.generalPostStorage(post);
		this.generalAuthorStorage(author);
		this.authorStorageFilter();
		String[] a1Array = storageAuthorFiltered.toArray(new String[storageAuthorFiltered.size()]);
		try {
			FileReader myFileReader = new FileReader(author);
			BufferedReader bufferedReader = new BufferedReader(myFileReader);
			while ((line = bufferedReader.readLine()) != null) {
				line.toLowerCase();
				nameCounter++;
				if (this.containsItemFromArray(line, a1Array) == true) {
					permLines.add(this.storagePost.get(nameCounter - 1));
					this.wordIdentifier(this.storagePost.get(nameCounter - 1), permLinesID);
				}
			} // while loop
			bufferedReader.close();
		} catch (FileNotFoundException ex) {
			System.out.println("Unable to open file '" + author + "'");
		} catch (IOException ex) {
			System.out.println("Error reading file '" + author + "'");
		} // catch
		this.commentTemplate("WORD COUNT");
		this.wordFrequencyFiltered(permLinesID, map, 50);
		Map<String, Integer> sortedMap = sortByValue(map);
		printMap(sortedMap);
		this.commentTemplate("NUMBER OF POSTS FROM USERS WHO ONLY POSTED ONCE");
		System.out.println(permLines.size());
	} // onePostAuthorAnalysis

	/**
	 * unfiltered analysis of the posts of the authors that only posted once
	 * 
	 * @param post
	 *            - posts text file
	 * @param author
	 *            - authors text file
	 */
	private void author20Analysis(String post, String author) {
		this.titleTemplate("Analysis on authors who posted 20+",
				"returns the most prevalant words, with above 200 occurrences, written by users who only posted once");
		permLines.clear();
		permLinesID.clear();
		map.clear();
		line = null;
		nameCounter = 0;
		this.generalPostStorage(post);
		this.generalAuthorStorage(author);
		this.authorStorageFilter20();
		String[] a1Array = storageAuthorFiltered.toArray(new String[storageAuthorFiltered.size()]);
		try {
			FileReader myFileReader = new FileReader(author);
			BufferedReader bufferedReader = new BufferedReader(myFileReader);
			while ((line = bufferedReader.readLine()) != null) {
				line.toLowerCase();
				nameCounter++;
				if (this.containsItemFromArray(line, a1Array) == true) {
					permLines.add(this.storagePost.get(nameCounter - 1));
					this.wordIdentifier(this.storagePost.get(nameCounter - 1), permLinesID);
				}
			} // while loop
			bufferedReader.close();
		} catch (FileNotFoundException ex) {
			System.out.println("Unable to open file '" + author + "'");
		} catch (IOException ex) {
			System.out.println("Error reading file '" + author + "'");
		} // catch
		this.commentTemplate("WORD COUNT");
		this.wordFrequency(permLinesID, map);
		Map<String, Integer> sortedMap = sortByValue(map);
		printMap(sortedMap);
		this.commentTemplate("NUMBER OF POSTS FROM USERS WHO POSTED 20+ TIMES");
		System.out.println(permLines.size());
		System.out.println("=============================================================\n");
	} // onePostAuthorAnalysis

	/**
	 * displays the amount of times a given user posted
	 * 
	 * @param authors
	 *            - data file containing authors
	 * @param specificAuthor
	 *            - String containing the characters that make up the name of the
	 *            user you are searching for e.g. 'drjarns'
	 */
	private void specificAuthorAnalysis(String posts, String authors, String specificAuthor) {
		this.titleTemplate("User analysis of authors",
				"returns all of the posts by the author with the username: " + specificAuthor);
		permLines.clear();
		permLinesID.clear();
		map.clear();
		line = null;
		nameCounter = 0;
		this.generalPostStorage(posts);
		this.generalAuthorStorage(authors);
		try {
			FileReader myFileReader = new FileReader(authors);
			BufferedReader bufferedReader = new BufferedReader(myFileReader);
			while ((line = bufferedReader.readLine()) != null) {
				line.toLowerCase();
				nameCounter++;
				if (line.contains(specificAuthor)) {
					permLines.add(this.storagePost.get(nameCounter - 1));
					this.wordIdentifier(this.storageAuthor.get(nameCounter - 1), permLinesID);
				}
			} // while loop
			bufferedReader.close();
		} catch (FileNotFoundException ex) {
			System.out.println("Unable to open file '" + authors + "'");
		} catch (IOException ex) {
			System.out.println("Error reading file '" + authors + "'");
		} // catch
		if (permLines.isEmpty())
			System.out.println("No posts associated with this username were found.");
		this.commentTemplate("POSTS BY '" + specificAuthor + "'");
		for (String str : permLines) {
			System.out.println(str);
		} // for loop
		this.commentTemplate("NUMBER OF POSTS FROM '" + specificAuthor + "'");
		System.out.println(permLines.size());
	}// specificAuthorAnalysis

	/**
	 * displays the word content/count and all the posts containing a specifc word
	 * 
	 * @param posts
	 *            - data file containing posts
	 * @param specificWord
	 *            - String containing the characters that make up the name of the
	 *            user you are searching for e.g. 'trump'
	 */
	private void specificWordAnalysis(String posts, String specificWord) {
		this.titleTemplate("User analysis of words",
				"returns all of the words that appear alongside '" + specificWord + "' in the file");
		permLines.clear();
		permLinesID.clear();
		map.clear();
		line = null;
		nameCounter = 0;
		this.generalPostStorage(posts);
		try {
			FileReader myFileReader = new FileReader(posts);
			BufferedReader bufferedReader = new BufferedReader(myFileReader);
			while ((line = bufferedReader.readLine()) != null) {
				line.toLowerCase();
				nameCounter++;
				if (line.contains(specificWord)) {
					permLines.add(this.storagePost.get(nameCounter - 1));
					this.wordIdentifier(this.storagePost.get(nameCounter - 1), permLinesID);
				} // if
			} // while loop
			bufferedReader.close();
		} catch (FileNotFoundException ex) {
			System.out.println("Unable to open file '" + posts + "'");
		} catch (IOException ex) {
			System.out.println("Error reading file '" + posts + "'");
		} // catch
		this.commentTemplate("WORD COUNT");
		if (permLinesID.isEmpty())
			System.out.println("No posts associated with this username were found.");
		this.wordFrequency(permLinesID, map);
		Map<String, Integer> sortedMap = sortByValue(map);
		printMap(sortedMap);
		this.commentTemplate("POSTS CONTAINING '" + specificWord + "'");
		if (permLines.isEmpty())
			System.out.println("No posts associated with this username were found.");
		for (String str : permLines) {
			System.out.println(str);
		} // for loop
		this.commentTemplate("NUMBER OF POSTS CONTAINING '" + specificWord + "'");
		System.out.println(permLines.size());
	}// authorAnalysis

	/**
	 * analyzes file for duplicate lines
	 * 
	 * @param posts
	 */
	public void postDuplicateAnalysis(String post, String author) {
		this.titleTemplate("Duplicate Post Analysis",
				"returns all of the posts that were reposted more than once, along with the frequency of repost");
		map.clear();
		storagePost.clear();
		this.generalPostStorage(post);
		this.wordFrequency(storagePost, map);
		Map<String, Integer> sortedMap = sortByValue(map);
		printMap(sortedMap);
		System.out.println("\nAn percent analysis of these posts show that:");
		float a = 0;
		float b = 0;
		for (int i = 2; i <= 5; i++) {
			a = this.postDuplicateAnalysisCounter(post, author, i);
			b = a + b;
			float percentAffectedPosts = a / 6466 * 100;
			if (i < 5)
				System.out.printf("%-1.2f%% of posts were posted %d times\n", percentAffectedPosts, i);
			if (i == 5)
				System.out.printf("%-1.2f%% of posts were posted %d or more times\n", percentAffectedPosts, i);
		}
		float percentAffectedPosts = b / 6466 * 100;
		System.out.printf("Total of %-1.2f%% of posts were affected by reposting\n", percentAffectedPosts);

	} // postDuplicateAnalysis

	/**
	 * analyzes file for duplicate lines filtered for filler words and prints
	 * associated authors
	 * 
	 * @param posts
	 */
	private void postDuplicateAnalysisFiltered(String post, String author) {
		this.titleTemplate("Detailed Duplicate Post Analysis using Guava Multimmaps",
				"returns all of the posts that were reposted more than once, along with the frequency of repost, and the authors");
		map.clear();
		permLines.clear();
		permLinesID.clear();
		storagePost.clear();
		this.generalPostStorage(post);
		this.generalAuthorStorage(author);
		this.wordFrequencyFiltered(storagePost, map, 2);
		nameCounter = 0;
		Set<String> keys = map.keySet();
		String[] keysArray = keys.toArray(new String[keys.size()]);
		map.clear();
		try {
			FileReader myFileReader = new FileReader(post);
			BufferedReader bufferedReader = new BufferedReader(myFileReader);
			while ((line = bufferedReader.readLine()) != null) {
				line.toLowerCase();
				nameCounter++;
				for (int i = 0; i < keysArray.length; i++) {
					if (line.contains(keysArray[i])) {
						map.put(keysArray[i], i);
						mapByPost.put(keysArray[i], this.storageAuthor.get(nameCounter - 1));
						this.wordIdentifier(this.storageAuthor.get(nameCounter - 1), permLinesID);
					} // if
				} // for loop
			} // while loop
			bufferedReader.close();
		} catch (FileNotFoundException ex) {
			System.out.println("Unable to open file '" + post + "'");
		} catch (IOException ex) {
			System.out.println("Error reading file '" + post + "'");
		} // catch
		Multimap<String, String> multimap = MultimapBuilder.treeKeys().linkedListValues().build(mapByPost);
		// prints full dataset as requested by user
		this.commentTemplate("SUSPECTED BOTS & THEIR POSTS");
		for (String posts : multimap.keySet()) {
			List<String> authors = (List<String>) mapByPost.get(posts);
			System.out.println(authors + ": " + posts);
		} // for loop
		this.commentTemplate("PERCENTAGE OF AFFECTED POSTS");
		float b = 0;
		for (int i = 2; i <= 5; i++) {
			float a = this.postDuplicateAnalysisCounter(post, author, i);
			b = a + b;
		}
		// float percentAffectedPosts = b/6466*100;
		// System.out.println(percentAffectedPosts);
	} // postDuplicateAnalysisFiltered

	/**
	 * analyzes file for duplicate lines filtered for filler words and prints
	 * associated authors
	 * 
	 * @param posts
	 */
	private float postDuplicateAnalysisCounter(String post, String author, int number) {
		map.clear();
		permLines.clear();
		permLinesID.clear();
		storagePost.clear();
		this.generalPostStorage(post);
		this.generalAuthorStorage(author);
		this.wordFrequencyFiltered(storagePost, map, number);
		nameCounter = 0;
		Set<String> keys = map.keySet();
		String[] keysArray = keys.toArray(new String[keys.size()]);
		map.clear();
		try {
			FileReader myFileReader = new FileReader(post);
			BufferedReader bufferedReader = new BufferedReader(myFileReader);
			while ((line = bufferedReader.readLine()) != null) {
				line.toLowerCase();
				nameCounter++;
				for (int i = 0; i < keysArray.length; i++) {
					if (line.contains(keysArray[i])) {
						map.put(keysArray[i], i);
					} // if
				} // for loop
			} // while loop
			bufferedReader.close();
		} catch (FileNotFoundException ex) {
			System.out.println("Unable to open file '" + post + "'");
		} catch (IOException ex) {
			System.out.println("Error reading file '" + post + "'");
		} // catch
		float postsAffected = map.size() * number;
		return postsAffected;
	} // postDuplicateAnalysisFiltered
		// ==========================================================================
		// ==========================================================================
		// Template for comments in console
		// ============================================
		// ============================================

	/**
	 * this is a template for the comments that pring out in the console
	 * 
	 * @param comment
	 */
	private void commentTemplate(String comment) {
		System.out.println("================" + comment + "==================");
	}

	/**
	 * this is a template for the title of each analysis that prints in the console
	 * 
	 * @param title
	 * @param explanation
	 */
	private void titleTemplate(String title, String explanation) {
		System.out.println("\n=====**************** " + title + " ******************=====");
		System.out.println("(" + explanation + ")\n");
	}

	// ==========================================================================
	// ==========================================================================
	// Narrative creation
	// ============================================
	// ============================================
	/**
	 * narrative code for general post analyses
	 * 
	 * @param post
	 *            - file to be analyzed
	 * @param decisionFlow
	 *            - user decision
	 */
	public void generalPostAnalysesN(String post, String decisionFlow) {
		Scanner input = new Scanner(System.in);
		if (decisionFlow.equalsIgnoreCase("y")) {
			System.out.println("\n****Be patient while the date loads... Thank you.****");
			this.generalPostAnalysisFiltered(post);
			System.out.println("=============================================================");
			System.out.println("=============================================================");
			System.out.println("\nThis is a general look at the words that occur in the file. \n"
					+ "I filtered out filler words and words that occur less than 450 times.\n "
					+ "This is a cool bird's eye view but doesn't tell us much since this is \n"
					+ "the type of convo/words that would be present in a thread like this. \n" + "Moving on...");
			System.out.println("\n=============================================================");
			System.out.println(
					"Enter [f] if you want to view the full unfiltered data set, [c] if you want to continue the narrative, \n"
							+ "[end] to go to conclusion");
			decisionFlow = input.nextLine();
			this.generalPostAnalysesN(post, decisionFlow);
		} else if (decisionFlow.equalsIgnoreCase("f")) {
			this.generalPostAnalysis(post);
		} else if (decisionFlow.equalsIgnoreCase("c")) {
			System.out.println("\nOnward!");
		} else if (decisionFlow.equalsIgnoreCase("end")) {
			this.conclusion();
		} else {
			System.out.println("\nEither you misspelled or you chose something ouside of your options.");
			System.out.println("Enter [f] for full dataset, [y] to continue narrative, [end] to go to conclusion");
			decisionFlow = input.nextLine();
			this.generalPostAnalysesN(post, decisionFlow);
		} // if statement
	} // generalAuthorAnalysesN

	/**
	 * narrative code for author percent analysis
	 * 
	 * @param author
	 *            - author file ot be analyzed
	 * @param decisionFlow
	 *            - user decision
	 */
	public void authorPercentAnalysisN(String author, String decisionFlow) {
		Scanner input = new Scanner(System.in);
		if (decisionFlow.equalsIgnoreCase("y")) {
			this.authorPercentAnalysis(author);
			System.out.println("=============================================================");
			System.out.println("=============================================================");
			System.out.println("\nThis indicates that most of the users are likely real people (~64%)\n"
					+ "who stumbled upon this reddit thread and decided to engage. \n"
					+ "The bottom ~35% are the authors that need to be analyzed more closely \n"
					+ "specifically for reposting (duplicates) and for content.\n\n"
					+ "First though, let's take a look at the content of the user's who \n" + "only posted once.");
		} else if (decisionFlow.equalsIgnoreCase("end")) {
			this.conclusion();
		} else {
			System.out.println("\nEither you misspelled or you chose something ouside of your options.");
			System.out.println("Enter [y] to continue narrative, [end] to go to conclusion");
			decisionFlow = input.nextLine();
			this.authorPercentAnalysisN(author, decisionFlow);
		} // if statement
	} // authorPercentAnalysisN

	/**
	 * narrative for analysis of authors who only posted once
	 * 
	 * @param post
	 *            - file to be analyzed
	 * @param author
	 *            - file to be analyzed
	 * @param decisionFlow
	 *            - user decision
	 */
	public void authorOneAnalysisN(String post, String author, String decisionFlow) {
		Scanner input = new Scanner(System.in);
		if (decisionFlow.equalsIgnoreCase("y")) {
			System.out.println("\n****Be patient while the date loads... Thank you.****");
			this.onePostAuthorAnalysisFiltered(post, author);
			System.out.println("=============================================================");
			System.out.println("=============================================================");
			System.out.println("\nThere doesn't seem to be any trend visible here. Prepositions and \n"
					+ "filler words removed, there doesn't seem like much going on other than general \n"
					+ "conversation. The top words after 'Trump' is the word 'you'. This could indicate \n"
					+ "users are very concerned with the plight of their fellow internet surfers. \n\n"
					+ "It's too early to tell now though. Let's plough on.");
			System.out.println("\n=============================================================");
			System.out.println(
					"Enter [f] if you want to view the full unfiltered data set, [c] if you want to continue the narrative");
			decisionFlow = input.nextLine();
			this.authorOneAnalysisN(post, author, decisionFlow);
		} else if (decisionFlow.equalsIgnoreCase("end")) {
			this.conclusion();
		} else if (decisionFlow.equalsIgnoreCase("c")) {
			System.out.println("\nContinuing on...\n");
		} else if (decisionFlow.equalsIgnoreCase("f")) {
			System.out.println("\n****Be patient while the date loads... Thank you.****");
			this.onePostAuthorAnalysis(post, author);
			System.out.println("\n=============================================================");
			System.out.println(
					"Enter [y] if you want to see the filtered set again, [f] if you want to view the full unfiltered data set again \n"
							+ "[c] if you want to continue the narrative, [end] if you want to view the conclusion.");
			decisionFlow = input.nextLine();
			this.authorOneAnalysisN(post, author, decisionFlow);
		} else {
			System.out.println("\nEither you misspelled or you chose something ouside of your options.");
			System.out.println("Enter [y] to continue narrative, [end] to go to conclusion");
			decisionFlow = input.nextLine();
			this.authorOneAnalysisN(post, author, decisionFlow);
		} // if statement
	} // authorOneAnalysisN

	/**
	 * narrative code for the general Author analyses
	 * 
	 * @param author
	 *            - file to be analyzed
	 * @param decisionFlow
	 *            - user decision
	 */
	public void generalAuthorAnalysesN(String author, String decisionFlow) {
		Scanner input = new Scanner(System.in);
		if (decisionFlow.equalsIgnoreCase("y")) {
			this.generalAuthorAnalysisFiltered(author);
			System.out.println("\n=============================================================");
			System.out.println("=============================================================");
			System.out.println("\nNow here we go! something interesting... these are the authors who \n"
					+ "posted more than 20 times in this thread. I wonder what they were posting \n" + "about...");
			System.out.println("\n=============================================================");
			System.out.println(
					"Enter [f] if you want to view the full unfiltered data set, [c] if you want to continue the narrative");
			decisionFlow = input.nextLine();
			this.generalAuthorAnalysesN(author, decisionFlow);
		} else if (decisionFlow.equalsIgnoreCase("f")) {
			this.generalAuthorAnalysis(author);
		} else if (decisionFlow.equalsIgnoreCase("end")) {
			this.conclusion();
		} else if (decisionFlow.equalsIgnoreCase("c")) {
			System.out.println("\nContinuing on...\n");
		} else {
			System.out.println("\nEither you misspelled or you chose something ouside of your options.");
			System.out.println("Enter [f] for full dataset, [y] to continue narrative, [end] to go to conclusion");
			decisionFlow = input.nextLine();
			this.generalAuthorAnalysesN(author, decisionFlow);
		} // if statement
	} // generalAuthorAnalysesN

	/**
	 * narrative for analysis of authos that posted 20+ times
	 * 
	 * @param post
	 *            - file to be analyzed
	 * @param author
	 *            - file to be analyzed
	 * @param decisionFlow
	 *            - user decision
	 */
	public void authorOneAnalysis20N(String post, String author, String decisionFlow) {
		Scanner input = new Scanner(System.in);
		if (decisionFlow.equalsIgnoreCase("y")) {
			System.out.println("\n****Be patient while the date loads... Thank you.****");
			this.author20Analysis(post, author);
			System.out.println("Again, there doesn't seem to be any trend visible here. \n"
					+ "For each user, the individual posts need to be qualitatively looked at.\n"
					+ "Still, maybe we can find some trend when we look for duplicates.\n"
					+ "users are very concerned with the plight of their fellow internet surfers. \n\n"
					+ "Don't worry! You'll have the chance to look at individual posts organized \n"
					+ "by username later if you want. For now, let's plough on...");
			System.out.println("\n=============================================================");
		} else if (decisionFlow.equalsIgnoreCase("end")) {
			this.conclusion();
		} else {
			System.out.println("\nEither you misspelled or you chose something ouside of your options.");
			System.out.println("Enter [y] to continue narrative, [end] to go to conclusion");
			decisionFlow = input.nextLine();
			this.authorOneAnalysis20N(post, author, decisionFlow);
		} // if statement
	} // authorOneAnalysisN

	/**
	 * narrative code for duplicate analyses
	 * 
	 * @param post
	 * @param author
	 * @param decisionFlow
	 */
	public void duplicateAnalysesN(String post, String author, String decisionFlow) {
		Scanner input = new Scanner(System.in);
		if (decisionFlow.equalsIgnoreCase("y")) {
			this.postDuplicateAnalysis(post, author);
			System.out.println("\n=============================================================");
			System.out.println("=============================================================");
			System.out.println("Interesting... The top post was a meme that was used to troll the left, \n"
					+ "the second was associated with obamacare, and the third was a big unconfirmed story \n"
					+ "about devil worshiping and wierd shit in the Clinton campaign. \n\n"
					+ "Further than this, we see that ~12% of posts were posted twice or more. This specific \n"
					+ "number of reposts is significant because it indicates likely bot activity if the \n"
					+ "post is reposted by the same user more than once. Let's check that...\n");
			System.out.println("=============================================================");
			System.out.println("=============================================================");
			System.out.println("Enter [y] to continue narrative, [end] to go to conclusion");
			decisionFlow = input.nextLine();
			this.duplicateAnalysesN2(post, author, decisionFlow);
		} else if (decisionFlow.equalsIgnoreCase("end")) {
			this.conclusion();
		} else {
			System.out.println("\nEither you misspelled or you chose something ouside of your options.");
			System.out.println("Enter [y] to continue narrative, [end] to go to conclusion");
			decisionFlow = input.nextLine();
			this.duplicateAnalysesN(post, author, decisionFlow);
		} // else statement
	} // duplicateAnalysesN

	/**
	 * narrative code that finds out if users can visualize the guava multimap
	 * 
	 * @param post
	 * @param author
	 * @param decisionFlow
	 */
	public void duplicateAnalysesN2(String post, String author, String decisionFlow) {
		Scanner input = new Scanner(System.in);
		if (decisionFlow.equalsIgnoreCase("y")) {
			System.out.println("\n****IMPORTANT****");
			System.out.println("This analysis requires the use of Guava Multimaps. The outpot will not \n"
					+ "be correct if you do not have this installed. You can't run this part of the code \n"
					+ "in cmd or terminal. If this is an issue, continue past this section by entering [n]");
			System.out.println("\nHave you added the guava-23.0 jar to this project's build path? [y] [n]");
			decisionFlow = input.nextLine();
			this.duplicateAnalysesAuthor(post, author, decisionFlow);
		} else if (decisionFlow.equalsIgnoreCase("end")) {
			this.conclusion();
		} else {
			System.out.println("\nEither you misspelled or you chose something ouside of your options.");
			System.out.println("Enter [y] to continue narrative, [end] to go to conclusion");
			decisionFlow = input.nextLine();
			this.duplicateAnalysesN(post, author, decisionFlow);
		}
	}

	/**
	 * narrative code that brings up multimap code once user has it installed
	 * 
	 * @param post
	 * @param author
	 * @param decisionFlow
	 */
	private void duplicateAnalysesAuthor(String post, String author, String decisionFlow) {
		Scanner input = new Scanner(System.in);
		if (decisionFlow.equalsIgnoreCase("y")) {
			this.postDuplicateAnalysisFiltered(post, author);
			System.out.println("\n=============================================================");
			System.out.println("=============================================================");
			System.out.println("If you see the data correctly, you'll see that for the majority of \n"
					+ "posts that two repostings (~11% of the data), it was the same user that reposted. \n"
					+ "These people make up a small group of individuals who only posted"
					+ "in the thread a few times with repetetive content. This is indicative of \n"
					+ "bot activity. Which leads me to my conclusion...");
			System.out.println("=============================================================");
			System.out.println("=============================================================");
		} else if (decisionFlow.equalsIgnoreCase("n")) {
			this.conclusion();
		} else {
			System.out.println("\nEither you misspelled or you chose something ouside of your options.");
			System.out.println("Enter [y] to have Guava, [n] if you don't to go to conclusion");
			decisionFlow = input.nextLine();
			this.duplicateAnalysesAuthor(post, author, decisionFlow);
		}
	}

	/**
	 * narrative code for user authorized specific author analysis
	 * 
	 * @param post
	 *            - file to be analyzed
	 * @param author
	 *            - file to be analyzed
	 * @param decisionFlow
	 *            - user decision
	 */
	public void specificAnalysesN(String post, String author, String decisionFlow) {
		Scanner input = new Scanner(System.in);
		if (decisionFlow.equalsIgnoreCase("y")) {
			System.out.println("You can search what words appeared alongside other words e.g. \n"
					+ "when clinton comes up, what other words are shown...\n");
			System.out.println(
					"You can also look up specific users posts e.g. \n" + "enter 'drjarnes' to see all of his posts");
			System.out.println("\n=============================================================");
			System.out.println("Enter [w] for a wordsearch, [a] for an author post search, [end] to go to conclusion");
			decisionFlow = input.nextLine();
			this.specificAnalysesN2(post, author, decisionFlow);
		} else if (decisionFlow.equalsIgnoreCase("n")) {
			this.conclusion();
		} else {
			System.out.println("\nEither you misspelled or you chose something ouside of your options.");
			System.out.println("Enter [y] if you want to do your own analysis, [n] to go to conclusion");
			decisionFlow = input.nextLine();
			this.specificAnalysesN(post, author, decisionFlow);
		} // if statement
	}

	/**
	 * narrative code that enables the user to choose what type of analysis they
	 * want
	 * 
	 * @param post
	 *            - file to be analyzed
	 * @param author
	 *            - file to be analyzed
	 * @param decisionFlow
	 *            - user decision
	 */
	private void specificAnalysesN2(String post, String author, String decisionFlow) {
		Scanner input = new Scanner(System.in);
		if (decisionFlow.equalsIgnoreCase("w")) {
			System.out.println("what word do you want to search for?");
			String search = input.nextLine();
			this.specificWordAnalysis(post, search);
			System.out.println("=============================================================");
			System.out.println("=============================================================");
			System.out.println("Do you want to conduct another analysis? [y] [n]");
			decisionFlow = input.nextLine();
			this.specificAnalysesN2(post, author, decisionFlow);
		} else if (decisionFlow.equalsIgnoreCase("a")) {
			// lists the top posters for the user to choose
			this.generalAuthorAnalysisFiltered(author);
			System.out.println("=============================================================");
			System.out.println("Above is a list of the top posters. Pick one to analyze by typing in their name.");
			System.out.println("Or type [full] to view the full list of names to choose from, \n"
					+ "or [end] to go to the conclusion");
			String search = input.nextLine();
			this.specificAuthorAnalysis(post, author, search);
			System.out.println("\n=============================================================");
			System.out.println("=============================================================");
			System.out.println("Do you want to conduct another analysis? [y] [n]");
			decisionFlow = input.nextLine();
			this.specificAnalysesN2(post, author, decisionFlow);
		} else if (decisionFlow.equalsIgnoreCase("full")) {
			// lists all of the authors in the file for the user to choose
			this.generalAuthorAnalysis(author);
			System.out.println("=============================================================");
			System.out
					.println("Above is a list of the all of the authors. Pick one to analyze by typing in their name.");
			String search = input.nextLine();
			this.specificAuthorAnalysis(post, author, search);
			System.out.println("\n=============================================================");
			System.out.println("=============================================================");
			System.out.println("Do you want to conduct another analysis? [y] [n]");
			decisionFlow = input.nextLine();
			this.specificAnalysesN2(post, author, decisionFlow);
		} else if (decisionFlow.equalsIgnoreCase("end") || decisionFlow.equalsIgnoreCase("n")) {
			// ends the thread
			this.conclusion();
		} else {
			System.out.println("\nEither you misspelled or you chose something ouside of your options.");
			System.out.println("Enter [w] for a wordsearch, [a] for an author post search, \n"
					+ "[full for the full author list or [end] to go to conclusion");
			decisionFlow = input.nextLine();
			this.specificAnalysesN2(post, author, decisionFlow);
		} // if statement
	}

	/**
	 * conclusion of code with link to full blogpost
	 */
	private void conclusion() {
		System.out.println("\n=============================================================");
		System.out.println("=============================================================");
		this.commentTemplate("CONCLUSION");
		System.out.println("=============================================================");
		System.out.println("=============================================================");
		System.out.println("This thread is made up mostly of individuals, individuals that came \n"
				+ "to this thread for one reason or another and left with very little engagement \n"
				+ "(64% of users only posted once). From here, a cursory glance at the top posters \n"
				+ "and their content, you don't find a many duplicates. My hypothesis here is that \n"
				+ "these are real people too. Overzealous passionate people who wanted their candidate \n"
				+ "to win. You can check this out your self by searching for the top users 'drjarns', \n"
				+ "'simi','trump', etc. (using the tool at the end) and you'll see this trend.");
		System.out.println("Here is a link to the full blog post.");
		System.out.println("https://medium.com/@cheez2012/trump-reddit-post-cs110-assignment-2-b789612ea54e");
	} // line
}// DataAnalyzer Class
