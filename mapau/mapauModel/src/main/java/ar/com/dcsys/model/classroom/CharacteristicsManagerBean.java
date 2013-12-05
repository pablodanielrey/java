package ar.com.dcsys.model.classroom;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.inject.Inject;
import javax.inject.Singleton;

import ar.com.dcsys.data.classroom.Characteristic;
import ar.com.dcsys.data.classroom.CharacteristicDAO;
import ar.com.dcsys.exceptions.MapauException;
import ar.com.dcsys.exceptions.MapauNotFoundException;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;

@Singleton
public class CharacteristicsManagerBean implements CharacteristicsManager {
	
	private static Logger logger = Logger.getLogger(CharacteristicsManagerBean.class.getName());
	
	private LoadingCache<String,Characteristic> characteristicCache;
	private LoadingCache<String,String> nameCache;

	private final CharacteristicDAO characteristicDAO;
	
	
	@Inject
	public CharacteristicsManagerBean(CharacteristicDAO characteristicDAO) {
		this.characteristicDAO = characteristicDAO;
		createCaches();
	} 
	
	
	private void createCaches() {	

		/*
		 * creo la cache principal de caracteristicas
		 */
		characteristicCache = CacheBuilder.newBuilder().maximumSize(100).build(new CacheLoader<String,Characteristic>() {

			@Override
			public Characteristic load(String id) throws Exception {				
				Characteristic characteristic = characteristicDAO.findById(id);
				if (characteristic == null) {
					throw new MapauNotFoundException();
				}
				return characteristic;
			}
			
		});
			
		/*
		 * creo la cache por nombre
		 */
		nameCache = CacheBuilder.newBuilder().maximumSize(100).build(new CacheLoader<String,String>() {

			@Override
			public String load(String key) throws Exception {				
				String characteristicId = characteristicDAO.findIdByName(key);
				if (characteristicId == null) {
					throw new MapauNotFoundException();
				}
				return characteristicId;
			}
				
		});
					
	}
	
	/*
	@PreDestroy
	private void destory() {
		
		characteristicCache.cleanUp();
		characteristicCache.invalidateAll();
		characteristicCache = null;
		
		nameCache.cleanUp();
		nameCache.invalidateAll();
		nameCache = null;
		
		characteristicManager = null;
	}
	*/
	private void invalidatesCaches(Characteristic chars) {
		String id = chars.getId();
		if (id != null) {
			characteristicCache.invalidate(id);
		}
		nameCache.invalidateAll();
	}
	
	
	@Override
	public List<Characteristic> findAll() throws MapauException {		
		List<String> allIds = characteristicDAO.findAllIds();
		if (allIds == null || allIds.size() <= 0) {
			return new ArrayList<Characteristic>();
		}
		
		try {
			return characteristicCache.getAll(allIds).values().asList();
		} catch (ExecutionException e) {
			logger.log(Level.SEVERE, e.getCause().getMessage(),e.getCause());
			throw new MapauException(e.getCause().getMessage());
		}		
	}
	
	@Override
	public Characteristic findById(String id) throws MapauException {
		try {
			return characteristicCache.get(id);
		} catch (ExecutionException e) {
			if (e.getCause() instanceof MapauNotFoundException) {
				return null;
			} else {
				logger.log(Level.SEVERE, e.getCause().getMessage(),e.getCause());
				throw new MapauException(e.getCause().getMessage());
			}
		}
	}
	
	@Override
	public Characteristic findByName(String name) throws MapauException {
		if (name == null) {
			throw new MapauException("nombre == null");
		}
		
		String id;
		try {
			id = nameCache.get(name);
			Characteristic chars = characteristicCache.get(id);
			return chars;
		} catch (ExecutionException e) {
			if (e.getCause() instanceof MapauNotFoundException) {
				return null;
			} else {
				throw new MapauException(e.getCause().getMessage());
			}
		}
	}
	
	@Override
	public String persist(Characteristic chars) throws MapauException {	
		invalidatesCaches(chars);
		
		String newId = characteristicDAO.persist(chars);
		
		return newId;
	}
	
	@Override
	public void remove(Characteristic chars) throws MapauException {
		invalidatesCaches(chars);
		characteristicDAO.remove(chars);
	}
}
