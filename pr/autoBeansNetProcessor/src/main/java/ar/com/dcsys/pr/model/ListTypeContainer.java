package ar.com.dcsys.pr.model;

import ar.com.dcsys.pr.Utils;

public class ListTypeContainer extends BasicTypeContainer {

	public ListTypeContainer(String pack, Param param) {
		super(pack, param);
	}

	@Override
	public String getType() {
		String type = getContainedType();
		String typeName = type.substring(type.indexOf("<") + 1, type.lastIndexOf(">"));
		return getPackage() + "." + Utils.extractName(typeName) + "ListContainer"; 
	}

	
	
	
}
