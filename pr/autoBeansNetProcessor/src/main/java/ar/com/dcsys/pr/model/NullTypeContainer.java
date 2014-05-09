package ar.com.dcsys.pr.model;

import javax.annotation.processing.ProcessingEnvironment;

public class NullTypeContainer extends BasicTypeContainer {

	public NullTypeContainer(String pack, String type) {
		super(type.substring(0,type.lastIndexOf(".")),type);
	}
	
	@Override
	public String getType() {
		return getContainedType();
	}
	

	@Override
	public void generateSourceFile(ProcessingEnvironment processingEnv) {
		
		// no se hace nada ya que no es necesario un container.
		
	}
	
}
