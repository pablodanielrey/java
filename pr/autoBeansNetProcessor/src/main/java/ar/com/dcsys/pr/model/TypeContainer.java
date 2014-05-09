package ar.com.dcsys.pr.model;

import javax.annotation.processing.ProcessingEnvironment;

public interface TypeContainer {

	public String getPackage();
	public String getType();
	public String getContainedType();

	public void generateSourceFile(ProcessingEnvironment processingEnv);
}
