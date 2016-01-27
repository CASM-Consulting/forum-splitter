package org.apache.nutch.index.filter.split;

// java imports
import java.util.Collection;

// nutch imports
import org.apache.nutch.index.filter.Post;
import org.apache.nutch.splitter.utils.GlobalFieldValues;

// jsoup imports
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

public class CarersUKForumSplitterFactory implements IForumSplitterFactory {
	
	private static final String BODY_NAME = "phpbb_postbody";
	private static final String CONTENT = "phpbb_content";
	@Override
	public IForumSplitter create() {
		return new CarersUKForumSplitter(BODY_NAME,CONTENT);
	}
	
	public class CarersUKForumSplitter extends ImplForumSplitter {
		
		private final String MEMBER = "phpbb_postprofile";

		public CarersUKForumSplitter(String bodyName, String contentName) {
			super(bodyName, contentName);
		}

		@Override
		public void mapFields(Collection<Post> posts) {
			for(Post post : posts) {
				Document doc = Jsoup.parse(post.post());
				Element el = doc.getElementsByClass(MEMBER).get(0);
				post.put(GlobalFieldValues.MEMBER, el.getElementsByTag("a").get(0).text());
				
				String text = el.text();
				// member location
				String location = text.split("Location:")[1].trim();
				post.put(GlobalFieldValues.LOCATION, location);
				//member since
				String memb_since = text.split("Joined:")[1].split("Location:")[0].trim();
				post.put(GlobalFieldValues.MEM_SINCE, memb_since);
			}
		}
		
	}

}
