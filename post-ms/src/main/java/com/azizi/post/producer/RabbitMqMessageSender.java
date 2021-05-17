package com.azizi.post.producer;

import com.azizi.common.constants.RabbitMqExchangeConstants;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class RabbitMqMessageSender {

    private final RabbitTemplate rabbitTemplate;

    public void sendMessage(String routingKey, Object message) {
        rabbitTemplate.convertAndSend(RabbitMqExchangeConstants.POST_EVENTS, routingKey, message);
        log.info("Post added to queue -> post: {}", message);
    }

}
