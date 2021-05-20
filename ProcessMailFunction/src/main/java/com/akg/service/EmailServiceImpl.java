package com.akg.service;

import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class EmailServiceImpl implements EmailService {

	private String mail_host;
	private String mail_port;
	private String mail_username;
	private String mail_password;
	private String mail_transport_protocol;
	private String mail_smtp_auth;
	private String mail_smtp_starttls_enable;
	private String mail_debug;

	public EmailServiceImpl(String mail_host, String mail_port, String mail_username, String mail_password,
			String mail_transport_protocol, String mail_smtp_auth, String mail_smtp_starttls_enable,
			String mail_debug) {
		super();
		this.mail_host = mail_host;
		this.mail_port = mail_port;
		this.mail_username = mail_username;
		this.mail_password = mail_password;
		this.mail_transport_protocol = mail_transport_protocol;
		this.mail_smtp_auth = mail_smtp_auth;
		this.mail_smtp_starttls_enable = mail_smtp_starttls_enable;
		this.mail_debug = mail_debug;
	}

	public boolean send2(String to) throws MessagingException {
		boolean flag = false;
		// Sender's email ID needs to be mentioned
		final String username = this.mail_username;
		final String password = this.mail_password;
		String from = this.mail_username;

		System.out.println("U: " + username);
		System.out.println("P: " + password);
		Properties props = new Properties();
		props.put("mail.smtp.auth", this.mail_smtp_auth);
		props.put("mail.smtp.starttls.enable", this.mail_smtp_starttls_enable);
		props.put("mail.smtp.host", this.mail_host);
		props.put("mail.smtp.port", this.mail_port);

		// Get the Session object.
		Session session = Session.getInstance(props, new javax.mail.Authenticator() {
			protected PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication(username, password);
			}
		});

		try {
			// Create a default MimeMessage object.
			Message message = new MimeMessage(session);

			// Set From: header field of the header.
			message.setFrom(new InternetAddress(from));

			// Set To: header field of the header.
			message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(to));

			// Set Subject: header field
			message.setSubject("Testing AWS Lambda " + Math.random());

			// Now set the actual message
			message.setText("Hello, This Email is for testing Lambda");

			// Send message
			Transport.send(message);
			flag = true;

			System.out.println("Sent message successfully....");

		} catch (MessagingException e) {
			System.out.println(e);
			flag = false;
		}
		return flag;
	}

	@Override
	public boolean send(String to) {
		System.out.println("Sending Message");
		System.out.println("Sent message successfully...."+to);
		return true;

	}
}
