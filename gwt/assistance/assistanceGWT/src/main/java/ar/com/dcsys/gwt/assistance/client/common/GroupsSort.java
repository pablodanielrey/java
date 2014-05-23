package ar.com.dcsys.gwt.assistance.client.common;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import ar.com.dcsys.data.group.Group;

public class GroupsSort {
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

			return arg0.getName().trim().compareTo(arg1.getName().trim());
		}
	};
	
	public static void sort(List<Group> groups) {
		Collections.sort(groups, comparator);
	}
}
