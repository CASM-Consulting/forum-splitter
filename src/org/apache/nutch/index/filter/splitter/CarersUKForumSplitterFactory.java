package org.apache.nutch.index.filter.splitter;

public class CarersUKForumSplitterFactory implements IForumSplitterFactory {
	
	private static final String BODY_NAME = "phpbb_postbody";
	private static final String CONTENT = "phpbb_content";
	@Override
	public IForumSplitter create() {
		return new DefaultForumSplitter(BODY_NAME,CONTENT);
	}

}
