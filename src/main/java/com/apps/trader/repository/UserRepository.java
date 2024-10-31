package com.apps.trader.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.apps.trader.model.User;


public interface UserRepository extends JpaRepository<User, Long> {


User findByEmail(String email);
}
