package com.baibei.hengjia.api.modules.settlement.biz;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baibei.hengjia.api.modules.settlement.bean.vo.CleanVo;
import com.baibei.hengjia.common.tool.utils.DateUtil;
import com.baibei.hengjia.common.tool.utils.NumberUtil;
import com.sdb.util.Base64;
import com.sdb.util.SignUtil;

import java.io.*;
import java.math.BigDecimal;
import java.security.SecureRandom;
import java.util.*;

/**
 * @author: 会跳舞的机器人
 * @date: 2019/6/27 3:54 PM
 * @description:
 */
public class FileReader {
    public static void main(String[] args) throws Exception {
        BigDecimal totalFee = NumberUtil.roundFloor(new BigDecimal("62").multiply((new BigDecimal("1").subtract(new BigDecimal("66.67").divide(new BigDecimal("100"))))), 2);
        BigDecimal restRate = new BigDecimal("1").subtract(new BigDecimal("66.67"));
        System.out.println(NumberUtil.roundFloor(new BigDecimal("62").multiply(restRate),2));
        System.out.println(totalFee);


    }


    private static Map<String, String> generateCleanFile(List<String> list) {
        Map<String, String> result = new HashMap<>();
        // BatQs+交易网代码（4位）+时间（14位）.txt
        String fileName = new StringBuffer("BatQs").append("AAAAA").append(DateUtil.yyyyMMddHHmmssNoLine.get().format(new Date())).append(".txt").toString();
        try {
            File writeName = new File("/Users/dreyer/clean" + "/" + fileName);
            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(writeName), "GBK"));

            // 写第一行
            bw.write("1");
            bw.write("\r\n"); // \r\n即为换行
            for (String s : list) {
                bw.write(s);
                bw.write("\r\n"); // \r\n即为换行
            }
            bw.close();
            result.put("fileSize", writeName.length() + "");
        } catch (IOException e) {
            e.printStackTrace();
        }
        result.put("fileName", fileName);
        return result;
    }


}
