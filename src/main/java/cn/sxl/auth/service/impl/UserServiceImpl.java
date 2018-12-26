package cn.sxl.auth.service.impl;

import cn.sxl.auth.entity.User;
import cn.sxl.auth.repository.UserRepository;
import cn.sxl.auth.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;

/**
 * 实现 {@link UserService}
 *
 * @author SxL
 * @since Created on 12/19/2018 3:07 PM.
 */
@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    @Autowired
    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public int getNames(Collection<String> result) {
        List<User> userList = userRepository.findAll();

        for (User user : userList) {
            result.add(user.getEmail());
        }

        return userList.size();
    }

    @Override
    public String getSecretByEmail(String email) {
        User user = userRepository.findByEmail(email);

        return user.getSecret();
    }

    @Override
    public User addUser(User user) {
        return userRepository.save(user);
    }

    @Override
    public User modifyUser(User user) {
        return userRepository.saveAndFlush(user);
    }

    @Override
    public User getUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }

}
