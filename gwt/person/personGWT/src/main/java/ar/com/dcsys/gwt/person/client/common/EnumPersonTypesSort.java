package ar.com.dcsys.gwt.person.client.common;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import ar.com.dcsys.data.person.PersonType;


public class EnumPersonTypesSort {
	public static void sort(List<PersonType> types) {
		Collections.sort(types, new Comparator<PersonType>() {
			@Override
			public int compare(PersonType arg0, PersonType arg1) {
				if (arg0 == null && arg1 == null) {
					return 0;
				}
				
				if (arg0 == null) {
					return -1;
				}
				
				if (arg1 == null) {
					return 1;
				}
				
				String description0 = arg0.getDescription().trim().toLowerCase();
				String description1 = arg1.getDescription().trim().toLowerCase();
				
				return description0.compareTo(description1);
			}
		});
	}	
}
