package com.baibei.hengjia.api.modules.cash.withdrawProsscess;

import com.baibei.hengjia.api.modules.cash.template.WithdrawTemplate;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author hwq
 * @date 2019/06/10
 * <p>
 * 交易网-->银行(1318接口文档中的代号)
 * 平安出金
 * </p>
 */
@Slf4j
@Component
@Transactional(rollbackFor = Exception.class)
public class PinganProcess extends WithdrawTemplate{
}
