package dev.eshan.productservice.eventlisteners;

import dev.eshan.productservice.events.EventName;
import dev.eshan.productservice.events.SupplierEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class SupplierEventListener {
    @EventListener
    public void printNotification(SupplierEvent supplierEvent) {
        if (EventName.SUPPLIER_RESTOCK_REQUEST.equals(supplierEvent.getEventName())) {
            int restockAmount = supplierEvent.getProduct().getLowStockThreshold() - supplierEvent.getProduct().getStockLevel();
            String subject = "Restock Notification: " + supplierEvent.getProduct().getTitle();
            String message = "Dear " + supplierEvent.getSupplier().getName() + ",\n\n" +
                    "The product '" + supplierEvent.getProduct().getTitle() + "' is running low in stock. " +
                    "We would like to request a restock of " + restockAmount + " units.\n\n" +
                    "Thank you,\nInventory Team";
            log.info("Send email to supplier: " + supplierEvent.getSupplier().getContactEmail() + " with subject: " + subject + " and message: " + message);
            // TODO: Send email to supplier via email service
        }
    }
}
