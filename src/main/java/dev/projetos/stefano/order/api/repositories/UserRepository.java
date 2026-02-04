package dev.projetos.stefano.order.api.repositories;

import dev.projetos.stefano.order.api.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
}
