package com.baibei.hengjia.api.modules.cash.component;

import com.baibei.hengjia.api.modules.sms.util.RandomUtils;
import com.baibei.hengjia.common.core.mybatis.Service;
import org.springframework.stereotype.Component;
import tk.mybatis.mapper.entity.Condition;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Component
public class SerialNumberComponent<T> {

    /**
     * 生成19位订单号
     *
     * @param Class
     * @param service
     * @param prefix
     * @param property
     * @return
     */
    public String generateOrderNo(Class<T> Class, Service<T> service, String prefix, String property) {
        StringBuilder orderNoStr = new StringBuilder();
        orderNoStr.append(prefix);
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyMMddHHmmss");
        orderNoStr.append(dtf.format(LocalDateTime.now()));
        orderNoStr.append(String.format("%04d", RandomUtils.getRandom(1, 10000)));
        String orderNo = orderNoStr.toString();
        List<T> result = findByNo(Class, service, property, orderNo);
        if (result.size() > 1) {  // 订单号重复
            return generateOrderNo(Class, service, prefix, property);
        }
        return orderNo;
    }

    public String generateThiredLogNo(Class<T> Class, Service<T> service, String property) {
        StringBuilder serialNumberStr = new StringBuilder();
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyMMddHHmmss");
        serialNumberStr.append(dtf.format(LocalDateTime.now()));
        serialNumberStr.append(String.format("%04d", RandomUtils.getRandom(1, 100000)));
        String serialNumberNo = serialNumberStr.toString();
        List<T> result = findByNo(Class, service, property, serialNumberNo);
        if (result.size() > 1) {  // 订单号重复
            return generateThiredLogNo(Class, service, property);
        }
        return serialNumberNo;
    }

    /**
     * 生成13位位随机流水号
     *
     * @return
     */
    public String generateThiredLogNo() {
        StringBuilder thiredLogNoStr = new StringBuilder();
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyMMddHHmmss");
        thiredLogNoStr.append(dtf.format(LocalDateTime.now()));
        thiredLogNoStr.append(String.format("%04d", RandomUtils.getRandom(1, 100000)));
        return thiredLogNoStr.toString();
    }

    /**
     * 检测唯一性
     *
     * @param Class
     * @param service
     * @param property
     * @param serialNumberNo
     * @return
     */
    public List<T> findByNo(Class<T> Class, Service<T> service, String property, String serialNumberNo) {
        Condition condition = new Condition(Class);
        condition.createCriteria().andEqualTo(property, serialNumberNo);
        List<T> result = service.findByCondition(condition);
        return result;
    }

}
