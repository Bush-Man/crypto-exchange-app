package com.apps.trader.serivice;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.apps.trader.model.User;
import com.apps.trader.repository.UserRepository;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class CustomUserDetailsService implements UserDetailsService{

    @Autowired
    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
       User user = userRepository.findByEmail(username);
       if (user == null){
        throw new UsernameNotFoundException("User " + username + " not found");
       }
       List<GrantedAuthority> authorities = new ArrayList<>();

       return new org.springframework.security.core.userdetails.User(user.getEmail(),user.getPassword(),authorities);
    }

}
