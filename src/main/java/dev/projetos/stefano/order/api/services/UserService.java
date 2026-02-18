package dev.projetos.stefano.order.api.services;

import dev.projetos.stefano.order.api.dtos.request.UserRequest;
import dev.projetos.stefano.order.api.dtos.request.UserUpdateRequest;
import dev.projetos.stefano.order.api.dtos.response.UserResponse;
import dev.projetos.stefano.order.api.entities.User;
import dev.projetos.stefano.order.api.mapper.UserMapper;
import dev.projetos.stefano.order.api.repositories.UserRepository;
import dev.projetos.stefano.order.api.resources.exceptions.DatabaseException;
import dev.projetos.stefano.order.api.services.exceptions.ResourceNotFoundException;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;

    public UserService(UserRepository userRepository, UserMapper userMapper) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
    }

    public List<UserResponse> findAll() {
        return userRepository.findAll()
                .stream()
                .map(userMapper::toResponse)
                .toList();
    }

    public UserResponse findById(Long id) {
        return userRepository.findById(id)
                .map(userMapper::toResponse)
                .orElseThrow(() -> new ResourceNotFoundException(id));
    }

    @Transactional
    public UserResponse insert(UserRequest request) {
        try {
            User user = userMapper.toEntity(request);

            return userMapper.toResponse(userRepository.save(user));
        } catch (DataIntegrityViolationException e) {
            throw new DatabaseException("Email already registered");
        }
    }

    @Transactional
    public void delete(Long id) {

        try {
            if (!userRepository.existsById(id)) {
                throw new ResourceNotFoundException(id);
            }

            userRepository.deleteById(id);
        } catch (DataIntegrityViolationException e) {
            throw new DatabaseException("Cannot delete: User has associated records");
        }

    }

    @Transactional
    public UserResponse update(Long id, UserUpdateRequest request) {
        try {
            User entity = userRepository.getReferenceById(id);

            userMapper.updateEntityFromRequest(request, entity);

            return userMapper.toResponse(userRepository.save(entity));
        } catch (EntityNotFoundException e) {
            throw new ResourceNotFoundException(id);
        } catch (DataIntegrityViolationException e) {
            throw new DatabaseException("Email already registered");
        }
    }
}
