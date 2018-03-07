package org.apache.nutch.parse.filter.filters;

import org.apache.nutch.splitter.utils.GlobalFieldValues;

public class PageSizeFilter extends AbstractPageFilter {

	public static final String REGEX = "size=";
	
	@Override
	public String name() {
		return GlobalFieldValues.PAGE_START;
	}

	@Override
	protected String splitRegex() {
		return REGEX;
	}
	

}
