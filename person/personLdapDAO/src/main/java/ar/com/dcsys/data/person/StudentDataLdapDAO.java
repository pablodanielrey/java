package ar.com.dcsys.data.person;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.inject.Inject;
import javax.naming.NamingEnumeration;
import javax.naming.NamingException;
import javax.naming.directory.Attribute;
import javax.naming.directory.Attributes;
import javax.naming.directory.BasicAttribute;
import javax.naming.directory.DirContext;
import javax.naming.directory.ModificationItem;
import javax.naming.directory.SearchControls;
import javax.naming.directory.SearchResult;

import ar.com.dcsys.data.OpenLdapContextProvider;
import ar.com.dcsys.exceptions.PersonException;

public class StudentDataLdapDAO implements StudentDataDAO {

	private static final Logger logger = Logger.getLogger(StudentDataLdapDAO.class.getName());
	private final OpenLdapContextProvider cp;
	
	@Inject
	public StudentDataLdapDAO(OpenLdapContextProvider cp) {
		this.cp = cp;
	}

	
	private class ObjectDescriptor {
		String dn;
		List<String> objectClasses;
	}
	
	
	private ObjectDescriptor findDnByUUid(DirContext ctx, String uuid) throws PersonException {
		if (uuid == null) {
			throw new PersonException("uuid == null");
		}
		
		try {
			SearchControls sc = new SearchControls();
			sc.setReturningAttributes(new String[]{"dn","objectClass"});
			sc.setSearchScope(SearchControls.SUBTREE_SCOPE);
	
			String filter = "(x-dcsys-uuid=" + uuid + ")";
			
			NamingEnumeration data = ctx.search(cp.getUsersBase(), filter, sc);
			try {
				if (!data.hasMore()) {
					String msg = "uuid " + uuid + " not found";
					logger.log(Level.WARNING,msg);
					throw new PersonException(msg);
				}
				
				SearchResult res = (SearchResult)data.next();
				Attributes attrs = res.getAttributes();
				Attribute dn = attrs.get("dn");
				
				Attribute oc = attrs.get("objectClass");
				NamingEnumeration<?> ocs = oc.getAll();
				List<String> obclass = new ArrayList<>();
				while (ocs.hasMore()) {
					obclass.add((String)ocs.next());
				}
				
				ObjectDescriptor od = new ObjectDescriptor();
				od.dn = (String)dn.get();
				od.objectClasses = obclass;
				
				return od;
				
			} finally {
				data.close();
			}

		} catch (Exception e) {
			logger.log(Level.SEVERE,e.getMessage(),e);
			throw new PersonException(e);
		}
	}	
	
	
	@Override
	public String persist(StudentData sd) throws PersonException {
		
		if (sd == null) {
			throw new PersonException("StudentData == null");
		}
		
		if (sd.getId() == null) {
			throw new PersonException("StudentData.id == null");
		}
				
		
		try {
			DirContext ctx = cp.getDirContext();
			try {
				ObjectDescriptor od = findDnByUUid(ctx, sd.getId());
				if (od == null || od.dn == null || od.objectClasses == null || od.objectClasses.size() <= 0) {
					logger.log(Level.SEVERE,"findDnByUUid failed");
					throw new PersonException("findDnByUUid failed");
				}

				List<ModificationItem> lMods = new ArrayList<ModificationItem>();
				
				Attribute legajo = new BasicAttribute("x-dcsys-legajo");
				legajo.add(sd.getStudentNumber());
				lMods.add(new ModificationItem(DirContext.REPLACE_ATTRIBUTE,legajo));
				
				// en el caso de que no sea estudiante, agrego esa clase a la persona.
				if (!od.objectClasses.contains("x-dcsys-estudiante")) {
					
					Attribute objectClass = new BasicAttribute("objectClass");
					for (String oc : od.objectClasses) {
						objectClass.add(oc);
					}
					objectClass.add("x-dcsys-estudiante");
					
					lMods.add(new ModificationItem(DirContext.REPLACE_ATTRIBUTE,objectClass));
				}
				
				// transformo la lista a array y ejecuto la modificación.
				
				ModificationItem[] mods = new ModificationItem[lMods.size()];
				for (int i = 0; i < lMods.size(); i++) {
					mods[i] = lMods.get(i);
				}

				ctx.modifyAttributes(od.dn,mods);				
			
				return sd.getId();
				
			} finally {
				ctx.close();
			}
			
		} catch (NamingException e) {
			throw new PersonException(e);
		}
		
	}

	@Override
	public StudentData findById(String id) throws PersonException {
		
		if (id == null) { 
			throw new PersonException("id == null");
		}
		
		try {
			DirContext ctx = cp.getDirContext();
			try {
				ObjectDescriptor od = findDnByUUid(ctx,id);
				
				Attributes attrs = ctx.getAttributes(od.dn, new String[]{"x-dcsys-legajo"});
				Attribute legajo = attrs.get("x-dcsys-legajo");
				String studentNumber = (String)legajo.get();
				
				StudentDataBean sd = new StudentDataBean();
				sd.setId(id);
				sd.setStudentNumber(studentNumber);
				
				return sd;
				
			} finally {
				ctx.close();
			}
			
		} catch (NamingException e) {
			logger.log(Level.SEVERE,e.getMessage(),e);
			throw new PersonException(e);
		}
	}

	/**
	 * Retorna todos los studentDatas que encuentra.
	 * en este caso era lo mismo retornar el legajo que los ids.
	 * en el caso que esta clase crezca entocnces debería ser mas eficiente obtener los ids solamente.
	 * @return
	 * @throws PersonException
	 */
	@Override
	public List<StudentData> findAll() throws PersonException {

		try {
			SearchControls sc = new SearchControls();
			sc.setReturningAttributes(new String[]{"x-dcsys-legajo","x-dcsys-uuid"});
			sc.setSearchScope(SearchControls.SUBTREE_SCOPE);
	
			String filter = "(x-dcsys-legajo=*)";

			DirContext ctx = cp.getDirContext();
			try {
			
				NamingEnumeration data = ctx.search(cp.getUsersBase(), filter, sc);
				try {
					
					List<StudentData> ret = new ArrayList<>(); 
					
					while (data.hasMore()) {
						SearchResult res = (SearchResult)data.next();
						Attributes attrs = res.getAttributes();

						Attribute legajo = attrs.get("x-dcsys-legajo");
						String studentNumber = (String)legajo.get();
						
						Attribute uuid = attrs.get("x-dcsys-uuid");
						String id = (String)uuid.get();
						
						StudentDataBean sd = new StudentDataBean();
						sd.setId(id);
						sd.setStudentNumber(studentNumber);
						
						ret.add(sd);
					}
					
					return ret;
					
					
				} finally {
					data.close();
				}
				
			} finally {
				ctx.close();
			}
			
		} catch (NamingException e) {
			logger.log(Level.SEVERE,e.getMessage(),e);
			throw new PersonException(e);			
		}
		
	}

	@Override
	public String findByStudentNumber(String sn) throws PersonException {
		
		if (sn == null) {
			throw new PersonException("StudentNumber == null");
		}
		
		try {
			SearchControls sc = new SearchControls();
			sc.setReturningAttributes(new String[]{"x-dcsys-uuid"});
			sc.setSearchScope(SearchControls.SUBTREE_SCOPE);
	
			String filter = "(x-dcsys-legajo=" +  sn + ")";

			DirContext ctx = cp.getDirContext();
			try {
			
				NamingEnumeration data = ctx.search(cp.getUsersBase(), filter, sc);
				try {
					
					if (!data.hasMore()) {
						throw new PersonException(sn + " not found");
					}
					
					SearchResult res = (SearchResult)data.next();
					Attributes attrs = res.getAttributes();

					Attribute uuid = attrs.get("x-dcsys-uuid");
					String id = (String)uuid.get();
						
					return id;
					
				} finally {
					data.close();
				}
				
			} finally {
				ctx.close();
			}
			
		} catch (NamingException e) {
			logger.log(Level.SEVERE,e.getMessage(),e);
			throw new PersonException(e);			
		}
	}

}
