package com.spring.config;


import com.spring.constants.constant;
import org.springframework.amqp.core.*;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitConfig {

    @Bean("HelloQueue")
    public Queue queue() {
        return QueueBuilder.durable("Hello").build();
    }

    @Bean("userQueue")
    public Queue userQueue() {
        return QueueBuilder.durable(constant.USER_QUEUE_NAME).build();
    }

    @Bean("userExchange")
    public FanoutExchange userExchange() {
        return ExchangeBuilder.fanoutExchange(constant.USER_EXCHANGE_NAME).build();
    }

    @Bean
    public Binding userBinding(@Qualifier("userQueue") Queue userQueue, @Qualifier("userExchange")FanoutExchange userExchange) {
        return BindingBuilder.bind(userQueue).to(userExchange);
    }
}
