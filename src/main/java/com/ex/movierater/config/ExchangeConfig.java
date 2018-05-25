package com.ex.movierater.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@EnableRabbit
@Configuration
public class ExchangeConfig {

    public static final String topicExchangeName = "reviewexchange";

    public static final String topicExchangeName2 = "verifiedexchange";

    public static final String queueName = "reviews";
    static final String queueName2 = "verified";

    @Bean
    Queue queue() {
        return new Queue(queueName, false);
    }

    @Bean
    Queue queue2() {
        return new Queue(queueName2, false);
    }

    @Bean
    TopicExchange exchange() {
        return new TopicExchange(topicExchangeName);
    }

    @Bean
    TopicExchange exchange2() {
        return new TopicExchange(topicExchangeName2);
    }

    @Bean
    Binding binding(Queue queue, TopicExchange exchange) {
        return BindingBuilder.bind(queue()).to(exchange()).with("reviews");
    }

    @Bean
    Binding binding2(Queue queue2, TopicExchange exchange2) {
        return BindingBuilder.bind(queue2()).to(exchange2()).with("verified");
    }

}
