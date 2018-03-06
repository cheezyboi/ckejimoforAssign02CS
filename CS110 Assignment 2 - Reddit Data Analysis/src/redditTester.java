
import java.io.IOException;

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
		 */

		String post = "redditPosts.txt";
		String author = "redditAuthors.txt";
		DataAnalyzer redditRead = new DataAnalyzer();

		//redditRead.generalPostAnalysis(post);
		//redditRead.generalAuthorAnalysis(author);
		redditRead.trumpAnalysis(post);
		//redditRead.drjarnsAnalysis(post ,author);
		//redditRead.specificAuthorAnalysis(post, author, "drjarns");
		//redditRead.specificWordAnalysis(post, "spirit");

	}// main
}// redditTester
