package com.baibei.hengjia.gateway.utils;

import com.alibaba.fastjson.JSON;
import com.baibei.hengjia.common.tool.page.PageParam;
import com.baibei.hengjia.gateway.dto.JsonRequest;
import org.springframework.util.StringUtils;
import sun.misc.BASE64Encoder;

import javax.crypto.Mac;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.beans.BeanInfo;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.net.URLEncoder;
import java.util.*;

/**
 * @author: 会跳舞的机器人
 * @date: 2019/5/2 9:19 PM
 * @description:
 */
public class SignUtil {
    public static void main(String[] args) {
        JsonRequest jsonRequest = new JsonRequest();
        // 设置公共参数
        jsonRequest.setRequestId("28166cc73cc847fc9a743460bf48f097");
        jsonRequest.setAccessToken("QA3NCFO5ORJZBFOG6PH1LQZ2QE27XK91");
        jsonRequest.setAppKey("keyb0afbb6441e543d7b84cb35c16c8be64");
        jsonRequest.setDeviceId("abcedfg");
        jsonRequest.setPlatform("H5");
        jsonRequest.setSign("123");
        jsonRequest.setTimestamp("1556804140154");
        jsonRequest.setVersion("1.0");

        // 设置data业务参数
        Map<String, Object> data = new HashMap<>();
        data.put("productTradeNo", "NB001");
        data.put("price","100.12");
        data.put("count",10);
        data.put("direction","buy");
        jsonRequest.setData(data);

        // 设置分页参数
        PageParam pageParam = new PageParam();
        pageParam.setCurrentPage(1);
        pageParam.setPageSize(10);
        //jsonRequest.setPage(pageParam);

        System.out.println("jsonRequest=" + JSON.toJSONString(jsonRequest));

        String uri = "/auth/api/demo";
        String appSecret = "secret3dec33a1877d4214a0a8aacdf47470ee";
        // 验签过程
        // step1.jsonRequest转为map
        Map<String, Object> paramMap = SignUtil.bean2Map(jsonRequest);
        paramMap.remove("sign");
        // step2.参数排序
        Map<String, Object> sortedMap = SignUtil.sort(paramMap);
        // step3.拼接参数：key1Value1key2Value2
        String urlParams = SignUtil.groupStringParam(sortedMap);
        // step4.拼接URI和AppSecret：stringURI + stringParams + AppSecret
        StringBuffer sb = new StringBuffer();
        sb.append(uri).append(urlParams).append(appSecret);
        String signContent = sb.toString();
        System.out.println("signContent=" + signContent);
        // step5.签名
        String mySign = SignUtil.encryptHMAC(appSecret, signContent);
        System.out.println("签名结果:" + mySign);
    }


    /**
     * bean转map
     *
     * @param obj
     * @return
     */
    public static Map<String, Object> bean2Map(Object obj) {
        if (obj == null) {
            return null;
        }
        Map<String, Object> map = new HashMap<>();
        try {
            BeanInfo beanInfo = Introspector.getBeanInfo(obj.getClass());
            PropertyDescriptor[] propertyDescriptors = beanInfo.getPropertyDescriptors();
            for (PropertyDescriptor property : propertyDescriptors) {
                String key = property.getName();
                // 过滤class属性
                if (!key.equals("class")) {
                    // 得到property对应的getter方法
                    Method getter = property.getReadMethod();
                    Object value = getter.invoke(obj);
                    if (StringUtils.isEmpty(value)) {
                        continue;
                    }
                    if (key.equals("data")) {
                        @SuppressWarnings("unchecked")
                        Map<String, Object> dataMap = (Map<String, Object>) value;
                        Set<String> keySet = dataMap.keySet();
                        for (String dataKey : keySet) {
                            if (StringUtils.isEmpty(dataMap.get(dataKey))) {
                                continue;
                            }
                            map.put(dataKey, dataMap.get(dataKey));
                        }
                    } else if (key.equals("page")) {
                        PageParam page = (PageParam) value;
                        map.put("currentPage", page.getCurrentPage());
                        map.put("pageSize", page.getPageSize());
                    } else {
                        map.put(key, value);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return map;
    }

    /**
     * 按照红黑树（Red-Black tree）的 NavigableMap 实现 按照字母大小排序
     */
    public static Map<String, Object> sort(Map<String, Object> map) {
        if (map == null) {
            return null;
        }
        Map<String, Object> result = new TreeMap<>((Comparator<String>) (o1, o2) -> {
            return o1.compareTo(o2);
        });
        result.putAll(map);
        return result;
    }

    /**
     * 组合参数
     *
     * @return 如：key1Value1Key2Value2....
     */
    public static String groupStringParam(Map<String, Object> map) {
        if (map == null) {
            return null;
        }
        StringBuffer sb = new StringBuffer();
        for (Map.Entry<String, Object> item : map.entrySet()) {
            if (item.getValue() != null) {
                sb.append(item.getKey());
                // 如果是集合,则转化成集合的json字符串
                if (item.getValue() instanceof List) {
                    sb.append(JSON.toJSONString(item.getValue()));
                } else {
                    sb.append(item.getValue());
                }
            }
        }
        return sb.toString();
    }

    /**
     * HMAC加密
     *
     * @param key     私钥
     * @param content 加密内容
     */
    public static String encryptHMAC(String key, String content) {
        try {
            Mac mac = Mac.getInstance("HmacSHA256");
            SecretKey secretKey = new SecretKeySpec(key.getBytes("UTF-8"), mac.getAlgorithm());
            mac.init(secretKey);
            byte[] data = mac.doFinal(content.getBytes("UTF-8"));
            String base64Result = new BASE64Encoder().encode(data);
            return URLEncoder.encode(base64Result, "UTF-8");
        } catch (Exception e) {
            return content;
        }
    }
}
