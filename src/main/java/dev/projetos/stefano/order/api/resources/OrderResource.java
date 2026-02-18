package dev.projetos.stefano.order.api.resources;

import dev.projetos.stefano.order.api.dtos.request.OrderRequest;
import dev.projetos.stefano.order.api.dtos.response.OrderResponse;
import dev.projetos.stefano.order.api.services.OrderService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/orders")
@Tag(name = "Orders", description = "Endpoint for orders management")
public class OrderResource {

    private final OrderService orderService;

    public OrderResource(OrderService orderService) {
        this.orderService = orderService;
    }

    @Operation(summary = "FindAll Orders", description = "Lists all orders. Returns an empty list if no records are found.")
    @GetMapping
    public ResponseEntity<List<OrderResponse>> findAll() {
        List<OrderResponse> orders = orderService.findAll();

        return ResponseEntity.ok().body(orders);
    }

    @Operation(summary = "FindById Order", description = "Returns a order's data. Throws an exception if the provided ID does not exist.")
    @GetMapping("/{id}")
    public ResponseEntity<OrderResponse> findById(@PathVariable Long id) {
        OrderResponse order = orderService.findById(id);
        return ResponseEntity.ok().body(order);
    }

    @Operation(summary = "Insert Order", description = "Insert a new order and return the data for that new order.")
    @PostMapping
    public  ResponseEntity<OrderResponse> insert(@RequestBody OrderRequest request) {
        OrderResponse response = orderService.insert(request);

        URI uri = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(response.id())
                .toUri();

        return ResponseEntity.created(uri).body(response);
    }
}
