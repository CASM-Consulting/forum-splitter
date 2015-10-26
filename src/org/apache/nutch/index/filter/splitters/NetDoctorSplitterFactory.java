package org.apache.nutch.index.filter.splitters;

public class NetDoctorSplitterFactory implements ForumSplitterFactory {

	@Override
	public ForumSplitter create() {
		return new NetDoctorSplitter();
	}

}
