package ar.com.dcsys.auth;

import java.security.Principal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.apache.shiro.subject.PrincipalCollection;

public class LdapPrincipalCollection implements PrincipalCollection {

	private static final long serialVersionUID = 1L;
	private final List<Principal> principals;
	
	public LdapPrincipalCollection(List<Principal> principals) {
		this.principals = principals;
	}
	
	
	@Override
	public Iterator iterator() {
		return principals.iterator();
	}

	@Override
	public Object getPrimaryPrincipal() {
		return principals.get(0);
	}

	@Override
	public <T> T oneByType(Class<T> type) {
		for (Principal p : principals) {
			if (p.getClass().getName().equals(type.getName())) {
				return (T)p;
			}
		}
		return null;
	}

	@Override
	public <T> Collection<T> byType(Class<T> type) {
		List<T> ts = new ArrayList<>();
		for (Principal p : principals) {
			if (p.getClass().getName().equals(type.getName())) {
				ts.add((T)p);
			}
		}
		return ts;
	}

	@Override
	public List asList() {
		return principals;
	}

	@Override
	public Set asSet() {
		return new HashSet(principals);
	}

	@Override
	public Collection fromRealm(String realmName) {
		return Arrays.asList(UserPasswordAuthLdapHandler.class.getName());
	}

	@Override
	public Set<String> getRealmNames() {
		HashSet<String> hs = new HashSet<>();
		hs.add(UserPasswordAuthLdapHandler.class.getName());
		return hs;
	}

	@Override
	public boolean isEmpty() {
		return (principals == null || principals.size() <= 0);
	}

}
