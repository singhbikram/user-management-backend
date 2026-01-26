package com.syn.usermanagement.service;

import org.springframework.stereotype.Service;

@Service
public class MessageProducer {

//    private final RabbitTemplate rabbitTemplate;
//
//    public MessageProducer(RabbitTemplate rabbitTemplate) {
//        this.rabbitTemplate = rabbitTemplate;
//    }
//
//    public void sendMessage(String message, String routingKey) {
//        rabbitTemplate.convertAndSend(
//                RabbitMQConfig.EXCHANGE_NAME,
//                routingKey,
//                message
//        );
//        System.out.println("Message sent: " + message + " with key: " + routingKey);
//    }
//
//    // Send object with routing key
//    public void sendObject(Object object, String routingKey) {
//        rabbitTemplate.convertAndSend(
//                RabbitMQConfig.EXCHANGE_NAME,
//                routingKey,
//                object
//        );
//    }
}
