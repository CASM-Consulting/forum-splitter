package org.apache.nutch.parse.filter.filters;

public class PersonEntityFilter extends AbstractNERFilter {
	
	private static final String NAME = "person";
	private static final String MODELNAME = "nerperson.bin";
	
	public PersonEntityFilter() {
		super(MODELNAME);
	}

	@Override
	public String name() {
		return NAME;
	}

}
