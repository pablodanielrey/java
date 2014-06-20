package ar.com.dcsys.pr.serializers.shared;

import ar.com.dcsys.pr.CSD;

public class StringSerializer implements CSD<String> {

	@Override
	public String toJson(String o) {
		return o;
	}

	@Override
	public String read(String json) {
		return json;
	}

}
