package org.apache.nutch.parse.forum.splitter;

import org.apache.nutch.splitter.utils.SplitterFactory;

/**
 * Used to instantiate 
 * Created by jp242 on 07/10/2015.
 */
@SplitterFactory
public interface IForumSplitterFactory {

	public IForumSplitter create();

}
