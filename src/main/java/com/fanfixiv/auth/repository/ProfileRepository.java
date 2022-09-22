package com.fanfixiv.auth.repository;

import com.fanfixiv.auth.entity.ProfileEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProfileRepository extends JpaRepository<ProfileEntity, Long> {
  boolean existsByNickname(String nickname);
}
