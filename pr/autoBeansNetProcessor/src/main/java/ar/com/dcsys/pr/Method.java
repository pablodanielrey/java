package ar.com.dcsys.pr;

import java.util.ArrayList;
import java.util.List;

public class Method {

	String name;
	List<Param> params = new ArrayList<>();
	Param receiver;
	
	public String getSignature() {
		StringBuilder sb = new StringBuilder();
		
		sb.append(name);
		for (Param p : params) {
			sb.append("-").append(p.getType()).append("-").append(p.getName());
		}
		
		return sb.toString();
	}
	
}
