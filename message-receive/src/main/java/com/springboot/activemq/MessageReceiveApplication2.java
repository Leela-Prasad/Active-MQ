package com.springboot.activemq;

import java.util.Scanner;

import javax.jms.Connection;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.MessageConsumer;
import javax.jms.Session;
import javax.jms.Topic;
import javax.jms.TopicSession;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.jms.connection.CachingConnectionFactory;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.support.destination.DestinationResolver;
import org.springframework.jms.support.destination.DynamicDestinationResolver;

@SpringBootApplication
public class MessageReceiveApplication2 {

	public static void main(String[] args) throws JMSException {
		ApplicationContext context = SpringApplication.run(MessageReceiveApplication2.class, args);
		
		JmsTemplate jmsTemplate = context.getBean(JmsTemplate.class);
		jmsTemplate.setConnectionFactory(new ActiveMQConnectionFactory());
		
		DestinationResolver destinationResolver = new DynamicDestinationResolver();
		Connection connection = null;
		try {
			connection = jmsTemplate.getConnectionFactory().createConnection();
			connection.setClientID("Listener2");
			Session session = connection.createSession(false, TopicSession.AUTO_ACKNOWLEDGE);
			Destination destination = destinationResolver.resolveDestinationName(session, "sampleTopic", true);
			
			MessageConsumer consumer = session.createDurableSubscriber((Topic)destination,"ReceiveApp");
			consumer.setMessageListener(new TextMessageListener());
			
			connection.start();
			
			boolean finished = false;
			while (!finished) {
				finished = lookForUserPressingX();
			}
			
		}finally {
			if(connection!=null)
				connection.close();
		}
	}
	
	private static boolean lookForUserPressingX() {
		Scanner scanner = new Scanner(System.in);
		return scanner.nextLine().toLowerCase().equals("x");
	}

}
