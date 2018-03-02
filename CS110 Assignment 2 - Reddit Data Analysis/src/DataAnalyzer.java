import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class DataAnalyzer {

	String userName;
	float accountAge;
	int counter = 0;
	ArrayList<String> trumpLines = new ArrayList<String>();
	ArrayList<String> storedLines= new ArrayList<String>();

public void wordIdentifier (String readLine) {
	System.out.println("inside wordIdentifier");
	for (int i = 0; i < readLine.length(); i++) {
		System.out.println("\nInside 1st for loop");
		String stringLine = Character.toString(readLine.charAt(i));
		if (stringLine.matches(".*[a-z].*")) {
			 storedLines.add(stringLine);
		} else {
			StringBuilder sb = new StringBuilder();
			for (String s : storedLines) {
				System.out.println("inside StringBuilder1");
				sb.append(s);
				System.out.println(sb.toString());
			}
		}
	}
	StringBuilder sb = new StringBuilder();
	for (String s : storedLines) {
		sb.append(s);
	}
	System.out.println(sb.toString());
}

	public void detectRussia(String line) {
		if (line.contains("clinton") && line.contains("trump")) {
			counter++;
			trumpLines.add(line);
		}
	}

	public void readRedditFile(String fileToRead) {
		System.out.println("Ready to read file.");
		String line = null;
		try {
			FileReader myFileReader = new FileReader(fileToRead);
			System.out.println("I was able to open your file!");
			BufferedReader bufferedReader = new BufferedReader(myFileReader);
			while ((line = bufferedReader.readLine()) != null) {
				//System.out.println(line);
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
