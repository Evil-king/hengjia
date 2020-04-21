package com.baibei.hengjia.common.tool.utils;

import org.apache.commons.lang3.RandomStringUtils;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author: hwq
 * @description: 系统编码生成工具类
 */
public class CodeUtils {


    public static SimpleDateFormat DATE_FORMAT_yyMMddHHmmssSSS = new SimpleDateFormat("yyMMddHHmmssSSS");

    /**
     * 生成唯一订单号
     *
     * @return
     */
    public static String generateTreeOrderCode() {
        return new SimpleDateFormat("yyMMddHHmmssSSS").format(new Date()) + RandomStringUtils.randomNumeric(5);
    }

    /**
     * 生成唯一提货订单号
     *
     * @return
     */
    public static String generateTreeDeliveryCode() {
        return "D" + DATE_FORMAT_yyMMddHHmmssSSS.format(new Date()) + RandomStringUtils.randomNumeric(6);
    }

    /**
     * 生成规则资产编号:USERID+五位编号（从1开始，不够前补0）
     *
     * @return
     */
    public static String getNewEquipmentNo(String equipmentType, String equipmentNo) {
        String newEquipmentNo = "00000";

        if (equipmentNo != null && !equipmentNo.isEmpty()) {
            int newEquipment = Integer.parseInt(equipmentNo) + 1;
            newEquipmentNo = String.format(equipmentType + "%05d", newEquipment);
        }
        return newEquipmentNo;
    }

    /**
     * 生成资产编码
     *
     * @return
     */
    public static String getAssetNo() {
        return "Y" + RandomStringUtils.randomNumeric(10);
    }

    public static void main(String[] args) {
        String code = CodeUtils.generateTreeOrderCode();
        System.out.println(code.length());
    }

}
