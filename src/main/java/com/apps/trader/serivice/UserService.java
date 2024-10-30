package com.apps.trader.serivice;

import com.apps.trader.enums.VerificationType;
import com.apps.trader.model.User;


public interface UserService {

    public User findUserByEmail(String email) throws Exception;

    public User findUserByJwt(String jwt) throws Exception;

    public User findUserById(Long userId) throws Exception;

    public User EnableTwoFactorAuthentication(VerificationType sendTo ,Long userId)throws Exception;

    public String updatePassword(User user, String oldPassword, String newPassword) throws Exception;


}
