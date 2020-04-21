package com.baibei.hengjia.common.tool.utils;

import org.springframework.util.StringUtils;

/**
 * @author: hyc
 * @date: 2019/5/27 11:31
 * @description:
 */
public class MobileUtils {
    /**
     * 判断是否为手机号
     *
     * @param mobiles
     * @return
     */
    public static boolean isMobileNO(String mobiles) {
        String telRegex = "[1]\\d{10}";
        // "[1]"代表第1位为数字1，"[3578]"代表第二位可以为3、5、8中的一个，"\\d{9}"代表后面是可以是0～9的数字，有9位。
        return mobiles.matches(telRegex);
    }

    public static String changeMobile(String mobile) {
        if (StringUtils.isEmpty(mobile)) {
            return null;
        }
        Boolean flag = isMobileNO(mobile);
        if (flag) {
            return mobile.substring(0, 3) + "****" + mobile.substring(7, mobile.length());
        } else {
            return mobile;
        }
    }


    /**
     * 隐藏一部分地址
     *
     * @param address 地址
     * @return
     */
    public static String hideAddress(String address) {
        if (StringUtils.isEmpty(address)) {
            return null;
        }
        StringBuilder stringBuffer = new StringBuilder(address.substring(0, 10));
        stringBuffer.append("***************");
        return stringBuffer.toString();
    }

    /**
     * 姓名加密
     *
     * @param name
     * @return
     */
    public static String changeName(String name) {
        if (name != null) {
            if (name.length() == 2) {
                name = name.substring(0, 1) + "*";
            } else if (name.length() > 2) {
                name = name.replaceAll("([\\u4e00-\\u9fa5]{1})(.*)", "$1" + createAsterisk(name.length() - 2)) + name.substring(name.length() - 1, name.length());
            }
            return name;
        } else {
            return name;
        }
    }

    public static String createAsterisk(int length) {
        StringBuffer stringBuffer = new StringBuffer();
        for (int i = 0; i < length; i++) {
            stringBuffer.append("*");
        }
        return stringBuffer.toString();
    }
}
