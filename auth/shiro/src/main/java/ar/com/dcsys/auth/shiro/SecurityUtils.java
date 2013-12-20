package ar.com.dcsys.auth.shiro;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpSession;

import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.env.WebEnvironment;
import org.apache.shiro.web.mgt.WebSecurityManager;
import org.apache.shiro.web.util.WebUtils;

public class SecurityUtils {

	/**
	 * Metodo utilitario que emula al SecurityUtils.getSubject() nativo de shiro.
	 * para usarlo dentro de los hanlders de los mensajes para obtener losd atos de autentificacion. y chequear persmisos.
	 * 
	 * @param session
	 * @return
	 */
	public static Subject getSubject(HttpSession session) {
		
		ServletContext scontext = session.getServletContext();
		WebEnvironment environment = WebUtils.getWebEnvironment(scontext);
		WebSecurityManager securityManager = environment.getWebSecurityManager();
		Subject subject = new Subject.Builder(securityManager).sessionId(session.getId()).buildSubject();		
				
		return subject;
	}
	
}
