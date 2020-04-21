package com.baibei.hengjia.common.tool.validate;

import com.alibaba.fastjson.JSONObject;
import com.baibei.hengjia.common.tool.utils.SpringUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * @author: 会跳舞的机器人
 * @date: 2019/5/27 7:24 PM
 * @description: 业务数据校验服务
 */
@Slf4j
@Component
public class ValidateService {
    private Class[] validatos = null;

    public void setValidatos(Class[] validatos) {
        this.validatos = validatos;
    }

    /**
     * 遍历所有的校验器进行校验
     *
     * @param tradeDataContext
     */
    public void validate(ValidatorDataContext tradeDataContext) {
        log.info("交易校验流程开始,{}", JSONObject.toJSONString(tradeDataContext));
        long start = System.currentTimeMillis();
        for (Class className : validatos) {
            // 遍历获取验证器并执行验证
            getValidator(className).validate(tradeDataContext);
        }
        log.info("校验流程通过,{},耗时={}ms", JSONObject.toJSONString(tradeDataContext), (System.currentTimeMillis() - start));
    }

    /**
     * 获取校验器
     *
     * @param className
     * @return
     */
    private ValidatorTemplate getValidator(Class className) {
        return (ValidatorTemplate) SpringUtil.getBean(className);
    }

}
