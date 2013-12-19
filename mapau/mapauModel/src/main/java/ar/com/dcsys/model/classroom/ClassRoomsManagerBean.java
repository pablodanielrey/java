package ar.com.dcsys.model.classroom;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.inject.Inject;
import javax.inject.Singleton;

import ar.com.dcsys.data.classroom.CharacteristicQuantity;
import ar.com.dcsys.data.classroom.ClassRoom;
import ar.com.dcsys.data.classroom.ClassRoomDAO;
import ar.com.dcsys.exceptions.MapauException;
import ar.com.dcsys.exceptions.MapauNotFoundException;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;

@Singleton
public class ClassRoomsManagerBean implements ClassRoomsManager {
	
	private static Logger logger = Logger.getLogger(ClassRoomsManagerBean.class.getName());
	private LoadingCache<String,ClassRoom> classroomCache;
	private LoadingCache<String,String> nameCache; 
	
	private final ClassRoomDAO classRoomDAO;
	
	
	@Inject
	public ClassRoomsManagerBean(ClassRoomDAO classRoomDAO) {
		this.classRoomDAO = classRoomDAO;
		createCaches();
	}
	
	
	private void createCaches() {
			
		/*
		 * creo la cache principal para classroom
		 */
		classroomCache = CacheBuilder.newBuilder().maximumSize(500).build(new CacheLoader<String,ClassRoom>() {

			@Override
			public ClassRoom load(String key) throws Exception {
				ClassRoom classRoom = classRoomDAO.findById(key);
				if (classRoom == null) {
					throw new MapauNotFoundException();
				}
				return classRoom;					
			}
			
		});

		/*
		 * creo la cache por nombre
		 */
		nameCache = CacheBuilder.newBuilder().maximumSize(100).build(new CacheLoader<String,String>() {

			@Override
			public String load(String key) throws Exception {
				String classRoomId = classRoomDAO.findIdByName(key);
				if (classRoomId == null) {
					throw new MapauNotFoundException();
				}
				return classRoomId;
			}
			
		});		
		
	}
/*
	@PreDestroy
	private void destroy() {
		
		classroomCache.cleanUp();
		classroomCache.invalidateAll();
		classroomCache = null;
		
		nameCache.cleanUp();
		nameCache.invalidateAll();
		nameCache = null;
		
		classRoomManager = null;
	}*/
	
	private void invalidatesCaches(ClassRoom classRoom) {
		String id = classRoom.getId();
		if (id != null) {
			classroomCache.invalidate(id);
		}
		nameCache.invalidateAll();
	}
	
	@Override
	public List<ClassRoom> findAll() throws MapauException {
		List<String> allIds = classRoomDAO.findAllIds();
		if (allIds == null || allIds.size() <= 0) {
			return new ArrayList<ClassRoom>();
		}
		
		try {
			return classroomCache.getAll(allIds).values().asList();
		} catch (ExecutionException e) {
			logger.log(Level.SEVERE, e.getCause().getMessage(),e.getCause());
			throw new MapauException(e.getCause().getMessage());
		}	
	}
	
	@Override
	public ClassRoom findById(String id) throws MapauException {
		try {
			return classroomCache.get(id);
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
	public ClassRoom findByName(String name) throws MapauException {
		if (name == null) {
			throw new MapauException("nombre == null");
		}
		
		String id;
		try {
			id = nameCache.get(name);
			ClassRoom cr = classroomCache.get(id);
			return cr;
		} catch (ExecutionException e) {
			if (e.getCause() instanceof MapauNotFoundException) {
				return null;
			} else {
				throw new MapauException(e.getCause().getMessage());
			}
		}
	}
	
	@Override
	public String persist(ClassRoom classRoom) throws MapauException {
		invalidatesCaches(classRoom);
		
		String newId = classRoomDAO.persist(classRoom);
		
		return newId;
	}
	
	@Override
	public void remove(ClassRoom classRoom) throws MapauException {
		invalidatesCaches(classRoom);
		classRoomDAO.remove(classRoom);
	}
	
	
	
	
	/////////////////////////////////////////////////////////////////////////////////////////////
	/////////////////////// CHARACTERISTICS /////////////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////////////////////////////////
	
	
	@Override
	public void persist(CharacteristicQuantity chars, ClassRoom classRoom) throws MapauException {
		classRoomDAO.persist(chars, classRoom);
	}
	
	@Override
	public void remove(CharacteristicQuantity chars, ClassRoom classRoom) throws MapauException {
		classRoomDAO.remove(chars, classRoom);
	}
	
	
	@Override
	public void removeAllCharacteristics(ClassRoom classRoom) throws MapauException {
		classRoomDAO.removeAllCharacteristics(classRoom);
	}
	
	
	
	
	
}
