package ar.com.dcsys.data.module;

public class ModuleBean implements Module {

	private final String id;
	
	public ModuleBean(String id) {
		this.id = id;
	}
	
	@Override
	public String getId() {
		return id;
	}
	
}
