package org.apache.nutch.index.filter.splitter;

/**
 * Creates instances of the PhpBBForumSplitter.
 * 
 * @author jp242
 *
 */
public class DefaultPhpBBSplitterFactory implements ForumSplitterFactory {
	
	private static final String BODY_NAME = "postbody";
	private static final String CONTENT = "content";

	@Override
	public ForumSplitter create() {
		return new PhpBBForumSplitter(BODY_NAME,CONTENT);
	}

}
