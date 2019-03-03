package com.springboot.activemq;

import javax.jms.Connection;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.Session;
import javax.jms.TextMessage;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.support.destination.DestinationResolver;
import org.springframework.jms.support.destination.DynamicDestinationResolver;

@SpringBootApplication
public class MessageReceiveApplication {

	public static void main(String[] args) throws JMSException {
		ApplicationContext context = SpringApplication.run(MessageReceiveApplication.class, args);
		
		JmsTemplate jmsTemplate = context.getBean(JmsTemplate.class);
		
		DestinationResolver destinationResolver = new DynamicDestinationResolver();
		Connection connection = jmsTemplate.getConnectionFactory().createConnection();
		Session session = connection.createSession(false, Session.CLIENT_ACKNOWLEDGE);
		Destination destination = destinationResolver.resolveDestinationName(session, "sampleQueue", false);
		
		MessageConsumer consumer = session.createConsumer(destination);
		
		connection.start();
		
		TextMessage message = (TextMessage)consumer.receive(1000);
		if(message!=null) {
			System.out.println(message.getText());
			//If below line message.acknowledge is not present then only message will read
			//but it will NOT be deleted from queue since we set acknowledge as CLIENT_ACKNOWLEDGE
			//and the acknowledge is manual in this mode. 
			message.acknowledge();
		}else {
			System.out.println("No Message");
		}
	}

}
