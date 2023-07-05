package com.fanfixiv.auth.repository.jpa;

import com.fanfixiv.auth.entity.ProfileEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProfileRepository extends JpaRepository<ProfileEntity, Long> {
  boolean existsByNickname(String nickname);
}
