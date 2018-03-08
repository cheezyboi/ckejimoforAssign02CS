
import java.io.IOException;

public class redditTester {
	public static void main(String[] args) throws IOException {

		String post = "redditPosts.txt";
		String author = "redditAuthors.txt";
		DataAnalyzer redditRead = new DataAnalyzer();

		//redditRead.generalPostAnalysis(post);
		//redditRead.generalAuthorAnalysis(author);
		//redditRead.trumpAnalysis(post);
		//redditRead.drjarnsAnalysis(post ,author);
		
		//redditRead.authorsWOnePost(author);
		//redditRead.authorPercentAnalysis(author);
		//redditRead.onePostAuthorAnalysis(post,author);
		
		//redditRead.postDuplicateAnalysis(post);
		redditRead.postDuplicateAnalysisFiltered(post, author);
		
		//redditRead.specificAuthorAnalysis(author, "drjarns");
		//redditRead.specificWordAnalysis(post, "spirit");

	}// main
}// redditTester
