package com.baibei.hengjia.api.modules.trade.biz.validator;

import com.baibei.hengjia.api.modules.match.model.BuymatchLog;
import com.baibei.hengjia.api.modules.match.service.IBuymatchLogService;
import com.baibei.hengjia.api.modules.trade.service.IBuyMatchWhiteListService;
import com.baibei.hengjia.api.modules.trade.service.IDealOrderService;
import com.baibei.hengjia.common.tool.api.ResultEnum;
import com.baibei.hengjia.common.tool.exception.ServiceException;
import com.baibei.hengjia.common.tool.utils.DateUtil;
import com.baibei.hengjia.common.tool.validate.ValidatorDataContext;
import com.baibei.hengjia.common.tool.validate.ValidatorTemplate;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * @author: 会跳舞的机器人
 * @date: 2019/10/12 14:21
 * @description:
 */
@Component
@Slf4j
public class BuyMatchValidator extends ValidatorTemplate {
    @Autowired
    private IBuymatchLogService buymatchLogService;
    @Autowired
    private IDealOrderService dealOrderService;
    @Autowired
    private IBuyMatchWhiteListService buyMatchWhiteListService;

    @Override
    protected void validateInner(ValidatorDataContext validatorDataContext) throws ServiceException {
        log.info("买一配一校验器正在执行");
        Object customerNo = validatorDataContext.getDataContextMap().get("customerNo");
        if (customerNo == null) {
            throw new ServiceException("客户编号参数不存在");
        }
        boolean whiteFlag = buyMatchWhiteListService.isWhiteList(customerNo.toString());
        if (whiteFlag) {
            log.info("客户{}为买一配一的白名单", customerNo.toString());
            return;
        }
        // 判断是否已经买入零售商品
        boolean isBuy = dealOrderService.isBuyToday(customerNo.toString());
        if (!isBuy) {
            log.info("客户{}今日未买入零售商品", customerNo.toString());
            throw new ServiceException(ResultEnum.NOT_BUY_OR_MATCH);
        }
        // 判断是否已经配票
        List<BuymatchLog> buymatchLogList = buymatchLogService.findBy("0002",
                DateUtil.yyyyMMddNoLine.get().format(new Date()), customerNo.toString(), new BigDecimal("960"));
        if (CollectionUtils.isEmpty(buymatchLogList)) {
            log.info("客户{}今日未配票", customerNo.toString());
            throw new ServiceException(ResultEnum.NOT_BUY_OR_MATCH);
        }
    }
}