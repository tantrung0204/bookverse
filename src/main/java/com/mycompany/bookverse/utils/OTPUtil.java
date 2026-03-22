package com.mycompany.bookverse.utils;

import java.security.SecureRandom;

/**
 *
 *
 * @author TrungNT - CE200064
 */
public class OTPUtil {

    private static final SecureRandom random = new SecureRandom();

    /**
     * Generates a random 6-digit OTP string.
     *
     * @return a 6-digit OTP as String
     */
    public static String generateOTP() {
        int otp = random.nextInt(900000) + 100000; // 100000 to 999999
        return String.valueOf(otp);
    }
}
