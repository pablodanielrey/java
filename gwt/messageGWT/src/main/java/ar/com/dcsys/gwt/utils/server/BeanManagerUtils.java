package ar.com.dcsys.gwt.utils.server;

import java.lang.reflect.Type;
import java.util.Iterator;

import javax.enterprise.context.spi.CreationalContext;
import javax.enterprise.inject.spi.Bean;
import javax.enterprise.inject.spi.BeanManager;
import javax.naming.NamingException;


public class BeanManagerUtils {

	  @SuppressWarnings("unchecked")
	  public static <T> T lookup(Class<T> clazz, BeanManager bm) {
		    Iterator<Bean< ? >> iter = bm.getBeans(clazz).iterator();
		    if (!iter.hasNext()) {
		      throw new IllegalStateException("CDI BeanManager cannot find an instance of requested type " + clazz.getName());
		    }
		    Bean<T> bean = (Bean<T>) iter.next();
		    CreationalContext<T> ctx = bm.createCreationalContext(bean);
		    T dao = (T) bm.getReference(bean, clazz, ctx);
		    return dao;
	  }

	  @SuppressWarnings({ "unchecked", "rawtypes" })
	  public static Object lookup(String name, BeanManager bm) {
		    Iterator<Bean< ? >> iter = bm.getBeans(name).iterator();
		    if (!iter.hasNext()) {
		      throw new IllegalStateException("CDI BeanManager cannot find an instance of requested type '" + name + "'");
		    }
		    Bean bean = iter.next();
		    CreationalContext ctx = bm.createCreationalContext(bean);
		    // select one beantype randomly. A bean has a non-empty set of beantypes.
		    Type type = (Type) bean.getTypes().iterator().next();
		    return bm.getReference(bean, type, ctx);
	  }
	  
	  public static <T> T lookup(Class<T> clazz) {
		  try {
		    BeanManager bm = BeanManagerLocator.getBeanManager();
		    return lookup(clazz, bm);
		  } catch (NamingException e) {
			  return null;
		  }
	  }
	  
	  public static Object lookup(String name) {
		  try {
		    BeanManager bm = BeanManagerLocator.getBeanManager();
		    return lookup(name, bm);
		  } catch (NamingException e) {
			  return null;
		  }
	  }
	
}
