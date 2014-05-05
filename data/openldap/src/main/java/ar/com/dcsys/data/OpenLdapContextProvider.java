package ar.com.dcsys.data;

import java.util.Hashtable;

import javax.inject.Inject;
import javax.naming.Context;
import javax.naming.NamingException;
import javax.naming.directory.DirContext;
import javax.naming.directory.InitialDirContext;

import ar.com.dcsys.config.Config;
import ar.com.dcsys.persistence.DirContextProvider;

public class OpenLdapContextProvider implements DirContextProvider {

	@Inject @Config String server;
	@Inject @Config String port;
	@Inject @Config String user;
	@Inject @Config String password;
	@Inject @Config String usersBase;
	
	private Hashtable<String,String> env;
	
	private Hashtable<String,String> getEnv() {
		if (env == null) {

			StringBuffer sb = new StringBuffer();
			sb.append("ldap://").append(server).append(":").append(port).append("/");
			String url = sb.toString();
			
			env = new Hashtable<>();
			env.put(Context.INITIAL_CONTEXT_FACTORY,"com.sun.jndi.ldap.LdapCtxFactory");
			env.put(Context.PROVIDER_URL, url);
			env.put(Context.SECURITY_AUTHENTICATION, "simple");
			env.put(Context.SECURITY_PRINCIPAL, user);
			env.put(Context.SECURITY_CREDENTIALS, password);
			env.put("com.sun.jndi.ldap.connect.pool", "true");

		}
		
		return env;
	}
	
	@Override
	public String getUsersBase() {
		return usersBase;
	}
	
	@Override
	public DirContext getDirContext() throws NamingException {
		Hashtable<String,String> env = getEnv();
		DirContext ctx = new InitialDirContext(env);
		return ctx;
	}

}
