package dev.projetos.stefano.order.api.resources;

import dev.projetos.stefano.order.api.dtos.request.CategoryRequest;
import dev.projetos.stefano.order.api.dtos.response.CategoryResponse;
import dev.projetos.stefano.order.api.services.CategoryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/categories")
@Tag(name = "Categories", description = "Endpoint for categories management")
public class CategoryResource {

    private final CategoryService categoryService;

    public CategoryResource(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @Operation(summary = "FindAll Categories", description = "Lists all categories. Returns an empty list if no records are found.")
    @GetMapping
    public ResponseEntity<List<CategoryResponse>> findAll() {
        List<CategoryResponse> categories = categoryService.findAll();

        return ResponseEntity.ok().body(categories);
    }

    @Operation(summary = "FindById Category", description = "Returns a category's data. Throws an exception if the provided ID does not exist.")
    @GetMapping("/{id}")
    public ResponseEntity<CategoryResponse> findById(@PathVariable Long id) {
        CategoryResponse category = categoryService.findById(id);
        return ResponseEntity.ok().body(category);
    }

    @Operation(summary = "Insert Category", description = "Insert a new category and return the data for that new category.")
    @PostMapping
    public ResponseEntity<CategoryResponse> insert(@Valid @RequestBody CategoryRequest request) {
        CategoryResponse resp = categoryService.insert(request);

        URI uri = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(resp.id())
                .toUri();

        return ResponseEntity.created(uri).body(resp);
    }

    @Operation(summary = "Delete Category", description = "Deletes a category from the database. Throws an exception if the given ID does not exist.")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        categoryService.delete(id);

        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Update Category", description = "Updates a category partially or completely. Throws an exception if the provided ID does not exist.")
    @PutMapping("/{id}")
    public ResponseEntity<CategoryResponse> update(@PathVariable Long id, @Valid @RequestBody CategoryRequest request) {
        return ResponseEntity.ok().body(categoryService.update(id, request));
    }

}
