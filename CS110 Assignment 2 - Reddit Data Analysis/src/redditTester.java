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
		
		System.out.println("\n=============================================================");
		System.out.println("Enter [y] to continue narrative, [end] to go to conclusion");
		decisionFlow = input.nextLine();
		redditRead.authorPercentAnalysisN(author, decisionFlow);

		System.out.println("\n=============================================================");
		System.out.println("Enter [y] to continue narrative, [end] to go to conclusion");
		decisionFlow = input.nextLine();
		redditRead.authorAnalysisN(post, author, decisionFlow);
		
		System.out.println("\n=============================================================");
		System.out.println("Enter [f] for full dataset, [y] to continue narrative, [end] to go to conclusion");
		decisionFlow = input.nextLine();
		redditRead.generalAuthorAnalysesN(author, decisionFlow);
		
		System.out.println("\n=============================================================");
		System.out.println("Enter [f] for full dataset, [y] to continue narrative, [end] to go to conclusion");
		decisionFlow = input.nextLine();
		redditRead.generalPostAnalysesN(post, decisionFlow);
		
		// redditRead.postDuplicateAnalysis(post);
		// redditRead.postDuplicateAnalysisFiltered(post, author, decisionFlow);

		System.out.println("Lastly, do you want to conduct your own analysis on the data? [y] [n]");
		decisionFlow = input.nextLine();
		redditRead.specificAnalysesN(post, author, decisionFlow);

		input.close();
	}// main
}// redditTester
