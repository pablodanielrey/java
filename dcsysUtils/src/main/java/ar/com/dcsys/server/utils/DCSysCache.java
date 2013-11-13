package ar.com.dcsys.server.utils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DCSysCache<T> {
	
	private Map<String,T> cache = new HashMap<String,T>();
	
	public T get(String id) {
		if (id == null) {
			return null;
		}
		return cache.get(id);
	}
	
	public List<T> getAll() {
		List<T> objects = new ArrayList<T>(cache.values());
		return Collections.unmodifiableList(objects);
	}
	
	public void put(String id, T value) {
		if (id == null) {
			return;
		}
		cache.put(id, value);
	}
	
	public void remove(String id) {
		if (id == null) {
			return;
		}
		cache.remove(id);
	}
	
	public void clear() {
		cache.clear();
	}
}
