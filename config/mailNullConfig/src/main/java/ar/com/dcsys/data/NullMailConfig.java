package ar.com.dcsys.data;

import java.util.logging.Logger;

import ar.com.dcsys.mail.MailData;

public class NullMailConfig implements MailData {

	private static final Logger logger = Logger.getLogger(NullMailConfig.class.getName());
	
	public NullMailConfig() {
	}
	
	
	@Override
	public String[] administrativeAccount() {
		return null;
	}
	
	@Override
	public String[] assistanceAdministrativeAccount() {
		return null;
	}
	
	@Override
	public String from() {
		return null;
	}
	
	@Override
	public String server() {
		return null;
	}
	
	@Override
	public String serverPassword() {
		return null;
	}
	
	@Override
	public String serverUser() {
		return null;
	}
	
	
}
