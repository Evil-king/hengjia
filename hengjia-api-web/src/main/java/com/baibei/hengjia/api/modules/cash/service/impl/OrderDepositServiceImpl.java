package com.baibei.hengjia.api.modules.cash.service.impl;

import com.baibei.hengjia.api.modules.cash.bean.dto.OrderDepositDto;
import com.baibei.hengjia.api.modules.cash.component.SerialNumberComponent;
import com.baibei.hengjia.api.modules.cash.dao.OrderDepositMapper;
import com.baibei.hengjia.api.modules.cash.model.OrderDeposit;
import com.baibei.hengjia.api.modules.cash.service.IOrderDepositService;
import com.baibei.hengjia.api.modules.user.dao.CustomerMapper;
import com.baibei.hengjia.api.modules.user.model.Customer;
import com.baibei.hengjia.common.core.mybatis.AbstractService;
import com.baibei.hengjia.common.tool.api.ApiResult;
import com.baibei.hengjia.common.tool.constants.Constants;
import com.baibei.hengjia.common.tool.enumeration.OrderTypeEnum;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import tk.mybatis.mapper.entity.Condition;
import tk.mybatis.mapper.entity.Example;

import java.util.Date;
import java.util.List;


/**
 * @author: uqing
 * @date: 2019/06/03 20:37:57
 * @description: OrderDeposit服务实现
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class OrderDepositServiceImpl extends AbstractService<OrderDeposit> implements IOrderDepositService {

    @Autowired
    private OrderDepositMapper tblTraOrderDepositMapper;

    @Autowired
    private CustomerMapper customerMapper;

    private final String ORDER_DEPOSIT_PREFIX = "D";

    private Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private SerialNumberComponent serialNumberComponent;


    @Override
    public ApiResult<Boolean> depositApplicationService(OrderDepositDto orderDepositDto) {
        logger.info(" 入金参数是:[{}]", orderDepositDto);
        OrderDeposit result = toEntity(orderDepositDto);
        Customer customer = new Customer();
        customer.setCustomerNo(orderDepositDto.getCustomerNo());
        customer.setFlag(new Byte(Constants.Flag.VALID));
        customer = customerMapper.selectOne(customer);
        if (customer.getId() == null) {
            return ApiResult.badParam("当前客户不存在");
        }
        tblTraOrderDepositMapper.insert(result);
        return ApiResult.success(true);
    }

    @Override
    public OrderDeposit getOrderByExternalNo(String externalNo) {
        Condition condition = new Condition(OrderDeposit.class);
        condition.setOrderByClause("create_time desc,id");
        Example.Criteria criteria = condition.createCriteria();
        if(!StringUtils.isEmpty(externalNo)){
            criteria.andEqualTo("externalNo",externalNo);
        }
        List<OrderDeposit> orderDepositList = tblTraOrderDepositMapper.selectByCondition(condition);
        return orderDepositList.size()==0?null:orderDepositList.get(0);
    }

    @Override
    public List<OrderDeposit> getPeriodOrderList(String period) {
        return tblTraOrderDepositMapper.selectPeriodOrderList(period);
    }

    @Override
    public List<OrderDeposit> getPeriodOrderListNotInBankOrders(String period) {
        return tblTraOrderDepositMapper.selectPeriodOrderListNotInBankOrders(period);
    }

    @Override
    public OrderDeposit getOrderByOrderNo(String orderNo) {
        Condition condition = new Condition(OrderDeposit.class);
        Example.Criteria criteria = condition.createCriteria();
        if(!StringUtils.isEmpty(orderNo)){
            criteria.andEqualTo("orderNo",orderNo);
        }
        List<OrderDeposit> orderDepositList = tblTraOrderDepositMapper.selectByCondition(condition);
        return orderDepositList.size()==0?null:orderDepositList.get(0);
    }


    public OrderDeposit toEntity(OrderDepositDto orderDepositDto) {
        OrderDeposit orderDeposit = new OrderDeposit();
        orderDeposit.setAccount(orderDepositDto.getInAcctId()); // 转入账号
        orderDeposit.setAccountName(orderDepositDto.getInAcctIdName());// 收款用户
        // orderDeposit.setHandelFee(); // 暂时入金不需要手续费
        orderDeposit.setOrderNo(serialNumberComponent.generateOrderNo(OrderDeposit.class, this, ORDER_DEPOSIT_PREFIX,"orderNo"));//入金订单号
        orderDeposit.setOrderType(OrderTypeEnum.DEPOSIT.getOrderType());// 订单状态
        orderDeposit.setOrderAmt(orderDepositDto.getTranAmount()); // 订单金额
        orderDeposit.setCreateTime(new Date());// 申请时间
//        orderDeposit.setDepositBank(orderDepositDto.getDepositBank());// 开户银行
        orderDeposit.setCustomerNo(orderDepositDto.getCustomerNo());
/*        orderDeposit.setStatus(ReviewStatusEnum.UNAUDIT.getStatus());// 设置入金审核状态*/
//        orderDeposit.setRemarks(orderDepositDto.getRemarks());//设置备注
        orderDeposit.setCreateTime(new Date()); //创建时间
        orderDeposit.setFlag(new Byte(Constants.Flag.VALID)); //表中默认值为1,如需要改变其他
        return orderDeposit;
    }

}
