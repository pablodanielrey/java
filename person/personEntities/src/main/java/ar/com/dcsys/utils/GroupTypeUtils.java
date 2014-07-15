package ar.com.dcsys.utils;

import java.util.List;

import ar.com.dcsys.data.group.GroupType;

public class GroupTypeUtils {

	public static String getDescription(GroupType type) {
		switch (type) {
			case ALIAS: return "Alias";
			case OFFICE: return "Oficina";
			case OU: return "Unidad";
			case POSITION: return "Cargo";
			case PROFILE: return "Perfil";
			case TIMETABLE: return "Horario";
		}
		return "";
	}
	
	public List<GroupType> filter(List<GroupType> types) {
		return types;
	}
	
}
