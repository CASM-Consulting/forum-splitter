package org.apache.nutch.splitter.utils;

public class InvalidCSSQueryException extends Exception {

	private static final long serialVersionUID = 1L;
	
	public InvalidCSSQueryException(String query) {
		super("The query: " + query + " is not a valid css query and will not produce any output from the page.");
	}

}
