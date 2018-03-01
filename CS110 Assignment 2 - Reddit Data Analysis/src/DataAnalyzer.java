import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;


public class DataAnalyzer {

	String userName;
	float accountAge;
	int counter = 0;

	boolean detectTrump(String linetoAnalyze) {
		String linetoAnalyzeLowerCase = linetoAnalyze.toLowerCase();
		if (linetoAnalyzeLowerCase.contains("trump") || linetoAnalyze.contains(linetoAnalyze)) {
			System.out.println("detected trump");
			return true;
		} else {
			System.out.println("no trump");
			return false;
		}
	}

	public void detectRussia(String line) {
		if (line.contains("russia")) {
			counter++;
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
			bufferedReader.close();
		} catch (FileNotFoundException ex) {
			System.out.println("Unable to open file '" + fileToRead + "'");
		} catch (IOException ex) {
			System.out.println("Error reading file '" + fileToRead + "'");
		}
	}
}
