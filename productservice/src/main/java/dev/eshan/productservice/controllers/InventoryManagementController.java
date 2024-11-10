package dev.eshan.productservice.controllers;


import dev.eshan.productservice.dtos.GenericProductDto;
import dev.eshan.productservice.exceptions.NotFoundException;
import dev.eshan.productservice.services.interfaces.InventoryManagementService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/inventory")
@Slf4j
public class InventoryManagementController {
    private final InventoryManagementService inventoryManagementService;

    public InventoryManagementController(@Qualifier("inventoryManagementServiceImpl")
                                         InventoryManagementService inventoryManagementService) {
        this.inventoryManagementService = inventoryManagementService;
    }

    @GetMapping("/low-stock")
    public List<GenericProductDto> getLowStockProducts() {
        try {
            return inventoryManagementService.getLowStockProducts();
        } catch (Exception e) {
            log.error("Error occurred while fetching low stock products", e);
            return null;
        }
    }

    @PostMapping("/{id}/restock")
    public void restockProduct(@PathVariable("id") String productId, @RequestParam int restockAmount) throws NotFoundException {
        try {
            inventoryManagementService.restockProduct(productId, restockAmount);
        } catch (Exception e) {
            log.error("Error occurred while restocking product with id: {}", productId, e);
        }
    }
}
