package org.example.authenticate;

import org.apache.commons.codec.digest.DigestUtils;
import org.example.dao.IUserRepository;
import org.example.model.User;

public class Authenticator {
    public static User login(String login, String password, IUserRepository userRepository) {
        User userFromDb = userRepository.getUser(login);
        if (userFromDb != null && hashPassword(password).equals(userFromDb.getPassword())) {
            return userFromDb;
        }
        return null;
    }

    public static String hashPassword(String password) {
        return DigestUtils.sha256Hex(password);
    }

}
