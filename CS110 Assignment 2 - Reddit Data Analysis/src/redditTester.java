


public class redditTester {
	public static void main(String[] args) {
		String fileName = "redditPosts.txt";
		DataAnalyzer redditRead = new DataAnalyzer();
		redditRead.readRedditFile(fileName);

		redditRead.wordIdentifier("i ate ate dynamite and cheese");
	}
}
