package ar.com.dcsys.utils;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import ar.com.dcsys.data.group.Group;

public class GroupSort {

	private static final String getSystems(Group g) {
		String name0 = "";	
		if (g == null) {
			return name0;
		}
		List<String> systems = g.getSystems();
		if (systems != null && systems.size() > 0) {
			Collections.sort(systems);
			for (String s : systems) {
				name0 = name0.concat(s + " ");
			}
		}
		return name0;
	}
	
	private static final String getName(Group g) {
		if (g == null) {
			return "";
		}
		String name = (g.getName() == null) ? "" : g.getName();
		return name;
	}
	
	private static final Comparator<Group> comparator = new Comparator<Group>() {
		
		@Override
		public int compare(Group arg0, Group arg1) {
			if (arg0 == null && arg1 == null) {
				return 0;
			}
			
			if (arg0 == null) {
				return -1;
			}
			
			if (arg1 == null) {
				return 1;
			}
			
			String name0 = getSystems(arg0) + getName(arg0);
			String name1 = getSystems(arg1) + getName(arg1);
	
			return name0.compareTo(name1);
		}
	};
	
	public static Comparator<Group> getComparator() {
		return comparator;
	}
	
	public static void sort(List<Group> groups) {
		Collections.sort(groups,getComparator());		
	}
	
}
