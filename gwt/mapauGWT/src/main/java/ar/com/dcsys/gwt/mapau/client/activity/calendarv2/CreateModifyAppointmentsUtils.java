package ar.com.dcsys.gwt.mapau.client.activity.calendarv2;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import ar.com.dcsys.data.classroom.Characteristic;
import ar.com.dcsys.data.person.Person;
import ar.com.dcsys.data.reserve.ReserveAttemptDateType;
import ar.com.dcsys.data.silabouse.Area;
import ar.com.dcsys.data.silabouse.Course;
import ar.com.dcsys.gwt.manager.shared.Receiver;
import ar.com.dcsys.gwt.mapau.client.manager.ClassRoomsManager;
import ar.com.dcsys.gwt.mapau.client.manager.MapauManager;
import ar.com.dcsys.gwt.mapau.client.manager.SilegManager;

public class CreateModifyAppointmentsUtils {
	
	
	/**
	 * Obtiene todos los cursos que puede reservar la persona y setea el curso seleccionado.
	 * @param rec
	 */
	public static void findCourses(MapauManager rf, final Receiver<List<Course>> rec) {
		rf.getCoursesToCreateReserve(new Receiver<List<Course>>() {
			@Override
			public void onSuccess(List<Course> courses) {
				
				// error fatal!!.
				if (courses == null || courses.size() <= 0) {
					rec.onSuccess(courses);
					return;
				}
				
				// los cursos deben ser únicos asi que chequeo aca esa condición.
				Set<Course> coursesSet = new HashSet<Course>(courses);
				List<Course> coursesL = new ArrayList<Course>();
				coursesL.addAll(coursesSet);
				
				rec.onSuccess(coursesL);
			}
			@Override
			public void onFailure(Throwable error) {
				rec.onFailure(error);
			}
		});
	}
	
	
	/**
	 * Setea todas las comisiones que se pueden seleccionar.
	 * @param rec
	 */
	public static void findStudentGroups(final Receiver<List<String>> rec) {
		List<String> studentGroups = new ArrayList<String>(); 
		for (int i = 1; i < 20; i++) {
			studentGroups.add("Comisión " + String.valueOf(i));
		}
		rec.onSuccess(studentGroups);
	}
	
	
	/**
	 * Obtiene todos los tipos de reservas a realizar y las setea en la vista.
	 * @param rec
	 */
	public static void findTypes(MapauManager rf, final Receiver<List<ReserveAttemptDateType>> rec) {
		rf.findAllReserveAttemptType(new Receiver<List<ReserveAttemptDateType>>() {
			@Override
			public void onSuccess(List<ReserveAttemptDateType> types) {
				rec.onSuccess(types);
			}
			public void onFailure(Throwable error) {
				rec.onFailure(error);
			};
		});
	}
	

	public static void findCharacteristics(ClassRoomsManager rf, final Receiver<List<Characteristic>> rec) {
		rf.findAllCharacteristics(new Receiver<List<Characteristic>>() {
			public void onSuccess(List<Characteristic> chars) {
				rec.onSuccess(chars);
			};
			@Override
			public void onFailure(Throwable error) {
				rec.onFailure(error);
			}
		});
	}	
	
	
	/**
	 * Busca las areas existentes y selecciona la indicada dentro del selection model.
	 * @param rec
	 */
	public static void findAreas(MapauManager rf, final Receiver<List<Area>> rec) {
		rf.findAllArea(new Receiver<List<Area>>() {
			@Override
			public void onSuccess(List<Area> areas) {
				rec.onSuccess(areas);
			}
			@Override
			public void onFailure(Throwable error) {
				rec.onFailure(error);
			}
		});
	}	
	
	/**
	 * Busca una persona por id dentro de la lsita, si la encuentra la retorna. en caso contrario retorna null.
	 * @param persons
	 * @param id
	 * @return
	 */
	public static Person findPerson(List<Person> persons, String id) {
		for (Person p : persons) {
			if (id.equals(p.getId())) {
				return p;
			}
		}
		return null;
	}
		

	/**
	 * Busca todas las personas que puedan relacionarse con la reserva para el curso determinado y setea en la vista
	 * @param rec
	 */
	public static void findPersons(SilegManager rf, Course course, final Receiver<List<Person>> rec) {
		rf.findTeachers(course, new Receiver<List<Person>>() {
			@Override
			public void onSuccess(List<Person> persons) {
				rec.onSuccess(persons);
			}
			@Override
			public void onFailure(Throwable error) {
				rec.onFailure(error);
			}
		});
	}
	
	
}
