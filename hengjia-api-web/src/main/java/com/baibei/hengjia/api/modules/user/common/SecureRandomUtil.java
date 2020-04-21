
package com.baibei.hengjia.api.modules.user.common;

import java.security.SecureRandom;
import java.util.Random;

/**
 * @author: hyc
 * @date: 2019/5/27 20:43
 * @description:
 */
public class SecureRandomUtil {

    private static Random random = null;

    static {
        try {
            random = SecureRandom.getInstance("SHA1PRNG", "SUN");
        } catch (Exception ex) {
            random = new Random();
        }
    }

    private final static char[] CHARSET = new char[]{'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N',
            'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z', '0', '1', '2', '3', '4', '5', '6', '7', '8', '9'};

    private final static char[] NUMBER_CHARSET = new char[]{'0', '1', '2', '3', '4', '5', '6', '7', '8', '9'};

    /**
     * 生成有字母和数字组成的随机码.
     *
     * @param length 随机码的长度
     * @return
     * @since kuai 1.0
     */
    public static String generateRandomCode(int length) {
        StringBuilder sb = new StringBuilder();

        for (int i = 1; i < length + 1; i++) {
            int index = random.nextInt(CHARSET.length);
            char c = CHARSET[index];
            sb.append(c);
        }

        return sb.toString();
    }

    /**
     * 生成指定位数的随机数.
     *
     * @param length 随机数的长度
     * @return
     * @since kuai 1.0
     */
    public static String generateRandomNumber(int length) {
        StringBuilder sb = new StringBuilder();

        for (int i = 1; i < length + 1; i++) {
            int index = random.nextInt(NUMBER_CHARSET.length);
            char c = NUMBER_CHARSET[index];
            sb.append(c);
        }

        return sb.toString();
    }

}

