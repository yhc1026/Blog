package com.spring.Listener;


import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
public class HelloQueueListener {

    @RabbitListener(queues = "Hello")
    public void handler(){

    }

}
