package org.apache.nutch.index.filter.splitter;

public class PatientInfoSplitterFactory implements ForumSplitterFactory {
	
	public static final String POST = "post";
	public static final String CONTENT = "post-content";

	@Override
	public ForumSplitter create() {
		return new DefaultForumSplitter(POST,CONTENT);
	}
	
}
