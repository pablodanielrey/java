package ar.com.dcsys.pr.serializers.shared;

import ar.com.dcsys.pr.CSD;

public class ViodSerializer implements CSD<Void> {

	@Override
	public String toJson(Void o) {
		return "null";
	}

	@Override
	public Void read(String json) {
		return null;
	}

}
