package com.apps.trader.serivice;

import com.apps.trader.model.TwoFactorOTP;
import com.apps.trader.model.User;

public interface TwoFactorOtpService {

    public TwoFactorOTP createTwoFactorOtp(User user, String otp, String jwt);

    public TwoFactorOTP findOtpByUser(Long userId);

    public TwoFactorOTP findOtpById(String otpId);

    public boolean verifyOtp(TwoFactorOTP twoFactorOtp, String otp);
    
    public void deleteTwoFactorOtp(TwoFactorOTP twoFactorOtp);



}
