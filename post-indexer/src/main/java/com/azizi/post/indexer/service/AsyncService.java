package com.azizi.post.indexer.service;

import com.azizi.common.payload.PostPayload;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AsyncService {

    private final ElasticPostService elasticPostService;

    @Async("postCreationExecutor")
    public void createPost(PostPayload payload) {
        elasticPostService.createPostAsync(payload);
    }

    @Async("postUpdateExecutor")
    public void updatePost(PostPayload payload) {
        elasticPostService.updatePostAsync(payload);
    }

    @Async("postDeletionExecutor")
    public void deletePost(PostPayload payload) {
        elasticPostService.deletePostAsync(payload);
    }

}
