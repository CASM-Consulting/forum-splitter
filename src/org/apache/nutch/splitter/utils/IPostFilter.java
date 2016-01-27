package org.apache.nutch.splitter.utils;

import org.apache.nutch.index.filter.Post;
import org.apache.nutch.parse.filter.IFilter;

/**
 * A type parameterised by @Element to indicate that it will scrape content pertaining to the individual forum posts.
 * @author jp242
 */
public interface IPostFilter extends IFilter<Post>{}
