package ar.com.dcsys.pr;

import java.util.Set;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Messager;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.PackageElement;
import javax.lang.model.element.TypeElement;
import javax.tools.Diagnostic.Kind;

import ar.com.dcsys.pr.runtime.Manager;

@SupportedAnnotationTypes({"ar.com.dcsys.pr.ClientManager"})
@SupportedSourceVersion(SourceVersion.RELEASE_7)
public class ManagersProcessor extends AbstractProcessor {

	private String getPackageName(Element e) {
		return ((PackageElement)e.getEnclosingElement()).getQualifiedName().toString();
	};	

	
	@Override
	public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
		
		Messager messager = processingEnv.getMessager();
		
		messager.printMessage(Kind.WARNING, "Iniciando procesamiento de los clients");
		
		for (TypeElement te : annotations) {
			
			Set<? extends Element> elements = roundEnv.getElementsAnnotatedWith(te);
			for (Element e : elements) {
				
				if (e.getKind() == ElementKind.INTERFACE) {
					
					String pName = getPackageName(e);
					if (!pName.endsWith("shared")) {
						// debe estar definida la interfaz en shared.
						continue;
					}
					
					Manager manager = Manager.create(e,processingEnv);
					manager.generateSourceFiles(processingEnv);
					
				}
				
			}
		}
		
		return true;
	}
	
}
