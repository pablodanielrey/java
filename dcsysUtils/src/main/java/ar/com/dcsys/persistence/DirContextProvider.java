package ar.com.dcsys.persistence;

import javax.naming.NamingException;
import javax.naming.directory.DirContext;

public interface DirContextProvider {

	public DirContext getDirContext() throws NamingException;
	public String getUsersBase();
	
}
