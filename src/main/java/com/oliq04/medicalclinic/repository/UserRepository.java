package com.oliq04.medicalclinic.repository;

import com.oliq04.medicalclinic.model.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    List<User> findAll();

    boolean existsByEmail(String email);

    Optional<User> findByEmail(String email);
}
