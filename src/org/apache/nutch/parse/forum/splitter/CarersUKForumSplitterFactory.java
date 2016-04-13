package org.apache.nutch.parse.forum.splitter;

// java imports
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.LinkedList;

//nutch imports
import org.apache.nutch.parse.filter.Post;
import org.apache.nutch.splitter.utils.GlobalFieldValues;
import org.apache.nutch.splitter.utils.SplitterFactory;
// jsoup imports
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

public class CarersUKForumSplitterFactory implements IForumSplitterFactory {
	
	// html class tag names for forum post
	private static final String BODY_NAME = "phpbb_postbody";
	private static final String CONTENT = "phpbb_content";
	private static final String AUTHOR = "phpbb_author";
	
	@Override
	public IForumSplitter create() {
		return new CarersUKForumSplitter(BODY_NAME,CONTENT);
	}
	
	public class CarersUKForumSplitter extends AbstractForumSplitter {
		
		// html class name containing the user name
		private final String MEMBER = "phpbb_author";

		public CarersUKForumSplitter(String bodyName, String contentName) {
			super(bodyName, contentName);
		}

		@Override
		public void mapFields(LinkedList<Post> posts) {
			for(Post post : posts) {
				
				Document doc = Jsoup.parse(post.postHTML());
				
				// Add the forum member who wrote this post as the (string) hash value of their username.
				for(Element el : doc.getElementsByClass(MEMBER)){
					post.put(GlobalFieldValues.MEMBER, String.valueOf(el.text().split(" ")[1].hashCode()));
				}

				// Add the post date
				String[] toks = doc.getElementsByClass(AUTHOR).text().split("\\s");
				DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd MMM uuuu");
				LocalDate date = LocalDate.parse(toks[toks.length-4].replace(",","") + " " + toks[toks.length-5].replace(",","") + " " + toks[toks.length-3],dtf);
				post.put(GlobalFieldValues.POST_DATE,date.toString());
				
			}
		}
		
	}

}
