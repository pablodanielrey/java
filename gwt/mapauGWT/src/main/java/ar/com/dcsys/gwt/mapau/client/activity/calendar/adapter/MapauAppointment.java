package ar.com.dcsys.gwt.mapau.client.activity.calendar.adapter;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import ar.com.dcsys.data.appointment.AppointmentV2;
import ar.com.dcsys.data.classroom.CharacteristicQuantity;
import ar.com.dcsys.data.classroom.ClassRoom;
import ar.com.dcsys.data.person.Person;
import ar.com.dcsys.data.reserve.ReserveAttemptDateType;
import ar.com.dcsys.data.silabouse.Area;
import ar.com.dcsys.data.silabouse.Course;
import ar.com.dcsys.data.silabouse.Subject;

import com.bradrydzewski.gwt.calendar.client.Appointment;
import com.bradrydzewski.gwt.calendar.client.AppointmentStyle;
import com.bradrydzewski.gwt.calendar.client.Attending;

public class MapauAppointment extends Appointment {

	private static final long serialVersionUID = 1L;

	public enum State {
		NEW, MODIFIED, UNTOUCHED, DELETED;
	}
	
	private AppointmentV2 appointment;
	
	private State state;
	
	private Date start;
	private Date end;
	private Course course;
	private String studentGroup;
	private ClassRoom classRoom;
	private Area area;
	private ReserveAttemptDateType type;
	private Person owner;
	private List<Person> relatedPersons;
	private String description;
	private List<CharacteristicQuantity> characteristics;
	private boolean visible;
	
	private static int idSerie = 0;
	
	
	public static String getNewId() {
		idSerie ++;
		return String.valueOf(idSerie);
	}
	
	/**
	 * Retorna una copia del appointment.
	 */
	@Override
	public MapauAppointment clone() {
		MapauAppointment mapp = new MapauAppointment();
		mapp.setId(getId());
		mapp.appointment = appointment;
		mapp.start = new Date(start.getTime());
		mapp.end = new Date(end.getTime());
		mapp.course = course;
		mapp.studentGroup = studentGroup;
		mapp.classRoom = classRoom;
		mapp.area = area;
		mapp.type = type;
		mapp.owner = owner;
		
		if (relatedPersons == null) {
			mapp.relatedPersons = null;
		} else {
			mapp.relatedPersons = new ArrayList<Person>(relatedPersons);
		}
		
		if (characteristics == null) {
			mapp.characteristics = null;
		} else {
			mapp.characteristics = new ArrayList<CharacteristicQuantity>(characteristics);
		}
		
		mapp.description = description;
		mapp.visible = visible;
		
		mapp.setState(getState());
		
		return mapp;
	}	
	
	
	public MapauAppointment() {
		super();
		super.setId(getNewId());
		setState(State.NEW);
	}
	
	/**
	 * Copia los campos desde el mapauappointment al appointment rpoxy
	 * @param app
	 */
	public void to(AppointmentV2 app) {
		app.setStart(new Date(getStart().getTime()));
		app.setEnd(new Date(getEnd().getTime()));
		app.setCourse(getCourse());
		app.setStudentGroup(getStudentGroup());
		app.setClassRoom(getClassRoom());
		app.setArea(getArea());
		app.setRaType(getType());
		app.setOwner(getOwner());
		
		if (getRelatedPersons() != null) {
			app.setRelatedPersons(new ArrayList<Person>(getRelatedPersons()));
		}
		
		List<CharacteristicQuantity> chars = getCharacteristics();
		if (chars != null) {
			List<CharacteristicQuantity> charsp = new ArrayList<CharacteristicQuantity>();
			charsp.addAll(chars);
			app.setCharacteristics(charsp);
		}

//		app.setDescription(getDescription());
		app.setVisible(isVisible());
	}
	
	/**
	 * Copia los campos del appointment proxy al mapauappointment.
	 * @param appointment
	 */
	public void from(AppointmentV2 appointment) {
		
		String id = (appointment.getR() != null) ? appointment.getR().getId() : ((appointment.getRad() != null) ? appointment.getRad().getId() : getId());
		setId(id);
		
		this.start = new Date(appointment.getStart().getTime());
		this.end = new Date(appointment.getEnd().getTime());
		course = appointment.getCourse();
		studentGroup = appointment.getStudentGroup();
		classRoom = appointment.getClassRoom();
		area = appointment.getArea();
		type = appointment.getRaType();
		owner = appointment.getOwner();
		
		relatedPersons = new ArrayList<Person>();
		if (appointment.getRelatedPersons() != null) {
			relatedPersons.addAll(appointment.getRelatedPersons());
		}
		
		characteristics = new ArrayList<CharacteristicQuantity>();
		List<CharacteristicQuantity> charsp = appointment.getCharacteristics();
		if (charsp != null) {
			characteristics.addAll(charsp);
		}
		
		description = appointment.getDescription();
		visible = (appointment.getVisible() == null) ? false : appointment.getVisible(); 
	}
	
	
	public MapauAppointment(AppointmentV2 appointment) {
		super();
		
		idSerie ++;
		super.setId(String.valueOf(idSerie));
		
		this.appointment = appointment;
		from(appointment);

		setState(State.UNTOUCHED);
	}	
	
	public AppointmentV2 getAppointment() {
		return appointment;
	}
	
	//////// metodos del Appointment del calendar /////////////
	
	
	public boolean isModified() {
		return !(start.equals(appointment.getStart()) && end.equals(appointment.getEnd()));
	}
	
	
	private String getFullName(Person person) {
		String name = person.getName();
		String lastName = person.getLastName();
		return ((name != null) ? name : "") + " " + ((lastName != null) ? lastName : ""); 
	}
	
	@Override
	public List<com.bradrydzewski.gwt.calendar.client.Attendee> getAttendees() {
		List<com.bradrydzewski.gwt.calendar.client.Attendee> attendees = new ArrayList<com.bradrydzewski.gwt.calendar.client.Attendee>();
		for (Person person : getRelatedPersons()) {
			Attendee a = new Attendee();
			a.setAttending(Attending.Yes);
			a.setId(person.getId());
			a.setName(getFullName(person));
			attendees.add(a);
		}
		return attendees;
	}
	
	@Override
	public String getCreatedBy() {
		return getFullName(getOwner());
	}
	
	private String fullName(Course course) {
		String name = course.getName();
		String subject = (course.getSubject() != null && course.getSubject().getName() != null) ? course.getSubject().getName() : "";
		return name + " " + subject;
	}
	
	@Override
	public String getTitle() {
		return fullName(getCourse());
	}
	

	
	///////////////////////////////////////////////////////////
	
	
	
	
	public void setAppointment(AppointmentV2 appointment) {
		this.appointment = appointment;
	}	
	
	
	public List<CharacteristicQuantity> getCharacteristics() {
		return characteristics;
	}


	public void setCharacteristics(List<CharacteristicQuantity> characteristics) {
		this.characteristics = characteristics;
	}


	public Course getCourse() {
		return course;
	}


	public Person getOwner() {
		return owner;
	}

	public void setOwner(Person owner) {
		this.owner = owner;
	}



	public void setCourse(Course course) {
		this.course = course;
	}

	public String getStudentGroup() {
		return studentGroup;
	}

	public void setStudentGroup(String studentGroup) {
		this.studentGroup = studentGroup;
	}

	public ClassRoom getClassRoom() {
		return classRoom;
	}

	public void setClassRoom(ClassRoom classRoom) {
		this.classRoom = classRoom;
	}

	public Area getArea() {
		return area;
	}

	public void setArea(Area area) {
		this.area = area;
	}

	public ReserveAttemptDateType getType() {
		return type;
	}

	public void setType(ReserveAttemptDateType type) {
		this.type = type;
	}

	public List<Person> getRelatedPersons() {
		return relatedPersons;
	}

	public void setRelatedPersons(List<Person> relatedPersons) {
		this.relatedPersons = relatedPersons;
	}

	public boolean isVisible() {
		return visible;
	}

	public void setVisible(boolean visible) {
		this.visible = visible;
	}

	public void setDescription(String description) {
		this.description = description;
	}
	
	@Override
	public String getDescription() {
		StringBuilder sb = new StringBuilder();
		
		Course course = getCourse();
		if (course != null) {
			String courseName = course.getName();
			if (courseName != null) {
				sb.append("<div>Curso: " + courseName + "</div>");
			}
		}
		
		Subject subject = course.getSubject();
		if (subject != null) {
			String subjectName = subject.getName();
			if (subjectName != null) {
				sb.append("<div>Materia: " + subjectName + "</div>");
			}
		}
		
		
		ClassRoom classRoom = getClassRoom();
		if (classRoom == null) {
			sb.append("<div>Aula: No tiene</div>");
		} else {
			String classRoomName = classRoom.getName();
			if (classRoomName == null) {
				sb.append("<div>Aula: Sin Nombre</div>");
			} else {
				sb.append("<div>Aula: " + classRoomName + "</div>");
			}
		}
		
		if (description  != null) {
			sb.append("<div>" + description + "</div>");
		}
		
		return sb.toString();
	}
	
	@Override
	public Date getStart() {
		return start;
	}
	
	@Override
	public void setStart(Date start) {
		this.start = start;
	}
	
	@Override
	public Date getEnd() {
		return end;
	}
	
	@Override
	public void setEnd(Date end) {
		this.end = end;
	}

	
	
	public State getState() {
		return state;
	}
	
	public void setState(State state) {
		this.state = state;
		
		switch (state) {
			case NEW:
				setStyle(AppointmentStyle.RED_ORANGE);
				break;
			case MODIFIED:
				setStyle(AppointmentStyle.GREEN);
				break;
			case UNTOUCHED:
				setStyle(AppointmentStyle.BLUE);
				break;
			case DELETED:
				setStyle(AppointmentStyle.DARK_PURPLE);
				break;
		}
	}

	
}

