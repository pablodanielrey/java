package ar.com.dcsys.data;

import java.util.Hashtable;

import javax.inject.Inject;
import javax.inject.Named;
import javax.naming.Context;
import javax.naming.NamingException;
import javax.naming.directory.DirContext;
import javax.naming.directory.InitialDirContext;

import ar.com.dcsys.persistence.DirContextProvider;
import ar.com.dcsys.persistence.PersistenceData;

@Named
public class OpenLdapContextProvider implements DirContextProvider {

	private final PersistenceData data;
	private Hashtable<String,String> env;
	
	@Inject
	public OpenLdapContextProvider(PersistenceData data) {
		this.data = data;

		StringBuffer sb = new StringBuffer();
		sb.append("ldap://").append(data.getServer()).append(":").append(data.getPort());
		String url = sb.toString();
		String user = data.getUserName();
		String password = data.getPassword();
		
		env = new Hashtable<>();
		env.put(Context.INITIAL_CONTEXT_FACTORY,"com.sun.jndi.ldap.LdapCtxFactory");
		env.put(Context.PROVIDER_URL, url);
		env.put(Context.SECURITY_AUTHENTICATION, "simple");
		env.put(Context.SECURITY_PRINCIPAL, user);
		env.put(Context.SECURITY_CREDENTIALS, password);
		env.put("com.sun.jndi.ldap.connect.pool", "true");
	}
	
	@Override
	public String getUsersBase() {
		return data.getUsersBase();
	}
	
	@Override
	public DirContext getDirContext() throws NamingException {
		DirContext ctx = new InitialDirContext(env);
		return ctx;
	}

}
