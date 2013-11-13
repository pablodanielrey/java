package ar.com.dcsys.mail;

public interface MailData {

	public String server();
	public String serverUser();
	public String serverPassword();
	public String from();
	
	public String[] administrativeAccount();
	public String[] assistanceAdministrativeAccount();
	
}
