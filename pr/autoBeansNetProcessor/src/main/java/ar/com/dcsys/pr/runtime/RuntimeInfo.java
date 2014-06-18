package ar.com.dcsys.pr.runtime;

import java.util.HashMap;
import java.util.Map;

public class RuntimeInfo {

	public Map<String,String> serverSerializersMap = new HashMap<String,String>();
	public Map<String,String> clientSerializersMap = new HashMap<String,String>();
	
	public Map<String,String> clientRuntimeVars = new HashMap<String,String>();
	public Map<String,String> serverRuntimeVars = new HashMap<String,String>();
	
	
}
