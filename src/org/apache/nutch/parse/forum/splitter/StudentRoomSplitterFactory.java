package org.apache.nutch.parse.forum.splitter;

import java.util.LinkedList;

import org.apache.nutch.parse.filter.Post;
import org.apache.nutch.splitter.utils.GlobalFieldValues;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

public class StudentRoomSplitterFactory implements IForumSplitterFactory {
	
	private static final String BODY = "post";
	private static final String CONTENT = "postcontent";
	private static final String MEMBER = "post-user";

	@Override
	public IForumSplitter create() {
		return new StudentForumSplitter();
	}
	
	private class StudentForumSplitter extends AbstractForumSplitter {

		public StudentForumSplitter() {
			super(BODY, CONTENT);
		}

		@Override
		public void mapFields(LinkedList<Post> posts) {
			for(Post post : posts) {
				Document doc = Jsoup.parse(post.postHTML());
				final String member = doc.getElementsByClass(MEMBER).first().text().split("\\s")[0].trim();
				post.put(GlobalFieldValues.MEMBER, member);
			}
		}
		
	}

}
