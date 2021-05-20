package com.akg.service;

import javax.mail.MessagingException;

public interface EmailService {

	boolean send(String to) throws MessagingException;

}
