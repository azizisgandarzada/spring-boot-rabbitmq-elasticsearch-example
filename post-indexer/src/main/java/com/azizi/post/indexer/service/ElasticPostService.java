package com.azizi.post.indexer.service;

import com.azizi.common.payload.PostPayload;
import com.azizi.post.indexer.document.ElasticPost;
import com.azizi.post.indexer.mapper.ElasticPostMapper;
import com.azizi.post.indexer.repository.ElasticPostRepository;
import java.util.concurrent.RejectedExecutionException;
import java.util.concurrent.TimeUnit;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Lazy))
@Slf4j
public class ElasticPostService {

    private static final Integer RETRY_LIMIT = 5;

    private final ElasticPostRepository elasticPostRepository;
    private final ElasticPostMapper elasticPostMapper;
    private final AsyncService asyncService;

    public void createPost(PostPayload payload) {
        try {
            asyncService.createPost(payload);
        } catch (RejectedExecutionException ex) {
            waitAndCreateAgainAsync(payload, 5, 1, TimeUnit.SECONDS);
        }
    }

    public void updatePost(PostPayload payload) {
        try {
            asyncService.updatePost(payload);
        } catch (RejectedExecutionException ex) {
            waitAndUpdateAgainAsync(payload, 5, 1, TimeUnit.SECONDS);
        }
    }

    public void deletePost(PostPayload payload) {
        try {
            asyncService.deletePost(payload);
        } catch (RejectedExecutionException ex) {
            waitAndDeleteAgainAsync(payload, 5, 1, TimeUnit.SECONDS);
        }
    }

    public void createPostAsync(PostPayload payload) {
        log.info("Post creating -> post: {}", payload);
        ElasticPost elasticPost = elasticPostMapper.toDocument(payload);
        elasticPostRepository.save(elasticPost);
        log.info("Post created -> post: {}", payload);
    }

    public void updatePostAsync(PostPayload payload) {
        log.info("Post updating -> post: {}", payload);
        elasticPostRepository.findById(payload.getId()).ifPresent(elasticPost -> {
            elasticPostRepository.save(elasticPostMapper.toDocument(payload, elasticPost));
            log.info("Post updated -> post: {}", payload);
        });
    }

    public void deletePostAsync(PostPayload payload) {
        log.info("Post deleting -> post: {}", payload);
        elasticPostRepository.findById(payload.getId()).ifPresent(elasticPostRepository::delete);
        log.info("Post deleted -> post: {}", payload);
    }

    private void waitAndCreateAgainAsync(PostPayload payload, int time, int retryCount, TimeUnit timeUnit) {
        if (retryCount > RETRY_LIMIT) {
            log.error("Post can not be created -> payload: {}, retryCount: {}, time: {}, timeUnit: {}", payload,
                    retryCount, time, timeUnit);
            return;
        }
        try {
            timeUnit.sleep(time);
        } catch (InterruptedException ex) {
            Thread.currentThread().interrupt();
        }
        try {
            asyncService.createPost(payload);
        } catch (RejectedExecutionException ex) {
            retryCount++;
            waitAndCreateAgainAsync(payload, time, retryCount, timeUnit);
        }
    }

    private void waitAndUpdateAgainAsync(PostPayload payload, int time, int retryCount, TimeUnit timeUnit) {
        if (retryCount > RETRY_LIMIT) {
            log.error("Post can not be updated -> payload: {}, retryCount: {}, time: {}, timeUnit: {}", payload,
                    retryCount, time, timeUnit);
            return;
        }
        try {
            timeUnit.sleep(time);
        } catch (InterruptedException ex) {
            Thread.currentThread().interrupt();
        }
        try {
            asyncService.updatePost(payload);
        } catch (RejectedExecutionException ex) {
            retryCount++;
            waitAndUpdateAgainAsync(payload, time, retryCount, timeUnit);
        }
    }

    private void waitAndDeleteAgainAsync(PostPayload payload, int time, int retryCount, TimeUnit timeUnit) {
        if (retryCount > RETRY_LIMIT) {
            log.error("Post can not be deleted -> payload: {}, retryCount: {}, time: {}, timeUnit: {}", payload,
                    retryCount, time, timeUnit);
            return;
        }
        try {
            timeUnit.sleep(time);
        } catch (InterruptedException ex) {
            Thread.currentThread().interrupt();
        }
        try {
            asyncService.deletePost(payload);
        } catch (RejectedExecutionException ex) {
            retryCount++;
            waitAndDeleteAgainAsync(payload, time, retryCount, timeUnit);
        }
    }

}
