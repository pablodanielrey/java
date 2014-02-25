package ar.com.dcsys.model.classroom;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.List;

import org.jboss.weld.environment.se.Weld;
import org.jboss.weld.environment.se.WeldContainer;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import ar.com.dcsys.data.classroom.ClassRoom;
import ar.com.dcsys.data.classroom.ClassRoomBean;
import ar.com.dcsys.exceptions.MapauException;
import ar.com.dcsys.model.classroom.ClassRoomsManager;

public class ClassRoomsTest {
	
	/*
	
	private static Weld weld;
	private static WeldContainer container;
	
	@BeforeClass
	public static void setEnvironment() {
		weld = new Weld();
		container = weld.initialize();
	}

	
	@AfterClass
	public static void destroyEnvironment() {
		if (weld != null) {
			weld.shutdown();
		}
	}	
	
	private ClassRoomsManager getClassRoomsManager() {
		ClassRoomsManager classRoomsManager = container.instance().select(ClassRoomsManager.class).get();
		return classRoomsManager;
	}
	
	
	@Test
	public void persistTest() throws MapauException {
		ClassRoomsManager classRoomsManager = getClassRoomsManager();
		
		////// pueblo que el persist agregue un aula en la base ///////////////
		
		List<ClassRoom> classRooms = classRoomsManager.findAll();
		assertNotNull(classRooms);
		
		int count = classRooms.size();
		
		ClassRoom classRoom = new ClassRoomBean();
		classRoom.setName("102");
		
		String id = classRoomsManager.persist(classRoom);
		assertNotNull(id);
		assertNotNull(classRoom.getId());
		assertEquals(classRoom.getId(),id);
		
		ClassRoom classRoom2 = classRoomsManager.findById(id);
		assertEquals(classRoom.getId(), classRoom2.getId());
		assertNotNull(classRoom2.getName());
		assertEquals(classRoom2.getName(),"102");		
			

		/////////////////// pruebo que las aulas en la base tienen todas diferentes ids ////////////////
				
		classRooms = classRoomsManager.findAll();
		assertEquals(classRooms.size(), count + 1);
		
		for (ClassRoom c : classRooms) {
			String idc = c.getId();
			int equals = 0;
			for (ClassRoom c2 : classRooms) {
				assertNotNull(c2.getId());
				if (idc.equals(c2.getId())) {
					equals++;
				}
			}
			assertEquals(equals,1);
		}		
	}
	
	@Test
	public void findAllTest() throws MapauException {
		ClassRoomsManager classRoomsManager = getClassRoomsManager();
		List<ClassRoom> classrooms = classRoomsManager.findAll();
		assertNotNull(classrooms);
		
		for (ClassRoom c : classrooms) {
			assertNotNull(c.getId());
		}
	}	

*/

}
