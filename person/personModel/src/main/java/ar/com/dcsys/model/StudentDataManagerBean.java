package ar.com.dcsys.model;

import java.util.concurrent.ExecutionException;

import javax.inject.Inject;

import ar.com.dcsys.config.Config;
import ar.com.dcsys.data.person.StudentData;
import ar.com.dcsys.data.person.StudentDataDAO;
import ar.com.dcsys.exceptions.PersonException;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;

public class StudentDataManagerBean implements StudentDataManager {

	@Inject @Config String cacheLoad;
	
	private LoadingCache<String,StudentData> dataCache;
	private LoadingCache<String,String> studentNumberIndex;
	
	private final StudentDataDAO studentDataDAO;
	
	@Inject
	public StudentDataManagerBean(StudentDataDAO studentDataDAO) {
		this.studentDataDAO = studentDataDAO;
		
		createCaches();
	}
	
	private void createCaches() {
		
		if (cacheLoad == null) {
			cacheLoad = "100";
		}
		int CACHE_LOAD = Integer.parseInt(cacheLoad);
		
		
		dataCache = CacheBuilder.newBuilder().maximumSize(CACHE_LOAD).build(new CacheLoader<String, StudentData>() {
			@Override
			public StudentData load(String id) throws Exception {
				StudentData sd = studentDataDAO.findById(id);
				return sd;
			}
		});
		
		studentNumberIndex = CacheBuilder.newBuilder().maximumSize(CACHE_LOAD).build(new CacheLoader<String, String>() {
			@Override
			public String load(String sn) throws Exception {
				String id = studentDataDAO.findByStudentNumber(sn);
				return id;
			}
		});
		
	}
	
	@Override
	public String fingIdByStudentNumber(String sn) throws PersonException {
		try {
			String id = studentNumberIndex.get(sn);
			return id;
			
		} catch (ExecutionException e) {
			throw new PersonException(e);
		}
	}
	
	
	@Override
	public StudentData findByStudentNumber(String sn) throws PersonException {
		
		try {
			String id = studentNumberIndex.get(sn);
			StudentData sd = dataCache.get(id);
			return sd;
			
		} catch (ExecutionException e) {
			throw new PersonException(e);
		}
	}
	
	
}
