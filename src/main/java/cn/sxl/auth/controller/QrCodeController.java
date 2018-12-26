package cn.sxl.auth.controller;

import cn.sxl.auth.entity.User;
import cn.sxl.auth.service.UserService;
import cn.sxl.utils.otp.OtpUtils;
import cn.sxl.utils.qrcode.QrCodeUtils;
import com.google.common.collect.Maps;
import com.google.zxing.EncodeHintType;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;

/**
 * 生成二维码
 *
 * @author SxL
 * @since 1.0
 * Created on 12/12/2018 5:07 PM.
 */
@RestController
public class QrCodeController {

    private final UserService userService;

    @Autowired
    public QrCodeController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/qrcode/{email}")
    public void generateQRCode(HttpServletResponse response, @PathVariable String email) {
        String secretKey = getSecretKey();

        addUser(email, secretKey);

        Map<EncodeHintType, Object> hints = Maps.newHashMap();
        hints.put(EncodeHintType.CHARACTER_SET, "UTF-8");
        hints.put(EncodeHintType.MARGIN, 0);

        try {
            BitMatrix bitMatrix = QrCodeUtils.generateQRCode(OtpUtils.getQRBarcode(email, secretKey), response, 300, 300, hints);
            MatrixToImageWriter.writeToStream(bitMatrix, "png", response.getOutputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void addUser(String email, String secret) {
        User user = userService.getUserByEmail(email);

        if (user != null) {
            user.setSecret(secret);

            userService.modifyUser(user);
        } else {
            user = new User();
            user.setEmail(email);
            user.setSecret(secret);

            userService.addUser(user);
        }
    }

    private String getSecretKey() {
        return OtpUtils.generateSecretKey();
    }

}
