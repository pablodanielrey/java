package ar.com.dcsys.data.person;

public class PersonTypeStudent implements PersonType {
	
	private String id;
	private String type;
	private String studentNumber;
	
	public PersonTypeStudent() {
		type = PersonTypeStudent.class.getName();
	}
	
	@Override
	public String getType() {
		return type;
	}

	@Override
	public String getName() {
		return "Estudiante";
	}

	@Override
	public String getId() {
		return id;
	}

	@Override
	public void setId(String id) {
		this.id = id;
	}
	
	public String getStudentNumber() {
		return studentNumber;
	}
	
	public void setStudentNumber(String studentNumber) {
		this.studentNumber = studentNumber;
	}

}
