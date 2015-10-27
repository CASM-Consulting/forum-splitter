package org.apache.nutch.index.filter.splitter;

public class CarersUKForumSplitterFactory implements ForumSplitterFactory {
	
	private static final String BODY_NAME = "phpbb_postbody";
	private static final String CONTENT = "phpbb_content";
	@Override
	public ForumSplitter create() {
		return new PhpBBForumSplitter(BODY_NAME,CONTENT);
	}
	
	

}
