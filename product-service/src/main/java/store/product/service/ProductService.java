package store.product.service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import store.product.dto.ProductIn;
import store.product.dto.ProductOut;
import store.product.exception.ProductNotFoundException;
import store.product.model.Product;
import store.product.repository.ProductRepository;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;

    @Transactional
    @CacheEvict(cacheNames = "products", key = "'all'")
    public ProductOut create(ProductIn productIn) {
        Product product = ProductMapper.toEntity(productIn);
        Product savedProduct = productRepository.save(product);
        return ProductMapper.toOutput(savedProduct);
    }

    @Transactional(readOnly = true)
    @Cacheable(cacheNames = "products", key = "'all'")
    public List<ProductOut> findAll() {
        return productRepository.findAll(Sort.by(Sort.Direction.ASC, "name"))
            .stream()
            .map(ProductMapper::toOutput)
            .collect(Collectors.toCollection(ArrayList::new));
    }

    @Transactional(readOnly = true)
    @Cacheable(cacheNames = "productById", key = "#id")
    public ProductOut findById(UUID id) {
        return productRepository.findById(id)
            .map(ProductMapper::toOutput)
            .orElseThrow(() -> new ProductNotFoundException(id));
    }

    @Transactional
    @Caching(evict = {
        @CacheEvict(cacheNames = "products", key = "'all'"),
        @CacheEvict(cacheNames = "productById", key = "#id")
    })
    public void delete(UUID id) {
        Product product = productRepository.findById(id)
            .orElseThrow(() -> new ProductNotFoundException(id));
        productRepository.delete(product);
    }
}
