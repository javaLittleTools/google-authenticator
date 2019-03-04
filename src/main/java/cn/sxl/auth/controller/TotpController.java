package cn.sxl.auth.controller;

import cn.sxl.auth.entity.User;
import cn.sxl.auth.service.UserService;
import cn.sxl.auth.totp.OtpProvider;
import cn.sxl.auth.totp.OtpSourceException;
import com.beust.jcommander.internal.Maps;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * 获取 TOTP 密码
 *
 * @author SxL
 * @since 1.0
 * Created on 12/19/2018 6:12 PM.
 */
@RestController
public class TotpController {

    private final UserService userService;

    @Autowired
    public TotpController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/totp/{email}")
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
}
