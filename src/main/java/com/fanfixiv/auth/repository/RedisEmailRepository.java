package com.fanfixiv.auth.repository;

import org.springframework.data.repository.CrudRepository;

import com.fanfixiv.auth.dto.redis.RedisEmailAuthDto;

public interface RedisEmailRepository extends CrudRepository<RedisEmailAuthDto, String> {
}
