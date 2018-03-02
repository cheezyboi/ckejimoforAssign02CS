import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public class DataAnalyzer {

	String userName;
	float accountAge;
	int counter = 0;
	ArrayList<String> trumpLines = new ArrayList<String>();
	ArrayList<String> tempStoredLines = new ArrayList<String>();
	ArrayList<String> permStoredLines = new ArrayList<String>();

	/**
	 * This method reads each character in a line and organizes it in an arrayList
	 * if it is a character from a - z When a word ends the program condenses the
	 * ArrayList into a string.
	 * @param readLine
	 */
	public void wordIdentifier(String readLine) {
		/*
		 * this loop reads one character at a time and checks if it is a value a-z.
		 * if it is it will store the character in an arraylist. when anything else is detected,
		 * the temporary arraylist is consolidated (into a word) into a permanent arraylist
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
		this.wordFrequency();
	}

	/**
	 * condenses the array of characters into a string and stores in a new arrayList
	 */
	public void arrayListCondenser() {
		StringBuilder sb = new StringBuilder();
		for (String s : tempStoredLines) {
			sb.append(s);
		}
		permStoredLines.add(sb.toString());
		System.out.println(permStoredLines);
	}

	/**
	 * counts the amount of times a word occurs in the data
	 */
	public void wordFrequency() {
		Set<String> unique = new HashSet<String>(permStoredLines);
		for (String key : unique) {
			System.out.println(key + ": " + Collections.frequency(permStoredLines, key));
		}
	}

	public void detectRussia(String line) {
		if (line.contains("clinton") && line.contains("trump")) {
			counter++;
			trumpLines.add(line);
		}
	}

	public void readRedditFile(String fileToRead) {
		// System.out.println("Ready to read file.");
		String line = null;
		try {
			FileReader myFileReader = new FileReader(fileToRead);
			// System.out.println("I was able to open your file!");
			BufferedReader bufferedReader = new BufferedReader(myFileReader);
			while ((line = bufferedReader.readLine()) != null) {
				// System.out.println(line);
				this.detectRussia(line);
			}
			// Always close files
			System.out.println(trumpLines.size());
			bufferedReader.close();
		} catch (FileNotFoundException ex) {
			System.out.println("Unable to open file '" + fileToRead + "'");
		} catch (IOException ex) {
			System.out.println("Error reading file '" + fileToRead + "'");
		}
	}
}
