package ar.com.dcsys.gwt.data.assistance.client;

import java.util.logging.Level;
import java.util.logging.Logger;

import ar.com.dcsys.data.device.Device;
import ar.com.dcsys.gwt.data.utils.client.DeviceUtilsSerializer;
import ar.com.dcsys.pr.CSD;

import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONParser;
import com.google.gwt.json.client.JSONValue;

public class DeviceSerializer implements CSD<Device> {

	public static final Logger logger = Logger.getLogger(DeviceSerializer.class.getName());
	
	@Override
	public String toJson(Device o) {
		JSONObject jo = DeviceUtilsSerializer.toJson(o);
		return jo.toString();
	}

	@Override
	public Device read(String json) {
		logger.log(Level.WARNING,"DeviceSerializer : " + json);
		try {
			JSONValue value = JSONParser.parseStrict(json);
			JSONObject deviceObj = value.isObject();
			return DeviceUtilsSerializer.read(deviceObj);
		} catch (Exception e) {
			logger.log(Level.SEVERE,e.getMessage());
			return null;
		}
	}

}
