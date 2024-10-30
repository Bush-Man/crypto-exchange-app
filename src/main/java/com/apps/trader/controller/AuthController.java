package com.apps.trader.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.apps.trader.OtpUtils.OtpUtils;
import com.apps.trader.model.TwoFactorOTP;
import com.apps.trader.model.User;
import com.apps.trader.repository.UserRepository;
import com.apps.trader.response.AuthResponse;
import com.apps.trader.serivice.CustomUserDetailsService;
import com.apps.trader.serivice.EmailService;
import com.apps.trader.serivice.JwtService;
import com.apps.trader.serivice.TwoFactorOtpService;

import lombok.AllArgsConstructor;

@RestController
@AllArgsConstructor
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private final UserRepository userRepository;

    @Autowired
    private final CustomUserDetailsService customUserDetailsService;

    @Autowired
    private final JwtService jwtService;

    @Autowired
    private final OtpUtils otpUtils;

    @Autowired 
    private final TwoFactorOtpService twoFactorOtpService;

    @Autowired
    private EmailService emailService;

    @PostMapping("/register")
    public ResponseEntity<AuthResponse> registerUser(@RequestBody User user) throws Exception{
        
    User existingUser = userRepository.findByEmail(user.getEmail());
     if(existingUser != null){
        AuthResponse response = new AuthResponse();
    response.setStatus(false);
    response.setMessage("Email Already Exist");
    return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);

    }   
    User newUser = new User();
        newUser.setPassword(user.getPassword());
        newUser.setEmail(user.getEmail());
        newUser.setFullName(user.getFullName());
    
    User savedUser = userRepository.save(newUser);
    if (savedUser != null){
    AuthResponse response = new AuthResponse();
    response.setStatus(true);
    response.setMessage("registration successful");
    return new ResponseEntity<>(response,HttpStatus.CREATED);
    }else{
     AuthResponse response = new AuthResponse();
    response.setStatus(false);
    response.setMessage("registration failed");
    return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
         
    }


    }

      @PostMapping("/login")
      public ResponseEntity<AuthResponse> loginUser(@RequestBody User user) throws Exception {

          try {

              UserDetails existingUser = customUserDetailsService.loadUserByUsername(user.getEmail());

              if (existingUser == null) {
                  AuthResponse response = new AuthResponse();
                  response.setStatus(false);
                  response.setMessage("Wrong email or password");
                  return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
              }
              //check password
              if (!user.getPassword().equals(existingUser.getPassword())) {
                  System.out.printf("dont match");
                  AuthResponse response = new AuthResponse();
                  response.setStatus(false);
                  response.setMessage("Wrong email or password");
                  return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
              }

              UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(existingUser,
                      existingUser.getPassword(), existingUser.getAuthorities());
              if (authToken.toString().isEmpty()) {
                  AuthResponse response = new AuthResponse();
                  response.setStatus(false);
                  response.setMessage("Auth Token not created using UsernamePasswordAuthenticationToken");
              }

              SecurityContextHolder.getContext().setAuthentication(authToken);

              String jwtToken = jwtService.generateToken(authToken);

              User authenticatedUser = userRepository.findByEmail(user.getEmail());

              if (authenticatedUser.getTwoFactorAuth().isEnabled()) {
                  AuthResponse response = new AuthResponse();
                  response.setMessage("Sending OTP");
                  response.setTwoFactorAuthEnabled(true);
                  String otp = otpUtils.generateOtp();

                  TwoFactorOTP oldTwoFactorOTP = twoFactorOtpService.findOtpByUser(authenticatedUser.getId());
                  if (oldTwoFactorOTP != null) {
                      twoFactorOtpService.deleteTwoFactorOtp(oldTwoFactorOTP);
                  }
                  TwoFactorOTP newTwoFactorOTP = twoFactorOtpService.createTwoFactorOtp(authenticatedUser, otp,
                          jwtToken);

                  emailService.sendVerificationOtpEmail(authenticatedUser.getEmail(), otp);

                  response.setSession(newTwoFactorOTP.getId());
                  return new ResponseEntity<>(response, HttpStatus.ACCEPTED);

              }
              AuthResponse response = new AuthResponse();
              response.setStatus(true);
              response.setJwt(jwtToken);
              response.setMessage("Login Success");

              return new ResponseEntity<>(response, HttpStatus.CREATED);

          } catch (Exception e) {

              AuthResponse response = new AuthResponse();
              response.setStatus(false);
              response.setMessage(e.getMessage());

              return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
          }
      }
 
      @PostMapping("/verify/{otp}/{id}")
      public ResponseEntity<AuthResponse> verifyLoginOtp(@PathVariable String otp, @RequestParam String otpId) throws  Exception {
          
          TwoFactorOTP twoFactorOTP = twoFactorOtpService.findOtpById(otpId);
          boolean isOtpValid = twoFactorOtpService.verifyOtp(twoFactorOTP, otp);
          if (isOtpValid) {
            AuthResponse response = new AuthResponse();
              
            response.setMessage("OTP Code valid");
            response.setTwoFactorAuthEnabled(isOtpValid);
            response.setJwt(twoFactorOTP.getJwt());
            
            return new ResponseEntity<>(response, HttpStatus.OK);

          }
          throw new Exception("Invalid OTP");
      }
      
      

}   
    
    
         
    


