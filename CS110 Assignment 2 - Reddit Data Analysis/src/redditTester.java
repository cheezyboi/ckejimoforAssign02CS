import java.util.ArrayList;

public class redditTester {
	public static void main(String[] args) {
		ArrayList<String> permTest = new ArrayList<String>();
		String fileName = "redditPosts.txt";
		String testLine = "I am a new father and I love doing the dishes dishes dishes.";
		DataAnalyzer redditRead = new DataAnalyzer();
		
		redditRead.wordIdentifier(testLine, permTest);
		redditRead.wordFrequency(permTest);
		//redditRead.generalAnalysis(fileName);
		//redditRead.trumpAnalysis(fileName);
		}
}
