package dev.eshan.productservice.services.impl;

import dev.eshan.productservice.dtos.GenericProductDto;
import dev.eshan.productservice.services.interfaces.InventoryManagementService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class InventoryManagementServiceImpl implements InventoryManagementService {
    @Override
    public List<GenericProductDto> getLowStockProducts() {
        return null;
    }

    @Override
    public void restockProduct(String id, int quantity) {

    }
}
