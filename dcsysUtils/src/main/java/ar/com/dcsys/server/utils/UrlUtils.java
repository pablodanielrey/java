package ar.com.dcsys.server.utils;

import javax.servlet.http.HttpServletRequest;

public class UrlUtils {

	/*
	 * Obtiene la url a enviar por mail para poder procesar el token.
	 */
	public static StringBuffer getTokenUrl(HttpServletRequest req) {
		StringBuffer url = new StringBuffer();
		if (req.isSecure()) {
			url.append("https://");
		} else {
			url.append("http://");
		}
		url.append(req.getServerName());
		url.append(":");
		url.append(req.getServerPort());
		url.append(req.getContextPath() + "/");
		url.append("processToken");
		return url;
	}	
	
}
