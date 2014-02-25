package ar.com.dcsys.mail;

public interface MailsManager {

	public void sendMail(String from, String to, String subject, String content) throws MailException;
	
}
