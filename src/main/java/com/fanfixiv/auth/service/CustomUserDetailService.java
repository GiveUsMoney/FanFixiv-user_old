package com.fanfixiv.auth.service;

import com.fanfixiv.auth.details.User;
import com.fanfixiv.auth.entity.UserEntity;
import com.fanfixiv.auth.repository.UserRepository;
import lombok.RequiredArgsConstructor;

import javax.transaction.Transactional;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomUserDetailService implements UserDetailsService {

  private final UserRepository userRepository;

  @Override
  @Transactional
  public UserDetails loadUserByUsername(String _id) throws UsernameNotFoundException {
    Long id = Long.parseLong(_id);
    UserEntity user = userRepository
        .findById(id)
        .orElseThrow(() -> new UsernameNotFoundException("사용자를 찾을수 없습니다."));

    return new User(user);
  }
}
