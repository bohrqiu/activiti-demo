package com.yiji.ac;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class MailSender {
	private static Logger logger = LoggerFactory.getLogger(MailSender.class);

	public void send(){
		logger.info("send email to {}");

	}
}
