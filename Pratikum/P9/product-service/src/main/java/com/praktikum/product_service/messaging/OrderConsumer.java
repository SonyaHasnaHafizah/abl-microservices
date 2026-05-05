package com.praktikum.product_service.messaging;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

@Service
public class OrderConsumer {

    @RabbitListener(queues = "orderQueue")
    public void receiveOrder(String message) {
        System.out.println("Pesan diterima di product-service: " + message);
    }
}