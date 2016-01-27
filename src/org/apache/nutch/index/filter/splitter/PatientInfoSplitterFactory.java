package org.apache.nutch.index.filter.splitter;

import java.util.Collection;

import org.apache.nutch.index.filter.ImplForumSplitter;
import org.apache.nutch.index.filter.Post;

public class PatientInfoSplitterFactory implements IForumSplitterFactory {
	
	public static final String POST = "post";
	public static final String CONTENT = "post-content";

	@Override
	public IForumSplitter create() {
		return new PatientInfoSplitter(POST,CONTENT);
	}
	
	public class PatientInfoSplitter extends ImplForumSplitter {

		public PatientInfoSplitter(String bodyName, String contentName) {
			super(bodyName, contentName);
		}

		@Override
		public void mapFields(Collection<Post> posts) {
			// TODO Auto-generated method stub
			
		}
		
	}
	
}
