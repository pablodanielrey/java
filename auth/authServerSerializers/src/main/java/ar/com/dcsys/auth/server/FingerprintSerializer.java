package ar.com.dcsys.auth.server;

import java.util.logging.Logger;

import ar.com.dcsys.pr.CSD;
import ar.com.dcsys.security.Fingerprint;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class FingerprintSerializer implements CSD<Fingerprint> {

	private static final Logger logger = Logger.getLogger(FingerprintSerializer.class.getName());
	
	private final Gson gson = (new GsonBuilder()).setDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSXXX").create();
	
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
