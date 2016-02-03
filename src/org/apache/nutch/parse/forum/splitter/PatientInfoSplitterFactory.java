package org.apache.nutch.parse.forum.splitter;

import java.util.Collection;
import java.util.LinkedList;

import org.apache.nutch.parse.filter.Post;

public class PatientInfoSplitterFactory implements IForumSplitterFactory {
	
	public static final String POST = "post";
	public static final String CONTENT = "post-content";

	@Override
	public IForumSplitter create() {
		return new PatientInfoSplitter(POST,CONTENT);
	}
	
	public class PatientInfoSplitter extends AbstractForumSplitter {

		public PatientInfoSplitter(String bodyName, String contentName) {
			super(bodyName, contentName);
		}

		@Override
		public void mapFields(LinkedList<Post> posts) {
			// TODO Auto-generated method stub
			
		}
		
	}
	
}
