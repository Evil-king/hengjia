package com.baibei.hengjia.admin.modules.withdraw.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.baibei.hengjia.admin.modules.security.utils.SecurityUtils;
import com.baibei.hengjia.admin.modules.withdraw.bean.dto.WithdrawListDto;
import com.baibei.hengjia.admin.modules.withdraw.bean.vo.WithdrawListVo;
import com.baibei.hengjia.admin.modules.withdraw.dao.OrderWithdrawMapper;
import com.baibei.hengjia.admin.modules.withdraw.model.OrderWithdraw;
import com.baibei.hengjia.admin.modules.withdraw.service.IOrderWithdrawService;
import com.baibei.hengjia.common.core.mybatis.AbstractService;
import com.baibei.hengjia.common.tool.api.ApiResult;
import com.baibei.hengjia.common.tool.exception.ServiceException;
import com.baibei.hengjia.common.tool.page.MyPageInfo;
import com.baibei.hengjia.common.tool.utils.MobileUtils;
import com.github.pagehelper.PageHelper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import tk.mybatis.mapper.entity.Condition;
import tk.mybatis.mapper.entity.Example;

import java.util.Date;
import java.util.List;


/**
 * @author: wenqing
 * @date: 2019/07/15 10:27:46
 * @description: OrderWithdraw服务实现
 */
@Slf4j
@Service
@Transactional(rollbackFor = Exception.class)
public class OrderWithdrawServiceImpl extends AbstractService<OrderWithdraw> implements IOrderWithdrawService {

    @Autowired
    private OrderWithdrawMapper orderWithdrawMapper;

    @Override
    public MyPageInfo<WithdrawListVo> pageList(WithdrawListDto withdrawListDto) {
        PageHelper.startPage(withdrawListDto.getCurrentPage(), withdrawListDto.getPageSize());
        if ("5".equals(withdrawListDto.getStatus()) || "6".equals(withdrawListDto.getStatus())) {
            withdrawListDto.setEffective("0");
        }
        List<WithdrawListVo> pageList = orderWithdrawMapper.pageList(withdrawListDto);
        for (WithdrawListVo withdrawListVo : pageList) {
            String strName = MobileUtils.changeName(withdrawListVo.getUserName());
            withdrawListVo.setUserName(strName);
            withdrawListVo.setMobile(MobileUtils.changeMobile(withdrawListVo.getMobile()));
        }
        MyPageInfo<WithdrawListVo> pageInfo = new MyPageInfo<>(pageList);
        return pageInfo;
    }

    @Override
    public ApiResult reviewStatus(String orderNo, String type) {
        Condition condition = new Condition(OrderWithdraw.class);
        Example.Criteria criteria = condition.createCriteria();
        criteria.andEqualTo("orderNo", orderNo);
        criteria.andEqualTo("effective", 1);
        List<OrderWithdraw> orderWithdrawList = orderWithdrawMapper.selectByCondition(condition);
        if (CollectionUtils.isEmpty(orderWithdrawList)) {
            throw new ServiceException("并没有该订单信息");
        }
        OrderWithdraw withdraw = orderWithdrawList.get(0);
        withdraw.setOrderNo(orderNo);
        if ("yes".equals(type)) {
            withdraw.setStatus("2");
        }
        if ("no".equals(type)) {
            withdraw.setStatus("6");
        }
        //获取登录用户
        UserDetails user = SecurityUtils.getUserDetails();
        log.info("user={}", JSONObject.toJSONString(user));
        withdraw.setReviewer(user.getUsername());
        withdraw.setUpdateTime(new Date());
        withdraw.setReviewerTime(new Date());
        if (orderWithdrawMapper.updateByConditionSelective(withdraw, condition) != 1) {
            throw new ServiceException("更新订单异常");
        }
        return ApiResult.success();
    }

    @Override
    public List<WithdrawListVo> list(WithdrawListDto withdrawListDto) {
        if ("5".equals(withdrawListDto.getStatus()) || "6".equals(withdrawListDto.getStatus())) {
            withdrawListDto.setEffective("0");
        }
        List<WithdrawListVo> pageList = orderWithdrawMapper.pageList(withdrawListDto);
        for (WithdrawListVo withdrawListVo : pageList) {
            String strName = MobileUtils.changeName(withdrawListVo.getUserName());
            withdrawListVo.setUserName(strName);
            withdrawListVo.setMobile(MobileUtils.changeMobile(withdrawListVo.getMobile()));
        }
        return pageList;
    }
}
