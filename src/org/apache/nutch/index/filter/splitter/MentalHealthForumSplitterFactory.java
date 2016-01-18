package org.apache.nutch.index.filter.splitter;

public class MentalHealthForumSplitterFactory implements ForumSplitterFactory {

	private static final String BODY_NAME = "posthead";
	private static final String CONTENT = "postcontent";

	@Override
	public ForumSplitter create() {
		return new DefaultForumSplitter(BODY_NAME,CONTENT);
	}

}
