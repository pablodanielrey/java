package ar.com.dcsys.auth.server;

import java.lang.reflect.Type;
import java.util.List;
import java.util.logging.Logger;

import ar.com.dcsys.pr.CSD;
import ar.com.dcsys.security.Fingerprint;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

public class FingerprintSerializer implements CSD<Fingerprint> {

	private static final Logger logger = Logger.getLogger(FingerprintSerializer.class.getName());
	
	private final Gson gson = (new GsonBuilder()).setDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSXXX").create();
	
	
	public String toJson(List<Fingerprint> os) {
		String json = gson.toJson(os);
		logger.fine("toJson : " + json);
		return json;
	}

	public List<Fingerprint> readList(String json) {
		Type list = new TypeToken<List<Fingerprint>>(){}.getType();
		logger.fine("read : " + json);
		List<Fingerprint> fps = gson.fromJson(json, list);
		return fps;		
	}
	
	@Override
	public String toJson(Fingerprint o) {
		String json = gson.toJson(o);
		logger.fine("toJson : " + json);
		return json;
	}

	@Override
	public Fingerprint read(String json) {
		logger.fine("read : " + json);
		Fingerprint fp = gson.fromJson(json, Fingerprint.class);
		return fp;
	}

}
