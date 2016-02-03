package org.apache.nutch.parse.forum.splitter;

import java.util.Collection;
import java.util.LinkedList;

import org.apache.nutch.parse.filter.Post;

public class NetDoctorSplitterFactory implements IForumSplitterFactory {
	
	private static final String CONTENT = "Item-BodyWrap";
	private static final String BODY = "Message";

	@Override
	public IForumSplitter create() {
		return new NetDoctorSplitter(CONTENT,BODY);
	}
	
	public class NetDoctorSplitter extends AbstractForumSplitter {

		public NetDoctorSplitter(String bodyName, String contentName) {
			super(bodyName, contentName);
		}

		@Override
		public void mapFields(LinkedList<Post> posts) {
			// TODO Auto-generated method stub
			
		}
		
	}

}
