package ar.com.dcsys.data.person;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

public class PersonBean implements Person, Serializable {

	private static final long serialVersionUID = 1L;

	private UUID id;
	private String name;
	private String lastName;
	private String dni;
	private String address;
	private String city;
	private String country;
	private String gender;
	private Date birthDate;
	private String studentNumber;

	
	private List<PersonType> types;
	private List<Telephone> telephones = new ArrayList<Telephone>();
		
	@Override
	public String getId() {
		if (id == null) {
			return null;
		}
		return id.toString();
	}

	@Override
	public void setId(String id) {
		this.id = UUID.fromString(id);
	}	
	
	@Override
	public List<PersonType> getTypes() {
		return types;
	}

	@Override
	public void setTypes(List<PersonType> types) {
		this.types = types;
	}

	@Override
	public String getStudentNumber() {
		return studentNumber;
	}

	@Override
	public void setStudentNumber(String studentNumber) {
		this.studentNumber = studentNumber;
	}

	@Override
	public List<Telephone> getTelephones() {
		return telephones;
	}

	@Override
	public void setTelephones(List<Telephone> telephones) {
		this.telephones = telephones;
	}

	@Override
	public String getName() {
		return name;
	}
	
	@Override
	public void setName(String name) {
		this.name = name;
	}
	
	@Override
	public String getLastName() {
		return lastName;
	}
	
	@Override
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	
	@Override
	public String getDni() {
		return dni;
	}
	
	@Override
	public void setDni(String dni) {
		this.dni = dni;
	}
	
	@Override
	public String getAddress() {
		return address;
	}
	
	@Override
	public void setAddress(String address) {
		this.address = address;
	}

	@Override
	public Date getBirthDate() {
		return birthDate;
	}

	@Override
	public void setBirthDate(Date birthDate) {
		this.birthDate = birthDate;
	}

	@Override
	public String getCity() {
		return city;
	}

	@Override
	public void setCity(String city) {
		this.city = city;
	}

	@Override
	public String getCountry() {
		return country;
	}

	@Override
	public void setCountry(String country) {
		this.country = country;
	}

	@Override
	public String getGender() {
		return gender;
	}

	@Override
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
			if (other.getId() != null)
				return false;
		} else if (!id.equals(other.getId())) 
			return false;
		return true;
	}
	
}
