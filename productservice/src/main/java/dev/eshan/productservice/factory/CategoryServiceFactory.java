package dev.eshan.productservice.factory;

import dev.eshan.productservice.services.interfaces.CategoryService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import java.util.Map;

import static java.util.Map.entry;

@Slf4j
@Component
public class CategoryServiceFactory {
    private static final String DEFAULT_BEAN_NAME = "categoryServiceImpl";

    @Autowired
    private ApplicationContext context;

    private static final Map<String, String> PUBLISHER_BEAN_NAME_MAPPING = Map.ofEntries(
            entry("self", "categoryServiceImpl")
    );

    public CategoryService create(String publisher) {
        String serviceBeanName = PUBLISHER_BEAN_NAME_MAPPING.get(publisher);
        try {
            if (serviceBeanName == null) {
                serviceBeanName = DEFAULT_BEAN_NAME;
            }
            log.info("Service bean name: " + serviceBeanName);
            return (CategoryService) context.getBean(serviceBeanName);
        } catch (Exception e) {
            log.error("Failed to create object of category service, exception=", e);
            throw e;
        }
    }
}
