package com.htecgroup.skynest.repository;

import com.htecgroup.skynest.model.enitity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, UUID> {
  Optional<UserEntity> findUserByEmail(String email);

  boolean existsByEmail(String email);
}