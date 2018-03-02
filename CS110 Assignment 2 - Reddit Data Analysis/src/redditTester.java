


public class redditTester {
	public static void main(String[] args) {
		String fileName = "redditPosts.txt";
		DataAnalyzer redditRead = new DataAnalyzer();
		redditRead.readRedditFile(fileName);

		redditRead.wordIdentifier("ate");

		System.out.println(redditRead.counter);
	}
}
