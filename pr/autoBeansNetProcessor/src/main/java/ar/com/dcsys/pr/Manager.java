package ar.com.dcsys.pr;

import java.util.ArrayList;
import java.util.List;

public class Manager {

	String packageName;
	String className;
	String serverFactory;
	List<Method> methods = new ArrayList<>();
	Factory factory;
	
	public String getClientPackage() {
		return packageName.replace(".shared", ".client");
	}
	
	public String getClientSimpleName() {
		return className + "Client";
	}
	
	public String getClientName() {
		return getClientPackage() + "." + getClientSimpleName();
	}
	
	
	public String getServerPackage() {
		return packageName.replace(".shared", ".server");
	}
	
	public String getServerSimpleName() {
		return className + "ServerHandler";
	}
	
	public String getServerName() {
		return getServerPackage() + "." + getServerSimpleName();
	}
	
	public FactoryMethod getFactoryMethod(Param p) {
		for (FactoryMethod fm : factory.methods) {
			if (fm.param.equals(p)) {
				return fm;
			}
		}
		return null;
	}
}
