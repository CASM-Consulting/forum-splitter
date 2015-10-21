package org.apache.nutch.index.filter;

/**
 * Creates instances of the PhpBBForumSplitter.
 * 
 * @author jp242
 *
 */
public class PhpBBForumSplitterFactory implements ForumSplitterFactory {

	@Override
	public ForumSplitter create() {
		return new PhpBBForumSplitter();
	}

}
