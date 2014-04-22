package ar.com.dcsys.data.person;

public class StudentDataBean implements StudentData {

	private String id;
	private String studentNumber;
	
	
	@Override
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public void setStudentNumber(String studentNumber) {
		this.studentNumber = studentNumber;
	}

	@Override
	public String getStudentNumber() {
		return studentNumber;
	}

}
