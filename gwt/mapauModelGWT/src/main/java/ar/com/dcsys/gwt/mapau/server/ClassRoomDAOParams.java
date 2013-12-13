package ar.com.dcsys.gwt.mapau.server;

import javax.inject.Inject;

import ar.com.dcsys.data.classroom.Characteristic;
import ar.com.dcsys.data.classroom.ClassRoomDAO;
import ar.com.dcsys.exceptions.MapauException;
import ar.com.dcsys.model.classroom.CharacteristicsManager;

public class ClassRoomDAOParams implements ClassRoomDAO.Params {

	private final CharacteristicsManager characteristicsManager;
	
	@Inject
	public ClassRoomDAOParams(CharacteristicsManager characteristicsManager) {
		this.characteristicsManager = characteristicsManager;
	}
	
	@Override
	public Characteristic findCharacteristicById(String id)	throws MapauException {
		return characteristicsManager.findById(id);
	}

	
	
}
