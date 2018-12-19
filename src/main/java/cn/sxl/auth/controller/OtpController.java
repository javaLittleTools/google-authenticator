package cn.sxl.auth.controller;

import cn.sxl.auth.entity.User;
import cn.sxl.auth.otp.OtpProvider;
import cn.sxl.auth.otp.OtpSourceException;
import cn.sxl.auth.repository.UserRepository;
import cn.sxl.auth.service.UserService;
import com.beust.jcommander.internal.Maps;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * 返回 OTP 密码
 *
 * @author SxL
 * @since Created on 12/19/2018 6:12 PM.
 */
@RestController
public class OtpController {

    private final UserService userService;

    private final UserRepository userRepository;

    @Autowired
    public OtpController(UserService userService, UserRepository userRepository) {
        this.userService = userService;
        this.userRepository = userRepository;
    }

    @GetMapping("/otp/{email}")
    public Map<String, String> getOtp(@PathVariable("email") String email) {
        Map<String, String> map = Maps.newHashMap();
        OtpProvider otpProvider = new OtpProvider(this.userService);

        try {
            map.put("currentCode", otpProvider.getCurrentCode(email));
            map.put("nextCode", otpProvider.getNextCode(email));
        } catch (OtpSourceException e) {
            e.printStackTrace();
            map.put("error", "error");
        }

        return map;
    }

    @PostMapping("/user")
    public void addUser(@RequestBody User user) {
        User user1 = userRepository.findByEmail(user.getEmail());

        if (user1 != null) {
            user1.setSecret(user.getSecret());
            userRepository.saveAndFlush(user1);
        } else {
            userRepository.save(user);
        }
    }

    @GetMapping("/user/{email}/{secret}")
    public void addUser(@PathVariable String email, @PathVariable String secret) {
        User user = userRepository.findByEmail(email);

        if (user != null) {
            user.setSecret(secret);

            userRepository.saveAndFlush(user);
        } else {
            user = new User();
            user.setEmail(email);
            user.setSecret(secret);

            userService.addUser(user);
        }
    }
}
