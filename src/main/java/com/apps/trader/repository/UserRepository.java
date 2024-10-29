package com.apps.trader.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.apps.trader.model.User;
import java.util.List;


public interface UserRepository extends JpaRepository<User, Long> {


User findByEmail(String email);
}
