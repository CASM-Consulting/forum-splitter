package org.apache.nutch.parse.forum.splitter;

// java imports
import java.util.LinkedList;

// jsoup imports
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

// logging imports
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.apache.nutch.parse.filter.Post;
import org.apache.nutch.splitter.utils.GlobalFieldValues;

public class MentalHealthForumSplitterFactory implements IForumSplitterFactory {

	private static final Logger LOG = LoggerFactory.getLogger(MentalHealthForumSplitterFactory.class);


	private static final String BODY_NAME = "postbitlegacy";
	private static final String CONTENT = "content";
	private static final String MEMBER = "username_container";

	@Override
	public IForumSplitter create() {

		LOG.info("instantiated the MentalHealthForumParser");
		return new MentalHealthForumSplitter(BODY_NAME,CONTENT);
	}

	public class MentalHealthForumSplitter extends AbstractForumSplitter {

		public MentalHealthForumSplitter(String bodyName, String contentName) {
			super(bodyName, contentName);
		}

		@Override
		public void mapFields(LinkedList<Post> posts) {

			LOG.info("mental health forum Posts size = "  + posts.size());
			for(Post post : posts) {
				Document doc = Jsoup.parse(post.postHTML());
				final String member = doc.getElementsByClass(MEMBER).first().text().split("\\s")[0].trim();
				post.put(GlobalFieldValues.MEMBER, String.valueOf(member.hashCode()));
			}
		}


	}

}
