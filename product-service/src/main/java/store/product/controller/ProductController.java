package store.product.controller;

import java.util.List;
import java.util.UUID;

import store.product.dto.ProductIn;
import store.product.dto.ProductOut;

public interface ProductController {

    ProductOut create(ProductIn productIn);

    List<ProductOut> findAll();

    ProductOut findById(UUID id);

    void delete(UUID id);
}
