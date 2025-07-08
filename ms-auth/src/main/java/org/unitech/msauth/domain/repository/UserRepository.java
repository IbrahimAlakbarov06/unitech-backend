package org.unitech.msauth.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.unitech.msauth.domain.entity.User;
import org.unitech.msauth.model.enums.Status;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);

    Optional<User> findByFin(String fin);

    boolean existsByFin(String fin);

    boolean existsByEmail(String email);
}