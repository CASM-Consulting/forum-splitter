package org.apache.nutch.parse.forum.splitter;

import java.util.LinkedList;

import org.apache.nutch.parse.filter.Post;
import org.apache.nutch.splitter.utils.SplitterFactory;

public class SaneSplitterFactory implements IForumSplitterFactory {
	
	private static final String BODY_NAME = "postbody";
	private static final String CONTENT = "content";

	@Override
	public IForumSplitter create() {
		return new SaneSplitter(BODY_NAME,CONTENT);
	}
	
	public class SaneSplitter extends AbstractForumSplitter {

		public SaneSplitter(String bodyName, String contentName) {
			super(bodyName, contentName);
		}

		@Override
		public void mapFields(LinkedList<Post> posts) {
			// TODO Auto-generated method stub
			
		}
		
	}

}
