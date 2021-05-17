package com.azizi.post.indexer.config;

import com.azizi.common.constants.RabbitMqExchangeConstants;
import com.azizi.common.constants.RabbitMqQueueConstants;
import com.azizi.common.constants.RabbitMqRoutingKeyConstants;
import com.azizi.common.property.RabbitMqProperties;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class RabbitMqConfig {

    private final RabbitMqProperties rabbitMqProperties;

    @Bean
    DirectExchange directExchange() {
        return new DirectExchange(RabbitMqExchangeConstants.POST_EVENTS);
    }

    @Bean
    public Queue postCreationQueue() {
        return new Queue(RabbitMqQueueConstants.POST_CREATED, true);
    }

    @Bean
    public Queue postUpdateQueue() {
        return new Queue(RabbitMqQueueConstants.POST_UPDATED, true);
    }

    @Bean
    public Queue postDeletionQueue() {
        return new Queue(RabbitMqQueueConstants.POST_DELETED, true);
    }

    @Bean
    public Binding postCreationBinding() {
        return BindingBuilder.bind(postCreationQueue())
                .to(directExchange())
                .with(RabbitMqRoutingKeyConstants.POST_CREATED);
    }

    @Bean
    public Binding postUpdateBinding() {
        return BindingBuilder.bind(postUpdateQueue())
                .to(directExchange())
                .with(RabbitMqRoutingKeyConstants.POST_UPDATED);
    }

    @Bean
    public Binding postDeletionBinding() {
        return BindingBuilder.bind(postDeletionQueue())
                .to(directExchange())
                .with(RabbitMqRoutingKeyConstants.POST_DELETED);
    }

    @Bean
    public ConnectionFactory connectionFactory() {
        CachingConnectionFactory cachingConnectionFactory = new CachingConnectionFactory();
        cachingConnectionFactory.setUsername(rabbitMqProperties.getUsername());
        cachingConnectionFactory.setPassword(rabbitMqProperties.getPassword());
        cachingConnectionFactory.setHost(rabbitMqProperties.getHost());
        cachingConnectionFactory.setPort(rabbitMqProperties.getPort());
        return cachingConnectionFactory;
    }

    @Bean
    public Jackson2JsonMessageConverter messageConverter() {
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        return new Jackson2JsonMessageConverter(mapper);
    }

    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory) {
        RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
        rabbitTemplate.setMessageConverter(messageConverter());
        return rabbitTemplate;
    }

}
