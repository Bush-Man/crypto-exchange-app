package com.apps.trader.serivice;

import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.apps.trader.model.TwoFactorOTP;
import com.apps.trader.model.User;
import com.apps.trader.repository.TwoFactorOtpRepository;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
class TwoFactorOtpServiceImpl implements TwoFactorOtpService {
    
    @Autowired
    private final TwoFactorOtpRepository twoFactorOtpRepository; 

    @Override
    public TwoFactorOTP createTwoFactorOtp(User user, String otp, String jwt) {
        UUID uuid = UUID.randomUUID();
        String id = uuid.toString();

        TwoFactorOTP twoFactorOtp = new TwoFactorOTP();
        twoFactorOtp.setId(id);
        twoFactorOtp.setOtp(otp);
        twoFactorOtp.setUser(user);
        twoFactorOtp.setJwt(jwt);

        TwoFactorOTP savedOtp = twoFactorOtpRepository.save(twoFactorOtp);

        return savedOtp;

    }

    @Override
    public TwoFactorOTP findOtpByUser(Long userId) {
        return twoFactorOtpRepository.findByUserId(userId);
    }

    @Override
    public TwoFactorOTP findOtpById(String otpId) {
      Optional<TwoFactorOTP> otp = twoFactorOtpRepository.findById(otpId);
        return otp.orElse(null);
    }

    @Override
    public boolean verifyOtp(TwoFactorOTP twoFactorOtp, String otp) {
        return twoFactorOtp.getOtp().equals(otp);
        
    }

    @Override
    public void deleteTwoFactorOtp(TwoFactorOTP twoFactorOtp) {
        twoFactorOtpRepository.delete(twoFactorOtp);
    }

}
