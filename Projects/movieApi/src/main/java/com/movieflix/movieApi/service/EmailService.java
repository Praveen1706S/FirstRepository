package com.movieflix.movieApi.service;

import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import com.movieflix.movieApi.dto.MailBody;

@Service
public class EmailService {
	
	private final JavaMailSender javaMailSender;

	public EmailService(JavaMailSender javaMailSender) {
		super();
		this.javaMailSender = javaMailSender;
	}
	
	
	public void sendSimpleMessage(MailBody mailbody) {
		
		SimpleMailMessage message = new SimpleMailMessage();
		
		message.setTo(mailbody.to());
		message.setFrom("");
		message.setSubject(mailbody.subject());
		message.setText(mailbody.text());
		
		javaMailSender.send(message);
	}

	
}
