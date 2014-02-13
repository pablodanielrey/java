package ar.com.dcsys.data.person;

public interface MailChange {

	public Mail getMail();
	public void setMail(Mail mail);
	
	public boolean isConfirmed();
	public void setConfirmed(boolean v);
	
	public String getToken();
	public void setToken(String token);
	
}
