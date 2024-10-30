package com.apps.trader.serivice;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.apps.trader.OtpUtils.OtpUtils;
import com.apps.trader.enums.VerificationType;
import com.apps.trader.model.TwoFactorAuth;
import com.apps.trader.model.User;
import com.apps.trader.repository.UserRepository;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class UserServiceImpl implements UserService {

    @Autowired
    private final UserRepository userRepository;

    @Autowired
    private final EmailService emailService;

    @Autowired
    private final OtpUtils otpUtils;

    @Override
    public User findUserByEmail(String email) throws Exception {
        User user = userRepository.findByEmail(email);
        if (user == null) {
            throw new Exception("user not found");
        }
        return user;

    }

    @Override
    public User findUserById(Long userId) throws Exception {
        User user = userRepository.findById(userId).get();
        if (user == null) {
            throw new Exception("user not found");
        }
        return user;

    }

    @Override
    public User EnableTwoFactorAuthentication(VerificationType sendTo, Long userId) throws Exception {
        try {
            User existingUser = findUserById(userId);
            boolean isTwoFactorAuthEnabled = existingUser.getTwoFactorAuth().isEnabled();
            if (isTwoFactorAuthEnabled) {
                throw new Exception("Two Factor Authentication Has Already Been Enabled");
            } else {
                TwoFactorAuth twoFactorAuth = new TwoFactorAuth();
                twoFactorAuth.setSendTo(sendTo);
                twoFactorAuth.setEnabled(true);
                existingUser.setTwoFactorAuth(twoFactorAuth);
                userRepository.save(existingUser);
                return existingUser;
            }

        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }

    }

    @Override
    public String updatePassword(User user, String oldPassword, String newPassword) throws Exception {
        try {
            User existingUser = findUserByEmail(user.getEmail());
            boolean isPasswordMatch = existingUser.getPassword().equals(oldPassword);
            if (!isPasswordMatch) {
                return "Invalid Old Password";
            }
            existingUser.setPassword(newPassword);
            userRepository.save(existingUser);
            return "password updated successfullt";

        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }

    }

}
