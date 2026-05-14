package store.product.service;

import store.product.dto.ProductIn;
import store.product.dto.ProductOut;
import store.product.model.Product;

public final class ProductMapper {

    private ProductMapper() {
    }

    public static Product toEntity(ProductIn productIn) {
        return Product.builder()
            .name(productIn.name().trim())
            .price(productIn.price())
            .unit(productIn.unit().trim())
            .build();
    }

    public static ProductOut toOutput(Product product) {
        return new ProductOut(
            product.getId(),
            product.getName(),
            product.getPrice(),
            product.getUnit()
        );
    }
}
