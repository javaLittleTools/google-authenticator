/*
 * Copyright 2010 Google Inc. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package cn.sxl.auth.totp;

import cn.sxl.auth.service.UserService;
import cn.sxl.auth.util.Utilities;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.security.GeneralSecurityException;
import java.util.Collection;

/**
 * Class containing implementation of HOTP/TOTP.
 * Generates OTP codes for one or more accounts.
 *
 * @author Steve Weis (sweis@google.com)
 * @author Cem Paya (cemp@google.com)
 */
@Component
public class OtpProvider implements OtpSource {

    private final UserService userService;

    // HOTP or TOTP
    private static final int PIN_LENGTH = 6;

    /**
     * Default passcode timeout period (in seconds)
     */
    public static final int DEFAULT_INTERVAL = 30;

    /**
     * Counter for time-based OTPs (TOTP).
     */
    private final TotpCounter totpCounter = new TotpCounter(DEFAULT_INTERVAL);

    @Autowired
    public OtpProvider(UserService userService) {
        this.userService = userService;
    }

    @Override
    public int enumerateAccounts(Collection<String> result) {
        return userService.getNames(result);
    }

    @Override
    public String getNextCode(String email) throws OtpSourceException {
        // Account name is required.
        if (email == null) {
            throw new OtpSourceException("No account name");
        }

        String secret = userService.getSecret(email);

        // For time-based OTP, the state is derived from clock.
        long otpState = totpCounter.getValueAtTime(
                Utilities.millisToSeconds(System.currentTimeMillis()) + DEFAULT_INTERVAL);

        return computePin(secret, otpState);
    }

    // This variant is used when an additional challenge, such as URL or
    // transaction details, are included in the OTP request.
    // The additional string is appended to standard HOTP/TOTP state before
    // applying the MAC function.

    @Override
    public TotpCounter getTotpCounter() {
        return totpCounter;
    }


    public String getCurrentCode(String email) throws OtpSourceException {
        // Account name is required.
        if (email == null) {
            throw new OtpSourceException("No account name");
        }

        String secret = userService.getSecret(email);

        // For time-based OTP, the state is derived from clock.
        long otp_state = totpCounter.getValueAtTime(
                Utilities.millisToSeconds(System.currentTimeMillis()));

        return computePin(secret, otp_state);
    }

    /**
     * Computes the one-time PIN given the secret key.
     *
     * @param secret    the secret key
     * @param otpState current token state (counter or time-interval)
     * @return the PIN
     */
    public String computePin(String secret, long otpState) throws OtpSourceException {
        if (secret == null || secret.length() == 0) {
            throw new OtpSourceException("Null or empty secret");
        }

        try {
            PasscodeGenerator.Signer signer = PasscodeGenerator.getSigningOracle(secret);
            PasscodeGenerator pcg = new PasscodeGenerator(signer, PIN_LENGTH);


            return pcg.generateResponseCode(otpState);

        } catch (GeneralSecurityException e) {
            throw new OtpSourceException("Crypto failure", e);
        }
    }

    /**
     * Reads the secret key that was saved on the phone.
     *
     * @param user Account name identifying the user.
     * @return the secret key as base32 encoded string.
     */
    String getSecret(String user) {
        return userService.getSecret(user);
    }

//    public static void main(String[] args) throws OtpSourceException {
//        OtpProvider otpProvider = new OtpProvider();
//        System.out.println(otpProvider.getCurrentCode("sxl"));
//        System.out.println(otpProvider.getNextCode("sxl"));
//    }
}
