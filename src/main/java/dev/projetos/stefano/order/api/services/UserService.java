package dev.projetos.stefano.order.api.services;

import dev.projetos.stefano.order.api.dtos.request.UserRequest;
import dev.projetos.stefano.order.api.dtos.request.UserUpdateRequest;
import dev.projetos.stefano.order.api.dtos.response.UserResponse;
import dev.projetos.stefano.order.api.entities.User;
import dev.projetos.stefano.order.api.repositories.UserRepository;
import dev.projetos.stefano.order.api.resources.exceptions.DatabaseException;
import dev.projetos.stefano.order.api.services.exceptions.ResourceNotFoundException;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public List<UserResponse> findAll() {
        return userRepository.findAll()
                .stream()
                .map(user -> new UserResponse(user.getId(), user.getName(), user.getEmail(), user.getPhone(), user.getPassword()))
                .toList();
    }

    public UserResponse findById(Long id) {
        return userRepository.findById(id)
                .map(user -> new UserResponse(user.getId(), user.getName(), user.getEmail(), user.getPhone(), user.getPassword()))
                .orElseThrow(() -> new ResourceNotFoundException(id));
    }

    public UserResponse insert(UserRequest request) {
        User user = new User();
        user.setName(request.name());
        user.setEmail(request.email());
        user.setPhone(request.phone());
        user.setPassword(request.password());

        User saveUser = userRepository.save(user);

        return new UserResponse(saveUser.getId(), saveUser.getName(), saveUser.getEmail(), saveUser.getPhone(), saveUser.getPassword());
    }

    public void delete(Long id) {

        try {
            if (!userRepository.existsById(id)) {
                throw new ResourceNotFoundException(id);
            }

            userRepository.deleteById(id);
        } catch (DataIntegrityViolationException e) {
            throw new DatabaseException(e.getMessage());
        }

    }

    public UserResponse update(Long id, UserUpdateRequest request) {
        try {
            User entity = userRepository.getReferenceById(id);

            updateData(request, entity);

            User updatedUser = userRepository.save(entity);

            return new UserResponse(updatedUser.getId(), updatedUser.getName(), updatedUser.getEmail(), updatedUser.getPhone(), updatedUser.getPassword());
        } catch (EntityNotFoundException _) {
            throw new ResourceNotFoundException(id);
        }
    }

    private static void updateData(UserUpdateRequest request, User entity) {
        entity.setName(request.name() == null ? entity.getName() : request.name());
        entity.setEmail(request.email() == null ? entity.getEmail() : request.email());
        entity.setPhone(request.phone() == null ? entity.getPhone() : request.phone());
    }
}
