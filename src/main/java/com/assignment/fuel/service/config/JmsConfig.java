package com.assignment.fuel.service.config;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.activemq.broker.BrokerService;
import org.apache.activemq.broker.TransportConnector;
import org.apache.activemq.command.ActiveMQQueue;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.support.converter.MappingJackson2MessageConverter;
import org.springframework.jms.support.converter.MessageConverter;
import org.springframework.jms.support.converter.MessageType;

import javax.jms.ConnectionFactory;
import javax.jms.Queue;
import java.net.URI;


@Configuration
public class JmsConfig {

    @Value("${spring.active-mq.broker-url}")
    private String brokerUrl;
    @Value("${fuel.service.queue}")
    private String queueName;

    /**
     * Bean for configuring connection Factory properties
     *
     * @return connectionFactory
     */
    @Bean
    public ConnectionFactory connectionFactory() {
        ActiveMQConnectionFactory activeMQConnectionFactory = new ActiveMQConnectionFactory();
        activeMQConnectionFactory.setBrokerURL(brokerUrl);
        return activeMQConnectionFactory;
    }

    /**
     * Bean for configuring Jms Template
     *
     * @return JmsTemplate
     */
    @Bean
    public JmsTemplate jmsTemplate() {
        JmsTemplate jmsTemplate = new JmsTemplate();
        jmsTemplate.setConnectionFactory(connectionFactory());
        jmsTemplate.setMessageConverter(jacksonJmsMessageConverter());
        return jmsTemplate;
    }

    /**
     * Bean for configuring Broker service
     * @return broker service
     * @throws Exception exception
     */
    @Bean
    public BrokerService createBrokerService() throws Exception {
        BrokerService broker = new BrokerService();

        TransportConnector connector = new TransportConnector();
        connector.setUri(new URI("tcp://localhost:61617"));
        broker.addConnector(connector);
        return broker;
    }

    /**
     * Message Converter that uses jackson to convert messages to and from JSON
     * @return message converter
     */
    @Bean
    public MessageConverter jacksonJmsMessageConverter() {
        MappingJackson2MessageConverter converter = new MappingJackson2MessageConverter();
        converter.setTargetType(MessageType.TEXT);
        return converter;
    }

    /**
     * This method creates new queue - Fuel Details.
     * @return queue
     */
    public Queue queue() {
        return new ActiveMQQueue(queueName);
    }
}
