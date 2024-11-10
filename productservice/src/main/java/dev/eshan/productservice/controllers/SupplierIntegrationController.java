package dev.eshan.productservice.controllers;

import dev.eshan.productservice.dtos.GenericSupplierDto;
import dev.eshan.productservice.dtos.RestockRequestDto;
import dev.eshan.productservice.models.Supplier;
import dev.eshan.productservice.services.interfaces.SupplierIntegrationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/api/v1/suppliers")
@Slf4j
public class SupplierIntegrationController {
    private final SupplierIntegrationService supplierIntegrationService;

    public SupplierIntegrationController(SupplierIntegrationService supplierIntegrationService) {
        this.supplierIntegrationService = supplierIntegrationService;
    }

    @PostMapping("/notify")
    public void notifySupplierForRestock(@RequestBody RestockRequestDto restockRequest) throws Exception {
        try {
            supplierIntegrationService.notifySupplierForRestock(
                    restockRequest.getSupplierId(),
                    restockRequest.getProductId(),
                    restockRequest.getRestockAmount()
            );
        } catch (Exception e) {
            log.error("Failed to notify supplier for restock", e);
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
                    "Failed to notify supplier for restock");
        }
    }

    @PostMapping
    public GenericSupplierDto createSupplier(@RequestBody GenericSupplierDto supplier) {
        try {
            return supplierIntegrationService.createSupplier(supplier);
        } catch (Exception e) {
            log.error("Failed to create supplier", e);
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Failed to create supplier");
        }
    }

    @PutMapping("/{id}")
    public Supplier updateSupplier(@PathVariable String id, @RequestBody GenericSupplierDto supplierDto) {
        try {
            return supplierIntegrationService.updateSupplier(id, supplierDto);
        } catch (Exception e) {
            log.error("Failed to update supplier", e);
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Failed to update supplier");
        }
    }

    @DeleteMapping("/{id}")
    public void deleteSupplier(@PathVariable String id) {
        try {
            supplierIntegrationService.deleteSupplier(id);
        } catch (Exception e) {
            log.error("Failed to delete supplier", e);
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Failed to delete supplier");
        }
    }
}
