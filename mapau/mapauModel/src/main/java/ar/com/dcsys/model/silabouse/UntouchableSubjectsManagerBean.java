package ar.com.dcsys.model.silabouse;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.inject.Inject;
import javax.inject.Singleton;

import ar.com.dcsys.data.silabouse.Area;
import ar.com.dcsys.data.silabouse.UntouchableSubject;
import ar.com.dcsys.data.silabouse.UntouchableSubjectDAO;
import ar.com.dcsys.exceptions.MapauException;
import ar.com.dcsys.exceptions.MapauNotFoundException;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;

@Singleton
public class UntouchableSubjectsManagerBean implements UntouchableSubjectsManager {

	private static Logger logger = Logger.getLogger(UntouchableSubjectsManagerBean.class.getName());
	
	private LoadingCache<String,UntouchableSubject> untouchableSubjectCache;
	private LoadingCache<String,String> areasCache;

	
	private final UntouchableSubjectDAO untouchableSubjectDAO;
	
	@Inject
	public UntouchableSubjectsManagerBean(UntouchableSubjectDAO untouchableSubjectDAO) {
		this.untouchableSubjectDAO = untouchableSubjectDAO;
		createCaches();
	}
	
	
	/*@PreDestroy
	private void destroy() {
		
		areasCache.cleanUp();
		areasCache.invalidateAll();
		areasCache = null;
		
		untouchableSubjectCache.cleanUp();
		untouchableSubjectCache.invalidateAll();
		untouchableSubjectCache = null;
		
		untouchableSubjectManager = null;
		untouchableSubjectBackEnd = null;
	}*/
	
	private void createCaches() {
			
		/*
		 * creo la cache para id -> untouchableSubject
		 */
		untouchableSubjectCache = CacheBuilder.newBuilder().maximumSize(300).build(new CacheLoader<String,UntouchableSubject>() {

			@Override
			public UntouchableSubject load(String untouchableSubjectId) throws Exception {				
				UntouchableSubject untouchableSubject = untouchableSubjectDAO.findById(untouchableSubjectId);
				if (untouchableSubject == null) {
					throw new MapauNotFoundException();
				}
				return untouchableSubject;
			}
			
		});
			
		/*
		 * creo la cache para area_id -> id
		 */
		areasCache = CacheBuilder.newBuilder().maximumSize(300).build(new CacheLoader<String,String>() {

			@Override
			public String load(String areaId) throws Exception {
				String id = untouchableSubjectDAO.findIdByArea(areaId);
				if (id == null) {
					throw new MapauNotFoundException();
				}
				return id;
			}
			
		});
	}
	
	/**
	 * Invalida las caches correctas en caso de cambio de datos
	 * @param us
	 */
	private void invalidateCaches(UntouchableSubject us) {
		String id = us.getId();
		if (id != null) {
			untouchableSubjectCache.invalidate(id);
		}
		
		areasCache.invalidateAll();
	}

	@Override
	public List<UntouchableSubject> findAll() throws MapauException {
		List<String> allIds = untouchableSubjectDAO.findAllIds();
		if (allIds == null || allIds.size() <= 0) {
			return new ArrayList<UntouchableSubject>();
		}
		
		try {
			return untouchableSubjectCache.getAll(allIds).values().asList();
		} catch (ExecutionException e) {
			throw new MapauException(e.getCause().getMessage());
		}
	}

	@Override
	public UntouchableSubject findById(String id) throws MapauException {

		try {
			return untouchableSubjectCache.get(id);
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
	public UntouchableSubject findBy(Area area) throws MapauException {
		if (area == null || area.getId() == null) {
			throw new MapauException("area == null");
		}
		try {
			String id = areasCache.get(area.getId());
			UntouchableSubject us = untouchableSubjectCache.get(id);			
			return us;
			
		} catch (ExecutionException e) {
			if (e.getCause() instanceof MapauNotFoundException) {
				return null;
			} else {
				throw new MapauException(e.getCause().getMessage());
			}
		}
	}
	
	@Override
	public String persist(UntouchableSubject us) throws MapauException {
		
		invalidateCaches(us);
		
		String newId = untouchableSubjectDAO.persist(us);
		
		return newId;
	}

	@Override
	public void remove(UntouchableSubject us) throws MapauException {
		invalidateCaches(us);
		
		untouchableSubjectDAO.remove(us);
	}

}
