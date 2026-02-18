package dev.projetos.stefano.order.api.dtos.request;

public record OrderItemRequest(
        Long productId,
        Integer quantity
) {
}
