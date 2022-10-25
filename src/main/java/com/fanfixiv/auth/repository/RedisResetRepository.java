package com.fanfixiv.auth.repository;

import org.springframework.data.repository.CrudRepository;

import com.fanfixiv.auth.dto.redis.RedisResetDto;

public interface RedisResetRepository extends CrudRepository<RedisResetDto, String> {
}
