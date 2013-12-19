package ar.com.dcsys.model.classroom;

import java.util.List;

import ar.com.dcsys.data.classroom.Characteristic;
import ar.com.dcsys.data.classroom.CharacteristicQuantity;
import ar.com.dcsys.data.classroom.ClassRoom;
import ar.com.dcsys.data.reserve.ReserveAttemptDate;
import ar.com.dcsys.exceptions.MapauException;

public interface CharacteristicQuantitiesManager {
	
	public CharacteristicQuantity createNew(Characteristic characteristic, Long quantity) throws MapauException;
	
	public void persist(CharacteristicQuantity characteristicQuantity, ClassRoom classRoom) throws MapauException;
	public void persist(List<CharacteristicQuantity> characteristicQuantities, ClassRoom classRoom) throws MapauException;
	public void remove(CharacteristicQuantity characteristicQuantity, ClassRoom classRoom) throws MapauException;
	public void removeAll(ClassRoom classRoom) throws MapauException;

	public void persist(CharacteristicQuantity characteristicQuantity, ReserveAttemptDate reserveAttemptDate) throws MapauException;
	public void persist(List<CharacteristicQuantity> characteristicQuantities, ReserveAttemptDate reserveAttemptDate) throws MapauException;
	public void remove(CharacteristicQuantity characteristicQuantity, ReserveAttemptDate reserveAttemptDate) throws MapauException;
	public void removeAll(ReserveAttemptDate reserveAttemptDate) throws MapauException;
}
