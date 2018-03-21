import java.io.IOException;
import java.util.Scanner;

public class redditTester {
	public static void main(String[] args) throws IOException {
		Scanner input = new Scanner(System.in);

		String post = "redditPosts.txt";
		String author = "redditAuthors.txt";
		String decisionFlow = null;
		DataAnalyzer redditRead = new DataAnalyzer();

		System.out.println("The following is several analyses of reddit posts and authors from \n"
				+ "a pro Trump thread that occurred during the 2016 election.");

		System.out.println("\nThese posts were analyzed for many things mainly to identify patterns \n"
				+ "in content and/or identify bot activity. Bot can be indicated by (1) multiple posts \n"
				+ "in a single thread and (2) multiple duplicate posts in a thread. This was the thought \n"
				+ "process going into the first analysis.");
		
		// shows words that appear over 450 times in the file,
		System.out.println("\n=============================================================");
		System.out.println("[y] to continue narrative, [end] to go to conclusion");
		decisionFlow = input.nextLine();
		redditRead.generalPostAnalysesN(post, decisionFlow);
		
		// shows the percent breakdown of authors that posted 'x' times
		System.out.println("\n=============================================================");
		System.out.println("Enter [y] to continue narrative, [end] to go to conclusion");
		decisionFlow = input.nextLine();
		redditRead.authorPercentAnalysisN(author, decisionFlow);

		// shows the content of authors that posted once
		System.out.println("\n=============================================================");
		System.out.println("Enter [y] to continue narrative, [end] to go to conclusion");
		decisionFlow = input.nextLine();
		redditRead.authorOneAnalysisN(post, author, decisionFlow);

		// lists users who posted 20+ times
		System.out.println("\n=============================================================");
		System.out.println("[y] to continue narrative, [end] to go to conclusion");
		decisionFlow = input.nextLine();
		redditRead.generalAuthorAnalysesN(author, decisionFlow);

		// shows the content of authors that posted 20+ times
		System.out.println("\n=============================================================");
		System.out.println("Enter [y] to continue narrative, [end] to go to conclusion");
		decisionFlow = input.nextLine();
		redditRead.authorOneAnalysis20N(post, author, decisionFlow);
		
		// shows posts that occurred more than once
		System.out.println("\n=============================================================");
		System.out.println("[y] to continue narrative, [end] to go to conclusion");
		decisionFlow = input.nextLine();
		redditRead.duplicateAnalysesN(post, author, decisionFlow);

		// gives the user the option to do their own analysis on specific words or authors
		System.out.println("\nLastly, do you want to conduct your own analysis on the data? [y] [n]");
		decisionFlow = input.nextLine();
		redditRead.specificAnalysesN(post, author, decisionFlow);
		
		input.close();
	}// main
}// redditTester
