package dev.projetos.stefano.order.api.mapper;

import dev.projetos.stefano.order.api.dtos.response.OrderResponse;
import dev.projetos.stefano.order.api.entities.Order;
import org.springframework.stereotype.Component;

@Component
public class OrderMapper {

    private final UserMapper userMapper;

    public OrderMapper(UserMapper userMapper) {
        this.userMapper = userMapper;
    }

    public OrderResponse toResponse(Order order) {
        return new OrderResponse(
                order.getId(),
                order.getMoment().toString(),
                order.getOrderStatus().toString(),
                userMapper.toResponse(order.getClient()),
                order.getItems()
        );
    }
}
