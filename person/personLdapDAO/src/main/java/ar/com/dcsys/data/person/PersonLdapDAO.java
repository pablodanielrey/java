/**
 *  Person :
 *    must:
 *  	cn, sn
 *  
 *    optional:
 *  	userPassword, telephoneNumber, seeAlso, description
 *  
 *  organizationalPerson <= Person :
 *    must :
 *  	-
 *    optional :
 *  	title, x121Address, registeredAddress, destinationIndicator, preferredDeliveryMethod, telexNumber, 
 *  	teletexTerminalIdentifier, telephoneNumber, internationaliSDNNumber, facsimileTelephoneNumber, 
 *  	street, postOfficeBox, postalCode, postalAddress, physicalDeliveryOfficeName, ou, st, l
 * 
 *	residentialPerson <= Person: 
 *		must :
 *			l
 *		optional:
 *			businessCategory, x121Address, registeredAddress, destinationIndicator, preferredDelveriMethod
 *		
 * 
 * 
 */
package ar.com.dcsys.data.person;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.inject.Inject;
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

import ar.com.dcsys.data.OpenLdapContextProvider;
import ar.com.dcsys.exceptions.PersonException;
import ar.com.dcsys.persistence.DirContextProvider;

public class PersonLdapDAO extends AbstractLdapPersonDAO {

	private static final String[] userAttrs = {"entryUUID", 
												"dn","uid",
												"userPassword", 
												"mail", 
												"sn", 
												"givenName", 
												"telephoneNumber",
												"localityName", "l",
												"friendlyCountryName", "co",
												"registeredAddress",
												"x-dcsys-dni",
												"x-dcsys-gender",
												"x-dcsys-uuid",
												"x-dcsys-mail",
												"objectClass",
												"modifyTimestamp"};
	
	private static final Logger logger = Logger.getLogger(PersonLdapDAO.class.getName());
	
	private final String userBaseDN = "ou=people,dc=econo";
	private final String userFilter = "(&(objectClass=inetOrgPerson)(objectClass=person))";
	
	private final DirContextProvider cp;

	@Inject
	public PersonLdapDAO(OpenLdapContextProvider cp) {
		this.cp = cp;
	}
	
	private String getStringAtt(String name, Attributes attrs) throws NamingException {
		Attribute att = attrs.get(name);
		if (att == null) {
			return null;
		}
		return (String)att.get();
	}
	
	
	private List<Person> findByFilter() throws PersonException {
		return findByFilter(null);
	}
	
	private List<Person> findByFilter(String filter) throws PersonException {
		
		List<Person> users = new ArrayList<>();
		
		try {
			DirContext ctx = cp.getDirContext();
			try {
				
				SearchControls sc = new SearchControls();
				sc.setReturningAttributes(userAttrs);
				sc.setSearchScope(SearchControls.SUBTREE_SCOPE);
				
				String effectiveUserFilter = userFilter;
				if (filter != null) {
					effectiveUserFilter = "(&" + userFilter + filter + ")";
				}
				
				logger.log(Level.FINE,"buscando usuarios en : " + userBaseDN + " usando el filtro " + effectiveUserFilter);
				
				NamingEnumeration data = ctx.search(userBaseDN, effectiveUserFilter, sc);
				try {
					while (data.hasMore()) {
						SearchResult res = (SearchResult)data.next();
						String dn = res.getName();
						Attributes attrs = res.getAttributes();
					
						logger.log(Level.FINE,"Se encontro : " + dn);
						
						String uid = getStringAtt("uid",attrs);
						
						// atributos de Person
						
						String uuid = getStringAtt("x-dcsys-uuid", attrs);
						String name = getStringAtt("givenName", attrs);
						String lastName = getStringAtt("sn",attrs);
						
						String dni = getStringAtt("x-dcsys-dni",attrs);
						String address = getStringAtt("registeredAddress",attrs);
						String gender = getStringAtt("x-dcsys-gender",attrs);
						String locality = getStringAtt("l",attrs);
						String country = getStringAtt("co",attrs);
						
						String version = getStringAtt("modifyTimestamp",attrs);
						
						
						/*
						String userPassword = null;
						try {
							byte[] userPasswordB = (byte[])(attrs.get("userPassword").get());
							if (userPasswordB != null) {
								userPassword = new String(userPasswordB);
							}
						} catch (Exception e) {
							logger.log(Level.WARNING,"Error obteniendo la clave del usuario (" + e.getMessage() + ")");
						}
						*/
						
						// obtengo los tipos de la persona.
						// TODO: solución a lo programador estructurado!!! hay que ver cual le damos en objetos.
						Attribute objectClass = attrs.get("objectClass");
						List<String> sTypes = new ArrayList<String>();
						NamingEnumeration ne = objectClass.getAll();
						while (ne.hasMore()) {
							sTypes.add((String)ne.next());
						}
						List<PersonType> types = fromObjectClass(sTypes);

						
						//Person person = getCachedObject(Person.class,uuid);
						Person person = null;
						if (person == null) {
							person = new PersonBean();
							person.setId(uuid);
							person.setName(name);
							person.setLastName(lastName);
							person.setDni(dni);
							person.setAddress(address);
							
							
							///// PARCHE PARA SACAR DESPUES YA QUE CAMBIO EL SISTEMA Y LOS DATOS GUARDADOS /////////////
							// TODO: corregir cuando se haga la migracion de la base.
							
							if (gender != null) {
								try {
									person.setGender(Gender.valueOf(gender));
								} catch (Exception e) {
									person.setGender(Gender.M);
								}
							}
							
							///////////////////////////////7
							
							
							person.setCity(locality);
							person.setCountry(country);
							person.setTypes(types);
							
							Attribute tels = attrs.get("telephoneNumber");
							if (tels != null) {
								NamingEnumeration ntels = tels.getAll();
								while (ntels.hasMore()) {
									String sTel = (String)ntels.next();
									if (!(sTel.trim().equals(""))) {
										Telephone tel = new TelephoneBean();
										tel.setNumber(sTel);
										person.getTelephones().add(tel);
									}
								}
							}
						}
						
						users.add(person);
					}
					
				} finally {
					data.close();
				}
				
			} finally {
				ctx.close();
			}
			
		} catch (Exception e) {
			String st = "";
			for (StackTraceElement ste : e.getStackTrace()) {
				st = st + ste.getFileName() + " - " + String.valueOf(ste.getLineNumber()) + " - " + ste.getClassName() + " - " + ste.getMethodName() + "\n";
			}
			logger.log(Level.SEVERE,"Error obteniendo los usuarios usando el filtro " + e.getMessage() + " " + st);
			
		}
		
		return users;
	}
	
	
	
	/**
	 * Retorna todos los ids de las personas que concuerdan con el filtro de busqueda.
	 * @param filter
	 * @return
	 * @throws PersonException
	 */
	private List<String> findIdsByFilter(String filter) throws PersonException {
		
		try {
			DirContext ctx = cp.getDirContext();
			try {

				List<String> ids = new ArrayList<String>();
				
				SearchControls sc = new SearchControls();
				sc.setReturningAttributes(new String[]{"x-dcsys-uuid"});
				sc.setSearchScope(SearchControls.SUBTREE_SCOPE);
				
				String effectiveUserFilter = userFilter;
				if (filter != null) {
					effectiveUserFilter = "(&" + userFilter + filter + ")";
				}
				
				NamingEnumeration data = ctx.search(userBaseDN, effectiveUserFilter, sc);
				try {
					while (data.hasMore()) {
						SearchResult res = (SearchResult)data.next();
						String dn = res.getName();
						Attributes attrs = res.getAttributes();
						String uuid = getStringAtt("x-dcsys-uuid", attrs);
						ids.add(uuid);
					}
					
				} finally {
					data.close();
				}
				
				return ids;
				
			} finally {
				ctx.close();
			}
				
		} catch (Exception e) {
			throw new PersonException(e);
		}
		
	}
	
	
	
	@Override
	public List<Person> findAll() throws PersonException {
		return findByFilter();
	}
	
	/**
	 * Retorna todas las peronsas indicadas por los ids pasados en el parámetro.
	 * en caso de no poder cargar todas retorna solo las que puede encontrar.
	 */
	@Override
	public List<Person> findAll(Iterable<? extends String> ids) throws PersonException {
		
		List<Person> persons = new ArrayList<Person>();
		Iterator<? extends String> it = ids.iterator();
		
		while (it.hasNext()) {
			// busco en tandas de N personas para que no se haga una cadena muy larga en el ldap.
			StringBuilder filter = new StringBuilder();
			filter.append("(|");
			for (int i = 0; (it.hasNext() && i < 20); i++) {
				String id = it.next();
				filter.append("(x-dcsys-uuid=").append(id).append(")");
			}
			filter.append(")");
			String f = filter.toString();
			persons.addAll(findByFilter(f));
		}
		return persons;
	}
	

	private String getFilterObjectClass(List<String> classes, String condition) {
		String filter = "";
		if (classes.size() == 1) {
			filter = "(objectClass=" + classes.get(0) + ")";
		} else {
			filter = "(" + condition;
			for (String t : classes) {
				filter = filter + "(objectClass=" + t + ")";
			}
			filter = filter + ")";
		}
		return filter;
	}
	
	/**
	 * REtorna todos los ids de personas que tengan alguno de los tipos pasados como parámetro.
	 * En el caso de que types.size == 0 retorna todas las peronsas que no tengan ningun tipo.
	 */
	@Override
	public List<String> findAllIdsBy(List<PersonType> types) throws PersonException {
		if (types == null) {
			throw new PersonException("no se puede buscar por tipos = null");
		}

		if (types.size() <= 0) {
			// si es vacío entonces busco por todas las personas que no tengan ningun tipo.
			types = findAllTypes();
			List<String> pts = toObjectClass(types);
			String filter = getFilterObjectClass(pts,"|");
			return findIdsByFilter("(!(" + filter + "))");
		} else {
			List<String> pts = toObjectClass(types);
			String filter = getFilterObjectClass(pts,"|");
			return findIdsByFilter(filter);
		}
	}
	
	/*
	@Override
	public Person findByStudentNumber(String number) throws PersonException {
		String filter = "(x-dcsys-legajo=" + number + ")";
		List<Person> persons = findByFilter(filter);
		if (persons == null || persons.size() <= 0) {
			return null;
		}
		return persons.get(0);
	}
	*/
	
	@Override
	public Person findById(String id) throws PersonException {
		String filter = "(x-dcsys-uuid="+id+")";
		List<Person> persons = findByFilter(filter);
		if (persons != null && persons.size() == 1) {
			return persons.get(0);
		}
		return null;
	}
	
	
	@Override
	public Person findByDni(String dni) throws PersonException {
		String filter = "(x-dcsys-dni=" + dni + ")";
		List<Person> persons = findByFilter(filter);
		if (persons != null && persons.size() >= 1) {
			return persons.get(0);
		}
		return null;
	}
	
	/*
	@Override
	public Person findByUid(String uid) throws PersonException {
		String filter = "(uid=" + uid + ")";
		List<Person> persons = findByFilter(filter);
		if (persons != null && persons.size() >= 1) {
			return persons.get(0);
		}
		return null;
	}
	*/
	
	/**
	 * Obtiene todos los ids de las personas.
	 */
	@Override
	public List<String> findAllIds() throws PersonException {
		try {
			SearchControls sc = new SearchControls();
			sc.setReturningAttributes(new String[]{"x-dcsys-uuid"});
			sc.setSearchScope(SearchControls.SUBTREE_SCOPE);

			DirContext ctx = cp.getDirContext();
			
			try {
				NamingEnumeration data = ctx.search(userBaseDN, userFilter, sc);
				try {
					
					List<String> ids = new ArrayList<String>();

					while (data.hasMore()) {
						SearchResult res = (SearchResult)data.next();
						String dn = res.getNameInNamespace();
						Attributes attrs = res.getAttributes();
						String uuid = getStringAtt("x-dcsys-uuid", attrs);
						ids.add(uuid);
					}
					
					return ids;

				} finally {
					data.close();
				}
			} finally {
				ctx.close();
			}
		} catch (Exception e) {
			logger.log(Level.SEVERE,e.getMessage());
			throw new PersonException(e);
		}
	}
	
	/**
	 * Retorna el uuid que tiene determinada persona con el dni pasado como parámetro.
	 */
	@Override
	public String findIdByDni(String dni) throws PersonException {
		if (dni == null) {
			throw new PersonException("dni == null");
		}
		
		try {
			SearchControls sc = new SearchControls();
			sc.setReturningAttributes(new String[]{"x-dcsys-uuid"});
			sc.setSearchScope(SearchControls.SUBTREE_SCOPE);
	
			String finalUserFilter = "(&" + userFilter + "(x-dcsys-dni=" + dni + "))";
			
			logger.log(Level.FINE,"Buscando usuario en : " + userBaseDN + " con el filtro : " + finalUserFilter);
			
			DirContext ctx = cp.getDirContext();
			try {
				NamingEnumeration data = ctx.search(userBaseDN, finalUserFilter, sc);
				try {
					
					if (!data.hasMore()) {
						logger.log(Level.FINE,"No se encontró ningun usuario");
						return null;
					}
				
					SearchResult res = (SearchResult)data.next();
					Attributes attrs = res.getAttributes();
					String uuid = getStringAtt("x-dcsys-uuid", attrs);
					return uuid;
					
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
	
	
	private String findDnByUUid(String uuid) throws PersonException {
		if (uuid == null) {
			throw new PersonException("uuid == null");
		}
		
		try {
			SearchControls sc = new SearchControls();
			sc.setReturningAttributes(userAttrs);
			sc.setSearchScope(SearchControls.SUBTREE_SCOPE);
	
			String finalUserFilter = "(&" + userFilter + "(x-dcsys-uuid=" + uuid + "))";
			
			logger.log(Level.FINE,"Buscando usuario en : " + userBaseDN + " con el filtro : " + finalUserFilter);
			
			DirContext ctx = cp.getDirContext();
			try {
				NamingEnumeration data = ctx.search(userBaseDN, finalUserFilter, sc);
				try {
					if (!data.hasMore()) {
						logger.log(Level.FINE,"No se encontró ningun usuario");
						return null;
					}
					
					SearchResult res = (SearchResult)data.next();
					String dn = res.getNameInNamespace();
					if (dn != null) {
						logger.log(Level.FINE,"Se encontro el usuario : " + dn);
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
	
	@Override
	public void remove(Person p) throws PersonException {
		// TODO Auto-generated method stub
		
	}
	
	/////////////////////////////////////////////////////////////////////////////////////////////////
	
	
	/**
	 * Agrega los objectClasses definidos por los tipos de personas.
	 * @param objectClass
	 * @param person
	 */
	private void addObjectClasses(Attribute objectClass, Person person) throws PersonException {
		
		List<PersonType> types = person.getTypes();
		if (types == null || types.size() <= 0) {
			// si no tiene tipo no agrega ningun object class adicional.
			return;
		}
		
		List<String> pts = toObjectClass(person.getTypes());
		for (String t : pts) {
			objectClass.add(t);
		}
	}
	
	/**
	 * suma todos los dígitos del dni y al resultado lo multiplica por el dni
	 * ej : 
	 * 2 + 7 + 2 + 9 + 4 + 5 + 5 + 7 = 41 
	 * 27294557 * 41 = bla bla ba bla  = clave
	 * @param dni
	 * @return
	 */
	private String getSuperGeneratedPasswordEmanuel(String dni) throws PersonException {

		String ddni = dni.replaceAll("[^\\d.]","").trim();
		if (ddni.length() <= 0) {
			// si no tengo dígitos en el dni entonces genero algo aleatorio.
			Random r = new Random(11);
			ddni = String.valueOf(7l * r.nextLong() * r.nextLong());
		}
		
		long ldni = Long.parseLong(ddni);
		long d = 10;
		long remainder = 0;
		long number = ldni;
		long total = 0;
		while (number > 0) {
			remainder = number % d;
			number = number / d;
			
			total = total + remainder;
		}
		return String.valueOf(total * ldni);
	}
	
	
	private boolean checkType(Person p, PersonType pt) {
		List<PersonType> pts = p.getTypes();
		if (pts == null) {
			return false;
		}
		for (PersonType t : pts) {
			if (t.equals(pt)) {
				return true;
			}
		}
		return false;
	}
	
	
	/**
	 * Actualiza o crea una nueva persona.
	 * si retorna exitosamente el método, la persona pasada como parámetro tiene cargado el id.
	 */
	@Override
	public String persist(Person p) throws PersonException {

		String dni = p.getDni();
		if (dni == null) {
			throw new PersonException("Person.dni == null");
		}
		
		String uuid = p.getId();
		String dn = null;
		if (uuid != null) {
			dn = findDnByUUid(uuid); 
		}
		
		try {
			DirContext ctx = cp.getDirContext();
			try {
			
				if (dn == null) {
					// hay que agregarlo
					Attributes attrs = new BasicAttributes();

					Attribute objectClass = new BasicAttribute("objectClass");
					objectClass.add("top");
					objectClass.add("person");
					objectClass.add("inetOrgPerson");
					objectClass.add("x-dcsys-entidad");
					objectClass.add("x-dcsys-persona");

					addObjectClasses(objectClass, p);
					attrs.put(objectClass);

					// por defecto el uid del usuario es el dni.
					Attribute uid = new BasicAttribute("uid",p.getDni());
					attrs.put(uid);
					
					Attribute cn = new BasicAttribute("cn");
					cn.add(p.getName() + " " + p.getLastName());
					attrs.put(cn);
					
					Attribute sn = new BasicAttribute("sn");
					sn.add(p.getLastName());
					attrs.put(sn);
					
					Attribute givenName = new BasicAttribute("givenName");
					givenName.add(p.getName());
					attrs.put(givenName);
					
					if (p.getAddress() != null && (!p.getAddress().trim().equals(""))) {
						Attribute address = new BasicAttribute("registeredAddress",p.getAddress());
						attrs.put(address);
					}
					
					if (p.getCity() != null && (!p.getCity().trim().equals(""))) {
						Attribute locality = new BasicAttribute("l",p.getCity());
						attrs.put(locality);
					}
					
					if (p.getCountry() != null && (!p.getCountry().trim().equals(""))) {
						Attribute country = new BasicAttribute("co",p.getCountry());
						attrs.put(country);
					}
					
					if (p.getGender() != null) {
						Attribute gender = new BasicAttribute("x-dcsys-gender",p.getGender().toString());
						attrs.put(gender);
					}
					
					Attribute xDcSysDni = new BasicAttribute("x-dcsys-dni",p.getDni());
					attrs.put(xDcSysDni);
					
					Attribute xuuid = new BasicAttribute("x-dcsys-uuid");
					String sxuuid = UUID.randomUUID().toString();
					xuuid.add(sxuuid);
					attrs.put(xuuid);
					p.setId(sxuuid);

					List<Telephone> tels = p.getTelephones();
					if (tels != null && tels.size() > 0) {
						Attribute tel = new BasicAttribute("telephoneNumber");
						tel.add(tels.get(0).getNumber());
						attrs.put(tel);
					}
					
					dn = "x-dcsys-uuid=" + sxuuid + "," + userBaseDN;
					p.setId(sxuuid);
					
					ctx.createSubcontext(dn, attrs);
					
				} else {
					/**
					 * Actualiza los datos del usuario
					 * 
					 */
					List<ModificationItem> lMods = new ArrayList<ModificationItem>();
					
					Attribute cn = new BasicAttribute("cn",p.getName() + " " + p.getLastName());
					lMods.add(new ModificationItem(DirContext.REPLACE_ATTRIBUTE,cn));
					
					Attribute sn = new BasicAttribute("sn", p.getLastName());
					lMods.add(new ModificationItem(DirContext.REPLACE_ATTRIBUTE,sn));
							
					Attribute givenName = new BasicAttribute("givenName", p.getName());
					lMods.add(new ModificationItem(DirContext.REPLACE_ATTRIBUTE,givenName));

					if (p.getAddress() != null && (!p.getAddress().trim().equals(""))) {
						Attribute address = new BasicAttribute("registeredAddress",p.getAddress());
						lMods.add(new ModificationItem(DirContext.REPLACE_ATTRIBUTE,address));
					}
							
					if (p.getCity() != null && (!p.getCity().trim().equals(""))) {
						Attribute locality = new BasicAttribute("l",p.getCity());
						lMods.add(new ModificationItem(DirContext.REPLACE_ATTRIBUTE,locality));
					}
							
					if (p.getCountry() != null && (!p.getCity().trim().equals(""))) {
						Attribute country = new BasicAttribute("co",p.getCountry());
						lMods.add(new ModificationItem(DirContext.REPLACE_ATTRIBUTE,country));
					}
							
					if (p.getGender() != null) {
						Attribute gender = new BasicAttribute("x-dcsys-gender",p.getGender().toString());
						lMods.add(new ModificationItem(DirContext.REPLACE_ATTRIBUTE,gender));
					}

					
					List<Telephone> tels = p.getTelephones();
					if (tels != null && tels.size() > 0) {
						Attribute tel = new BasicAttribute("telephoneNumber");
						tel.add(tels.get(0).getNumber());
						lMods.add(new ModificationItem(DirContext.REPLACE_ATTRIBUTE,tel));
					}					
					
					Attribute objectClass = new BasicAttribute("objectClass");
					objectClass.add("top");
					objectClass.add("person");
					objectClass.add("inetOrgPerson");
					objectClass.add("x-dcsys-entidad");
					objectClass.add("x-dcsys-persona");
					addObjectClasses(objectClass,p);
					lMods.add(new ModificationItem(DirContext.REPLACE_ATTRIBUTE,objectClass));
					
					if (p.getDni() != null && (!(p.getDni().trim().equals("")))) {
						Attribute adni = new BasicAttribute("x-dcsys-dni",p.getDni());
						lMods.add(new ModificationItem(DirContext.REPLACE_ATTRIBUTE,adni));
					}
					
					logger.log(Level.FINE,"Tratando de modificar el usuario " + dn);
					
					// transformo a array asi puedo ejecutar el comando.
					// no funciono con el comando toArray()
					ModificationItem[] mods = new ModificationItem[lMods.size()];
					for (int i = 0; i < lMods.size(); i++) {
						mods[i] = lMods.get(i);
					}

					ctx.modifyAttributes(dn,mods);
				}
				
				
				return p.getId();
				
				/**
				 * 
				 * TODO: VER SI ES NECESARIO O SI ESTA OK ESTO ACA!!!. ME PARECE QUE DEBERÍA SER UNA FUNCIÓN INDEPENDIENTE
				 * COMO ESTA AHORA. SOLO EN changePassword.
				
				// despues de agregarlo o modificarlo, paso a manejar el tema de la autentificacion.
				
				List<AuthData> adata = p.getAuthData();
				if (adata != null && adata.size() > 0) {
					// por ahora solo manejo tipo de autentificacion de password.
					
					logger.log(Level.FINE,"Cambiando la clave del usuario ya que existen datos de autentificacion");
					
					for (AuthData c : adata) {
						if (c instanceof Password) {
							
							logger.log(Level.FINE,"Cambiando password");
							
							changePassword(p,(Password)c);
						}
					}
				}
				
				*/
				
			} catch (Exception e) {
				logger.log(Level.SEVERE,e.getMessage());
				throw new PersonException(e);
				
			} finally {
				ctx.close();
			}
		
		} catch (Exception e) {
			throw new PersonException(e);
		}
		
	}


	
	////////// METODOS AUXILIARES PARA VER SI SE UTILIZAN EN UN FUTURO /////////////////
	
	
	
	public void assignTelephone(Person p, Telephone t)  throws PersonException {
		
		if (t == null || t.getNumber() == null || "".equals(t.getNumber().trim())) {
			return;
		}
		
        String uuid = p.getId();
        String dn = findDnByUUid(uuid);

        if (dn == null) {
            throw new RuntimeException("No se puede agregar telefonos a una persona que no existe");
        }
        
        try {
            DirContext ctx = cp.getDirContext();
            try {
                    
				Attribute tel = new BasicAttribute("telephoneNumber",t.getNumber());
				
				List<Telephone> tels = p.getTelephones();
				if (tels != null && tels.size() > 0) {
					tel.add(tels.get(0).getNumber());
				}
                   
                ModificationItem[] mods = new ModificationItem[]{new ModificationItem(DirContext.REPLACE_ATTRIBUTE,tel)};
                
                logger.log(Level.FINE,"tratando de modificar el telefono " + dn);
                
                ctx.modifyAttributes(dn, mods);

                // actualizo el objeto en memoria.
                p.getTelephones().add(t);
                    
            } finally {
                    ctx.close();
            }
                    
	    } catch (NamingException e) {
	            throw new RuntimeException("Error ldap " + e.getMessage());
	    }     
        
	}
	
	/*
	public void changePassword(Person p, Password pass) throws PersonException {
        String uuid = p.getId();
        String dn = findDnByUUid(uuid);
        
        if (dn == null) {
                throw new RuntimeException("No se puede agregar datos de autenticación a una persona que no existe");
        }

        // hay que actualizarlo

        try {
                DirContext ctx = getDirContext();
                try {
                        
                        Attribute password = new BasicAttribute("userPassword",pass.getPassword());
                        
                        ModificationItem[] mods = new ModificationItem[1];
                        mods[0] = new ModificationItem(DirContext.REPLACE_ATTRIBUTE,password);

                        logger.log(Level.FINE,"tratando de modificar el " + dn);
                        
                        ctx.modifyAttributes(dn, mods);
                        
                        
                        // actualizo el objeto en memoria.
                        pass.setPerson(p);
                        p.setAuthData(Arrays.asList((AuthData)pass));
                        
                } finally {
                        ctx.close();
                }
                        
        } catch (NamingException e) {
                throw new RuntimeException("No se pudo conectar al server ldap " + e.getMessage());
        }
	}
	
	*/
	
	public void assignMail(Person p, Mail m) throws PersonException {
		String uuid = p.getId();
		String dn = findDnByUUid(uuid);
		
		if (dn == null) {
			return;
		}
		
		try {
			DirContext ctx = cp.getDirContext();
			try {
				
				String mailS = m.getMail();
				if (mailS.trim().equals("")) {
					mailS = null;
				}
				Attribute mail = new BasicAttribute("x-dcsys-mail", mailS);
				ModificationItem[] mods = new ModificationItem[] { 
												new ModificationItem(DirContext.REPLACE_ATTRIBUTE, mail)};
				ctx.modifyAttributes(dn, mods);
				
			} finally {
				ctx.close();
			}
		} catch (Exception e) {
			logger.log(Level.SEVERE,"error actualizando el mail " + e.getMessage());
		}
	}
	
	
	@Override
	public void addMail(String personId, Mail mail) throws PersonException {
		throw new PersonException("no implementado");
	}
	
	@Override
	public List<Mail> findAllMails(String personId) throws PersonException {
		throw new PersonException("no implementado");
	}
	
	@Override
	public void removeMail(String personId, Mail mail) throws PersonException {
		throw new PersonException("no implementado");
	}
	
}
