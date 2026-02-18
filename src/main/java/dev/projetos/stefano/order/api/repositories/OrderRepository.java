package dev.projetos.stefano.order.api.repositories;

import dev.projetos.stefano.order.api.entities.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
    @Query("SELECT obj FROM Order obj " +
            "JOIN FETCH obj.client " +
            "JOIN FETCH obj.items item " +
            "JOIN FETCH item.id.product")
    List<Order> findAllWithDetails();
}
