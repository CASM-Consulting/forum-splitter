package org.apache.nutch.index.filter.split;

import java.util.Collection;

import org.apache.nutch.index.filter.Post;

public class MentalHealthForumSplitterFactory implements IForumSplitterFactory {

	private static final String BODY_NAME = "posthead";
	private static final String CONTENT = "postcontent";

	@Override
	public IForumSplitter create() {
		return new MentalHealthForumSplitter(BODY_NAME,CONTENT);
	}
	
	public class MentalHealthForumSplitter extends ImplForumSplitter {

		public MentalHealthForumSplitter(String bodyName, String contentName) {
			super(bodyName, contentName);
		}

		@Override
		public void mapFields(Collection<Post> posts) {
			// TODO Auto-generated method stub
			
		}

		
	}

}
