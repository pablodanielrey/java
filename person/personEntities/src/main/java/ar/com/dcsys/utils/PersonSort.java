package ar.com.dcsys.utils;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import ar.com.dcsys.data.person.Person;

public class PersonSort {

	private static final Comparator<Person> comparator = new Comparator<Person>() {
		
		@Override
		public int compare(Person arg0, Person arg1) {
			if (arg0 == null && arg1 == null) {
				return 0;
			}
			
			if (arg0 == null) {
				return -1;
			}
			
			if (arg1 == null) {
				return 1;
			}
			
			
			
			String name0 = (((arg0.getLastName() != null) ? arg0.getLastName() : "") + ((arg0.getName() != null) ? arg0.getName() : "")).trim().toLowerCase();
			if (name0 != null & name0.trim().equals("")) {
				name0 = null;
			}
			String name1 = (((arg1.getLastName() != null) ? arg1.getLastName() : "") + ((arg1.getName() != null) ? arg1.getName() : "")).trim().toLowerCase();
			if (name1 != null & name1.trim().equals("")) {
				name1 = null;
			}

			
			if (name0 == null && name1 == null) {
				
				if (arg0.getDni() != null && arg1.getDni() != null) {
					return arg0.getDni().compareTo(arg1.getDni());
				}
				
			}
			
			if (name0 == null) {
				return -1;
			}
			
			if (name1 == null) {
				return 1;
			}
			
			return name0.compareTo(name1);
		}
	};
	
	public static Comparator<Person> getComparator() {
		return comparator;
	}
	
	public static void sort(List<Person> persons) {
		Collections.sort(persons,getComparator());		
	}
	
}
