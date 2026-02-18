package dev.projetos.stefano.order.api.dtos.response;

import dev.projetos.stefano.order.api.entities.OrderItem;

import java.util.Set;

public record OrderResponse(
        Long id,

        String moment,

        String orderStatus,

        UserResponse client,

        Set<OrderItem> items
) {
}
