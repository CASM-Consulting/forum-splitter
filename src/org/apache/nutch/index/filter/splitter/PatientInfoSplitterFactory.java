package org.apache.nutch.index.filter.splitter;

public class PatientInfoSplitterFactory implements ForumSplitterFactory {
	
	private static final String BODY = "post post-root";
	private static final String CONTENT = "post-content break-word";

	@Override
	public ForumSplitter create() {
		return new DefaultForumSplitter(BODY,CONTENT);
	}
	
}
