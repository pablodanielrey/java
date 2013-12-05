package ar.com.dcsys.model.silabouse;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ExecutionException;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.inject.Inject;
import javax.inject.Singleton;

import ar.com.dcsys.data.group.Group;
import ar.com.dcsys.data.silabouse.Area;
import ar.com.dcsys.data.silabouse.AreaDAO;
import ar.com.dcsys.exceptions.MapauException;
import ar.com.dcsys.exceptions.MapauNotFoundException;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;

@Singleton
public class AreasManagerBean implements AreasManager {

	
	private static Logger logger = Logger.getLogger(AreasManagerBean.class.getName());
	
	private LoadingCache<String,Area> areaCache;
	private LoadingCache<String,List<String>> groupCache;
	
	private final AreaDAO areaDAO;
	
	@Inject
	public AreasManagerBean(AreaDAO areaDAO) {
		this.areaDAO = areaDAO;
		createCaches();
	}
	
	private void createCaches() {		
		/*
		 * creo la cache principal de area
		 */
		areaCache = CacheBuilder.newBuilder().maximumSize(50).build(new CacheLoader<String,Area>(){

			@Override
			public Area load(String id) throws Exception {				
				Area area = areaDAO.findById(id);
				if (area == null) {
					throw new MapauNotFoundException();
				}
				return area;
			}
			
		});
		
		/*
		 * creo la cache que mapea por grupo las areas
		 */
		groupCache = CacheBuilder.newBuilder().maximumSize(500).build(new CacheLoader<String,List<String>>() {

			@Override
			public List<String> load(String id) throws Exception {				
				List<String> ids = areaDAO.findIdsByGroup(id);
				if (ids == null || ids.size() <= 0) {
					throw new MapauNotFoundException();
				}
				return ids;
			}
		});
		
	}
	
/*
	@PreDestroy
	private void destroy() {
		
		areaCache.cleanUp();
		areaCache.invalidateAll();
		areaCache = null;
		
		groupCache.cleanUp();
		groupCache.invalidateAll();
		groupCache = null;
		
		areaManager = null;
	}
	*/
	
	private void invalidateCaches(Area area) {
		String id = area.getId();
		if (id != null) {
			areaCache.invalidate(id);
		}
		groupCache.invalidateAll();
	}
		
	@Override
	public List<Area> findAll() throws MapauException {
		List<String> allIds = areaDAO.findAllIds();
		if (allIds == null || allIds.size() <= 0) {
			return new ArrayList<Area>();
		}
		
		try {
			return areaCache.getAll(allIds).values().asList();
		} catch (ExecutionException e) {
			logger.log(Level.SEVERE, e.getCause().getMessage(),e.getCause());
			throw new MapauException(e.getCause().getMessage());
		}
	}

	@Override
	public Area findById(String id) throws MapauException {
		try {
			return areaCache.get(id);
		} catch (ExecutionException e) {
			if (e.getCause() instanceof MapauNotFoundException) {
				return null;
			} else {
				logger.log(Level.SEVERE, e.getCause().getMessage(),e.getCause());
				throw new MapauException(e.getCause().getMessage());
			}
		}
	}
	
	private List<Area> findBy(Group group) throws MapauException {
		if (group == null || group.getId() == null) {
			return new ArrayList<Area>();
		}
		
		try {
			List<String> ids = groupCache.get(group.getId());
			List<Area> areas = new ArrayList<Area>();
			for (String id : ids) {
				Area area = areaCache.get(id);
				if (area != null) {
					areas.add(area);
				}
			}
			return areas;
		} catch (ExecutionException e) {
			if (e.getCause() instanceof MapauNotFoundException) {
				return null;
			} else {
				throw new MapauException(e.getCause().getMessage());
			}
		}		
	}
	
	@Override
	public List<Area> findBy(List<Group> groups) throws MapauException {		
		if (groups == null) {
			logger.log(Level.SEVERE, "groups == null");
			throw new MapauException("groups == null");
		}
		
		Set<Area> areas = new HashSet<Area>();
		for (Group group : groups) {
			areas.addAll(findBy(group));
		}
		return new ArrayList<Area>(areas);
	}
		
	@Override
	public String persist(Area area) throws MapauException {
		invalidateCaches(area);
		
		String newId = areaDAO.persist(area);
		
		return newId;
	}
	
	@Override
	public void remove(Area area) throws MapauException {
		invalidateCaches(area);
		areaDAO.persist(area);
	}
}
