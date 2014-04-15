package ar.com.dcsys.pr;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import java.util.Set;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Messager;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.Name;
import javax.lang.model.element.PackageElement;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.VariableElement;
import javax.lang.model.type.TypeKind;
import javax.lang.model.type.TypeMirror;
import javax.tools.Diagnostic.Kind;
import javax.tools.JavaFileObject;


@SupportedAnnotationTypes(
		{"ar.com.dcsys.pr.ClientManager"}
	)
@SupportedSourceVersion(SourceVersion.RELEASE_7)
public class ManagersProcessor extends AbstractProcessor {

	@Override
	public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
		
		Messager messager = processingEnv.getMessager();
		
		messager.printMessage(Kind.WARNING, "Iniciando procesamiento de los clients");
		
		try {
		
			for (TypeElement te : annotations) {
				
				Set<? extends Element> elements = roundEnv.getElementsAnnotatedWith(te);
				for (Element e : elements) {
					
					if (e.getKind() == ElementKind.INTERFACE) {
						
						PackageElement packageElement = (PackageElement)e.getEnclosingElement();
						
						String className = e.getSimpleName() + "BeanAuto";
						String classQName = packageElement.getQualifiedName() + "." + className;
						
						JavaFileObject jfo = processingEnv.getFiler().createSourceFile(classQName);
						PrintWriter out = new PrintWriter(jfo.openWriter());
						
						out.println("package " + packageElement.getQualifiedName() + ";");
						out.println("");
						out.println("public class " + className + " {");
						out.println("");
	
						
						List<? extends Element> eelements = e.getEnclosedElements();
						for (Element ee : eelements) {

							if (ee.getKind() == ElementKind.FIELD) {
								
								VariableElement ve = (VariableElement)ee;
								
								// las ignoro.
								
							}
							
							if (ee.getKind() == ElementKind.METHOD) {
								
								ExecutableElement eee = (ExecutableElement)ee;
								
								TypeMirror tm = eee.getReturnType();
								List<? extends VariableElement> params = eee.getParameters();
								String name = eee.getSimpleName().toString();
								
								
								out.println("public " + tm.toString() + " " + name + "(");
								for (VariableElement va : params) {
									out.println(va.toString());
								}
								out.println(") {}");
								
								
							}
							
						}
						
						
						out.println("}");
						out.println("");
						out.flush();
						out.close();
						
					}
					
				}
				
				
			}
	
		} catch (IOException e) {
			messager.printMessage(Kind.ERROR, e.getMessage());
		}
		
		return true;
	
	}

	
	private String processMethod(ExecutableElement ee) {
		
		StringBuilder sb = new StringBuilder();
		
		TypeMirror tm = ee.getReturnType();
		if (tm.getKind() == TypeKind.) {
			
		}
		
		return sb.toString();
	}
	
	
}
