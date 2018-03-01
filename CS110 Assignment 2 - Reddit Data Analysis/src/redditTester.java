


public class redditTester {
	public static void main(String[] args) {
		String fileName = "redditPosts.txt";
		redditDataAnalyzer redditRead = new redditDataAnalyzer();
		redditRead.readRedditFile(fileName);

		redditRead.detectTrump("");

		System.out.println(redditRead.counter);




	}
}
