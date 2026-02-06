package dev.projetos.stefano.order.api.services;

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

    public List<User> findAll() {
        return userRepository.findAll();
    }

    public User findById(Long id) {
        return userRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException(id));
    }

    public User insert(User user) {
        return userRepository.save(user);
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

    public User update(Long id, User user) {
        try {
            User entity = userRepository.getReferenceById(id);
            updateData(user, entity);
            return userRepository.save(entity);
        } catch (EntityNotFoundException _) {
            throw new ResourceNotFoundException(id);
        }
    }

    private static void updateData(User user, User entity) {
        entity.setName(user.getName());
        entity.setEmail(user.getEmail());
        entity.setPhone(user.getPhone());
    }
}
