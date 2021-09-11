/*
 * arg license
 *
 */

package com.arg.common.helper;

import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;

import com.arg.common.exception.ArgException;

public class PasswordGenerator {

    private static final String CHAR_LOWER = "abcdefghijklmnopqrstuvwxyz";
    private static final String CHAR_UPPER = CHAR_LOWER.toUpperCase();
    private static final String NUMBER = "0123456789";
    private static final String OTHER_CHAR = "!@#$%&*?";

    // optional, make it more random
    private static final String PASSWORD_ALLOW_BASE_SHUFFLE =
            shuffleString(CHAR_LOWER + CHAR_UPPER + NUMBER + OTHER_CHAR);

    // optional, make it more random
    private static final String ALPHA_NUMERIC_ALLOW_BASE_SHUFFLE = shuffleString(CHAR_LOWER + CHAR_UPPER + NUMBER);

    private static final String ALPHA_NUMERIC_CAP_ALLOW_BASE_SHUFFLE = shuffleString(CHAR_UPPER + NUMBER);

    private static final String ALPHABATIC_ALLOW_BASE_SHUFFLE = shuffleString(CHAR_LOWER + CHAR_UPPER);

    private static final String NUMERIC_ALLOW_BASE_SHUFFLE = shuffleString(NUMBER);

    private static SecureRandom random = new SecureRandom();

    // shuffle
    private static String shuffleString(String string) {
        List<String> letters = Arrays.asList(string.split(""));
        Collections.shuffle(letters);
        return letters.stream().collect(Collectors.joining());
    }

    public static String gererateOtp(int length) {
        String numbers = "0123456789";
        Random rndm_method = new Random();
        char[] otp = new char[length];
        for (int i = 0; i < length; i++) {
            otp[i] = numbers.charAt(rndm_method.nextInt(numbers.length()));
        }
        return String.valueOf(otp);
        // return "1234";// TODO for testing
    }

    public static String generateRandomPassword(int length) {
        if (length < 1)
            throw new IllegalArgumentException();

        StringBuilder sb = new StringBuilder(length);
        for (int i = 0; i < length; i++) {

            int rndCharAt = random.nextInt(PASSWORD_ALLOW_BASE_SHUFFLE.length());
            char rndChar = PASSWORD_ALLOW_BASE_SHUFFLE.charAt(rndCharAt);

            sb.append(rndChar);
        }
        return sb.toString();
    }

    public static String generateNumericRandomCode(int length) {
        if (length < 1)
            throw new IllegalArgumentException();

        StringBuilder sb = new StringBuilder(length);
        for (int i = 0; i < length; i++) {

            int rndCharAt = random.nextInt(NUMERIC_ALLOW_BASE_SHUFFLE.length());
            char rndChar = NUMERIC_ALLOW_BASE_SHUFFLE.charAt(rndCharAt);

            sb.append(rndChar);

        }
        return sb.toString();
    }

    public static String generateAlphaNumericCode(int length) {
        if (length < 1)
            throw new IllegalArgumentException();

        StringBuilder sb = new StringBuilder(length);
        for (int i = 0; i < length; i++) {

            int rndCharAt = random.nextInt(ALPHA_NUMERIC_ALLOW_BASE_SHUFFLE.length());
            char rndChar = ALPHA_NUMERIC_ALLOW_BASE_SHUFFLE.charAt(rndCharAt);

            sb.append(rndChar);

        }
        return sb.toString();
    }

    public static String generateAlphaNumericCapCode(int length) {
        if (length < 1)
            throw new IllegalArgumentException();

        StringBuilder sb = new StringBuilder(length);
        for (int i = 0; i < length; i++) {

            int rndCharAt = random.nextInt(ALPHA_NUMERIC_CAP_ALLOW_BASE_SHUFFLE.length());
            char rndChar = ALPHA_NUMERIC_CAP_ALLOW_BASE_SHUFFLE.charAt(rndCharAt);

            sb.append(rndChar);

        }
        return sb.toString();
    }

    public static String generateAlphabeticCode(int length) {
        if (length < 1)
            throw new IllegalArgumentException();

        StringBuilder sb = new StringBuilder(length);
        for (int i = 0; i < length; i++) {

            int rndCharAt = random.nextInt(ALPHABATIC_ALLOW_BASE_SHUFFLE.length());
            char rndChar = ALPHABATIC_ALLOW_BASE_SHUFFLE.charAt(rndCharAt);

            sb.append(rndChar);

        }
        return sb.toString();
    }

    public static boolean validateOtp(String otp, String encodedOtp, LocalDateTime expirationDate) {
        if (encodedOtp.equals(otp) && LocalDateTime.now().isBefore(expirationDate)) {
            return true;
        }
        throw new ArgException("Wrong OTP or expired!", HttpStatus.BAD_REQUEST);
    }

}
