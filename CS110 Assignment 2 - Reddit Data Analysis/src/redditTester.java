import java.util.ArrayList;
import java.util.Map;
import java.util.TreeMap;

public class redditTester {
	public static void main(String[] args) {
		ArrayList<String> permTest = new ArrayList<String>();
		Map<String, Integer> testMap = new TreeMap<String, Integer>();

		String fileName = "redditPosts.txt";
		String testLine = "I am a ska beetlejuice new father and I love doing the dishes dishes dishes.";
		DataAnalyzer redditRead = new DataAnalyzer();
		
		redditRead.wordIdentifier(testLine, permTest);
		redditRead.wordFrequency(permTest, testMap);
		//redditRead.generalAnalysis(fileName);
		//redditRead.trumpAnalysis(fileName);
		}
}
