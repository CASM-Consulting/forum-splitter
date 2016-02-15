package org.apache.nutch.parse.filter.filters;

public class LocationEntityFilter extends AbstractNERFilter{
	
	private static final String NAME = "location"; 
	private static final String MODELNAME = "nerlocation.bin";

	public LocationEntityFilter() {
		super(MODELNAME);
	}

	@Override
	public String name() {
		return NAME;
	}

}
