package com.azizi.common.constants;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class RabbitMqRoutingKeyConstants {

    public static final String POST_CREATED = "PostCreatedEvent";
    public static final String POST_UPDATED = "PostUpdatedEvent";
    public static final String POST_DELETED = "PostDeletedEvent";

}
