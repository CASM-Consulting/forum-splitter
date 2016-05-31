package org.apache.nutch.parse.filter.filters;

import org.apache.nutch.splitter.utils.GlobalFieldValues;

/**
 * A filter which attempts to extract the page number from the url.
 * @author jp242
 */
public class PageNumberFilter extends AbstractPageFilter {
	
	private static final String REGEX = "page[-=]";

	@Override
	public String name() {
		return GlobalFieldValues.PAGE_NUMBER;
	}

	@Override
	protected String splitRegex() {
		return REGEX;
	}

}
