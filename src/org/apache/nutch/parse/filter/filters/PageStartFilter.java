package org.apache.nutch.parse.filter.filters;

import org.apache.nutch.splitter.utils.GlobalFieldValues;

public class PageStartFilter extends AbstractPageFilter {
	
	public static final String REGEX = "start=";

	@Override
	protected String splitRegex() {
		return REGEX;
	}

	@Override
	public String name() {
		return GlobalFieldValues.PAGE_START;
	}

}
