/**
 * Usa el formado ISO8601 -
 * ej : 2011-10-26T20:29:59-07:00
 */

package ar.com.dcsys.pr.serializers.server;

import java.util.logging.Level;
import java.util.logging.Logger;

import ar.com.dcsys.pr.CSD;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class BooleanSerializer implements CSD<Boolean> {

	private static final Logger logger = Logger.getLogger(BooleanSerializer.class.getName());
	
	private static final Gson gson = (new GsonBuilder()).create();
	
	@Override
	public String toJson(Boolean o) {
		String d = gson.toJson(o);
		logger.log(Level.WARNING, "gson : " + d);
		return d;
	}

	@Override
	public Boolean read(String json) {
		logger.log(Level.WARNING,"gson : " + json);
		Boolean c = gson.fromJson(json, Boolean.class);
		return c;
	}

}
