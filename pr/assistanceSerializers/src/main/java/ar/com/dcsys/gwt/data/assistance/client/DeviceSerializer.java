package ar.com.dcsys.gwt.data.assistance.client;

import java.util.logging.Level;
import java.util.logging.Logger;

import name.pehl.piriti.json.client.JsonReader;
import name.pehl.piriti.json.client.JsonWriter;
import ar.com.dcsys.data.device.Device;
import ar.com.dcsys.pr.CSD;

import com.google.gwt.core.client.GWT;

public class DeviceSerializer implements CSD<Device> {

	public static final Logger logger = Logger.getLogger(DeviceSerializer.class.getName());
	
	public interface Reader extends JsonReader<Device> {}
	public static final Reader READER = GWT.create(Reader.class);

	public interface Writer extends JsonWriter<Device> {}
	public static final Writer WRITER = GWT.create(Writer.class);	
	
	@Override
	public String toJson(Device o) {
		String rs = WRITER.toJson(o);
		logger.log(Level.WARNING, rs);
		return rs;
	}

	@Override
	public Device read(String json) {
		logger.log(Level.WARNING,json);
		Device rs = READER.read(json);
		return rs;
	}

}
