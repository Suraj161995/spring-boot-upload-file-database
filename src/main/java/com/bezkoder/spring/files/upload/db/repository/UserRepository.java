package com.bezkoder.spring.files.upload.db.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.bezkoder.spring.files.upload.db.model.User;

public interface UserRepository extends JpaRepository<User,Long> {
}
