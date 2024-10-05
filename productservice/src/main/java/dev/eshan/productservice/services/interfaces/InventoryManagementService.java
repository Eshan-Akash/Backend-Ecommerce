package dev.eshan.productservice.services.interfaces;

import dev.eshan.productservice.dtos.GenericProductDto;

import java.util.List;

public interface InventoryManagementService {
    List<GenericProductDto> getLowStockProducts();
    void restockProduct(String id, int quantity);
}
