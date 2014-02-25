package ar.com.dcsys.data;

import ar.com.dcsys.mail.MailData;

public class FakeMailConfig implements MailData {

	@Override
	public String[] administrativeAccount() {
		return new String[]{"pablo@econo.unlp.edu.ar"};
	}
	
	@Override
	public String[] assistanceAdministrativeAccount() {
		return new String[]{"pablo@econo.unlp.edu.ar"};
	}
	
	@Override
	public String from() {
		return "pablo@econo.unlp.edu.ar";
	}
	
	@Override
	public String server() {
		return "163.10.17.115";
	}
	
	@Override
	public String serverPassword() {
		return "dcsys";
	}
	
	@Override
	public String serverUser() {
		return "sysdc";
	}
	
	
	
}
