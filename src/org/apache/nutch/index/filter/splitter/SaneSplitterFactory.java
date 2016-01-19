package org.apache.nutch.index.filter.splitter;

public class SaneSplitterFactory implements IForumSplitterFactory {
	
	private static final String BODY_NAME = "postbody";
	private static final String CONTENT = "content";

	@Override
	public IForumSplitter create() {
		return new DefaultForumSplitter(BODY_NAME,CONTENT);
	}

}
