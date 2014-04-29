package ar.com.dcsys.pr;

import java.io.PrintWriter;
import java.util.ArrayList;
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


@SupportedAnnotationTypes({"ar.com.dcsys.pr.ClientManager"})
@SupportedSourceVersion(SourceVersion.RELEASE_7)
public class ManagersProcessor extends AbstractProcessor {


	private static final String methodSpacer = "    ";
	private static final String sentenceSpacer = methodSpacer + "    ";

	
	private String getPackageName(Element e) {
		return ((PackageElement)e.getEnclosingElement()).getQualifiedName().toString();
	};


	private void generateServerFiles(List<Manager> managers) {
		for (Manager manager : managers) {
			
			String serverPackage = manager.packageName.replace(".shared", ".server");
			String serverSimpleName = manager.className + "ServerHandler";
			String serverName = serverPackage + "." + serverSimpleName;
			
			StringBuilder sb = new StringBuilder();
			sb.append("package " + serverPackage).append(";\n\n");
			
			// imports
			sb.append("import ar.com.dcsys.gwt.manager.server.handler.MethodHandler;\n");
			sb.append("\n");
			sb.append("import ar.com.dcsys.gwt.messages.server.cdi.HandlersContainer;\n");
			sb.append("\n");
			sb.append("import javax.enterprise.event.Observes;\n");

			sb.append("\n\n");
			
			
			sb.append("public class ").append(serverSimpleName).append(" ").append("implements MethodHandler").append(" {").append("\n\n");

			// el register del handler
			sb.append(methodSpacer).append("public void register(@Observes HandlersContainer<MethodHandler> handlers) { handlers.add(this); }").append("\n\n");

			// metodo principal de procesamiento del mensaje
			sb.append(methodSpacer).append("public void handle() {").append("\n");
			
			sb.append("/*\n\n");		// comentario de prueba
			
			for (Method method : manager.methods) {
				sb.append(sentenceSpacer).append(method.name).append("(");
				
				for (Param param : method.params) {
					sb.append(param.name).append(",");
				}
				sb.append(method.receiver.name);
				
				sb.append(");");				
			}
			
			sb.append("*/\n\n");		// comentario de prueba
			
			sb.append("}").append("\n");		// method }
			
			sb.append("}");						// class }
			
			
			try {
				JavaFileObject jfo = processingEnv.getFiler().createSourceFile(serverName);
				PrintWriter out = new PrintWriter(jfo.openWriter());
				out.println(sb.toString());
				out.flush();
				out.close();
				
			} catch (Exception e) {
				
			}
			
		}
	}

	private void generateClientFiles(List<Manager> managers) {
		for (Manager manager : managers) {
			
			String clientPackage = manager.packageName.replace(".shared", ".client");
			String clientSimpleName = manager.className + "Client";
			String clientName =  clientPackage + "." + clientSimpleName;

			StringBuilder sb = new StringBuilder();
			sb.append("package " + clientPackage).append(";\n\n");
			sb.append("public class ").append(clientSimpleName).append(" {").append("\n");
			
			for (Method method : manager.methods) {
				sb.append("\n").append(methodSpacer).append("public void ").append(method.name).append("(");
				
				// los parametros
				for (Param param : method.params) {
					String t = param.typeMirror.toString();
					if (t.startsWith("java.lang.")) {
						t = t.replace("java.lang.", "");
					}
					sb.append(t).append(" ").append(param.name).append(", ");
				}

				// el receiver
				sb.append(method.receiver.typeMirror.toString());
				sb.append(" ").append(method.receiver.name);
				
				sb.append(") {\n");
				
				// implementacion del metodo.
				
				sb.append("\n").append(methodSpacer).append("}");				// method }
			}
			
			sb.append("\n").append("}");		// class }
			
			
			try {
				JavaFileObject jfo = processingEnv.getFiler().createSourceFile(clientName);
				PrintWriter out = new PrintWriter(jfo.openWriter());
				out.println(sb.toString());
				out.flush();
				out.close();
				
			} catch (Exception e) {
				
			}
			
		}
	}
	
	private void generateFactories(List<Manager> managers) {
		
	}
	
	
	
	@Override
	public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
		
		Messager messager = processingEnv.getMessager();
		
		messager.printMessage(Kind.WARNING, "Iniciando procesamiento de los clients");
		
		List<Manager> managers = new ArrayList<>();
		
		for (TypeElement te : annotations) {
			
			Set<? extends Element> elements = roundEnv.getElementsAnnotatedWith(te);
			for (Element e : elements) {
				
				if (e.getKind() == ElementKind.INTERFACE) {
					
					String pName = getPackageName(e);
					if (!pName.endsWith("shared")) {
						// debe estar definida la interfaz en shared.
						continue;
					}
					
					Manager manager = new Manager();
					manager.packageName = getPackageName(e);
					manager.className = ((TypeElement)e).getSimpleName().toString();

					
					List<? extends Element> eelements = e.getEnclosedElements();
					for (Element ee : eelements) {

						if (ee.getKind() == ElementKind.FIELD) {
							
							VariableElement ve = (VariableElement)ee;
							
							// las ignoro.
							
						}
						
						if (ee.getKind() == ElementKind.METHOD) {
							
							ExecutableElement eee = (ExecutableElement)ee;
							processMethod(manager,eee);
							
						}
						
					}
					
					managers.add(manager);
				}
				
			}
		}
	
		if (managers.size() > 0) {
			generateClientFiles(managers);
			generateServerFiles(managers);
		}
		
		return true;
	
	}

	

	/**
	 * procesa a ver si el método coincide con el prototipo que debería tener.
	 * 
	 * public void metodo(Parametro1 p1, Parametro2 p2, .., Receiver<Resultado> rec)
	 * 
	 * los parámetros a codificar son opcionales, pero NO el receiver final
	 * 
	 * @param ee
	 * @return
	 */
	private void processMethod(Manager manager, ExecutableElement ee) {
		
		// debe ser void el retorno del mensaje. si no lo ignoro.
		
		/*
		
		TypeMirror tm = ee.getReturnType();
		if (tm.getKind() != TypeKind.VOID) {
			return;
		}
*/
		
		List<? extends VariableElement> params = ee.getParameters();
		if (params.size() <= 0) {
			return;
		}
		
		
		VariableElement rec = params.get(params.size() - 1);
		
		/*
		TypeMirror type = rec.asType();
		if (!"ar.com.dcsys.manager.shared.Receiver".equals(type.toString())) {
			return;
		}
		*/
		
		Method method = new Method();
		method.name = ee.getSimpleName().toString();

		Param receiver = new Param();
		receiver.name = "rec";
		receiver.typeMirror = rec.asType();
		receiver.typeKind = receiver.typeMirror.getKind();
		method.receiver = receiver;
		
		for (int i = 0; i < params.size() - 1; i++) {
			VariableElement ve = params.get(i);
			Param param = new Param();
			param.name = ve.getSimpleName().toString();
			param.typeMirror = ve.asType();
			param.typeKind = param.typeMirror.getKind();
			method.params.add(param);
		}
		
		manager.methods.add(method);
	}

		/*
		
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
	
	*/
	
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
