package com.syn.usermanagement.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Properties;

@Component
@EnableScheduling
public class RabbitMQMonitoring {

//    private static final Logger log =
//            LoggerFactory.getLogger(RabbitMQMonitoring.class);
//
//    @Autowired
//    private RabbitAdmin rabbitAdmin;
//
//    @Scheduled(fixedRate = 60000)
//    public void monitorQueues() {
//
//        Properties queueInfo = rabbitAdmin.getQueueProperties("creation");
//
//        if (queueInfo == null) {
//            log.warn("Queue myqueue does not exist");
//            return;
//        }
//
//        Integer messageCount =
//                (Integer) queueInfo.get(RabbitAdmin.QUEUE_MESSAGE_COUNT);
//
//        Integer consumerCount =
//                (Integer) queueInfo.get(RabbitAdmin.QUEUE_CONSUMER_COUNT);
//
//        if (messageCount != null && messageCount > 10_000) {
//            log.warn("Queue depth high: {}", messageCount);
//        }
//
//        if (consumerCount != null && consumerCount == 0) {
//            log.error("No consumers on queue myqueue");
//        }
//    }
}
