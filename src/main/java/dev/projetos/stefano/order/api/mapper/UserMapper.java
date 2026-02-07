package dev.projetos.stefano.order.api.mapper;

import dev.projetos.stefano.order.api.dtos.request.UserRequest;
import dev.projetos.stefano.order.api.dtos.request.UserUpdateRequest;
import dev.projetos.stefano.order.api.dtos.response.UserResponse;
import dev.projetos.stefano.order.api.entities.User;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {

    public UserResponse toResponse(User user) {
        return new UserResponse(
                user.getId(),
                user.getName(),
                user.getEmail(),
                user.getPhone()
        );
    }

    public User toEntity(UserRequest request) {
        User user = new User();
        user.setName(request.name());
        user.setEmail(request.email());
        user.setPhone(request.phone());
        user.setPassword(request.password());
        return user;
    }

    public void updateEntityFromRequest(UserUpdateRequest request, User entity) {
        if (request.name() != null) entity.setName(request.name());
        if (request.email() != null) entity.setEmail(request.email());
        if (request.phone() != null) entity.setPhone(request.phone());
    }

}
