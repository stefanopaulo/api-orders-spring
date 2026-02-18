package dev.projetos.stefano.order.api.resources;

import dev.projetos.stefano.order.api.dtos.request.UserRequest;
import dev.projetos.stefano.order.api.dtos.request.UserUpdateRequest;
import dev.projetos.stefano.order.api.dtos.response.UserResponse;
import dev.projetos.stefano.order.api.services.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/users")
@Tag(name = "User", description = "Endpoint for user management")
public class UserResource {

    private final UserService userService;

    public UserResource(UserService userService) {
        this.userService = userService;
    }

    @Operation(summary = "FindAll Users", description = "Lists all users. Returns an empty list if no records are found.")
    @GetMapping
    public ResponseEntity<List<UserResponse>> findAll() {
        List<UserResponse> users = userService.findAll();

        return ResponseEntity.ok().body(users);
    }

    @Operation(summary = "FindById User", description = "Returns a user's data. Throws an exception if the provided ID does not exist.")
    @GetMapping("/{id}")
    public ResponseEntity<UserResponse> findById(@PathVariable Long id) {
        UserResponse user = userService.findById(id);
        return ResponseEntity.ok().body(user);
    }

    @Operation(summary = "Insert User", description = "Insert a new user and return the data for that new user.")
    @PostMapping
    public ResponseEntity<UserResponse> insert(@Valid @RequestBody UserRequest request) {
        UserResponse resp = userService.insert(request);

        URI uri = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(resp.id())
                .toUri();

        return ResponseEntity.created(uri).body(resp);
    }

    @Operation(summary = "Delete User", description = "Deletes a user from the database. Throws an exception if the given ID does not exist.")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        userService.delete(id);

        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Update User", description = "Updates a user partially or completely. Throws an exception if the provided ID does not exist.")
    @PutMapping("/{id}")
    public ResponseEntity<UserResponse> update(@PathVariable Long id, @Valid @RequestBody UserUpdateRequest request) {
        return ResponseEntity.ok().body(userService.update(id, request));
    }

}
