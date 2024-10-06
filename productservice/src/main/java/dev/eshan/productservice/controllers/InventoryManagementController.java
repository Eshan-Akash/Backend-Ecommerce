package dev.eshan.productservice.controllers;


import dev.eshan.productservice.dtos.GenericProductDto;
import dev.eshan.productservice.exceptions.NotFoundException;
import dev.eshan.productservice.services.interfaces.InventoryManagementService;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/inventory")
public class InventoryManagementController {
    private final InventoryManagementService inventoryManagementService;

    public InventoryManagementController(@Qualifier("inventoryManagementServiceImpl")
                                         InventoryManagementService inventoryManagementService) {
        this.inventoryManagementService = inventoryManagementService;
    }

    @GetMapping("/low-stock")
    public List<GenericProductDto> getLowStockProducts() {
        return inventoryManagementService.getLowStockProducts();
    }

    @PostMapping("/{id}/restock")
    public void restockProduct(@PathVariable("id") String productId, @RequestParam int restockAmount) throws NotFoundException {
        inventoryManagementService.restockProduct(productId, restockAmount);
    }
}
