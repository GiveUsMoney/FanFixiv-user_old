package com.fanfixiv.auth.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.fanfixiv.auth.entity.UserEntity;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, Long> {
  boolean existsByEmail(String email);

  UserEntity findByEmail(String email);
}
