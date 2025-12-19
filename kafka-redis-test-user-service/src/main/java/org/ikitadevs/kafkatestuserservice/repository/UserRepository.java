package org.ikitadevs.kafkatestuserservice.repository;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;
import org.ikitadevs.kafkatestuserservice.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    boolean existsByEmail(String email);
}
