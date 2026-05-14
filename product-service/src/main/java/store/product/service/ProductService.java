package store.product.service;

import java.util.List;
import java.util.UUID;

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
    public ProductOut create(ProductIn productIn) {
        Product product = ProductMapper.toEntity(productIn);
        Product savedProduct = productRepository.save(product);
        return ProductMapper.toOutput(savedProduct);
    }

    @Transactional(readOnly = true)
    public List<ProductOut> findAll() {
        return productRepository.findAll(Sort.by(Sort.Direction.ASC, "name"))
            .stream()
            .map(ProductMapper::toOutput)
            .toList();
    }

    @Transactional(readOnly = true)
    public ProductOut findById(UUID id) {
        return productRepository.findById(id)
            .map(ProductMapper::toOutput)
            .orElseThrow(() -> new ProductNotFoundException(id));
    }

    @Transactional
    public void delete(UUID id) {
        Product product = productRepository.findById(id)
            .orElseThrow(() -> new ProductNotFoundException(id));
        productRepository.delete(product);
    }
}
