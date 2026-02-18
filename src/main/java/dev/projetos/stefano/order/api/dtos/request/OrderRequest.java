package dev.projetos.stefano.order.api.dtos.request;

import java.util.Set;

public record OrderRequest(
        Long clientId,
        Set<OrderItemRequest> items
) {
}
