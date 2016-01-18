package org.apache.nutch.index.filter.splitter;

public class NetDoctorSplitterFactory implements ForumSplitterFactory {
	
	private static final String CONTENT = "Item-BodyWrap";
	private static final String BODY = "Message";

	@Override
	public ForumSplitter create() {
		return new DefaultForumSplitter(CONTENT,BODY);
	}

}
