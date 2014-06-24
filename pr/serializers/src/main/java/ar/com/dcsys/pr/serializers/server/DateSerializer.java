/**
 * Usa el formado ISO8601 -
 * ej : 2011-10-26T20:29:59-07:00
 */

package ar.com.dcsys.pr.serializers.server;

import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

import ar.com.dcsys.pr.CSD;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class DateSerializer implements CSD<Date> {

	private static final Logger logger = Logger.getLogger(DateSerializer.class.getName());
	
	private static final Gson gson = (new GsonBuilder()).setDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSXXX").create();
	
	@Override
	public String toJson(Date o) {
		String d = gson.toJson(o);
		logger.log(Level.WARNING, "gson : " + d);
		return d;
	}

	@Override
	public Date read(String json) {
		logger.log(Level.WARNING,"gson : " + json);
		Date date = gson.fromJson(json, Date.class);
		return date;
	}

}
