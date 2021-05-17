package com.azizi.common.constants;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class RabbitMqQueueConstants {

    public static final String POST_CREATED = "PostCreatedQueue";
    public static final String POST_UPDATED = "PostUpdatedQueue";
    public static final String POST_DELETED = "PostDeletedQueue";

}
