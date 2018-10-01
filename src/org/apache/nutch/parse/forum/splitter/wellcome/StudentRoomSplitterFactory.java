package org.apache.nutch.parse.forum.splitter.wellcome;

// java imports
import java.util.LinkedList;

// jsoup imports
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import org.apache.nutch.parse.filter.Post;
import org.apache.nutch.parse.forum.splitter.AbstractForumSplitter;
import org.apache.nutch.parse.forum.splitter.IForumSplitter;
import org.apache.nutch.parse.forum.splitter.IForumSplitterFactory;
import org.apache.nutch.splitter.utils.GlobalFieldValues;

public class StudentRoomSplitterFactory implements IForumSplitterFactory {
	
	private static final String BODY = "post";
	private static final String CONTENT = "postcontent";
	private static final String MEMBER = "post-user";

	@Override
	public IForumSplitter create() {
		return new StudentForumSplitter();
	}
	
	@Override
	public boolean correctDomain(String url) {
		return false;
	}
	
	private class StudentForumSplitter extends AbstractForumSplitter {

		public StudentForumSplitter() {
			super(BODY, CONTENT);
		}

		@Override
		public void mapFields(LinkedList<Post> posts) {
			for(Post post : posts) {
				Document doc = Jsoup.parse(post.postHTML());
				final Elements member = doc.getElementsByClass(MEMBER);
				if(member.first() != null) {
					final String mem =  member.first().text().split("\\s")[0].trim();
					post.put(GlobalFieldValues.MEMBER, String.valueOf(mem.hashCode()));
				}
			}
		}
		
	}

}
