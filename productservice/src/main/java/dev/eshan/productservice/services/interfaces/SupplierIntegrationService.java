package dev.eshan.productservice.services.interfaces;

import dev.eshan.productservice.dtos.GenericSupplierDto;
import dev.eshan.productservice.models.Product;
import dev.eshan.productservice.models.Supplier;
import org.springframework.web.bind.annotation.RequestBody;

public interface SupplierIntegrationService {
    void notifySupplierForRestock(String supplierId, String productId, int restockAmount) throws Exception;
    GenericSupplierDto createSupplier(@RequestBody GenericSupplierDto supplierDto);
    Supplier updateSupplier(String id, GenericSupplierDto supplierDto);
    void deleteSupplier(String id);
}
