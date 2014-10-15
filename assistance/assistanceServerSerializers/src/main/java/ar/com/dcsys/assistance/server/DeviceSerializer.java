package ar.com.dcsys.assistance.server;

import java.util.logging.Logger;

import ar.com.dcsys.data.device.Device;
import ar.com.dcsys.pr.CSD;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class DeviceSerializer implements CSD<Device> {
	
	public static final Logger logger = Logger.getLogger(DeviceSerializer.class.getName());
	
	private final Gson gson = (new GsonBuilder()).setDateFormat("HH:mm:ss dd/MM/yyyy").create();
	
	@Override
	public String toJson(Device o) {
		String d = gson.toJson(o);
		logger.warning(d);
		return d;
	}

	@Override
	public Device read(String json) {
		logger.warning(json);
		Device rs = gson.fromJson(json, Device.class);
		return rs;
	}
	
}
