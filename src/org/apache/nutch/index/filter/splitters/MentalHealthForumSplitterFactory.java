package org.apache.nutch.index.filter.splitters;

public class MentalHealthForumSplitterFactory implements ForumSplitterFactory {

	private static final String BODY_NAME = "posthead";
	private static final String CONTENT = "postcontent restore ";

	@Override
	public ForumSplitter create() {
		return new PhpBBForumSplitter(BODY_NAME,CONTENT);
	}

}
