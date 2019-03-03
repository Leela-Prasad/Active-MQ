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

/**
 * This Sender code will post 20 messages in a single shot 
 * using transaction.
 * @author Leela
 */
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
			Session session = connection.createSession(true, Session.AUTO_ACKNOWLEDGE);
			Destination destination = destinationResolver.resolveDestinationName(session, "sampleQueue", false);
			MessageProducer producer = session.createProducer(destination);
			
			for(int i=0;i<20;++i) {
				Message message = session.createTextMessage("Sample Message!");
				producer.send(message);
			}
			
			// This Try block is important even though you enabled the boolean flag for transaction mode
			// It will not post all the messages in a single transaction until you explicitly commit the 
			// session, if there is any exception while executing this transaction we can very well rollback
			// these messages.
			try {
				session.commit();
			}catch(Exception e) {
				session.rollback();
				System.out.println(e);
			}
		}finally {
			if(null!= connection)
				connection.close();
		}
	}

}
