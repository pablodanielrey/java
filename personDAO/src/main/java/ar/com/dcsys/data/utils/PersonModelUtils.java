package ar.com.dcsys.data.utils;

import java.io.Serializable;
import java.util.List;

import ar.com.dcsys.data.person.Mail;
import ar.com.dcsys.data.person.Person;

public class PersonModelUtils implements Serializable {

	private static final long serialVersionUID = 1L;

	public static Mail getMail(Person person) {
		List<Mail> mails = person.getMails();
		if (mails == null || mails.size() <= 0) {
			return null;
		}
		Mail mail = mails.get(0);
		return mail;
	}
	
	public static Mail getPrimaryMail(Person person) {
		List<Mail> mails = person.getMails();
		if (mails == null || mails.size() <= 0) {
			return null;
		}
		for (Mail m : mails) {
			if (m.isPrimary()) {
				return m;
			}
		}
		return null;
	}
	
	public static String getFullName(Person person) {
		StringBuffer name = new StringBuffer();
		if (person.getName() != null) {
			name.append(person.getName());
		}
		if (person.getLastName() != null) {
			name.append(" " + person.getLastName());
		}
		return name.toString();
	}	
	
}
