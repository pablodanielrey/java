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


	private static final String ms = "    ";
	private static final String ss = ms + "    ";
	private static final String autoBeanPackage = "com.google.web.bindery.autobean.shared";

	
	private String getPackageName(Element e) {
		return ((PackageElement)e.getEnclosingElement()).getQualifiedName().toString();
	};


	private void generateServerFiles(List<Manager> managers) {
		for (Manager manager : managers) {
			
			StringBuilder sb = new StringBuilder();
			sb.append("package " + manager.getServerPackage()).append(";\n\n");
			
			// imports
			sb.append("import ar.com.dcsys.gwt.manager.server.handler.MethodHandler;\n");
			sb.append("\n");
			sb.append("import ar.com.dcsys.gwt.messages.server.cdi.HandlersContainer;\n");
			sb.append("\n");
			sb.append("import javax.enterprise.event.Observes;\n");

			sb.append("\n\n");
			
			
			sb.append("public class ").append(manager.getServerSimpleName()).append(" ").append("implements MethodHandler").append(" {").append("\n\n");

			// el register del handler
			sb.append(ms).append("public void register(@Observes HandlersContainer<MethodHandler> handlers) { handlers.add(this); }").append("\n\n");

			// metodo principal de procesamiento del mensaje
			sb.append(ms).append("public void handle() {").append("\n");
			
			sb.append("/*\n\n");		// comentario de prueba
			
			for (Method method : manager.methods) {
				sb.append(ss).append(method.name).append("(");
				
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
				JavaFileObject jfo = processingEnv.getFiler().createSourceFile(manager.getServerName());
				PrintWriter out = new PrintWriter(jfo.openWriter());
				out.println(sb.toString());
				out.flush();
				out.close();
				
			} catch (Exception e) {
				
			}
			
		}
	}

	
	private String getAutoBeanName(Param p) {
		return p.getName() + "AutoBean";
	}
	
	private void generateClientFiles(List<Manager> managers) {
		for (Manager manager : managers) {
			
			StringBuilder sb = new StringBuilder();
			sb.append("package " + manager.getClientPackage()).append(";\n\n");
			
			///////// imports /////////////
			
			sb.append("\n").append("import ar.com.dcsys.gwt.messages.shared.TransportReceiver;");
			
			sb.append("\n").append("import ar.com.dcsys.gwt.manager.shared.lang.TypeFactory;");
			sb.append("\n").append("import ar.com.dcsys.gwt.manager.shared.lang.BooleanContainer;");
			sb.append("\n").append("import ar.com.dcsys.gwt.manager.shared.lang.IntegerContainer;");
			sb.append("\n").append("import ar.com.dcsys.gwt.manager.shared.lang.StringContainer;");
			sb.append("\n").append("import ar.com.dcsys.gwt.manager.shared.lang.LongContainer;");
			sb.append("\n").append("import ar.com.dcsys.gwt.manager.shared.lang.BooleanListContainer;");
			sb.append("\n").append("import ar.com.dcsys.gwt.manager.shared.lang.IntegerListContainer;");
			sb.append("\n").append("import ar.com.dcsys.gwt.manager.shared.lang.LongListContainer;");
			sb.append("\n").append("import ar.com.dcsys.gwt.manager.shared.lang.StringListContainer;");
			
			sb.append("\n").append("import ").append(manager.factory.getPackageName()).append(".*;");
			
			
			sb.append("\n").append("import ar.com.dcsys.gwt.manager.shared.message.MessageFactory;");
			sb.append("\n").append("import ar.com.dcsys.gwt.manager.shared.message.Message;");
			
			sb.append("\n").append("import ").append(manager.factory.getPackageName()).append(".").append(manager.factory.getType()).append(";");

			sb.append("\n").append("import ar.com.dcsys.gwt.ws.client.WebSocket;");

			sb.append("\n").append("import javax.inject.Inject;");
			sb.append("\n").append("import java.util.List;");
			sb.append("\n").append("import java.util.ArrayList;");
			
			sb.append("\n").append("import com.google.web.bindery.autobean.vm.AutoBeanFactorySource;");			// ESTO VA EN EL SERVIDOR. pero de prueba ahora.
			sb.append("\n").append("import ").append(autoBeanPackage).append(".AutoBeanCodex;");
			sb.append("\n").append("import ").append(autoBeanPackage).append(".AutoBean;");
			
			sb.append("\n\n");
			
			////////// definicion de la clase /////////////////////
						
			sb.append("public class ").append(manager.getClientSimpleName()).append(" {").append("\n");
			
			////// variables de intancia //////
			
			sb.append("\n").append(ms).append("private final TypeFactory typeFactory = AutoBeanFactorySource.create(TypeFactory.class);");
			sb.append("\n").append(ms).append("private final MessageFactory messageFactory = AutoBeanFactorySource.create(MessageFactory.class);");
			sb.append("\n").append(ms).append("private final ").append(manager.factory.getType()).append(" ").append(manager.factory.getName()).append(" = AutoBeanFactorySource.create(").append(manager.factory.getType()).append(".class);");
			
			sb.append("\n").append(ms).append("private final WebSocket ws;");
			sb.append("\n\n");
			
			/////////////// constuctor //////////////////////////
			
			
			sb.append("\n").append(ms).append("@Inject\n").append(ms).append("public ").append(manager.getClientSimpleName()).append("(WebSocket ws) {\n");
			sb.append(ss).append("this.ws = ws;");
			sb.append("\n").append(ms).append("}\n");
			
			//////////////// definición de metodos ////////////////
			
			for (Method method : manager.methods) {
				
				sb.append("\n").append(ms).append("public void ").append(method.name).append("(");
				
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
				
				
				//////////////// try inicial /////////////////////
				
				sb.append("\n").append(ss).append("try {\n");
				
				////////////////// codifico los parametros /////////////////////

				sb.append("\n").append(ss).append(ss).append("List<String> params = new ArrayList<>();");
				sb.append("\n");
				
				
				for (Param param : method.params) {
					
					// Boolean, String, Integer, Long
					
					String type = param.getType();
					
					if (type.startsWith("java.lang.")) {
						
						// tipo primitivo.
						String t = type.replace("java.lang.","");
						String fname = "get" + t + "();";
						
						sb.append("\n").append(ss).append(ss).append("AutoBean<").append(t).append("Container").append("> ").append(getAutoBeanName(param)).append(" = typeFactory.").append(fname);
						sb.append("\n").append(ss).append(ss).append(getAutoBeanName(param)).append(".as().setValue(").append(param.getName()).append(");");						
						
					} else if (type.startsWith("java.util.List")) {
						
						// es una lista.
						String subType = getSubtype(type);
						if (subType.startsWith("java.lang.")) {
							
							// tipo primario.
							String t = subType.replace("java.lang.","");
							String flname = "get" + t + "ListContainer();";

							/*
							// hago el wrapping de cada elemento de la lista en un autobean.
							sb.append("\n").append(ss).append(ss).append("List<").append(t).append("> ").append(listName).append(" = new ArrayList<>();");
							sb.append("\n").append(ss).append(ss).append("for (").append(t).append(" e : ").append(param.getName()).append(") {");
							sb.append("\n").append(ss).append(ss).append(ss).append("AutoBean<").append(t).append("Container").append("> ").append("eAutoBean").append(" = typeFactory.").append(fname);
							sb.append("\n").append(ss).append(ss).append(ss).append("eAutoBean").append(".as().setValue(").append("e").append(");");
							sb.append("\n").append(ss).append(ss).append(ss).append(listName).append(".add(").append("eAutoBean.as()").append(");");
							sb.append("\n").append(ss).append(ss).append("}");
							*/

							// asigno la lista wrapeada al autobean de la lista.
							sb.append("\n").append(ss).append(ss).append("AutoBean<").append(t).append("ListContainer").append("> ").append(getAutoBeanName(param)).append(" = typeFactory.").append(flname);
							sb.append("\n").append(ss).append(ss).append(getAutoBeanName(param)).append(".as().setValue(").append(param.getName()).append(");");						
														
							
						} else {
							
							// objeto definido por el usuario
							
							String t = subType.substring(subType.lastIndexOf(".") + 1);
							String flname = "get" + t + "ListContainer();";
							
							// asigno la lista wrapeada al autobean de la lista.
							sb.append("\n").append(ss).append(ss).append("AutoBean<").append(t).append("ListContainer").append("> ").append(getAutoBeanName(param)).append(" = ").append(manager.factory.getName()).append(".").append(flname);
							sb.append("\n").append(ss).append(ss).append(getAutoBeanName(param)).append(".as().setValue(").append(param.getName()).append(");");									
							
						}
						
					} else {
						
						// busco en las factories.
						FactoryMethod fm = manager.getFactoryMethod(param);
						Factory f = manager.factory;
						sb.append("\n").append(ss).append(ss).append("AutoBean<").append(type).append("> ").append(getAutoBeanName(param)).append(" = ").append(f.getName()).append(".").append(fm.name).append("(").append(param.getName()).append(");");
						
					}
					
					sb.append("\n").append(ss).append(ss).append("String e").append(getAutoBeanName(param)).append(" = ").append("AutoBeanCodex.encode(").append(getAutoBeanName(param)).append(").getPayload();");
					sb.append("\n").append(ss).append(ss).append("params.add(e").append(getAutoBeanName(param)).append(");");
					sb.append("\n\n");
					
					
				}

				/*
				sb.append("\n").append(ss).append(ss).append("AutoBean<StringListContainer> slc = typeFactory.getStringListContainer();");
				sb.append("\n").append(ss).append(ss).append("slc.as().setValue(params);");
				sb.append("\n").append(ss).append(ss).append("String eslc = AutoBeanCodex.encode(slc).getPayload();");
				sb.append("\n\n");
				*/

				sb.append("\n").append(ss).append(ss).append("AutoBean<Message> msg = messageFactory.getMessage();");
				sb.append("\n").append(ss).append(ss).append("msg.as().setFunction(\"").append(method.getSignature()).append("\");");
				sb.append("\n").append(ss).append(ss).append("msg.as().setParams(params);");
				sb.append("\n").append(ss).append(ss).append("String emsg = AutoBeanCodex.encode(msg).getPayload();");
				sb.append("\n\n");
								
				
				/////////////////// realizo la llamada al servidor /////////////////////
				
				
				sb.append("\n").append(ss).append(ss).append("ws.open();");
				sb.append("\n").append(ss).append(ss).append("ws.send(emsg, new TransportReceiver() {");
				
				sb.append("\n").append(ss).append(ss).append(ss).append("@Override");
				sb.append("\n").append(ss).append(ss).append(ss).append("public void onSuccess(String msg) {");
				sb.append("\n").append(ss).append(ss).append(ss).append(ss).append(" // decodifico el mensaje");
				sb.append("\n").append(ss).append(ss).append(ss).append("};");

				sb.append("\n").append(ss).append(ss).append(ss).append("@Override");
				sb.append("\n").append(ss).append(ss).append(ss).append("public void onFailure(String error) {");
				sb.append("\n").append(ss).append(ss).append(ss).append(ss).append(" // envío el error");
				sb.append("\n").append(ss).append(ss).append(ss).append("};");

				sb.append("\n").append(ss).append(ss).append("});");

				
				/////////////////////// cierro el try inicial /////////////////////////////
				
				sb.append("\n\n");
				sb.append("\n").append(ss).append("} catch (Exception e) {");
				sb.append("\n").append(ss).append(ss).append("// nada por ahora.");
				sb.append("\n").append(ss).append("};");
				
				
				///////////////////////////////////////////////////////////////////////////
				
				sb.append("\n\n").append(ms).append("}");				// method }
				sb.append("\n\n");
			}
			
			sb.append("\n").append("}");		// class }
			
			
			try {
				JavaFileObject jfo = processingEnv.getFiler().createSourceFile(manager.getClientName());
				PrintWriter out = new PrintWriter(jfo.openWriter());
				out.println(sb.toString());
				out.flush();
				out.close();
				
			} catch (Exception e) {
				
			}
			
		}
	}
	
	
	private void generateListContainers(List<Manager> managers) {
		
		for (Manager manager : managers) {
			Factory factory = manager.factory;
			
			for (String lc : factory.listContainers) {
				
				String type = lc.substring(lc.lastIndexOf(".") + 1);
				
				StringBuilder sb = new StringBuilder();
				
				sb.append("package ").append(factory.getPackageName()).append(";");
				
				sb.append("\n\n");
				sb.append("\nimport ar.com.dcsys.gwt.manager.shared.TypeContainer;");
				sb.append("\nimport ").append(lc).append(";");
				sb.append("\nimport java.util.List;");
				
				sb.append("\n\n");

				sb.append("public interface ").append(type).append("ListContainer extends TypeContainer<List<").append(type).append(">> {");
				sb.append("\n").append("}");
				
				
				try {
					JavaFileObject jfo = processingEnv.getFiler().createSourceFile(factory.getPackageName() + "." + type + "ListContainer");
					PrintWriter out = new PrintWriter(jfo.openWriter());
					out.println(sb.toString());
					out.flush();
					out.close();
					
				} catch (Exception e) {
					
				}
			}
		}
		
	}
	
	private void generateFactories(List<Manager> managers) {

		StringBuilder sb = new StringBuilder();
		for (Manager manager : managers) {
			Factory factory = manager.factory;
			
			sb.append("package ").append(factory.getPackageName()).append(";");
			sb.append("\n\n");
			
			sb.append("\nimport com.google.web.bindery.autobean.shared.AutoBean;");
			sb.append("\nimport com.google.web.bindery.autobean.shared.AutoBeanFactory;");
			sb.append("\n\n");

			
			sb.append("public interface ").append(factory.getType()).append(" extends AutoBeanFactory {");
			
			////////////////// los factory methods ////////
			
			for (FactoryMethod fm : factory.methods) {
				sb.append("\n");
				sb.append("\n").append(ms).append("public AutoBean<").append(fm.param.getType()).append("> ").append(fm.name).append("();");
				sb.append("\n").append(ms).append("public AutoBean<").append(fm.param.getType()).append("> ").append(fm.name).append("(").append(fm.param.getType()).append(" ").append(fm.param.getName()).append(");");
			}
			sb.append("\n");

			///////////////// los list containers /////////
			
			for (String lc : factory.listContainers) {
				
				String type = lc.substring(lc.lastIndexOf(".") + 1) + "ListContainer";
				
				sb.append("\n");
				sb.append("\n").append(ms).append("public AutoBean<").append(type).append("> ").append("get").append(type).append("();");
				sb.append("\n").append(ms).append("public AutoBean<").append(type).append("> ").append("get").append(type).append("(").append(type).append(" ").append("l);");
				
			}
			sb.append("\n\n");
			
			sb.append("}");
			sb.append("\n\n");
			
			try {
				JavaFileObject jfo = processingEnv.getFiler().createSourceFile(factory.getPackageName() + "." + factory.getType());
				PrintWriter out = new PrintWriter(jfo.openWriter());
				out.println(sb.toString());
				out.flush();
				out.close();
				
			} catch (Exception e) {
				
			}
			
		}
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
			generateListContainers(managers);
			generateFactories(managers);
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
		
		/////////// controlo las precondiciones de los métodos antes de procesarlos //////////////
		//
		//  retorno = void y último parámetro = Receiver.
		//
		///////////////
		
		TypeMirror tm = ee.getReturnType();
		if (tm.getKind() != TypeKind.VOID) {
			return;
		}
		
		List<? extends VariableElement> params = ee.getParameters();
		if (params.size() <= 0) {
			return;
		}
		VariableElement rec = params.get(params.size() - 1);
		TypeMirror type = rec.asType();
		if (!type.toString().startsWith("ar.com.dcsys.gwt.manager.shared.Receiver")) {
			return;
		}
		
		/////////////////////////////////////////
		
		
		
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
			
			
			if (param.getType().startsWith("java.lang.")) {
				
				// tipo primitivo, no hace falta factory
				
				
			} else if (param.getType().startsWith("java.util.List")) {
				
				// es una lista.
				String subType = getSubtype(param.getType());
				if (!subType.startsWith("java.lang.")) {
					
					// no es un tipo primitivo asi que hay que generar los containers.
					if (!manager.factory.listContainers.contains(subType)) {
						manager.factory.listContainers.add(subType);
					}
					
				}
				
				
				
			} else {
				
				// tipo definido por el usuario. debe generarse mediante un factory del manager.
				FactoryMethod fm = new FactoryMethod();
				fm.param = param;
				fm.name = "get_" + param.getType().replace(".","_");
				manager.factory.methods.add(fm);
				
			}
			
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
