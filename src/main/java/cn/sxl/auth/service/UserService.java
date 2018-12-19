package cn.sxl.auth.service;


import cn.sxl.auth.entity.User;

import java.util.Collection;

/**
 * 用户 Service
 *
 * @author SxL
 * @since Created on 12/19/2018 3:06 PM.
 */
public interface UserService {

    int getNames(Collection<String> result);

    String getSecret(String email);

    User addUser(User user);
}
