package com.azizi.post.exception;

import com.azizi.common.exception.NotFoundException;

public class PostNotFoundException extends NotFoundException {

    private static final String MESSAGE = "Post not found!";

    public PostNotFoundException() {
        super(MESSAGE);
    }

}
