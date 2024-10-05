package dev.eshan.productservice.controllers;

import dev.eshan.productservice.dtos.RestockRequestDto;
import dev.eshan.productservice.models.Supplier;
import dev.eshan.productservice.services.interfaces.SupplierIntegrationService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/suppliers")
public class SupplierIntegrationController {
    private final SupplierIntegrationService supplierIntegrationService;

    public SupplierIntegrationController(SupplierIntegrationService supplierIntegrationService) {
        this.supplierIntegrationService = supplierIntegrationService;
    }

    @PostMapping("/notify")
    public void notifySupplierForRestock(@RequestBody RestockRequestDto restockRequest) throws Exception {
        supplierIntegrationService.notifySupplierForRestock(
                restockRequest.getSupplier(),
                restockRequest.getProduct(),
                restockRequest.getRestockAmount()
        );
    }

    @PostMapping
    public Supplier createSupplier(@RequestBody Supplier supplier) {
        // 1. Create a new supplier
        // 2. SUPPLIER_CREATED event is published
        return supplierIntegrationService.createSupplier(supplier);
    }

    @PutMapping("/{id}")
    public Supplier updateSupplier(@PathVariable String id, @RequestBody Supplier supplier) {
        // 1. Update the supplier
        // 2. SUPPLIER_UPDATED event is published
        return supplierIntegrationService.updateSupplier(id, supplier);
    }

    @DeleteMapping("/{id}")
    public void deleteSupplier(@PathVariable String id) {
        // 1. Delete the supplier
        // 2. SUPPLIER_DELETED event is published
        supplierIntegrationService.deleteSupplier(id);
    }
}
