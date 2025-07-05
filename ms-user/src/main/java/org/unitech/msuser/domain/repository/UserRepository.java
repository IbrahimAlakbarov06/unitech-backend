package org.unitech.msuser.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.unitech.msuser.domain.entity.User;
import org.unitech.msuser.model.enums.Status;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> finByEmail(String email);

    Optional<User> findByFin(String fin);

    boolean existsByFin(String fin);

    boolean existsByEmail(String email);

    @Query("select User from User where User.status = :status")
    List<User> findByStatus(@Param("status") Status status);

    @Query("select count (User) from User where User.status = 'ACTIVE'")
    Long countActiveUsers();
}
