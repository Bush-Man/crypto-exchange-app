package com.apps.trader.OtpUtils;

import java.util.Random;

import org.springframework.stereotype.Service;

@Service
public class OtpUtils {

    public String generateOtp() {
        int otpLength = 6;
        Random random = new Random();

        StringBuilder otp = new StringBuilder(otpLength);
        for(int i = 0; i<otpLength; i++) {
            otp.append(random.nextInt(10));
            
        }

     return otp.toString();
   }
}
