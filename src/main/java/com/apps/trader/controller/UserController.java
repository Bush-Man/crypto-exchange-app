package com.apps.trader.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import com.apps.trader.serivice.EmailService;
import com.apps.trader.serivice.UserService;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.apps.trader.model.User;

import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.apps.trader.enums.VerificationType;
import org.springframework.web.bind.annotation.PostMapping;




@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserService userService;
    @Autowired
    private EmailService emailService;

    @GetMapping("/{userId}")
    public ResponseEntity<User> getUserById(@RequestParam Long userId) throws Exception {

        User user = userService.findUserById(userId);

        return new ResponseEntity<>(user, HttpStatus.OK);

    }

    
    @GetMapping("/{email}")
    public ResponseEntity<User> getUserByEmail(@RequestParam String email) throws Exception {

        User user = userService.findUserByEmail(email);

        return new ResponseEntity<>(user, HttpStatus.OK);

    }

    @PatchMapping("/enable-two-factor/{userId}")
    public ResponseEntity<User> enableTwoFactorAuthentication(@PathVariable Long userId) throws Exception {
        VerificationType verificationType = VerificationType.EMAIL;
        User user = userService.EnableTwoFactorAuthentication(verificationType, userId);
        return new ResponseEntity<>(user, HttpStatus.OK);
    }
    
    
    
    
    
}
