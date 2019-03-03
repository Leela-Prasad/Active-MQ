package com.springboot.activemq;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;

public class TextMessageListener implements MessageListener{

	@Override
	public void onMessage(Message message) {
		try {
			System.out.println("Processing Message :" + ((TextMessage)message).getText());
		} catch (JMSException e) {
			e.printStackTrace();
		}
	}
	
}
