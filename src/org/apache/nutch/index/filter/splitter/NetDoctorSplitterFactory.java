package org.apache.nutch.index.filter.splitter;

public class NetDoctorSplitterFactory implements ForumSplitterFactory {

	@Override
	public ForumSplitter create() {
		return new NetDoctorSplitter();
	}

}
