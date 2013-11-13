package ar.com.dcsys.data.group.backend;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.naming.NamingEnumeration;
import javax.naming.NamingException;
import javax.naming.directory.Attribute;
import javax.naming.directory.Attributes;
import javax.naming.directory.BasicAttribute;
import javax.naming.directory.BasicAttributes;
import javax.naming.directory.DirContext;
import javax.naming.directory.ModificationItem;
import javax.naming.directory.SearchControls;
import javax.naming.directory.SearchResult;

import ar.com.dcsys.group.entities.Group;
import ar.com.dcsys.group.entities.types.Alias;
import ar.com.dcsys.group.entities.types.GroupType;
import ar.com.dcsys.group.entities.types.Office;
import ar.com.dcsys.group.entities.types.Ou;
import ar.com.dcsys.group.entities.types.Position;
import ar.com.dcsys.group.entities.types.Profile;
import ar.com.dcsys.group.entities.types.TimeTable;
import ar.com.dcsys.persistence.DirContextProvider;
import ar.com.dcsys.person.PersonException;
import ar.com.dcsys.person.entities.Mail;
import ar.com.dcsys.person.entities.Person;


/**
 * @author pablo
 *
 * Grupos en ldap.
 * 
 * tienen los siguientes objectClass:
 * 
 * top
 * x-dcsys-entity
 * sambaGroupMapping
 * 
 * estoy entre 
 * 
 * posixGroup
 * o 
 * groupOfUniqueNames
 * 
 * y los siguientes atributos
 * 
 * memberUid
 * sambaGroupType
 * sambaSID
 * gidnumber
 * cn
 * x-dcsys-uuid
 * 
 *
 *
 */

public class LdapGroupBackEnd implements GroupBackEnd {

	private static final long serialVersionUID = 1L;
	private static final Logger logger = Logger.getLogger(LdapGroupBackEnd.class.getName());
	
	
	private static final String[] groupIdAttrs = {
		"x-dcsys-uuid"
	};
	
	private static final String[] groupAttrs = {"entryUUID", 
		"dn","cn",
		"mail",
		"gidNumber",
		"uidNumber",
		"x-dcsys-uuid",
		"x-dcsys-parent",
		"objectClass"
	};
	
	private static final String[] groupMemberAttrs = {"entryUUID", 
		"memberUid",							// RFC 2307 
		"uniqueMember"							// RFC 2256
	};	
	
	
	
	private final String groupBaseDN = "ou=groups,dc=econo";
	private final String groupFilter = "(objectClass=posixGroup)";	

	private final DirContextProvider cp;
	private final Params params;

	public LdapGroupBackEnd(DirContextProvider cp, Params params) {
		this.cp = cp;
		this.params = params;
	}
	
	@Override
	public void initialize() throws PersonException {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public void destroy() throws PersonException {
		// TODO Auto-generated method stub
		
	}
	
	private String getStringAtt(String name, Attributes attrs) throws NamingException {
		Attribute att = attrs.get(name);
		if (att == null) {
			return null;
		}
		return (String)att.get();
	}

	
	private List<Person> findMembers(List<String> membersUids) throws PersonException {
		List<Person> persons = new ArrayList<Person>();
		for (String m : membersUids) {
			
			/****
			 * 
			 * TODO: AHORA CATCHEO LA EXCEPCIÓN ACA!! PERO DEBERÍA TIRARLA EN REALIDAD.
			 * es por los nombres de usuarios que a veces tienen nombre.apellido y a veces dni.
			 * 
			 */

			try {		
			
			
			
				
				
				List<Person> p = params.findPersonByMemberField(m);
				if (p != null) {
					persons.addAll(p);
				}
			
			
			
			
			} catch (PersonException e) {
				// nada por ahora!! . solo no lo agrego al grupo.
				logger.log(Level.SEVERE,"find members : ( " + m + " ) " + e.getMessage());
			}
			
			
		}
		return persons;
	}
	
	
	
	////// Tipos de Grupos ///////////////
	
	@Override
	public List<GroupType> findAllTypes() {
		return Arrays.asList(new Alias(), new Office(), new Position(), new Ou(), new TimeTable(), new Profile());
	}

	private List<GroupType> getGroupTypes(List<String> types) {
		logger.log(Level.FINE,"getGroupTypes:");
		
		List<GroupType> gt = new ArrayList<>();
		if (types == null || types.size() <= 0) {
			return gt;
		}
		for (String g : types) {
			if (g.equals("x-dcsys-office")) {
				gt.add(new Office());
				continue;
			}
			if (g.equals("x-dcsys-alias")) {
				gt.add(new Alias());
				continue;
			}
			if (g.equals("x-dcsys-position")) {
				gt.add(new Position());
				continue;
			}
			if (g.equals("x-dcsys-ou")) {
				gt.add(new Ou());
			}
			if (g.equals("x-dcsys-timetable")) {
				gt.add(new TimeTable());
			}
			if (g.equals("x-dcsys-profile")) {
				gt.add(new Profile());
			}
		}
		
		logger.log(Level.FINE,gt.toString());
		
		return gt;
	}
	
	private List<String> getObjectClasses(List<GroupType> types) {
		
		logger.log(Level.FINE,"getObjectClasses:");
		
		List<String> gt = new ArrayList<>();
		if (types == null || types.size() <= 0) {
			return gt;
		}
		for (GroupType g : types) {
			if (g instanceof Office) {
				gt.add("x-dcsys-office");
				continue;
			}
			if (g instanceof Alias) {
				gt.add("x-dcsys-alias");
				continue;
			}
			if (g instanceof Position) {
				gt.add("x-dcsys-position");
				continue;
			}
			if (g instanceof Ou) {
				gt.add("x-dcsys-ou");
			}
			if (g instanceof TimeTable) {
				gt.add("x-dcsys-timetable");
			}
			if (g instanceof Profile) {
				gt.add("x-dcsys-profile");
			}
		}
		logger.log(Level.FINE,gt.toString());
		return gt;
	}
	
	/**
	 * Retorna los ids de los grupos que tienen determinado tipo.
	 */
	@Override
	public List<String> findByType(GroupType type) throws PersonException {
		List<String> oc = getObjectClasses(Arrays.asList(type));
		assert oc.size() == 1;
		String filter = "(objectClass=" + oc.get(0) + ")";
		return findIdByFilter(filter);
	}
	
	/////////////////////////////////////////////////////////////
	
	
	/**
	 * Retorna el id del grupo padre del grupo pasado como parámetro.
	 * en caso de no tener padre retorna null.
	 */
	@Override
	public String findParent(Group g) throws PersonException {
		
		String idToFind = null;
		
		String filter = "(x-dcsys-uuid=" + g.getId() + ")";
		try {
			DirContext ctx = cp.getDirContext();
			try {
				
				SearchControls sc = new SearchControls();
				sc.setReturningAttributes(new String[]{"x-dcsys-parent"});
				sc.setSearchScope(SearchControls.SUBTREE_SCOPE);
				
				String effectiveGroupFilter = groupFilter;
				if (filter != null) {
					effectiveGroupFilter = "(&" + groupFilter + filter + ")";
				}

				NamingEnumeration data = ctx.search(groupBaseDN, effectiveGroupFilter, sc);
				try {
					while (data.hasMore()) {
						SearchResult res = (SearchResult)data.next();
						String dn = res.getName();
						Attributes attrs = res.getAttributes();
						idToFind = getStringAtt("x-dcsys-parent",attrs);
					}
				} catch (Exception e) {
					throw new PersonException(e);
				} finally {
					data.close();
				}
				
			} catch (PersonException e) {
				throw e;
			} catch (Exception e) {
				throw new PersonException(e);
			} finally {
				ctx.close();
			}
		
		} catch (Exception e) {
			logger.log(Level.SEVERE,"Error " + e.getMessage());
			throw new PersonException(e);
		}
		
		return idToFind;
	}
				
	
	/**
	 * CArga los miembros del grupo y los setea usando setPersons.
	 */
	@Override
	public void loadMembers(Group g) throws PersonException {
		if (g.getId() == null) {
			throw new PersonException("group.id == null");
		}
		
		try {
			DirContext ctx = cp.getDirContext();
			try {
				
				SearchControls sc = new SearchControls();
				sc.setReturningAttributes(groupMemberAttrs);
				sc.setSearchScope(SearchControls.SUBTREE_SCOPE);
				
				String effectiveGroupFilter = "(&" + groupFilter + "(x-dcsys-uuid=" + g.getId() +"))";
				
				NamingEnumeration data = ctx.search(groupBaseDN, effectiveGroupFilter, sc);
				try {
					if (data.hasMore()) {
						SearchResult res = (SearchResult)data.next();
						String dn = res.getName();
						
						Attributes attrs = res.getAttributes();
						
						Attribute membersA = attrs.get("memberUid");
						if (membersA != null) {
							NamingEnumeration ne = membersA.getAll();
							List<String> members = new ArrayList<String>();
							while (ne.hasMore()) {
								String memberUid = (String)ne.nextElement();
								members.add(memberUid);
							}
							List<Person> persons = findMembers(members);
							g.setPersons(persons);
						}
					}
				
				} catch (PersonException e) {
					logger.log(Level.SEVERE,e.getMessage());
					throw e;
					
				} catch (Exception e) {
					logger.log(Level.SEVERE,e.getMessage());
					throw new PersonException(e);
					
				} finally {
					data.close();
				}
				
			} catch (PersonException e) {
				logger.log(Level.SEVERE,e.getMessage());
				throw e;

			} catch (Exception e) {
				logger.log(Level.SEVERE,e.getMessage());
				throw new PersonException(e);

			} finally {
				ctx.close();
			}
			
		} catch (Exception e) {
			logger.log(Level.SEVERE,e.getMessage());
			throw new PersonException(e);
		}
	}
	
	
	/**
	 * REtorna todos los ids de los grupos que cumplen con el filtro pasado por parámetro.
	 * @param filter
	 * @return
	 * @throws PersonException
	 */
	private List<String> findIdByFilter(String filter) throws PersonException {
		List<String> groups = new ArrayList<>();
		
		try {
			DirContext ctx = cp.getDirContext();
			try {
				
				SearchControls sc = new SearchControls();
				sc.setReturningAttributes(groupIdAttrs);
				sc.setSearchScope(SearchControls.SUBTREE_SCOPE);
				
				String effectiveGroupFilter = groupFilter;
				if (filter != null) {
					effectiveGroupFilter = "(&" + groupFilter + filter + ")";
				}
				
				logger.log(Level.FINE,"findByfilter() - " + effectiveGroupFilter);
				
				NamingEnumeration data = ctx.search(groupBaseDN, effectiveGroupFilter, sc);
				try {
					while (data.hasMore()) {
						SearchResult res = (SearchResult)data.next();
						String dn = res.getName();
						
						logger.log(Level.FINER,"findByfilter() encontrado : " + dn);
						
						Attributes attrs = res.getAttributes();
						
						String uuid = getStringAtt("x-dcsys-uuid", attrs);
						groups.add(uuid);
					}
				
				} catch (Exception e) {
					logger.log(Level.SEVERE,"findByFilter error : " + e.getMessage());
					throw new PersonException(e);
					
				} finally {
					data.close();
				}
				
			} catch (PersonException e) {
				logger.log(Level.SEVERE,"findByFilter error : " + e.getMessage());
				throw e;

			} catch (Exception e) {
				logger.log(Level.SEVERE,"findByFilter error : " + e.getMessage());
				throw new PersonException(e);

			} finally {
				ctx.close();
			}
			
		} catch (Exception e) {
			logger.log(Level.SEVERE,"findByFilter error : " + e.getMessage());
			throw new PersonException(e);
		}
		return groups;		
	}
	
	
	/**
	 * Retorna todos los grupos que cumplen con el filtro pasado por parámetro.
	 * @param filter
	 * @return
	 * @throws PersonException
	 */
	private List<Group> findByFilter(String filter) throws PersonException {
		
		List<Group> groups = new ArrayList<>();
		
		try {
			DirContext ctx = cp.getDirContext();
			try {
				
				SearchControls sc = new SearchControls();
				sc.setReturningAttributes(groupAttrs);
				sc.setSearchScope(SearchControls.SUBTREE_SCOPE);
				
				String effectiveGroupFilter = groupFilter;
				if (filter != null) {
					effectiveGroupFilter = "(&" + groupFilter + filter + ")";
				}
				
				logger.log(Level.FINE,"findByfilter() - " + effectiveGroupFilter);
				
				NamingEnumeration data = ctx.search(groupBaseDN, effectiveGroupFilter, sc);
				try {
					while (data.hasMore()) {
						SearchResult res = (SearchResult)data.next();
						String dn = res.getName();
						
						logger.log(Level.FINER,"findByfilter() encontrado : " + dn);
						
						Attributes attrs = res.getAttributes();
						
						// aca sacaría el grupo de la cache en caso de encontrarse por el x-dcsys-uuid
						Group group = null;
						if (group == null) {
							group = new Group();
						}
						
						String uuid = getStringAtt("x-dcsys-uuid", attrs);
						group.setId(uuid);
						
						String name = getStringAtt("cn", attrs);
						group.setName(name);
						
						/*String parent = getStringAtt("x-dcsys-parent", attrs);
						if (parent != null) {
							Group parentGroup = new Group();
							parentGroup.setId(parent);
							group.setParent(parentGroup);
						}*/
						
						String mail = getStringAtt("mail",attrs);
						if (mail != null && (!"".equals(mail))) {
							Mail m = new Mail();
							m.setMail(mail);
							m.setPrimary(true);
							group.getMails().add(m);
						}
					
						
						Attribute oc = attrs.get("objectClass");
						NamingEnumeration ne = oc.getAll();
						List<String> t = new ArrayList<String>();
						while (ne.hasMore()) {
							t.add((String)ne.next());
						}
						List<GroupType> types = getGroupTypes(t);
						group.setTypes(types);
						
						/*
						Attribute membersA = attrs.get("memberUid");
						if (membersA != null) {
							ne = membersA.getAll();
							List<String> members = new ArrayList<String>();
							while (ne.hasMore()) {
								String memberUid = (String)ne.nextElement();
								members.add(memberUid);
							}
							List<Person> persons = findMembers(members);
							group.setPersons(persons);
						}
						*/
						
						groups.add(group);
					}
				
//				} catch (PersonException e) {
//					logger.log(Level.SEVERE,"findByFilter error : " + e.getMessage());
//					throw e;
					
				} catch (Exception e) {
					logger.log(Level.SEVERE,"findByFilter error : " + e.getMessage());
					throw new PersonException(e);
					
				} finally {
					data.close();
				}
				
			} catch (PersonException e) {
				logger.log(Level.SEVERE,"findByFilter error : " + e.getMessage());
				throw e;

			} catch (Exception e) {
				logger.log(Level.SEVERE,"findByFilter error : " + e.getMessage());
				throw new PersonException(e);

			} finally {
				ctx.close();
			}
			
		} catch (Exception e) {
			logger.log(Level.SEVERE,"findByFilter error : " + e.getMessage());
			throw new PersonException(e);
		}
		return groups;
	}
	

	/**
	 * REtorna todos los grupos de la base
	 */
	@Override
	public List<Group> findAll() throws PersonException {
		return findByFilter(null);
	}
	
	/**
	 * REtorna todos los ids de los grupos que existen en la base.
	 */
	@Override
	public List<String> findAllIds() throws PersonException {
		return findIdByFilter(null);
	}
	
	/**
	 * REtorna los ids de los padres de los grupos pasados como parámetro.
	 */
	@Override
	public List<String> findAllParents(List<Group> groups) throws PersonException {
		List<String> parents = new ArrayList<String>();
		for (Group g : groups) {
			String parent = findParent(g);
			if (parent != null && (!parents.contains(parent))) {
				parents.add(parent);
			}
		}
		return parents;
	}
	
	
	/**
	 * REtorna todos los ids de los grupos hijos a los grupos pasados como parametro.
	 */
	@Override
	public List<String> findAllSons(List<Group> groups) throws PersonException {
		if (groups == null) {
			throw new PersonException("No se puede buscar los hijos de un grupo = null");
		}

		if (groups.size() == 0) {
			return new ArrayList<String>();
		}
		
		try {
		
			if (groups.size() == 1) {
				String filter = "(x-dcsys-parent=" + groups.get(0).getId() + ")";
				return findIdByFilter(filter);
			}
			
			String filter = "(|";
			for (Group g : groups) {
				filter = filter + "(x-dcsys-parent=" + g.getId() + ")";
			}
			filter = filter + ")";
			List<String> sons = findIdByFilter(filter);
			return sons;
		
		} catch (PersonException e) {
			throw e;
			
		} catch (Exception e) {
			throw new PersonException(e);
		}
	}
	
	@Override
	public Group findById(String id) throws PersonException {
		String filter = "(x-dcsys-uuid="+ id + ")";
		List<Group> groups = findByFilter(filter);
		if (groups != null && groups.size() >= 1) {
			return groups.get(0);
		}
		return null;
	}

	
	@Override
	public List<String> findByPerson(Person p) throws PersonException {
		String member = params.getMemberFieldContent(p);
		if (member == null) {
			throw new PersonException("Información de autentificación insuficiente. No se puede determinar el nombre de usuario para buscar el grupo");
		}
		String filter = "(memberUid=" + member + ")";
		return findIdByFilter(filter);
	}

	
	private String findDnByUUid(String uuid) {
		if (uuid == null) {
			return null;
		}
		try {
			SearchControls sc = new SearchControls();
			sc.setReturningAttributes(groupAttrs);
			sc.setSearchScope(SearchControls.SUBTREE_SCOPE);
	
			String finalUserFilter = "(&" + groupFilter + "(x-dcsys-uuid=" + uuid + "))";
			
			logger.log(Level.INFO,"Buscando usuario en : " + groupBaseDN + " con el filtro : " + finalUserFilter);
			
			DirContext ctx = cp.getDirContext();
			try {
				NamingEnumeration data = ctx.search(groupBaseDN, finalUserFilter, sc);
				if (!data.hasMore()) {
					logger.log(Level.INFO,"No se encontró ningun grupo");
					return null;
				}
				
				try {
					SearchResult res = (SearchResult)data.next();
					String dn = res.getNameInNamespace();
					if (dn != null) {
						logger.log(Level.FINE,"Se encontro el grupo : " + dn);
						return dn;
					}
					
				} finally {
					data.close();
				}
				
			} finally {
				ctx.close();
			}
		} catch (Exception e) {
			logger.log(Level.SEVERE,e.getMessage());
		}
		return null;
	}	
	
	/**
	 * Obtiene el máximo gid de todos los grupos definidos.
	 * @return
	 * @throws PersonException
	 */
	private long getLastGidNumber() throws PersonException {
		Long maxGid = 0l;

		try {
			DirContext ctx = cp.getDirContext();
			try {
				
				SearchControls sc = new SearchControls();
				sc.setReturningAttributes(new String[]{"gidNumber"});
				sc.setSearchScope(SearchControls.SUBTREE_SCOPE);
				
				logger.log(Level.INFO,"buscando grupos en : " + groupBaseDN + " usando el filtro " + groupFilter);
				
				NamingEnumeration data = ctx.search(groupBaseDN, groupFilter, sc);
				try {
					while (data.hasMore()) {
						SearchResult res = (SearchResult)data.next();
						Attributes attrs = res.getAttributes();
						String gidNumber = getStringAtt("gidNumber", attrs);
						Long lgidNumber = Long.parseLong(gidNumber);
						if (maxGid < lgidNumber) {
							maxGid = lgidNumber;
						}
					}
				} finally {
					data.close();
				}
				
			} finally {
				ctx.close();
			}
			
		} catch (Exception e) {
			logger.log(Level.SEVERE,"Error " + e.getMessage());
			throw new PersonException(e);
		}
		
		return maxGid;
	}

	
	
	/**
	 * Persiste los miembros del grupo.
	 * usado por las funciones de agregar una persona y de eliminar una persona.
	 * @param ctx
	 * @param g
	 * @throws PersonException
	 */
	private void persistMembers(DirContext ctx, Group g) throws PersonException {
		
		String id = g.getId();
		String dn = findDnByUUid(id);
		
		if (dn == null) {
			throw new PersonException("No se encuentra el dn del grupo : " + id);
		}
		
		try {
			// actualizo el grupo
			List<ModificationItem> modsL = new ArrayList<ModificationItem>();
			
			Attribute memberUid = new BasicAttribute("memberUid");
			
			List<Person> persons = g.getPersons();
			if (persons != null && persons.size() > 0) {
				for (Person p : persons) {
					String member = params.getMemberFieldContent(p);
					if (member == null) {
						if (p.getDni() != null && g.getName() != null) {
							throw new PersonException("No se encuentra el atributo para agregar a la persona : " + p.getDni() + " al grupo : " + g.getName());								
						} else {
							throw new PersonException("No se encuentra el atributo para agregar una persona al grupo");
						}
					}
					memberUid.add(member);
				}
			}
			
			modsL.add(new ModificationItem(DirContext.REPLACE_ATTRIBUTE,memberUid));
			ModificationItem[] mods = new ModificationItem[modsL.size()];
			for (int i = 0; i < modsL.size(); i++) {
				mods[i] = modsL.get(i);
			}
			
			ctx.modifyAttributes(dn,mods);
		} catch (Exception e) {
			logger.log(Level.SEVERE,"error " + e.getMessage());
			throw new PersonException(e);
		}

	}
	
	
	
	/**
	 * Agrego una persona al grupo indicado como parametro.
	 */
	@Override
	public void addPersonToGroup(Group g, Person p) throws PersonException {

		if (g == null) {
			throw new PersonException("group == null");
		}
		
		if (g.getId() == null) {
			throw new PersonException("group.id == null");
		}
		
		loadMembers(g);

		String personId = p.getId();
		List<Person> persons = g.getPersons();
		for (Person p2 : persons) {
			if (personId.equals(p2.getId())) {
				// ya existe la persona en el grupo.
				return;
			}
		}
		g.getPersons().add(p);

		
		try {
			DirContext ctx = cp.getDirContext();
			try {

				persistMembers(ctx, g);
			
			} catch (Exception e) {
				logger.log(Level.SEVERE,"error " + e.getMessage());
				throw new PersonException(e);
			} finally {
				ctx.close();
			}
			
		} catch (NamingException e) {
			throw new PersonException(e);
		}	
	}
	

	/**
	 * Si existe elimino la persona del grupo.
	 */
	@Override
	public void removePersonFromGroup(Group g, Person p) throws PersonException {
		
		if (g == null) {
			throw new PersonException("group == null");
		}
		
		if (g.getId() == null) {
			throw new PersonException("group.id == null");
		}
		
		loadMembers(g);

		boolean removed = false;
		String personId = p.getId();
		Iterator<Person> it = g.getPersons().iterator();
		while (it.hasNext()) {
			Person p2 = it.next();
			if (personId.equals(p2.getId())) {
				removed = true;
				it.remove();
			}
		}

		if (!removed) {
			throw new PersonException("No existe esa persona dentro del grupo");
		}
		
		try {
			DirContext ctx = cp.getDirContext();
			try {

				persistMembers(ctx, g);
			
			} catch (Exception e) {
				logger.log(Level.SEVERE,"error " + e.getMessage());
				throw new PersonException(e);
			} finally {
				ctx.close();
			}
			
		} catch (NamingException e) {
			throw new PersonException(e);
		}			
	}
	
	
	
	
	/**
	 * Persiste los datos del grupo pasado como parámetro.
	 * NO PERSISTE LOS MIEMBROS!! para eso se usan 2 funciones adicionales.
	 */
	@Override
	public String persist(Group g) throws PersonException {
		String id = g.getId();
		String dn = null;
		if (id != null) {
			dn = findDnByUUid(id);
		}
		
		long maxGid = getLastGidNumber() + 1l;
		
		try {
			DirContext ctx = cp.getDirContext();
			try {
				
				Attribute objectClass = new BasicAttribute("objectClass");
				objectClass.add("top");
				objectClass.add("posixGroup");
				objectClass.add("x-dcsys-group");
				//objectClass.add("groupOfUniqueNames");
				objectClass.add("x-dcsys-entidad");
				
				
				if (dn == null) {
					// hay que agregarlo
					
					Attributes attrs = new BasicAttributes();
					
					List<GroupType> types = g.getTypes();
					if (types != null && types.size() > 0) {
						List<String> oc = getObjectClasses(types);
						for (String o : oc) {
							objectClass.add(o);
						}
					}
					attrs.put(objectClass);

					String sxuuid = UUID.randomUUID().toString();
					Attribute xuuid = new BasicAttribute("x-dcsys-uuid",sxuuid);
					attrs.put(xuuid);
					g.setId(sxuuid);
					
					Attribute cn = new BasicAttribute("cn",g.getName());
					attrs.put(cn);
					
					// descubierto automáticamnete. se incrementa en 1 en cada inserción de un nuevo grupo.
					// después no se toca.
					Attribute gidNumber = new BasicAttribute("gidNumber",String.valueOf(maxGid));
					attrs.put(gidNumber);
					
					List<Mail> mails = g.getMails();
					if (mails != null && mails.size() > 0) {
						Attribute mail = new BasicAttribute("mail");
						mail.add(mails.get(0).getMail());
						attrs.put(mail);
					}
					
					/**
					 * Esto ahora se hace en un metodo separado.
					 */
					/*
					// agrego a todas las peronas miembros del grupo.
					List<Person> persons = g.getPersons();
					if (persons != null && persons.size() > 0) {
						Attribute memberUid = new BasicAttribute("memberUid");
						boolean added = false;
						for (Person p : persons) {
							String member = params.getMemberFieldContent(p);
							if (member == null) {
								continue;
							}
							memberUid.add(member);
							added = true;
						}
						if (added) {
							attrs.put(memberUid);
						}
					}
					*/
					
					/**
					 * Esto ahora se hace en un método separado. 
					 * es modificar la estructura del grupo.
					 */
					/*
					// si tiene padre lo seteo
					Group parent = g.getParent();
					if (parent != null && parent.getId() != null) {
						String xuuidparent = parent.getId();
						Attribute xuuiparentA = new BasicAttribute("x-dcsys-parent",xuuidparent);
						attrs.put(xuuiparentA);
					}
					*/
					
					dn = "x-dcsys-uuid=" + sxuuid + "," + groupBaseDN;
					ctx.createSubcontext(dn,attrs);
				} else {
					
					// actualizo el grupo
					List<ModificationItem> modsL = new ArrayList<ModificationItem>();
					
					Attribute cn = new BasicAttribute("cn",g.getName());
					modsL.add(new ModificationItem(DirContext.REPLACE_ATTRIBUTE,cn));

					Attribute mail = new BasicAttribute("mail");
					List<Mail> mails = g.getMails();
					if (mails != null && mails.size() > 0) {
						mail.add(mails.get(0).getMail());
						modsL.add(new ModificationItem(DirContext.REPLACE_ATTRIBUTE,mail));
					} else {
						modsL.add(new ModificationItem(DirContext.REPLACE_ATTRIBUTE,mail));
					}

					/**
					 * Esto ahora se hace en un metood separado
					 */
					/*
					Attribute memberUid = new BasicAttribute("memberUid");
					List<Person> persons = g.getPersons();
					if (persons != null && persons.size() > 0) {
						boolean added = false;
						for (Person p : persons) {
							String member = params.getMemberFieldContent(p);
							if (member == null) {
								continue;
							}
							memberUid.add(member);
							added = true;
						}
						if (added) {
							modsL.add(new ModificationItem(DirContext.REPLACE_ATTRIBUTE,memberUid));
						} else {
							modsL.add(new ModificationItem(DirContext.REPLACE_ATTRIBUTE,memberUid));
						}
					} else {
						modsL.add(new ModificationItem(DirContext.REPLACE_ATTRIBUTE,memberUid));
					}
					*/
					
					List<GroupType> types = g.getTypes();
					if (types != null && types.size() > 0) {
						List<String> oc = getObjectClasses(types);
						for (String o : oc) {
							objectClass.add(o);
						}
					}
					modsL.add(new ModificationItem(DirContext.REPLACE_ATTRIBUTE,objectClass));

					/**
					 * Esto ahora se hace en un método separado. 
					 * es modificar la estructura del grupo.
					 */
					/*					
					// si tiene padre lo seteo
					Attribute xuuiparentA = new BasicAttribute("x-dcsys-parent");
					Group parent = g.getParent();
					if (parent != null && parent.getId() != null) {
						String xuuidparent = parent.getId();
						xuuiparentA.add(xuuidparent);
						modsL.add(new ModificationItem(DirContext.REPLACE_ATTRIBUTE,xuuiparentA));
					} else {
						modsL.add(new ModificationItem(DirContext.REPLACE_ATTRIBUTE,xuuiparentA));
					}
					*/
					
					ModificationItem[] mods = new ModificationItem[modsL.size()];
					for (int i = 0; i < modsL.size(); i++) {
						mods[i] = modsL.get(i);
					}
					
					ctx.modifyAttributes(dn,mods);
				}
				return g.getId();
				
			} catch (Exception e) {
				logger.log(Level.SEVERE,"error " + e.getMessage());
				throw new PersonException(e);
				
			} finally {
				ctx.close();
			}
		} catch (PersonException e) {
			throw e;
		} catch (Exception e) {
			throw new PersonException("No se pudo conectar al ldap " + e.getMessage());
		}
	}
	
	@Override
	public void setParent(Group son, Group parent) throws PersonException {
		
		
		if (son.getId() == null) {
			throw new PersonException("Id del grupo hijo no puede ser nulo");
		}
		
		if (parent != null && parent.getId() == null) {
			throw new PersonException("El id del grupo padre no puede ser nulo");
		}
		
		String id = son.getId();
		String dn = findDnByUUid(id);

		try {
			DirContext ctx = cp.getDirContext();
			try {
				// actualizo el grupo
				List<ModificationItem> modsL = new ArrayList<ModificationItem>();
				
				Attribute xuuiparentA = new BasicAttribute("x-dcsys-parent");
				if (parent != null) {
					String xuuidparent = parent.getId();
					xuuiparentA.add(xuuidparent);
				}
				modsL.add(new ModificationItem(DirContext.REPLACE_ATTRIBUTE,xuuiparentA));
				
				ModificationItem[] mods = new ModificationItem[modsL.size()];
				for (int i = 0; i < modsL.size(); i++) {
					mods[i] = modsL.get(i);
				}
				
				ctx.modifyAttributes(dn,mods);
			} catch (Exception e) {
				logger.log(Level.SEVERE,"error " + e.getMessage());
				throw new PersonException(e);
			} finally {
				ctx.close();
			}
			
		} catch (NamingException e) {
			throw new PersonException(e);
		}
		
	}
	

	/**
	 * Elimina un grupo del ldap.
	 */
	@Override
	public void remove(Group g) throws PersonException {
		if (g == null) {
			throw new PersonException("grupo == null");
		}
		
		if (g.getId() == null) {
			throw new PersonException("grupo.id == null");
		}
		
		List<String> sons = findAllSons(Arrays.asList(g));
		if (sons != null && sons.size() > 0) {
			throw new PersonException("No se puede eliminar un grupo con hijos");
		}
		
		
		String dn = findDnByUUid(g.getId());
		if (dn == null || dn.trim().equals("")) {
			throw new PersonException("No se puede encontrar el dn para el grupo con uuid " +  g.getId());
		}
		
		try {
			DirContext ctx = cp.getDirContext();
			try {
				ctx.destroySubcontext(dn);
			} catch (Exception e) {
					logger.log(Level.SEVERE,"error " + e.getMessage());
					throw new PersonException(e);
			} finally {
				ctx.close();
			}
		} catch (NamingException e) {
			throw new PersonException(e);
		}		
	}

}
