package ar.com.dcsys.pr;

import java.util.ArrayList;
import java.util.List;

public class Factory {

	Manager manager;
	List<FactoryMethod> methods = new ArrayList<>();
	
	
	public Factory(Manager manager) {
		this.manager = manager;
	}
	
	public String getPackageName() {
		return manager.getSharedPackage();
	}
	
	public String getName() {
		return manager.className.substring(0,1).toLowerCase() + manager.className.substring(1) + "Factory";
	}
	
	public String getType() {
		return manager.className + "Factory";
	}
	
	
}
