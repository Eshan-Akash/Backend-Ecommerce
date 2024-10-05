package dev.eshan.productservice.events;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.UUID;

@FieldDefaults(level = AccessLevel.PRIVATE)
@Getter
@ToString
@Setter
@EqualsAndHashCode
public class BaseEvent {
    String eventId = UUID.randomUUID().toString();
    long eventTimestamp = System.currentTimeMillis();
    EventName eventName;
}
