package store.order.repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import store.order.model.Order;

public interface OrderRepository extends JpaRepository<Order, UUID> {

    List<Order> findAllByAccountIdOrderByDateDesc(String accountId);

    Optional<Order> findByIdAndAccountId(UUID id, String accountId);
}
