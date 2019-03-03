package com.springboot.activemq;

import javax.jms.Connection;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageProducer;
import javax.jms.Session;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.support.destination.DestinationResolver;
import org.springframework.jms.support.destination.DynamicDestinationResolver;

@SpringBootApplication
public class MessageSendApplication {

	public static void main(String[] args) throws JMSException {
		ApplicationContext context = SpringApplication.run(MessageSendApplication.class, args);
		JmsTemplate jmsTemplate = context.getBean(JmsTemplate.class);
		
		Connection connection = null;
		try {
			DestinationResolver destinationResolver = new DynamicDestinationResolver();
			connection = jmsTemplate.getConnectionFactory().createConnection();
			// In Sender code there is no effect Acknowledge type this is just to be 
			// complient with JMS Specification.
			// Here the boolean flag "true" will send all messages in a single transaction
			Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
			Destination destination = destinationResolver.resolveDestinationName(session, "sampleTopic", true);
			MessageProducer producer = session.createProducer(destination);
			
			for(int i=0;i<20;++i) {
				Message message = session.createTextMessage("Sample Message from Topic!");
				producer.send(message);
			}
		}finally {
			if(null!= connection)
				connection.close();
		}
	}

}
