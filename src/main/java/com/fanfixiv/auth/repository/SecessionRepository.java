package com.fanfixiv.auth.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.fanfixiv.auth.entity.SecessionEntity;

public interface SecessionRepository extends JpaRepository<SecessionEntity, Long> {
  boolean existsByEmail(String email);
  SecessionEntity findByEmail(String email);
}
