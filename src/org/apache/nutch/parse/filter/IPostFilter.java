package org.apache.nutch.parse.filter;

import org.apache.nutch.index.filter.Post;

/**
 * A type parameterised by @Element to indicate that it will scrape content pertaining to the individual forum posts.
 * @author jp242
 */
public interface IPostFilter extends IFilter<Post>{}
