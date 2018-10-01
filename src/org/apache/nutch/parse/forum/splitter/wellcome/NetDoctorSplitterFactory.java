package org.apache.nutch.parse.forum.splitter.wellcome;

import java.util.LinkedList;

import org.apache.nutch.parse.filter.Post;
import org.apache.nutch.parse.forum.splitter.AbstractForumSplitter;
import org.apache.nutch.parse.forum.splitter.IForumSplitter;
import org.apache.nutch.parse.forum.splitter.IForumSplitterFactory;
import org.apache.nutch.splitter.utils.SplitterFactory;

public class NetDoctorSplitterFactory implements IForumSplitterFactory {
	
	private static final String CONTENT = "Item-BodyWrap";
	private static final String BODY = "Message";

	@Override
	public IForumSplitter create() {
		return new NetDoctorSplitter(CONTENT,BODY);
	}
	
	@Override
	public boolean correctDomain(String url) {
		return false;
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
