package dev.eshan.productservice.services.impl;

import dev.eshan.productservice.dtos.GenericProductDto;
import dev.eshan.productservice.dtos.GenericSupplierDto;
import dev.eshan.productservice.events.EventName;
import dev.eshan.productservice.events.SupplierEvent;
import dev.eshan.productservice.models.Product;
import dev.eshan.productservice.models.Supplier;
import dev.eshan.productservice.repositories.ProductRepository;
import dev.eshan.productservice.repositories.SupplierRepository;
import dev.eshan.productservice.services.interfaces.SupplierIntegrationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class SupplierIntegrationServiceImpl implements SupplierIntegrationService {
    private final ProductRepository productRepository;
    private final SupplierRepository supplierRepository;
    private final ApplicationEventPublisher eventPublisher;
    private final RedisTemplate<String, Object> redisTemplate;

    public SupplierIntegrationServiceImpl(ProductRepository productRepository, SupplierRepository supplierRepository,
                                          ApplicationEventPublisher eventPublisher, RedisTemplate<String, Object> redisTemplate) {
        this.productRepository = productRepository;
        this.supplierRepository = supplierRepository;
        this.eventPublisher = eventPublisher;
        this.redisTemplate = redisTemplate;
    }

    @Override
    public void notifySupplierForRestock(String supplierId, String productId, int restockAmount) throws Exception {
        if (supplierId == null || productId == null) {
            throw new Exception("Supplier or Product is null, cannot notify for restock.");
        }
        Product product = productRepository.findById(productId).orElseThrow(() -> new Exception("Product not found by id: " + productId));
        Supplier supplier = supplierRepository.findById(supplierId).orElseThrow(() -> new Exception("Supplier not found by id: " + supplierId));

        // trigger event to send email to supplier
        try {
            eventPublisher.publishEvent(getSupplierEvent(
                    GenericSupplierDto.builder()
                            .id(supplier.getId())
                            .name(supplier.getName())
                            .contactPersonName(supplier.getContactPersonName())
                            .contactEmail(supplier.getContactEmail())
                            .contactPhone(supplier.getContactPhone())
                            .address(supplier.getAddress())
                            .build(),
                    GenericProductDto.builder()
                            .id(product.getId())
                            .title(product.getTitle())
                            .description(product.getDescription())
                            .imageUrl(product.getImageUrl())
                            .price(product.getPrice())
                            .stockLevel(product.getStockLevel())
                            .lowStockThreshold(product.getLowStockThreshold())
                            .build(),
                    EventName.SUPPLIER_RESTOCK_REQUEST));
        } catch (Exception e) {
            log.error("Error publishing event: " + e.getMessage());
        }
    }

    @Override
    public GenericSupplierDto createSupplier(GenericSupplierDto supplierDto) {
        Supplier supplier = new Supplier();
        supplier.setName(supplierDto.getName());
        supplier.setContactPersonName(supplierDto.getContactPersonName());
        supplier.setContactEmail(supplierDto.getContactEmail());
        supplier.setContactPhone(supplierDto.getContactPhone());
        supplier.setAddress(supplierDto.getAddress());
        Supplier savedSupplier = supplierRepository.save(supplier);
        supplierDto.setId(savedSupplier.getId());
        try {
            eventPublisher.publishEvent(getSupplierEvent(supplierDto, null, EventName.SUPPLIER_CREATED));
        } catch (Exception e) {
            log.error("Error publishing event: " + e.getMessage());
        }
        return supplierDto;
    }

    @Override
    public Supplier updateSupplier(String id, GenericSupplierDto supplierDto) {
        Supplier supplier = supplierRepository.findById(id).orElseThrow(() -> new RuntimeException("Supplier not found by id: " + id));
        if (supplierDto.getName() != null) {
            supplier.setName(supplierDto.getName());
        }
        if (supplierDto.getContactPersonName() != null) {
            supplier.setContactPersonName(supplierDto.getContactPersonName());
        }
        if (supplierDto.getContactEmail() != null) {
            supplier.setContactEmail(supplierDto.getContactEmail());
        }
        if (supplierDto.getContactPhone() != null) {
            supplier.setContactPhone(supplierDto.getContactPhone());
        }
        if (supplierDto.getAddress() != null) {
            supplier.setAddress(supplierDto.getAddress());
        }
        try {
            eventPublisher.publishEvent(getSupplierEvent(supplierDto, null, EventName.SUPPLIER_UPDATED));
        } catch (Exception e) {
            log.error("Error publishing event: " + e.getMessage());
        }
        return supplierRepository.save(supplier);
    }

    @Override
    public void deleteSupplier(String id) {
        eventPublisher.publishEvent(getSupplierEvent(null, null, EventName.SUPPLIER_DELETED));
        supplierRepository.deleteById(id);
    }

    private SupplierEvent getSupplierEvent(GenericSupplierDto supplierDto, GenericProductDto productDto, EventName eventName) {
        SupplierEvent supplierEvent = new SupplierEvent();
        supplierEvent.setEventName(eventName);
        supplierEvent.setSupplier(supplierDto);
        supplierEvent.setProduct(productDto);
        return supplierEvent;
    }
}
