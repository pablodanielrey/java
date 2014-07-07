package ar.com.dcsys.config;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.enterprise.inject.Produces;
import javax.enterprise.inject.spi.InjectionPoint;

public class CdiConfigFactory {

	private static final Logger logger = Logger.getLogger(CdiConfigFactory.class.getName());
	private Properties properties;
	
	private synchronized Properties getProperties() {
		if (properties == null) {
			properties = new Properties();
			
			String home = System.getProperty("user.home");
			String separator = System.getProperty("file.separator");
			String configValues = home + separator + "dcsys.properties";
			
			try {
				properties.load(new FileInputStream(configValues));
			} catch (IOException e) {
				logger.log(Level.SEVERE, e.getMessage(),e);
			}
		}
		return properties;
	}
	
	private String getName(InjectionPoint p) {
		return p.getMember().getDeclaringClass().getName() + "." + p.getMember().getName();
	}
	
	private String getSimpleName(InjectionPoint p) {
		return p.getMember().getDeclaringClass().getSimpleName() + "." + p.getMember().getName();
	}
	
	private String getMemberName(InjectionPoint p) {
		return p.getMember().getName();
	}
	
	public @Produces @Config String getConfiguration(InjectionPoint p) {
		Properties prop = getProperties();
		String keyN = getName(p);
		String r = prop.getProperty(keyN);
		if (r == null) {

			String keyS = getSimpleName(p);
			r = prop.getProperty(keyS);
			if (r == null) {
				
				String keyM = getMemberName(p);
				r = prop.getProperty(keyM);
				if (r == null) { 
				
					logger.log(Level.SEVERE, "config  " + keyN + " or " + keyS + " or " + keyM + " does not exist" );
					return null;
				}
			}
		}
		return r;
	}
	
}
