package ar.com.dcsys.gwt.data.utils.client;

import java.util.ArrayList;
import java.util.List;

import ar.com.dcsys.data.device.Device;

import com.google.gwt.json.client.JSONArray;
import com.google.gwt.json.client.JSONBoolean;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONString;
import com.google.gwt.json.client.JSONValue;

public class DeviceUtilsSerializer {
	
	public static JSONArray toJsonArray(List<Device> devices) {
		if (devices == null) {
			return null;
		}
		JSONArray devicesObj = new JSONArray();
		for (int i = 0; i < devices.size(); i++) {
			devicesObj.set(i, toJson(devices.get(i)));
		}
		return devicesObj;
	}
	
	public static JSONObject toJson(Device d) {
		
		JSONObject deviceObj = new JSONObject();
		
		//id
		String id = d.getId();
		if (id != null) {
			deviceObj.put("id", new JSONString(id));
		}
		
		//name
		String name = d.getName();
		if (name != null) {
			deviceObj.put("name", new JSONString(name));
		}
		
		//description
		String description = d.getDescription();
		if (description != null) {
			deviceObj.put("description", new JSONString(description));
		}
				
		//mac
		String mac = d.getMac();
		if (mac != null) {
			deviceObj.put("mac", new JSONString(mac));
		}
		
		//ip
		String ip = d.getIp();
		if (ip != null) {
			deviceObj.put("ip", new JSONString(ip));
		}
		
		//netmask
		String netmask = d.getNetmask();
		if (netmask != null) {
			deviceObj.put("netmask", new JSONString(netmask));
		}
		
		//gateway
		String gateway = d.getGateway();
		if (gateway != null) {
			deviceObj.put("gateway", new JSONString(gateway));
		}
		//enabled
		Boolean enabled = d.getEnabled();
		if (enabled != null) {
			deviceObj.put("enabled", JSONBoolean.getInstance(enabled));
		}
		
		return deviceObj;
	}
	
	public static List<Device> read(JSONArray devicesArray) {
		
		List<Device> devices = new ArrayList<>();
		
		if (devicesArray != null) {
			for (int i = 0; i < devices.size(); i++) {
				JSONObject deviceObj = devicesArray.get(i).isObject();
				if (deviceObj != null) {
					devices.add(read(deviceObj));
				}
			}
		}
		
		return devices;
	}
	
	public static Device read(JSONObject deviceObj) {
		
		if (deviceObj == null) {
			return null;
		}
		
		Device d = new Device();
		
		//id
		JSONValue idVal = deviceObj.get("id");
		if (idVal != null) {
			String id = idVal.isString().stringValue();
			d.setId(id);
		}
		
		//name
		JSONValue nameVal = deviceObj.get("name");
		if (nameVal != null) {
			String name = nameVal.isString().stringValue();
			d.setName(name);
		}
		
		//description
		JSONValue descriptionVal = deviceObj.get("description");
		if (descriptionVal != null) {
			String description = descriptionVal.isString().stringValue();
			d.setDescription(description);
		}
		
		//mac
		JSONValue macVal = deviceObj.get("mac");
		if (macVal != null) {
			String mac = macVal.isString().stringValue();
			d.setMac(mac);
		}
		
		//ip
		JSONValue ipVal = deviceObj.get("ip");
		if (ipVal != null) {
			String ip = ipVal.isString().stringValue();
			d.setMac(ip);
		}
		
		//netmask
		JSONValue netmaskVal = deviceObj.get("netmask");
		if (netmaskVal != null) {
			String netmask = netmaskVal.isString().stringValue();
			d.setNetmask(netmask);
		}
		
		//gateway
		JSONValue gwVal = deviceObj.get("gateway");
		if (gwVal != null) {
			String gw = gwVal.isString().stringValue();
			d.setGateway(gw);
		}
		
		//enabled
		JSONValue enabledVal = deviceObj.get("enabled");
		if (enabledVal != null) {
			Boolean enabled = enabledVal.isBoolean().booleanValue();
			d.setEnabled(enabled);
		}
		
		return d;
	}

}
