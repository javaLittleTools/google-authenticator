package cn.sxl.auth.controller;

import cn.sxl.auth.entity.Verify;
import cn.sxl.auth.service.UserService;
import cn.sxl.utils.otp.OtpUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * 验证模块控制器
 *
 * @author SxL
 * @since 1.0
 * Created on 12/26/2018 2:46 PM.
 */
@RestController
public class VerifyController {

    private final UserService userService;

    @Autowired
    public VerifyController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/verify/{email}/{code}")
    public ResponseEntity<String> verifyCode(@PathVariable String email, @PathVariable Long code) {
        long t = System.currentTimeMillis();
        OtpUtils otpUtils = new OtpUtils();
        otpUtils.setWindowSize(5);

        String secret = userService.getSecretByEmail(email);

        return ResponseEntity.ok(otpUtils.check_code(secret, code, t) ? "<h1>Success</h1>" : "<h1>Failed</h1>");
    }

    @PostMapping("/verify")
    public ResponseEntity verifyCode(@RequestBody Verify verify) {
        long t = System.currentTimeMillis();
        OtpUtils otpUtils = new OtpUtils();
        otpUtils.setWindowSize(5);

        String secret = userService.getSecretByEmail(verify.getEmail());
        return ResponseEntity.ok(otpUtils.check_code(secret, verify.getCode(), t) ? "<h1>Success</h1>" : "<h1>Failed</h1>");
    }
}
