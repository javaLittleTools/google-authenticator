package cn.sxl.auth.controller;

import cn.sxl.auth.entity.User;
import cn.sxl.auth.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

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

    @PostMapping("/user")
    public void addUser(@RequestBody User user) {
        User existUser = userService.getUserByEmail(user.getEmail());

        if (existUser != null) {
            existUser.setSecret(user.getSecret());
            userService.modifyUser(existUser);
        } else {
            userService.addUser(user);
        }
    }
}
