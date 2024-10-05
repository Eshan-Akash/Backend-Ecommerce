package dev.eshan.productservice.controllers;


import dev.eshan.productservice.dtos.GenericProductDto;
import dev.eshan.productservice.services.interfaces.InventoryManagementService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/inventory")
public class InventoryManagementController {
    private final InventoryManagementService inventoryManagementService;

    public InventoryManagementController(InventoryManagementService inventoryManagementService) {
        this.inventoryManagementService = inventoryManagementService;
    }

    @GetMapping("/low-stock")
    public List<GenericProductDto> getLowStockProducts() {
        // Get all products with low stock
        return inventoryManagementService.getLowStockProducts();
    }

    @PostMapping("/{id}/restock")
    public void restockProduct(@PathVariable("id") String productId, @RequestParam int restockAmount) {
        inventoryManagementService.restockProduct(productId, restockAmount);
    }
}
