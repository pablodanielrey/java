package ar.com.dcsys.gwt.person.client.common;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import ar.com.dcsys.data.group.GroupType;
import ar.com.dcsys.utils.GroupTypeUtils;


public class EnumGroupTypesSort {
	public static void sort(List<GroupType> groups) {
		if (groups == null || groups.size() <= 0) {
			return;
		}
		Collections.sort(groups, new Comparator<GroupType>() {
			@Override
			public int compare(GroupType arg0, GroupType arg1) {
				if (arg0 == null && arg1 == null) {
					return 0;
				}
				
				if (arg0 == null) {
					return -1;
				}
				
				if (arg1 == null) {
					return 1;
				}
				
				String description0 = GroupTypeUtils.getDescription(arg0).trim().toLowerCase();
				String description1 = GroupTypeUtils.getDescription(arg1).trim().toLowerCase();
				
				return description0.compareTo(description1);
			}
		});
	}	
}