package ar.com.dcsys.model.classroom;

import javax.inject.Inject;

import ar.com.dcsys.data.classroom.Characteristic;
import ar.com.dcsys.data.classroom.ClassRoomDAO;
import ar.com.dcsys.exceptions.MapauException;

public class ClassRoomParams implements ClassRoomDAO.Params {


	private final CharacteristicsManager characteristicsManager;
	
	@Inject
	public ClassRoomParams(CharacteristicsManager characteristicsManager) {
		this.characteristicsManager = characteristicsManager;
	}
	
	@Override
	public Characteristic findCharacteristicById(String id) throws MapauException {
		return characteristicsManager.findById(id);
	}
}
