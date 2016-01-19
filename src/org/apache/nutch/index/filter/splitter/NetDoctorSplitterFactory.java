package org.apache.nutch.index.filter.splitter;

public class NetDoctorSplitterFactory implements IForumSplitterFactory {
	
	private static final String CONTENT = "Item-BodyWrap";
	private static final String BODY = "Message";

	@Override
	public IForumSplitter create() {
		return new DefaultForumSplitter(CONTENT,BODY);
	}

}
