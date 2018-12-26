package cn.sxl.auth.controller;

import cn.sxl.auth.entity.User;
import cn.sxl.auth.repository.UserRepository;
import cn.sxl.auth.service.UserService;
import cn.sxl.utils.otp.OtpUtils;
import cn.sxl.utils.qrcode.QrCodeUtils;
import com.google.common.collect.Maps;
import com.google.zxing.EncodeHintType;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
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
public class WebController {

    private final UserRepository userRepository;

    private final UserService userService;

    private String secretKey;

    @Autowired
    public WebController(UserRepository userRepository, UserService userService) {
        this.userRepository = userRepository;
        this.userService = userService;
    }

    @GetMapping("/qrcode/{email}")
    public void generateQRCode(HttpServletResponse response, @PathVariable String email) {
        secretKey = getSecretKey();

        TotpController totpController = new TotpController(this.userService, this.userRepository);
        totpController.addUser(email, secretKey);

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

    @GetMapping("/verify/{email}/{code}")
    public ResponseEntity<String> verifyCode(@PathVariable String email, @PathVariable Long code) {
        long t = System.currentTimeMillis();
        OtpUtils otpUtils = new OtpUtils();
        otpUtils.setWindowSize(5);

        System.out.println("code:" + code);
        System.out.println("secretKey:" + secretKey);

        if ("".equals(secretKey) || secretKey == null) {
            User user = userRepository.findByEmail(email);
            secretKey = user.getSecret();
        }

        return ResponseEntity.ok(otpUtils.check_code(secretKey, code, t) ? "<h1>Success</h1>" : "<h1>Failed</h1>");
    }

    private String getSecretKey() {
        return OtpUtils.generateSecretKey();
    }

}
