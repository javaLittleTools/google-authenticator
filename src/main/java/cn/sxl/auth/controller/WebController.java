package cn.sxl.auth.controller;

import cn.sxl.utils.otp.OtpUtils;
import cn.sxl.utils.qrcode.QrCodeArgs;
import cn.sxl.utils.qrcode.QrCodeUtils;
import com.google.zxing.EncodeHintType;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
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

    private String secretKey;

    @GetMapping("/qrcode")
    public void generateQRCode(HttpServletResponse response) {

        Map<EncodeHintType, Object> hints = new HashMap<>();
        hints.put(EncodeHintType.CHARACTER_SET, "UTF-8");
        hints.put(EncodeHintType.MARGIN, 0);

        QrCodeArgs qrCodeArgs = new QrCodeArgs();
        qrCodeArgs.setWidth(200);
        qrCodeArgs.setHeight(200);
        qrCodeArgs.setHints(hints);

        try {
            BitMatrix bitMatrix = QrCodeUtils.generateQRCode(qrCodeContent(), response, qrCodeArgs);
            MatrixToImageWriter.writeToStream(bitMatrix, "png", response.getOutputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @GetMapping("/verify/{code}")
    public boolean verifyCode(@PathVariable("code") Long code) {
        long t = System.currentTimeMillis();
        OtpUtils ga = new OtpUtils();
        ga.setWindowSize(5);
        System.out.println("code:" + code);
        System.out.println("secretKey:" + secretKey);
        return ga.check_code(secretKey, code, t);
    }

    private String qrCodeContent() {
        secretKey = OtpUtils.generateSecretKey();
        System.out.println("secret:" + secretKey);
        return OtpUtils.getQRBarcode("tulip_sxl@outlook.com", secretKey);
    }

}
