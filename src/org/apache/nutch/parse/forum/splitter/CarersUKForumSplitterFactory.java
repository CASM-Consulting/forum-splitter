package org.apache.nutch.parse.forum.splitter;

// java imports
import java.util.LinkedList;

import org.apache.nutch.parse.filter.Post;

// jsoup imports
//import org.jsoup.Jsoup;
//import org.jsoup.nodes.Document;

public class CarersUKForumSplitterFactory implements IForumSplitterFactory {
	
	private static final String BODY_NAME = "phpbb_postbody";
	private static final String CONTENT = "phpbb_content";
	
	@Override
	public IForumSplitter create() {
		return new CarersUKForumSplitter(BODY_NAME,CONTENT);
	}
	
	public class CarersUKForumSplitter extends AbstractForumSplitter {
		
		private final String MEMBER = "phpbb_postprofile";

		public CarersUKForumSplitter(String bodyName, String contentName) {
			super(bodyName, contentName);
		}

		@Override
		public void mapFields(LinkedList<Post> posts) {
//			for(Post post : posts) {
				
//				Document doc = Jsoup.parse(post.postHTML());
								
//				for(Element el : doc.getElementsByClass(MEMBER)){
//					post.put(GlobalFieldValues.MEMBER, el.text());
//				}
								
//				String text = el.text();
//				// member location
//				String location = text.split("Location:")[1].trim();
//				post.put(GlobalFieldValues.LOCATION, location);
//				//member since
//				String memb_since = text.split("Joined:")[1].split("Location:")[0].trim();
//				post.put(GlobalFieldValues.MEM_SINCE, memb_since);
//			}
		}
		
	}

}
