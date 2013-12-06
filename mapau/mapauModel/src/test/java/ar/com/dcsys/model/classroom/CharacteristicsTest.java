package ar.com.dcsys.model.classroom;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.List;

import org.jboss.weld.environment.se.Weld;
import org.jboss.weld.environment.se.WeldContainer;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import ar.com.dcsys.data.classroom.Characteristic;
import ar.com.dcsys.data.classroom.CharacteristicBean;
import ar.com.dcsys.exceptions.MapauException;

public class CharacteristicsTest {
	
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
	
	private CharacteristicsManager getCharacteristicsManager() {
		CharacteristicsManager characteristicsManager = container.instance().select(CharacteristicsManager.class).get();
		return characteristicsManager;
	}
	
	
	@Test
	public void persistTest() throws MapauException {
		CharacteristicsManager characteristicsManager = getCharacteristicsManager();
		
		////// pueblo que el persist agregue una caracteristica en la base ///////////////
		
		List<Characteristic> characteristics = characteristicsManager.findAll();
		assertNotNull(characteristics);
		
		int count = characteristics.size();
		
		Characteristic characteristic = new CharacteristicBean();
		characteristic.setName("aire2");
		
		String id = characteristicsManager.persist(characteristic);
		assertNotNull(id);
		assertNotNull(characteristic.getId());
		assertEquals(characteristic.getId(),id);
		
		Characteristic characteristic2 = characteristicsManager.findById(id);
		assertEquals(characteristic.getId(), characteristic2.getId());
		assertNotNull(characteristic2.getName());
		assertEquals(characteristic2.getName(),"aire2");		
		
		Characteristic characteristic3 = characteristicsManager.findByName("aire2");
		assertNotNull(characteristic3);
		assertNotNull(characteristic3.getId());
		assertEquals(id, characteristic3.getId());
			

		/////////////////// pruebo que las caracteristicas en la base tienen todas diferentes ids ////////////////
				
		characteristics = characteristicsManager.findAll();
		assertEquals(characteristics.size(), count + 1);
		
		for (Characteristic c : characteristics) {
			String idc = c.getId();
			int equals = 0;
			for (Characteristic c2 : characteristics) {
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
		CharacteristicsManager characteristicsManager = getCharacteristicsManager();
		List<Characteristic> characteristics = characteristicsManager.findAll();
		assertNotNull(characteristics);
		
		for (Characteristic c : characteristics) {
			assertNotNull(c.getId());
		}
	}	

}
