package com.azizi.post.indexer.consumer;

import com.azizi.common.constants.RabbitMqQueueConstants;
import com.azizi.common.payload.PostPayload;
import com.azizi.post.indexer.service.ElasticPostService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class RabbitMqPostConsumer {

    private final ElasticPostService elasticPostService;

    @RabbitListener(queues = RabbitMqQueueConstants.POST_CREATED)
    public void consumeCreatedPost(PostPayload payload) {
        log.info("Post received to create -> post: {}", payload);
        elasticPostService.createPost(payload);
    }

    @RabbitListener(queues = RabbitMqQueueConstants.POST_UPDATED)
    public void consumeUpdatedPost(PostPayload payload) {
        log.info("Post received to update -> post: {}", payload);
        elasticPostService.updatePost(payload);
    }

    @RabbitListener(queues = RabbitMqQueueConstants.POST_DELETED)
    public void consumeDeletedPost(PostPayload payload) {
        log.info("Post received to delete -> post: {}", payload);
        elasticPostService.deletePost(payload);
    }


}