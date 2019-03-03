package com.springboot.activemq;

import java.util.ArrayList;
import java.util.List;

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
		Connection connection = null;
		try {
			connection = jmsTemplate.getConnectionFactory().createConnection();
			Session session = connection.createSession(true, Session.SESSION_TRANSACTED);
			Destination destination = destinationResolver.resolveDestinationName(session, "sampleQueue", false);
			
			MessageConsumer consumer = session.createConsumer(destination);
			
			connection.start();
			
			List<TextMessage> messages = new ArrayList<>();
			while(messages.size()<15) {
				TextMessage message = (TextMessage)consumer.receive(1000);
				if(message!=null)
					messages.add(message);
			}
			
			for(TextMessage message : messages) {
				System.out.println(message.getText());
			}
			
			session.commit();
		}finally {
			if(connection!=null)
				connection.close();
		}
	}

}
