package org.apache.nutch.parse.forum.splitter;

// java imports
import java.util.LinkedList;

// jsoup imports
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import org.apache.nutch.parse.filter.Post;
import org.apache.nutch.splitter.utils.GlobalFieldValues;
import org.apache.nutch.splitter.utils.SplitterFactory;

public class MPAForumSplitterFactory extends AbstractForumSplitter {
	
	private static final String BODY_NAME = "post_block";
	private static final String CONTENT = "post";
	private static final String MEMBER = "author";

	public MPAForumSplitterFactory() {
		super(BODY_NAME, CONTENT);
	}

	@Override
	public void mapFields(LinkedList<Post> posts) {
		
		for(Post post : posts) {
			Document doc = Jsoup.parse(post.postHTML());
			final String member = doc.getElementsByClass(MEMBER).first().text().trim();
			post.put(GlobalFieldValues.MEMBER, String.valueOf(member.hashCode()));
		}
		
		
	}

}
