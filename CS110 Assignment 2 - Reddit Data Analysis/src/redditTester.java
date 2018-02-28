


public class redditTester {
	public static void main(String[] args) {
		String fileName = "redditPosts.txt";
		redditUser redditRead = new redditUser();
		redditRead.readRedditFile(fileName);
				
		redditRead.detectTrump("");
		System.out.println(redditRead.counter);
		
	}
}
