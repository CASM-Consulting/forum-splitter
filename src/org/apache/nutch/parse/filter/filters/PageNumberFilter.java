package org.apache.nutch.parse.filter.filters;

import org.apache.nutch.splitter.utils.GlobalFieldValues;

/**
 * A filter which attempts to 
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
	
	public static void main(String[] args) {
		
		String s = "?page=4930293verver";
		String s1 = "?page-244322vre";
		
		System.out.println(s.split(REGEX)[1]);
		System.out.println(s1.split(REGEX)[1]);
	}

}
