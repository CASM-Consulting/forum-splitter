package org.apache.nutch.splitter.utils;

import org.apache.nutch.parse.filter.IFilter;
import org.apache.nutch.protocol.Content;

/**
 * A type paramterised by @Content to indicate that it will scrape content pertaining to the entire web page being analysed.
 * @author jp242
 *
 */
public interface IPageFilter extends IFilter<Content> {}
