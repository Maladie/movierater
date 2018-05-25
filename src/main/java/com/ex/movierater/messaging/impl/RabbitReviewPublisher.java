package com.ex.movierater.messaging.impl;

import com.ex.movierater.config.ExchangeConfig;
import com.ex.movierater.messaging.ReviewPublisher;
import com.ex.movierater.model.ReviewDto;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class RabbitReviewPublisher implements ReviewPublisher {

    private final RabbitTemplate rabbitTemplate;

    private ObjectMapper mapper;

    protected final Logger log = org.slf4j.LoggerFactory.getLogger(getClass());

    @Autowired
    public RabbitReviewPublisher(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
        mapper = new ObjectMapper();
    }

    @Override
    public void publish(ReviewDto reviewDto) {
        try {
            String reviewJson = mapper.writeValueAsString(reviewDto);
            rabbitTemplate.convertAndSend(ExchangeConfig.topicExchangeName, ExchangeConfig.queueName, reviewJson);
            log.info("Sending reviewDto", reviewJson);
        } catch (JsonProcessingException e) {
            log.error("Error mapping reviewDto", reviewDto, e);
        }

    }
}
