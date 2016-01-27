package org.apache.nutch.index.filter.split;

import java.util.Collection;

import org.apache.nutch.index.filter.Post;

public class SaneSplitterFactory implements IForumSplitterFactory {
	
	private static final String BODY_NAME = "postbody";
	private static final String CONTENT = "content";

	@Override
	public IForumSplitter create() {
		return new SaneSplitter(BODY_NAME,CONTENT);
	}
	
	public class SaneSplitter extends ImplForumSplitter {

		public SaneSplitter(String bodyName, String contentName) {
			super(bodyName, contentName);
		}

		@Override
		public void mapFields(Collection<Post> posts) {
			// TODO Auto-generated method stub
			
		}
		
	}

}
