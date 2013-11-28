package ar.com.dcsys.data.person;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import ar.com.dcsys.data.person.types.PersonType;

public class Person implements Serializable {

	private static final long serialVersionUID = 1L;

	private String name;
	private String lastName;
	private String dni;
	private String address;
	private String city;
	private String country;
	private String gender;
	private Date birthDate;
	private String studentNumber;
	private UUID id;
	private String version;
	
	private List<PersonType> types;
	
	private List<Mail> mails = new ArrayList<Mail>();
	private List<Telephone> telephones = new ArrayList<Telephone>();
		
	
	public String getId() {
		if (id == null) {
			return null;
		}
		return id.toString();
	}

	public void setId(String id) {
		this.id = UUID.fromString(id);
	}	
	
	public void setVersion(String version) {
		this.version = version;
	}

	public String getVersion() {
		return version;
	}
	
	
	public List<PersonType> getTypes() {
		return types;
	}

	public void setTypes(List<PersonType> types) {
		this.types = types;
	}

	public String getStudentNumber() {
		return studentNumber;
	}

	public void setStudentNumber(String studentNumber) {
		this.studentNumber = studentNumber;
	}

	public List<Telephone> getTelephones() {
		return telephones;
	}

	public void setTelephones(List<Telephone> telephones) {
		this.telephones = telephones;
	}



	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public String getLastName() {
		return lastName;
	}
	
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	
	public String getDni() {
		return dni;
	}
	
	public void setDni(String dni) {
		this.dni = dni;
	}
	
	public String getAddress() {
		return address;
	}
	
	public void setAddress(String address) {
		this.address = address;
	}

	public Date getBirthDate() {
		return birthDate;
	}

	public void setBirthDate(Date birthDate) {
		this.birthDate = birthDate;
	}

	public List<Mail> getMails() {
		return mails;
	}

	public void setMails(List<Mail> mails) {
		this.mails = mails;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Person other = (Person) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}
	
}
