package com.fanfixiv.auth.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.fanfixiv.auth.dto.redis.RedisAuthDto;

@Repository
public interface RedisAuthRepository extends CrudRepository<RedisAuthDto, String> {
}
