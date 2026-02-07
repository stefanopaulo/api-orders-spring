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
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DataIntegrityViolationException;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private UserMapper userMapper;

    @InjectMocks
    private UserService userService;

    @Test
    void findById_whenUserExists_shouldReturnUserResponse() {
        // Given
        User user = new User(1L, "Maria Brown", "maria@gmail.com", "988888888", "123456");
        UserResponse expectedResponse = new UserResponse(1L, "Maria Brown", "maria@gmail.com", "988888888");

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(userMapper.toResponse(user)).thenReturn(expectedResponse);

        // When
        UserResponse result = userService.findById(1L);

        // Then
        assertEquals(result, expectedResponse);
        assertEquals(1L, result.id());
        verify(userRepository).findById(1L);
        verify(userMapper).toResponse(user);
    }

    @Test
    void findById_whenUserNotFound_shouldThrowResourceNotFoundException() {
        // Given
        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        // When/Then
        assertThrows(ResourceNotFoundException.class, () -> userService.findById(1L));

        verify(userRepository).findById(1L);
        verify(userMapper, never()).toResponse(any());
    }

    @Test
    void findAll_whenUsersExist_shouldReturnListOfUserResponses() {
        // Given
        User u1 = new User(1L, "Maria Brown", "maria@gmail.com", "988888888", "123456");
        User u2 = new User(2L, "Alex Green", "alex@gmail.com", "977777777", "123456");
        User u3 = new User(3L, "Bob Grey", "bob@gmail.com", "955555555", "abcdef");

        List<User> users = List.of(u1, u2, u3);

        UserResponse r1 = new UserResponse(1L, "Maria Brown", "maria@gmail.com", "988888888");
        UserResponse r2 = new UserResponse(2L, "Alex Green", "alex@gmail.com", "977777777");
        UserResponse r3 = new UserResponse(3L, "Bob Grey", "bob@gmail.com", "955555555");

        when(userRepository.findAll()).thenReturn(users);
        when(userMapper.toResponse(u1)).thenReturn(r1);
        when(userMapper.toResponse(u2)).thenReturn(r2);
        when(userMapper.toResponse(u3)).thenReturn(r3);

        // When
        List<UserResponse> response = userService.findAll();

        // Then
        assertEquals(3, response.size());
        verify(userRepository).findAll();
        verify(userMapper, times(3)).toResponse(any());
    }

    @Test
    void findAll_whenNoUsersExist_shouldReturnEmptyList() {
        // Given
        when(userRepository.findAll()).thenReturn(List.of());

        // When
        List<UserResponse> users = userService.findAll();

        // Then
        assertTrue(users.isEmpty());
        verify(userRepository).findAll();
        verify(userMapper, never()).toResponse(any());
    }

    @Test
    void insert_whenValidRequest_shouldSaveAndReturnUserResponse() {
        // Given
        User savedUser = new User(1L, "Maria Brown", "maria@gmail.com", "988888888", "123456");
        UserRequest request = new UserRequest("Maria Brown", "maria@gmail.com", "988888888", "123456");
        UserResponse response = new UserResponse(1L, "Maria Brown", "maria@gmail.com", "988888888");
        User userWithoutId = new User(null, "Maria Brown", "maria@gmail.com", "988888888", "123456");

        when(userMapper.toEntity(request)).thenReturn(userWithoutId);
        when(userRepository.save(userWithoutId)).thenReturn(savedUser);
        when(userMapper.toResponse(savedUser)).thenReturn(response);

        // When
        UserResponse result = userService.insert(request);

        // Then
        assertEquals(result, response);
        verify(userMapper).toEntity(request);
        verify(userRepository).save(userWithoutId);
        verify(userMapper).toResponse(savedUser);
    }

    @Test
    void insert_whenEmailAlreadyExists_shouldThrowDatabaseException() {
        // Given
        UserRequest request = new UserRequest("Maria", "duplicado@email.com", "123", "senha");
        User user = new User();

        when(userMapper.toEntity(request)).thenReturn(user);
        when(userRepository.save(user)).thenThrow(DataIntegrityViolationException.class);

        // When/Then
        DatabaseException exception = assertThrows(
                DatabaseException.class,
                () -> userService.insert(request)
        );

        assertEquals("Email already registered", exception.getMessage());
        verify(userRepository).save(user);
    }

    @Test
    void delete_whenUserExists_shouldDeleteSuccessfully() {
        // Given
        Long userId = 1L;

        when(userRepository.existsById(userId)).thenReturn(true);
        doNothing().when(userRepository).deleteById(userId);

        // When
        userService.delete(userId);

        // Then
        verify(userRepository).existsById(userId);
        verify(userRepository).deleteById(userId);
    }

    @Test
    void delete_whenUserNotFound_shouldThrowResourceNotFoundException() {
        // Given
        Long userId = 999L;

        when(userRepository.existsById(userId)).thenReturn(false);

        // When/Then
        ResourceNotFoundException exception = assertThrows(
                ResourceNotFoundException.class,
                () -> userService.delete(userId)
        );

        assertTrue(exception.getMessage().contains("999"));

        verify(userRepository).existsById(userId);
        verify(userRepository, never()).deleteById(any());
    }

    @Test
    void delete_whenUserHasAssociatedRecords_shouldThrowDatabaseException() {
        // Given
        Long userId = 1L;

        when(userRepository.existsById(userId)).thenReturn(true);
        doThrow(DataIntegrityViolationException.class)
                .when(userRepository).deleteById(userId);

        // When/Then
        DatabaseException exception = assertThrows(
                DatabaseException.class,
                () -> userService.delete(userId)
        );

        assertEquals("Cannot delete: User has associated records", exception.getMessage());

        verify(userRepository).existsById(userId);
        verify(userRepository).deleteById(userId);
    }

    @Test
    void update_whenUserExists_shouldUpdateAndReturnUserResponse() {
        // Given
        Long userId = 1L;

        User existingUser = new User(userId, "Maria Brown", "maria@email.com", "111111111", "oldpass");

        UserUpdateRequest request = new UserUpdateRequest(null, "new.maria@email.com", null);

        User updatedUser = new User(userId, "Maria Brown", "new.maria@email.com", "111111111", "oldpass");

        UserResponse expectedResponse = new UserResponse(userId, "Maria Brown", "new.maria@email.com", "111111111");

        when(userRepository.getReferenceById(userId)).thenReturn(existingUser);
        when(userRepository.save(existingUser)).thenReturn(updatedUser);
        when(userMapper.toResponse(updatedUser)).thenReturn(expectedResponse);

        // When
        UserResponse result = userService.update(userId, request);

        // Then
        assertEquals(expectedResponse, result);
        verify(userRepository).getReferenceById(userId);
        verify(userMapper).updateEntityFromRequest(request, existingUser);
        verify(userRepository).save(existingUser);
        verify(userMapper).toResponse(updatedUser);
    }

    @Test
    void update_whenUserNotFound_shouldThrowResourceNotFoundException() {
        // Given
        Long userId = 999L;
        UserUpdateRequest request = new UserUpdateRequest("Maria Brown", null, null);

        when(userRepository.getReferenceById(userId))
                .thenThrow(EntityNotFoundException.class);

        // When/Then
        assertThrows(ResourceNotFoundException.class,
                () -> userService.update(userId, request));

        verify(userRepository).getReferenceById(userId);
        verify(userRepository, never()).save(any());
    }

    @Test
    void update_whenEmailAlreadyExists_shouldThrowDatabaseException() {
        // Given
        Long userId = 1L;
        User existingUser = new User(userId, "Maria Brown", "maria@email.com", "111", "pass");
        UserUpdateRequest request = new UserUpdateRequest(null, "duplicate.maria@email.com", null);

        when(userRepository.getReferenceById(userId)).thenReturn(existingUser);
        when(userRepository.save(existingUser))
                .thenThrow(DataIntegrityViolationException.class);

        // When/Then
        DatabaseException exception = assertThrows(DatabaseException.class,
                () -> userService.update(userId, request));

        assertEquals("Email already registered", exception.getMessage());
        verify(userRepository).getReferenceById(userId);
        verify(userMapper).updateEntityFromRequest(request, existingUser);
        verify(userRepository).save(existingUser);
    }

}
