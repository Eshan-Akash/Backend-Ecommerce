package dev.eshan.productservice.eventlisteners;

import dev.eshan.productservice.events.ProductEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class ProductEventListener {
    @EventListener
    public void printNotification(ProductEvent productEvent) {
        log.info("Event with name: {} with body: {}", productEvent.getEventName(), productEvent.getProduct());
    }
}
