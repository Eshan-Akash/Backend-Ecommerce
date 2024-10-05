package dev.eshan.productservice.services.interfaces;

import dev.eshan.productservice.models.Product;
import dev.eshan.productservice.models.Supplier;
import org.springframework.web.bind.annotation.RequestBody;

public interface SupplierIntegrationService {
    void notifySupplierForRestock(Supplier supplier, Product product, int restockAmount) throws Exception;
    Supplier createSupplier(@RequestBody Supplier supplier);
    Supplier updateSupplier(String id, Supplier supplier);
    void deleteSupplier(String id);
}
