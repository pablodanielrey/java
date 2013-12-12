package ar.com.dcsys.auth.shiro;

import java.security.Principal;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.logging.Logger;

import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;

public class DCSysShiroRealm extends AuthorizingRealm {

	private static final Logger logger = Logger.getLogger(DCSysShiroRealm.class.getName());
	
	@Override
	protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection pc) {
		
		logger.info("autorizando");
		
		List<Principal> principals = pc.asList();
		
		// aca se chequearian y se obtendrían los roles de los principals.
		
		Set<String> roles = new HashSet<>();
		roles.addAll(Arrays.asList("admin","user","guest"));
		
		AuthorizationInfo info = new SimpleAuthorizationInfo(roles);
		
		return info;
	}

	@Override
	protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken pc) throws AuthenticationException {
		
		logger.info("autenticando");
				
		if (!(pc instanceof UsernamePasswordToken)) {
			throw new AuthenticationException("AuthenticationToken not supported");
		}
		
		
		UsernamePasswordToken ut = (UsernamePasswordToken)pc;
		String userName = ut.getUsername();
		String password = new String(ut.getPassword());

		// chequear las credenciales a ver si existe el usuario.
		// si no existe tirar un AuthenticationException
		
		SimpleAuthenticationInfo info = new SimpleAuthenticationInfo(ut.getPrincipal(),ut.getCredentials(),getName());
		
		return info;
	}

}
