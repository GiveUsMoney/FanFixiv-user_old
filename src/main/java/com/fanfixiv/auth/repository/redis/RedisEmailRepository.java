package com.fanfixiv.auth.repository.redis;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.fanfixiv.auth.dto.redis.RedisEmailAuthDto;

@Repository
public interface RedisEmailRepository extends CrudRepository<RedisEmailAuthDto, String> {
}
