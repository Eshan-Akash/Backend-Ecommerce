package dev.eshan.productservice.controllers;

import dev.eshan.productservice.dtos.GenericSupplierDto;
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
                restockRequest.getSupplierId(),
                restockRequest.getProductId(),
                restockRequest.getRestockAmount()
        );
    }

    @PostMapping
    public GenericSupplierDto createSupplier(@RequestBody GenericSupplierDto supplier) {
        return supplierIntegrationService.createSupplier(supplier);
    }

    @PutMapping("/{id}")
    public Supplier updateSupplier(@PathVariable String id, @RequestBody GenericSupplierDto supplierDto) {
        return supplierIntegrationService.updateSupplier(id, supplierDto);
    }

    @DeleteMapping("/{id}")
    public void deleteSupplier(@PathVariable String id) {
        supplierIntegrationService.deleteSupplier(id);
    }
}
