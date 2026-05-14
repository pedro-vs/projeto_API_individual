package store.product.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import store.product.model.Product;

public interface ProductRepository extends JpaRepository<Product, UUID> {
}
