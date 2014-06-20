package ar.com.dcsys.pr.server.serializers;

import java.util.List;

import ar.com.dcsys.pr.CSD;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;


public class StringListSerializer implements CSD<List<String>> {

	private final Gson gson = (new GsonBuilder()).create();
	
	@Override
	public String toJson(List<String> o) {
		return gson.toJson(o);
	}

	@Override
	public List<String> read(String json) {
		TypeToken<List<String>> type = new TypeToken<List<String>>() {};
		return gson.fromJson(json, type.getType());
	}

}
