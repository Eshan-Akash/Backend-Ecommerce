package dev.eshan.productservice.services.impl;

import dev.eshan.productservice.models.Product;
import dev.eshan.productservice.models.Supplier;
import dev.eshan.productservice.services.interfaces.SupplierIntegrationService;
import org.springframework.stereotype.Service;

@Service
public class SupplierIntegrationServiceImpl implements SupplierIntegrationService {
    @Override
    public void notifySupplierForRestock(Supplier supplier, Product product, int restockAmount) throws Exception {

    }

    @Override
    public Supplier createSupplier(Supplier supplier) {
        return null;
    }

    @Override
    public Supplier updateSupplier(String id, Supplier supplier) {
        return null;
    }

    @Override
    public void deleteSupplier(String id) {

    }
}
