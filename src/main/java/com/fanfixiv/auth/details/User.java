package com.fanfixiv.auth.details;

import com.fanfixiv.auth.entity.UserEntity;

import java.util.Collection;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class User implements UserDetails {

  private Long userSeq;

  private String userEmail;

  private List<SimpleGrantedAuthority> roles;

  private UserEntity user;

  public User(UserEntity user) {
    this.user = user;
    this.userSeq = user.getSeq();
    this.userEmail = user.getEmail();
    this.roles = user.getRole().stream().map(item -> new SimpleGrantedAuthority(item.getRole().name())).toList();
  }

  @Override
  public Collection<? extends GrantedAuthority> getAuthorities() {
    return this.roles;
  }

  @Override
  public String getPassword() {
    return null;
  }

  @Override
  public String getUsername() {
    return userEmail;
  }

  @Override
  public boolean isAccountNonExpired() {
    return true;
  }

  @Override
  public boolean isAccountNonLocked() {
    return true;
  }

  @Override
  public boolean isCredentialsNonExpired() {
    return true;
  }

  @Override
  public boolean isEnabled() {
    return true;
  }
}
