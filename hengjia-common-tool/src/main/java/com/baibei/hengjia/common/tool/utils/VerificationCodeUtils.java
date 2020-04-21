package com.baibei.hengjia.common.tool.utils;

import java.util.Random;

/**
 * @author: hyc
 * @date: 2019/5/28 18:19
 * @description:
 */
public class VerificationCodeUtils {
    private static String str = "abcdefghigklmnopqrstuvwxyzABCDEFGHIGKLMNOPQRSTUVWXYZ0123456789";

    public static String getRandomCode(Integer number) {
        Random r = new Random();
        String arr[] = new String[number];
        String b = "";
        for (int i = 0; i < number; i++) {
            int n = r.nextInt(62);

            arr[i] = str.substring(n, n + 1);
            b += arr[i];

        }
        return b;
    }

    /**
     * 是否包含大小写字母及数字且在6-20位
     * 判断用户名和密码是否负责规范
     *
     * @param str
     * @return
     */
    public static boolean isLetterDigit(String str) {
        boolean isDigit = false;//定义一个boolean值，用来表示是否包含数字
        boolean isLetter = false;//定义一个boolean值，用来表示是否包含字母
        for (int i = 0; i < str.length(); i++) {
            if (Character.isDigit(str.charAt(i))) {  //用char包装类中的判断数字的方法判断每一个字符
                isDigit = true;
            } else if (Character.isLetter(str.charAt(i))) { //用char包装类中的判断字母的方法判断每一个字符
                isLetter = true;
            }
        }
        String regex = "^[a-zA-Z0-9]{6,20}$";
        boolean isRight = isDigit && isLetter && str.matches(regex);
        return isRight;
    }


}
