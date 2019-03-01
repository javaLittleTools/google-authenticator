package cn.sxl.auth.controller;

import cn.sxl.auth.entity.User;
import cn.sxl.auth.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author SxL
 * @since 2.0.1
 * 2019-03-01 13:07
 */

@RestController
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/user/{email}")
    public User getUserByEmail(@PathVariable("email") String email) {
        return userService.getUserByEmail(email);
    }

    @GetMapping("/user/secret/{email}")
    public String getUserSecretByEmail(@PathVariable("email") String email) {
        return userService.getSecretByEmail(email);
    }
}
