package com.springboot.activemq;

import java.util.Random;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.jms.core.JmsTemplate;

/**
 * priority is not working in Active MQ
 * @author Leela
 */
@SpringBootApplication
public class MessageSendApplication {

	public static void main(String[] args) {
		ApplicationContext context = SpringApplication.run(MessageSendApplication.class, args);
		JmsTemplate jmsTemplate = context.getBean(JmsTemplate.class);

		//one way to set all Qos Properties like persistence, priority, expiry of the message that we are sending
		//jmsTemplate.setQosSettings(new QosSettings(DeliveryMode.NON_PERSISTENT, 5, 1000));
		
		//**This property setExplicitQos is not needed when we are using QosSettings() constructor,
		//but this should be used when we are using setter methods to set these settins.
		jmsTemplate.setExplicitQosEnabled(true);
		jmsTemplate.setDeliveryPersistent(false);
		jmsTemplate.setTimeToLive(0);
		//jmsTemplate.setTimeToLive(20000);
		Random random = new Random();
		for(int i=0;i<20;++i) {
			int priority = random.nextInt(10);
			jmsTemplate.setPriority(priority);
			jmsTemplate.convertAndSend("sampleQueue", "Message From Application with priority : " + priority);
		}
	}

}
