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
import java.util.Set;
import java.util.TreeMap;

public class DataAnalyzer {
		private String line = null;
	private ArrayList<String> tempStoredLines = new ArrayList<String>();
	private int nameCounter = 0;
	private ArrayList<String> permLines = new ArrayList<String>();
	private ArrayList<String> permLinesID = new ArrayList<String>();
	private ArrayList<String> storagePost = new ArrayList<String>();
	private ArrayList<String> storageAuthor = new ArrayList<String>();
	private ArrayList<String> storageAuthorFiltered = new ArrayList<String>();
	private Map<String, Integer> map = new TreeMap<String, Integer>();
	private Map<String, String> mapForDuplicateAnalysis = new TreeMap<String, String>();

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
	public void generalPostStorage(String fileToRead) {
		storagePost.clear();
		System.out.println("Ready to read file.");
		line = null;
		try {
			FileReader myFileReader = new FileReader(fileToRead);
			System.out.println("I was able to open your file!");
			BufferedReader bufferedReader = new BufferedReader(myFileReader);
			while ((line = bufferedReader.readLine()) != null) {
				storagePost.add(line);
			} // while loop
			this.commentTemplate("SUCCESSFULLY INDEXED POSTS");
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
	public void generalAuthorStorage(String author) {
		storageAuthor.clear();
		line = null;
		System.out.println("Ready to read file.");
		try {
			FileReader myFileReader = new FileReader(author);
			System.out.println("I was able to open your file!");
			BufferedReader bufferedReader = new BufferedReader(myFileReader);
			while ((line = bufferedReader.readLine()) != null) {
				storageAuthor.add(line);
			} // while loop
			this.commentTemplate("SUCCESSFULLY INDEXED POSTS");
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
		Map<String, Integer> sortedMap = sortByValue(map);
		printMap(sortedMap);
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
	 * Orders wordFrequency treeMap by the integer i.e. the # of times it occurs in the file
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
			// this.textFileWriter(key, value);
		} // while loop
	}// printMap
	/**
	 * Filters wordFrequency by removing filler words and words that occur below a user-defined frequency
	 * @param permList - arrayList of lines that are being analyzed
	 * @param map - map where frequency values are being stored with their associated word
	 * @param number - user defined frequency of word occurance
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
					&& Collections.frequency(permList, key) >= number) {
				map.put(key, Collections.frequency(permList, key));
			}
		} // for loop
		Map<String, Integer> sortedMap = sortByValue(map);
		printMap(sortedMap);
	}// wordFrequency
	// ==========================================================================
	// ==========================================================================
	// methods for calculating percents
	// ============================================
	// ============================================
	/**
	 * returns a float value for the total number of users
	 * @param permList - index of authors
	 * @param map - stores the post if it is from a user that posted the specified amount of times
	 * @param postNumber - number of posts that an author posted
	 * @param noInterations - how many segments you want to break the data up into
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
	 * @param permList - index of authors
	 * @param map - stores the post if it is from a user that posted the specified amount of times
	 * @param postNumber - number of posts that an author posted
	 * @param noInterations - how many segments you want to break the data up into
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
	 * returns percent values for the number of users that post 'x' times
	 * @param author - text file that is being analyzed
	 */
	public void authorPercentAnalysis(String author) {
		map.clear();
		this.generalAuthorStorage(author);
		line = null;
		float noOfAuthors = 0;
		float noOfAuthorsFiltered = 0;
		float percent = 0;
		int noInterations = 5;
		noOfAuthors = this.authorFrequency(storageAuthor, map);
		for (int i = 1; i <= noInterations; i++) {
			noOfAuthorsFiltered = this.authorFrequencyFiltered(storageAuthor, map, i, noInterations);
			percent = noOfAuthorsFiltered / noOfAuthors * 100;
			System.out.println(percent);
		}
	} // authorPercentAnalysis
	/**
	 * filters the author storage method for authors that have only posted once
	 */
	public void authorStorageFilter() {
		Set<String> unique = new HashSet<String>(storageAuthor);
		for (String key : unique) {
			if (key.equalsIgnoreCase("") == false && Collections.frequency(storageAuthor, key) == 1) {
				storageAuthorFiltered.add(key);
			} // if frequency = postnubmer
		} // for loop
		this.commentTemplate("AUTHOR POSTS FILTERED");
	}
	/**
	 * returns boolean of whether or not the string matches any of the items in the array
	 * @param inputString - the line in the file being analyzed
	 * @param items - the array that the line is being analyzed against
	 * @return boolean of whether or not the string matches any of the items in the array
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
	public void generalPostAnalysis(String fileToRead) {
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
			this.commentTemplate("OVERALL WORD COUNT");
			this.wordFrequency(permLines, map);
			bufferedReader.close();
		} catch (FileNotFoundException ex) {
			System.out.println("Unable to open file '" + fileToRead + "'");
		} catch (IOException ex) {
			System.out.println("Error reading file '" + fileToRead + "'");
		} // catch
	}// generalPostAnalysis
	/**
	 * generalPostAnalysis filtered to remove filler words and words that appear under 50 times
	 * @param fileToRead - file being analyzed
	 */
	public void generalPostAnalysisFiltered(String fileToRead) {
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
			this.commentTemplate("OVERALL WORD COUNT");
			this.wordFrequencyFiltered(permLines, map, 50);
			bufferedReader.close();
		} catch (FileNotFoundException ex) {
			System.out.println("Unable to open file '" + fileToRead + "'");
		} catch (IOException ex) {
			System.out.println("Error reading file '" + fileToRead + "'");
		} // catch
	}// generalPostAnalysisFiltered
	/**
	 * shows which authors posted the most in order from biggest to smallest
	 * 
	 * @param fileToRead
	 *            - the file that is being analyzed
	 */
	public void generalAuthorAnalysis(String fileToRead) {
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
			this.commentTemplate("OVERALL AUTHOR POST COUNT");
			this.wordFrequency(permLines, map);
			bufferedReader.close();
		} catch (FileNotFoundException ex) {
			System.out.println("Unable to open file '" + fileToRead + "'");
		} catch (IOException ex) {
			System.out.println("Error reading file '" + fileToRead + "'");
		} // catch
	}// generalAuthorAnalysis
	/**
	 * returns an arrayList of all of the authors that have posted only once
	 * @param author
	 */
	public void authorsWOnePost(String author) {
		this.generalAuthorStorage(author);
		this.authorStorageFilter();
		System.out.println(storageAuthorFiltered);
	} // authorsWOnePost
	/**
	 * analysis of the posts of the authors that only posted once
	 * @param post - posts text file
	 * @param author - authors text file
	 */
	public void onePostAuthorAnalysis(String post, String author) {
		permLines.clear();
		permLinesID.clear();
		map.clear();
		line = null;
		nameCounter = 0;
		this.generalPostStorage(post);
		this.generalAuthorStorage(author);
		this.authorStorageFilter();
		String[] a1Array = storageAuthorFiltered.toArray(new String[storageAuthorFiltered.size()]);
		System.out.println("Ready to read file.");
		try {
			FileReader myFileReader = new FileReader(author);
			System.out.println("I was able to open your file!");
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
		this.wordFrequencyFiltered(permLinesID, map, 200);
		this.commentTemplate("NUMBER OF POSTS FROM USERS WHO ONLY POSTED ONCE");
		System.out.println(permLines.size());

	}
	/**
	 * displays the amount of times a given user posted
	 * @param authors
	 *            - data file containing authors
	 * @param specificAuthor
	 *            - String containing the characters that make up the name of the
	 *            user you are searching for e.g. 'drjarns'
	 */
	public void specificAuthorAnalysis(String authors, String specificAuthor) {
		permLines.clear();
		permLinesID.clear();
		map.clear();
		line = null;
		nameCounter = 0;
		this.generalAuthorStorage(authors);
		System.out.println("Ready to read file.");
		try {
			FileReader myFileReader = new FileReader(authors);
			System.out.println("I was able to open your file!");
			BufferedReader bufferedReader = new BufferedReader(myFileReader);
			while ((line = bufferedReader.readLine()) != null) {
				line.toLowerCase();
				nameCounter++;
				if (line.contains(specificAuthor)) {
					permLines.add(this.storageAuthor.get(nameCounter - 1));
					this.wordIdentifier(this.storageAuthor.get(nameCounter - 1), permLinesID);
				} // if
			} // while loop
			bufferedReader.close();
		} catch (FileNotFoundException ex) {
			System.out.println("Unable to open file '" + authors + "'");
		} catch (IOException ex) {
			System.out.println("Error reading file '" + authors + "'");
		} // catch
		this.commentTemplate("WORD COUNT");
		this.wordFrequency(permLinesID, map);
		this.commentTemplate("POSTS CONTAINING '" + specificAuthor + "'");
		for (String str : permLines) {
			System.out.println(str);
		} // for loop
		this.commentTemplate("NUMBER OF POSTS CONTAINING '" + specificAuthor + "'");
		System.out.println(permLines.size());
		System.out.println(nameCounter);
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
	public void specificWordAnalysis(String posts, String specificWord) {
		permLines.clear();
		permLinesID.clear();
		map.clear();
		line = null;
		nameCounter = 0;
		this.generalPostStorage(posts);
		System.out.println("Ready to read file.");
		try {
			FileReader myFileReader = new FileReader(posts);
			System.out.println("I was able to open your file!");
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
		this.wordFrequency(permLinesID, map);
		this.commentTemplate("POSTS CONTAINING '" + specificWord + "'");
		for (String str : permLines) {
			System.out.println(str);
		} // for loop
		this.commentTemplate("NUMBER OF POSTS CONTAINING '" + specificWord + "'");
		System.out.println(permLines.size());
	}// authorAnalysis
	/**
	 * analyzes file for duplicate lines
	 * @param posts
	 */
	public void postDuplicateAnalysis(String posts) {
		map.clear();
		storagePost.clear();
		this.generalPostStorage(posts);
		this.wordFrequency(storagePost, map);
		this.wordFrequency(storagePost, map);
	} // postDuplicateAnalysis
	/**
	 * analyzes file for duplicate lines filtered for filler words and prints associated authors
	 * @param posts
	 */
	public void postDuplicateAnalysisFiltered(String post, String author) {
		map.clear();
		permLines.clear();
		permLinesID.clear();
		storagePost.clear();
		this.generalPostStorage(post);
		this.generalAuthorStorage(author);
		this.wordFrequencyFiltered(storagePost, map, 3);
		nameCounter = 0;
		Set<String> keys = map.keySet();
		String[] keysArray = keys.toArray(new String[keys.size()]);
		System.out.println("Ready to read file.");
		try {
			FileReader myFileReader = new FileReader(post);
			System.out.println("I was able to open your file!");
			BufferedReader bufferedReader = new BufferedReader(myFileReader);
			while ((line = bufferedReader.readLine()) != null) {
				line.toLowerCase();
				nameCounter++;
				/*
				for (int i =0; i<keysArray.length; i++) {
					if(line.contains(keysArray[i])) {
						mapForDuplicateAnalysis.put(this.storageAuthor.get(nameCounter - 1), keysArray[i]);
						this.wordIdentifier(this.storageAuthor.get(nameCounter - 1), permLinesID);
					}
				}
				*/
				if (this.containsItemFromArray(line, keysArray) == true) {
					//permLines.add(this.storageAuthor.get(nameCounter - 1));
					permLines.add(this.storageAuthor.get(nameCounter - 1));
					this.wordIdentifier(this.storageAuthor.get(nameCounter - 1), permLinesID);
				} // if
			} // while loop
			bufferedReader.close();
		} catch (FileNotFoundException ex) {
			System.out.println("Unable to open file '" + post + "'");
		} catch (IOException ex) {
			System.out.println("Error reading file '" + post + "'");
		} // catch
		map.clear();
		this.wordFrequencyFiltered(permLines, map, 0);
		/*
		for (String item : permLines) {
			System.out.println(item);
		}
		*/
	} // postDuplicateAnalysisFiltered
	// ==========================================================================
	// ==========================================================================
	public void commentTemplate(String comment) {
		System.out.println("============================================");
		System.out.println("================" + comment + "==================");
		System.out.println("============================================");
	}
	// ============================================
	// ============================================
}// DataAnalyzer Class
