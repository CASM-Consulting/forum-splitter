package org.apache.nutch.index.filter.splitter;

public class SaneSplitterFactory implements ForumSplitterFactory {
	
	private static final String BODY_NAME = "postbody";
	private static final String CONTENT = "content";

	@Override
	public ForumSplitter create() {
		return new DefaultForumSplitter(BODY_NAME,CONTENT);
	}

}
