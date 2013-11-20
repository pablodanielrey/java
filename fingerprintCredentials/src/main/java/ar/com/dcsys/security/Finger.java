package ar.com.dcsys.security;

import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;

public enum Finger {
	
	RIGHT_THUMB(0), RIGHT_INDEX(1), RIGHT_MIDDLE(2), RIGHT_RING(3), RIGHT_PINKY(4), 
	LEFT_THUMB(5), LEFT_INDEX(6), LEFT_MIDDLE(7), LEFT_RING(8), LEFT_PINKY(9);

	private final int id;
	
	Finger(int id) {
		this.id = id;
	}
	
	public int getId() {
		return id;
	}

	
	// esto es necesario para la reversa del getId. es una restricción de la clase enum que no tiene todo lo necesario.
	
	
	private static final Map<Integer,Finger> reverseMap = new HashMap<Integer,Finger>();
	
	static {
		for (Finger f : EnumSet.allOf(Finger.class)) {
			reverseMap.put(f.getId(), f);
		}
	}
	
	public static Finger getFinger(int id) {
		return reverseMap.get(id);
	}
	
}

