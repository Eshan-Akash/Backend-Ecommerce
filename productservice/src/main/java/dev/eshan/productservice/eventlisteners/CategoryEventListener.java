package dev.eshan.productservice.eventlisteners;

import dev.eshan.productservice.events.CategoryEvent;
import dev.eshan.productservice.events.ProductEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class CategoryEventListener {
    @EventListener
    public void printNotification(CategoryEvent categoryEvent) {
        log.info("Category Event: " + categoryEvent.getCategory().getName() + " has been created");
    }
}
