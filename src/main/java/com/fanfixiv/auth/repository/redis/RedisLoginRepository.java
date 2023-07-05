package com.fanfixiv.auth.repository.redis;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.fanfixiv.auth.dto.redis.RedisLoginDto;

@Repository
public interface RedisLoginRepository extends CrudRepository<RedisLoginDto, String> {
}
