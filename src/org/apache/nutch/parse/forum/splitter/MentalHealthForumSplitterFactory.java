package org.apache.nutch.parse.forum.splitter;

import java.util.Collection;
import java.util.LinkedList;

import org.apache.nutch.parse.filter.Post;

public class MentalHealthForumSplitterFactory implements IForumSplitterFactory {

	private static final String BODY_NAME = "posthead";
	private static final String CONTENT = "postcontent";

	@Override
	public IForumSplitter create() {
		return new MentalHealthForumSplitter(BODY_NAME,CONTENT);
	}
	
	public class MentalHealthForumSplitter extends AbstractForumSplitter {

		public MentalHealthForumSplitter(String bodyName, String contentName) {
			super(bodyName, contentName);
		}

		@Override
		public void mapFields(LinkedList<Post> posts) {
			// TODO Auto-generated method stub
			
		}

		
	}

}
