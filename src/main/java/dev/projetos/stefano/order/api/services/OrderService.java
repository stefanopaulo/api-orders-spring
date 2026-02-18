package dev.projetos.stefano.order.api.services;

import dev.projetos.stefano.order.api.dtos.request.OrderRequest;
import dev.projetos.stefano.order.api.dtos.response.OrderResponse;
import dev.projetos.stefano.order.api.entities.Order;
import dev.projetos.stefano.order.api.entities.OrderItem;
import dev.projetos.stefano.order.api.entities.Product;
import dev.projetos.stefano.order.api.entities.User;
import dev.projetos.stefano.order.api.entities.enums.OrderStatus;
import dev.projetos.stefano.order.api.mapper.OrderMapper;
import dev.projetos.stefano.order.api.repositories.OrderRepository;
import dev.projetos.stefano.order.api.repositories.ProductRepository;
import dev.projetos.stefano.order.api.repositories.UserRepository;
import dev.projetos.stefano.order.api.services.exceptions.ResourceNotFoundException;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class OrderService {

    private final OrderRepository orderRepository;
    private final OrderMapper orderMapper;
    private final UserRepository userRepository;
    private final ProductRepository productRepository;

    public OrderService(OrderRepository orderRepository, OrderMapper orderMapper, UserRepository userRepository, ProductRepository productRepository) {
        this.orderRepository = orderRepository;
        this.orderMapper = orderMapper;
        this.userRepository = userRepository;
        this.productRepository = productRepository;
    }

    public List<OrderResponse> findAll() {
        return orderRepository.findAllWithDetails()
                .stream().map(orderMapper::toResponse)
                .toList();
    }

    public OrderResponse findById(Long id) {
        return orderRepository.findById(id).map(orderMapper::toResponse).orElseThrow(() -> new ResourceNotFoundException(id));
    }

    public OrderResponse insert(OrderRequest request) {
        User client = userRepository.findById(request.clientId()).orElseThrow(() -> new ResourceNotFoundException(request.clientId()));

        Order order = new Order();
        order.setMoment(Instant.now());
        order.setOrderStatus(OrderStatus.WAITING_PAYMENT);
        order.setClient(client);

        Set<OrderItem> items = request.items().stream().map(item -> {
            Product product = productRepository.findById(item.productId()).orElseThrow(() -> new ResourceNotFoundException(item.productId()));

            return new OrderItem(order, product, item.quantity(), product.getPrice());
        }).collect(Collectors.toSet());

        order.getItems().addAll(items);

        return orderMapper.toResponse(orderRepository.save(order));
    }
}
