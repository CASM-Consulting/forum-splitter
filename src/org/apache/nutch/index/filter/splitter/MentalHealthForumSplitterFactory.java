package org.apache.nutch.index.filter.splitter;

public class MentalHealthForumSplitterFactory implements IForumSplitterFactory {

	private static final String BODY_NAME = "posthead";
	private static final String CONTENT = "postcontent";

	@Override
	public IForumSplitter create() {
		return new DefaultForumSplitter(BODY_NAME,CONTENT);
	}

}
