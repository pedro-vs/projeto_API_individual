package store.product.resource;

import java.util.List;
import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import store.product.controller.ProductController;
import store.product.dto.ProductIn;
import store.product.dto.ProductOut;
import store.product.service.ProductService;

@RestController
@RequestMapping("/products")
@RequiredArgsConstructor
public class ProductResource implements ProductController {

    private final ProductService productService;

    @Override
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ProductOut create(@Valid @RequestBody ProductIn productIn) {
        return productService.create(productIn);
    }

    @Override
    @GetMapping
    public List<ProductOut> findAll() {
        return productService.findAll();
    }

    @Override
    @GetMapping("/{id}")
    public ProductOut findById(@PathVariable UUID id) {
        return productService.findById(id);
    }

    @Override
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable UUID id) {
        productService.delete(id);
    }
}
