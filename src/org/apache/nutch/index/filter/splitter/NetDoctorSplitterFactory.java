package org.apache.nutch.index.filter.splitter;

import java.util.Collection;

import org.apache.nutch.index.filter.ImplForumSplitter;
import org.apache.nutch.index.filter.Post;

public class NetDoctorSplitterFactory implements IForumSplitterFactory {
	
	private static final String CONTENT = "Item-BodyWrap";
	private static final String BODY = "Message";

	@Override
	public IForumSplitter create() {
		return new NetDoctorSplitter(CONTENT,BODY);
	}
	
	public class NetDoctorSplitter extends ImplForumSplitter {

		public NetDoctorSplitter(String bodyName, String contentName) {
			super(bodyName, contentName);
		}

		@Override
		public void mapFields(Collection<Post> posts) {
			// TODO Auto-generated method stub
			
		}
		
	}

}
