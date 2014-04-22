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
								
								String method = processMethod(eee);
								
								out.println(method);
								
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

	
	/**
	 * procesa a ver si el método coincide con el prototipo que debería tener.
	 * 
	 * public void metodo(Parametro1 p1, Parametro2 p2, .., Receiver<Resultado> rec)
	 * 
	 * los parámetros a codificar son opcionales, pero NO el receiver final
	 * el el caso de que no corresponda con el prototipo retorna un string = ""
	 * 
	 * @param ee
	 * @return
	 */
	private String processMethod(ExecutableElement ee) {
		
		StringBuilder sb = new StringBuilder();
		
		// debe ser void el retorno del mensaje. si no lo ignoro.
		TypeMirror tm = ee.getReturnType();
		if (tm.getKind() != TypeKind.VOID) {
			return "";
		}
		sb.append("    ").append("public void ");
		
		String name = ee.getSimpleName().toString();
		sb.append(name);
		
		sb.append("(");
		List<? extends VariableElement> params = ee.getParameters();
		if (params.size() == 0) {
			return "";
		}
		
		if (params.size() >= 2) {
			int end = params.size() - 1;
			for (int i = 0; i < end; i++) {
				VariableElement ve = params.get(i);
				
				TypeMirror clazz = ve.asType();
				//String clazz = ve.getSimpleName().toString();
				String pname = ve.toString();

				sb.append(clazz).append(" ").append(pname).append(",");
			}
		}
		
		// aca proceso el parametro del tipo de retorno
		
		VariableElement receiver = params.get(params.size() - 1);
		TypeMirror clazz = receiver.asType();
		
		sb.append(clazz).append(" ").append("rec");
		
		sb.append(")").append(" ").append("{").append("\n");
		
		
		/// ahora escribo el cuerpo del mensaje que es codificar los parámetros y llamar al websocket 
		
		String type = clazz.toString();
		String subtype = type.substring(type.indexOf("<") + 1, type.lastIndexOf(">"));
		sb.append("//" + subtype);
		
		if (type.startsWith("java.lang.")) {
			// es un tipo primitivo, no necesita codificacion.
			
		} else if (type.startsWith(List.class.getName())) {
			// es una lista. asi que saco el tipo de la lista
			int ini = type.indexOf("<") + 1;
			int end = type.indexOf(">");
			String subsubtype = type.substring(ini, end);
			
			sb.append("        ").append(" // ").append(subsubtype).append("\n");
			
		} else {
			sb.append("// no se reconoce el tipo de parámetro").append("\n");
		}
		
		
		/// fin dle método 
		
		sb.append("};").append("\n");
		
		
		return sb.toString();
	}
	
	
	private Param processParam(VariableElement ve) {
		TypeMirror tm = ve.asType();
		String type = tm.toString();
		
		Param p = new Param();
		
		if (hasSubtype(type)) {
			
		}
		
		return p;
	}
	
	
	private class Param {
		boolean isList;
		String clazz;
		String name;
	}
	
	
	
	private boolean hasSubtype(String t) {
		return ((t.indexOf("<") != -1) && (t.indexOf(">") != -1));
	}
	
	private String getSubtype(String t) {
		int ini = t.indexOf("<") + 1;
		int end = t.lastIndexOf(">");
		String subtype = t.substring(ini, end);
		return subtype;
		
	}
	
	private String getType(String t) {
		return t.substring(0,t.indexOf("<"));
	}
	
	
	
	private <T> boolean isType(TypeMirror type, Class<T> t) {
		return type.toString().startsWith(t.getName());
	}
	
	private boolean isPrimitive(String type) {
		return type.startsWith("java.lang.");
		
	}
	
	
	
}
