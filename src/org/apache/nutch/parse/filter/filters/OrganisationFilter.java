package org.apache.nutch.parse.filter.filters;

public class OrganisationFilter extends AbstractNERFilter {
	
	private static final String NAME = "organisationmention";
	private static final String MODELNAME = "nerorganization.bin";

	public OrganisationFilter() {
		super(MODELNAME);
	}

	@Override
	public String name() {
		return NAME;
	}

}
