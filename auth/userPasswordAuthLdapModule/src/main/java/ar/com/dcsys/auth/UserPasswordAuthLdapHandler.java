package ar.com.dcsys.auth;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import javax.inject.Inject;
import javax.naming.NamingEnumeration;
import javax.naming.NamingException;
import javax.naming.directory.Attribute;
import javax.naming.directory.Attributes;
import javax.naming.directory.DirContext;
import javax.naming.directory.SearchControls;
import javax.naming.directory.SearchResult;

import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authc.UsernamePasswordToken;

import ar.com.dcsys.data.OpenLdapContextProvider;
import ar.com.dcsys.data.auth.AbstractAuthHandler;
import ar.com.dcsys.data.auth.principals.DniPrincipal;
import ar.com.dcsys.data.auth.principals.IdPrincipal;
import ar.com.dcsys.data.auth.principals.UserNamePrincipal;
import ar.com.dcsys.exceptions.AuthenticationException;

public class UserPasswordAuthLdapHandler extends AbstractAuthHandler {

	private static final Logger logger = Logger.getLogger(UserPasswordAuthLdapHandler.class.getName());

	private final String userBaseDN = "ou=people,dc=econo";
	private final String userFilter = "(&(objectClass=inetOrgPerson)(objectClass=person))";	
	private final String[] userAttrs = {"uid","x-dcsys-uuid","x-dcsys-dni"};
	
	
	private final OpenLdapContextProvider cp;
	
	
	
	@Inject
	public UserPasswordAuthLdapHandler(OpenLdapContextProvider cp) {
		this.cp = cp;
	}
	
	
	@Override
	public List<Principal> findAllPrincipals(IdPrincipal p) throws AuthenticationException {
		
		String filter = "(x-dcsys-uuid=" + p.getName() + ")";
		
		try {
			DirContext ctx = cp.getDirContext();
			try {
				SearchControls sc = new SearchControls();
				sc.setReturningAttributes(userAttrs);
				sc.setSearchScope(SearchControls.SUBTREE_SCOPE);
				NamingEnumeration<SearchResult> ne = ctx.search(userBaseDN,filter,sc);
				try {
					List<Principal> principals = new ArrayList<>();
					principals.add(p);
					
					if (ne.hasMore()) {
						SearchResult rs = ne.next();
						Attributes attrs = rs.getAttributes();
	
						Attribute username = attrs.get("uid");
						if (username != null) {
							UserNamePrincipal up = new UserNamePrincipal((String)username.get());
							principals.add(up);
						}
						
						Attribute dni = attrs.get("x-dcsys-dni");
						if (dni != null) {
							DniPrincipal dp = new DniPrincipal((String)dni.get());
							principals.add(dp);
						}
						
						return principals;
						
					} else {
						throw new AuthenticationException("user not found");
						
					}
					
				} finally {
					ne.close();
				}
			
			} finally {
				ctx.close();
			}
		} catch (NamingException e) {
			throw new AuthenticationException(e.getMessage());
		}
		
		
	}
	
	@Override
	public String findIdByPrincipal(Principal p) throws AuthenticationException {
		
		String filter = "";
		
		if (p instanceof UserNamePrincipal) {
			filter = "(uid=" + ((UserNamePrincipal)p).getName() + ")";
		} else if (p instanceof DniPrincipal) {
			filter = "(x-dcsys-dni=" + ((DniPrincipal)p).getName() + ")";
		} else {
			throw new AuthenticationException("Principal not supported");
		}
		
		try {
			String id = findId(filter);
			if (id == null) {
				throw new AuthenticationException("user not found");
			}
			return id;
			
		} catch (NamingException e) {
			throw new AuthenticationException(e.getMessage());
		}

	}
	
	
	private String findId(String filter) throws NamingException {
		
		DirContext ctx = cp.getDirContext();
		try {
			SearchControls sc = new SearchControls();
			sc.setReturningAttributes(userAttrs);
			sc.setSearchScope(SearchControls.SUBTREE_SCOPE);
			NamingEnumeration<SearchResult> ne = ctx.search(userBaseDN,filter,sc);
			try {
				List<Principal> principals = new ArrayList<>();
				if (ne.hasMore()) {
					SearchResult rs = ne.next();
					Attributes attrs = rs.getAttributes();
					
					Attribute uuid = attrs.get("x-dcsys-uuid");
					if (uuid == null) {
						return null;
					}
					
					String suuid = (String)uuid.get();
					return suuid;
					
				} else {
					return null;
					
				}
				
			} finally {
				ne.close();
			}
		
		} finally {
			ctx.close();
		}
	}
	
	
	private AuthenticationInfo authenticate(UsernamePasswordToken token) throws NamingException, AuthenticationException {
		
		String username = token.getUsername();
		String password = new String(token.getPassword());
	
		DirContext ctx = cp.getDirContext();
		try {
		
			String filter = "(&(uid=" + username + ")(userPassword=" + password + "))";
			
			SearchControls sc = new SearchControls();
			sc.setReturningAttributes(userAttrs);
			sc.setSearchScope(SearchControls.SUBTREE_SCOPE);
			NamingEnumeration<SearchResult> ne = ctx.search(userBaseDN,filter,sc);
			try {
				List<Principal> principals = new ArrayList<>();
				if (ne.hasMore()) {
					SearchResult rs = ne.next();
					Attributes attrs = rs.getAttributes();
					
					Attribute uuid = attrs.get("x-dcsys-uuid");
					String suuid = (String)uuid.get();
					IdPrincipal idPrincipal = new IdPrincipal(suuid);
					principals.add(idPrincipal);
					
					UserNamePrincipal userPrincipal = new UserNamePrincipal(username);
					principals.add(userPrincipal);
					
					Attribute dni = attrs.get("x-dcsys-dni");
					if (dni != null) {
						DniPrincipal dniPrincipal = new DniPrincipal((String)dni.get());
						principals.add(dniPrincipal);
					}
					
					AuthenticationInfo info = new SimpleAuthenticationInfo(new LdapPrincipalCollection(principals), token.getPassword());
					return info;
					
				} else {
					throw new AuthenticationException("No existe el usuario para esas credenciales"); 
				}
				
			} finally {
				ne.close();
			}
		
		} finally {
			ctx.close();
		}
	}
	
	@Override
	public AuthenticationInfo autenticate(AuthenticationToken token) throws AuthenticationException {
		
		UsernamePasswordToken upt = (UsernamePasswordToken)token;

		String host = upt.getHost();
		String username = upt.getUsername();

		try {
			AuthenticationInfo info = authenticate(upt);
			return info;
			
//		Principal p = (Principal)info.getPrincipals().getPrimaryPrincipal();
//		logSuccess(con,host,username,p);
					

		} catch (NamingException e) {
			throw new AuthenticationException(e.getMessage());
			
		} catch (AuthenticationException e) {
			throw e;
		}
//					char[] password = upt.getPassword();
//					logError(con, host, username, new String(password));
//					throw e;
//				}
				
	}
	
	@Override
	public boolean handles(AuthenticationToken token) {
		
		return (token instanceof UsernamePasswordToken);
		
	}
	
}
