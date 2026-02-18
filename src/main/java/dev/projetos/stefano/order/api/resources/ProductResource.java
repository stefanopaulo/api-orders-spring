package dev.projetos.stefano.order.api.resources;

import dev.projetos.stefano.order.api.dtos.request.ProductRequest;
import dev.projetos.stefano.order.api.dtos.request.ProductUpdateRequest;
import dev.projetos.stefano.order.api.dtos.response.ProductResponse;
import dev.projetos.stefano.order.api.services.ProductService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/products")
@Tag(name = "Products", description = "Endpoint for products management")
public class ProductResource {

    private final ProductService productService;

    public ProductResource(ProductService productService) {
        this.productService = productService;
    }

    @Operation(summary = "FindAll Products", description = "Lists all products. Returns an empty list if no records are found.")
    @GetMapping
    public ResponseEntity<List<ProductResponse>> findAll() {
        List<ProductResponse> products = productService.findAll();

        return ResponseEntity.ok().body(products);
    }

    @Operation(summary = "FindById Product", description = "Returns a product's data. Throws an exception if the provided ID does not exist.")
    @GetMapping("/{id}")
    public ResponseEntity<ProductResponse> findById(@PathVariable Long id) {
        ProductResponse product = productService.findById(id);
        return ResponseEntity.ok().body(product);
    }

    @Operation(summary = "Insert Product", description = "Insert a new product and return the data for that new product.")
    @PostMapping
    public ResponseEntity<ProductResponse> insert(@Valid @RequestBody ProductRequest request) {
        ProductResponse response = productService.insert(request);

        URI uri = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(response.id())
                .toUri();

        return ResponseEntity.created(uri).body(response);
    }

    @Operation(summary = "Delete Product", description = "Deletes a product from the database. Throws an exception if the given ID does not exist.")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        productService.delete(id);

        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Update Product", description = "Updates a product partially or completely. Throws an exception if the provided ID does not exist.")
    @PatchMapping("/{id}")
    public ResponseEntity<ProductResponse> update(@PathVariable Long id, @Valid @RequestBody ProductUpdateRequest request) {
        ProductResponse response = productService.update(id, request);

        return ResponseEntity.ok().body(response);
    }

}
