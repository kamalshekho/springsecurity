 package com.springmvcproject.stickynotes.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.springmvcproject.stickynotes.model.entity.UserEntity;

public interface UserRepo extends JpaRepository<UserEntity, Long> {

    Optional<UserEntity> findByUsername(String username);
}