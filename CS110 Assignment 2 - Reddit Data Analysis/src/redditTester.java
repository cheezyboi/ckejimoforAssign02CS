
import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;
import java.util.TreeMap;

public class redditTester {
	public static void main(String[] args) throws IOException {
		/**
		 * for testing
		 */
		/*
		 * ArrayList<String> permTest = new ArrayList<String>(); //Map<String, Integer>
		 * testMap = new TreeMap<String, Integer>(); //String testLine =
		 * "I am a ska beetlejuice   junglecat pizza@@ % ,,.new father and I love doing the dishes dishes dishes."
		 * ; //redditRead.wordIdentifier(testLine, permTest);
		 * //redditRead.wordFrequency(permTest, testMap);
		 * 
		 */
		
		String fileName = "redditPosts.txt";
		DataAnalyzer redditRead = new DataAnalyzer();

		redditRead.generalAnalysis(fileName);
		redditRead.trumpAnalysis(fileName);
	}// main
}// redditTester
