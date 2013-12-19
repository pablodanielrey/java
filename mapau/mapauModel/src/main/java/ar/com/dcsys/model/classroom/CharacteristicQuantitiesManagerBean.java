package ar.com.dcsys.model.classroom;

import java.util.List;

import javax.inject.Inject;

import ar.com.dcsys.data.classroom.Characteristic;
import ar.com.dcsys.data.classroom.CharacteristicQuantity;
import ar.com.dcsys.data.classroom.CharacteristicQuantityBean;
import ar.com.dcsys.data.classroom.ClassRoom;
import ar.com.dcsys.data.reserve.ReserveAttemptDate;
import ar.com.dcsys.exceptions.MapauException;
import ar.com.dcsys.model.reserve.ReserveAttemptDatesManager;

public class CharacteristicQuantitiesManagerBean implements CharacteristicQuantitiesManager {

	private final ClassRoomsManager classRoomsManager;
	private final ReserveAttemptDatesManager reserveAttemptDatesManager;
	
	@Inject
	public CharacteristicQuantitiesManagerBean(ClassRoomsManager classRoomsManager, ReserveAttemptDatesManager reserveAttemptDatesManager) {
		this.classRoomsManager = classRoomsManager;
		this.reserveAttemptDatesManager = reserveAttemptDatesManager;
	}
	
	@Override
	public CharacteristicQuantity createNew(Characteristic characteristic,Long quantity) throws MapauException {
		CharacteristicQuantity chars = new CharacteristicQuantityBean();
		chars.setCharacteristic(characteristic);
		chars.setQuantity(quantity);
		return chars;
	}

	@Override
	public void persist(CharacteristicQuantity characteristicQuantity,ClassRoom classRoom) throws MapauException {
		classRoomsManager.persist(characteristicQuantity, classRoom);
	}
	
	@Override
	public void persist(List<CharacteristicQuantity> characteristicQuantities,ClassRoom classRoom) throws MapauException {
		if (characteristicQuantities == null || classRoom == null) {
			return;
		}
		
		for (CharacteristicQuantity cq : characteristicQuantities) {
			classRoomsManager.persist(cq, classRoom);
		} 
	}

	@Override
	public void remove(CharacteristicQuantity characteristicQuantity,ClassRoom classRoom) throws MapauException {
		classRoomsManager.remove(characteristicQuantity, classRoom);
	}
	
	@Override
	public void removeAll(ClassRoom classRoom) throws MapauException {
		classRoomsManager.removeAllCharacteristics(classRoom);
	}

	@Override
	public void persist(CharacteristicQuantity characteristicQuantity,ReserveAttemptDate reserveAttemptDate) throws MapauException {
		reserveAttemptDatesManager.persist(characteristicQuantity, reserveAttemptDate);
	}
	
	@Override
	public void persist(List<CharacteristicQuantity> characteristicQuantities,ReserveAttemptDate reserveAttemptDate) throws MapauException {
		if (characteristicQuantities == null || reserveAttemptDate == null) {
			return;
		}
		
		for (CharacteristicQuantity cq : characteristicQuantities) {
			reserveAttemptDatesManager.persist(cq, reserveAttemptDate);
		} 
	}

	@Override
	public void remove(CharacteristicQuantity characteristicQuantity,ReserveAttemptDate reserveAttemptDate) throws MapauException {
		reserveAttemptDatesManager.remove(characteristicQuantity, reserveAttemptDate);
	}
	
	@Override
	public void removeAll(ReserveAttemptDate reserveAttemptDate) throws MapauException {
		reserveAttemptDatesManager.removeAllCharacteristics(reserveAttemptDate);
	}

}
