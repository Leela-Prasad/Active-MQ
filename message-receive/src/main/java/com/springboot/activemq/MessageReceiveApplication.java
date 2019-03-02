package com.springboot.activemq;

import java.util.Enumeration;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.QueueBrowser;
import javax.jms.Session;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.jms.core.BrowserCallback;
import org.springframework.jms.core.JmsTemplate;

@SpringBootApplication
public class MessageReceiveApplication {

	public static void main(String[] args) {
		ApplicationContext context = SpringApplication.run(MessageReceiveApplication.class, args);
		/*JmsTemplate jmsTemplate = context.getBean(JmsTemplate.class);
		System.out.println(jmsTemplate.getReceiveTimeout());
		jmsTemplate.setReceiveTimeout(1000);
		System.out.println(jmsTemplate.receiveAndConvert("sampleQueue"));*/
		
		JmsTemplate jmsTemplate = context.getBean(JmsTemplate.class);
		int messageCount = jmsTemplate.browse("positionQueue", new BrowserCallback<Integer>() {
			@Override
			public Integer doInJms(Session session, QueueBrowser browser) throws JMSException {
				Enumeration<Message> messages = browser.getEnumeration();
				int count=0;
				while(messages.hasMoreElements()) {
					System.out.println(messages.nextElement());
					++count;
				}
				return count;
			}
			
		});
		System.out.println("Message Count :" + messageCount);
	}

}
