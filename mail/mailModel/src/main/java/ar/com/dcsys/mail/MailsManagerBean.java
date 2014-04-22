package ar.com.dcsys.mail;

import java.util.Date;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.inject.Inject;
import javax.mail.Authenticator;
import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMessage.RecipientType;
import javax.mail.internet.MimeMultipart;

import ar.com.dcsys.config.Config;

public class MailsManagerBean implements MailsManager {

	
	private static final Logger logger = Logger.getLogger(MailsManagerBean.class.getName());
	
	@Inject @Config String user;
	@Inject @Config String password;
	@Inject @Config String server;
	@Inject @Config String auth;			// true o false
	
	
	private Session getSession() {

		Properties env = new Properties();
		env.put("mail.smtp.host", server);
		env.put("mail.debug",true);
		
		if (auth == null) {
			if (user != null && password != null) {
				env.put("mail.smtp.auth",true);
			} else {
				env.put("mail.smtp.auth",false);
			}
		} else {
			env.put("mail.smtp.auth",auth);
		}
	
		return Session.getInstance(env,new Authenticator() {
			@Override
			protected PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication(user,password);
			}
		});
	}	
	
	
	@Override
	public void sendMail(String from, String to, String subject, String content) throws MailException {
	
		if (subject == null || from == null || to == null || content == null) {
			throw new MailException("subject == null || from == null || to == null || content == null");
		}
		 
		try {
			
			Session mailSession = getSession();
			
			InternetAddress aFrom = new InternetAddress(from);
			InternetAddress aTo = new InternetAddress(to);
			
			BodyPart bp = new MimeBodyPart();
			bp.setText(content);
			
			MimeMultipart mm = new MimeMultipart();
			mm.addBodyPart(bp);
			
			Message message = new MimeMessage(mailSession);
			message.setSubject(subject);
			message.setSentDate(new Date());
			message.setFrom(aFrom);
			message.setRecipient(RecipientType.TO, aTo);
			message.setContent(mm);
			
			Transport.send(message);
			
		 } catch (MessagingException e) {
			 logger.log(Level.SEVERE,"Error enviando\nfrom: " + from + " subject :  " + subject + " to : " + to + " causa : " + e.getMessage());
			 throw new MailException(e); 
		 }
	}
	
}
