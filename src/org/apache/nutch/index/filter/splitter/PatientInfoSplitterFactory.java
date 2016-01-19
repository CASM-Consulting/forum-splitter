package org.apache.nutch.index.filter.splitter;

public class PatientInfoSplitterFactory implements IForumSplitterFactory {
	
	public static final String POST = "post";
	public static final String CONTENT = "post-content";

	@Override
	public IForumSplitter create() {
		return new DefaultForumSplitter(POST,CONTENT);
	}
	
}
