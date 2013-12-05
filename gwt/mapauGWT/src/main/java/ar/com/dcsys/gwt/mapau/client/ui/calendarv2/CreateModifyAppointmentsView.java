package ar.com.dcsys.gwt.mapau.client.ui.calendarv2;

import java.util.Date;
import java.util.List;

import ar.com.dcsys.data.classroom.Characteristic;
import ar.com.dcsys.data.classroom.CharacteristicQuantity;
import ar.com.dcsys.data.classroom.ClassRoom;
import ar.com.dcsys.data.person.Person;
import ar.com.dcsys.data.reserve.ReserveAttemptDateType;
import ar.com.dcsys.data.silabouse.Area;
import ar.com.dcsys.data.silabouse.Course;
import ar.com.dcsys.gwt.mapau.client.activity.calendar.adapter.MapauAppointment;

import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.view.client.MultiSelectionModel;
import com.google.gwt.view.client.SingleSelectionModel;

public interface CreateModifyAppointmentsView extends IsWidget {

	
	public void setAppointment(MapauAppointment a);
	
	public void setCourses(List<Course> courses);
	public void setCoursesSelectionModel(SingleSelectionModel<Course> selection);
	public void setCourseReadOnly(boolean v);

	public void setStudentGroups(List<String> studentGroups);
	public String getStudentGroup();
	public void setStudentGroup(String studentGroupApp);
	public void setStudentGroupReadOnly(boolean v);
	
	public void setTypes(List<ReserveAttemptDateType> types);
	public void setTypesSelection(SingleSelectionModel<ReserveAttemptDateType> selection);
	public void setTypeReadOnly(boolean v);
	
	public void setCharacteristics(List<Characteristic> chars);
	public void setCharacteristicQuantity(List<CharacteristicQuantity> chars);
	public List<CharacteristicQuantity> getCharacteristicsQuantity();
	public void setCharacteristicsReadOnly(boolean v);
	
	public void setAreas(List<Area> areas);
	public void setAreasSelectionModel(SingleSelectionModel<Area> selection);
	public void setAreaReadOnly(boolean v);
	
	public void setPersons(List<Person> persons);
	public void setRelatedPersons(List<Person> persons);
	public List<Person> getRelatedPersons();
	public void setRelatedPersonsReadOnly(boolean v);

	public void setNotes(String notes);
	public String getNotes();
	public void setNotesReadOnly(boolean v);
	
	public void setClassRooms(List<ClassRoom> classRooms);
	public void setClassRoomsSelectionModel(MultiSelectionModel<ClassRoom> selection);
	public void setClassRoomReadOnly(boolean v);
	
	public Date getStart();
	public void setStart(Date start);
	public Date getEnd();
	public void setEnd(Date end);
	
	public void setVisibleReadOnly(boolean v);
		

	public void setPresenter(Presenter p);
	public void clear();	
	
	public interface Presenter {
		public void persist();
		public void cancel();
	}
	
}
