package com.baibei.hengjia.admin.modules.tradingQuery.service.impl;

import com.baibei.hengjia.admin.modules.paramConfiguration.dao.LookUpMapper;
import com.baibei.hengjia.admin.modules.paramConfiguration.model.LookUp;
import com.baibei.hengjia.admin.modules.tradingQuery.bean.dto.ShopOrderDto;
import com.baibei.hengjia.admin.modules.tradingQuery.bean.vo.LookOrderDetailVo;
import com.baibei.hengjia.admin.modules.tradingQuery.bean.vo.LookUpVo;
import com.baibei.hengjia.admin.modules.tradingQuery.bean.vo.ShopOrderVo;
import com.baibei.hengjia.admin.modules.tradingQuery.dao.ShpOrderMapper;
import com.baibei.hengjia.admin.modules.tradingQuery.model.ShpOrder;
import com.baibei.hengjia.admin.modules.tradingQuery.service.IShpOrderService;
import com.baibei.hengjia.common.core.mybatis.AbstractService;
import com.baibei.hengjia.common.tool.page.MyPageInfo;
import com.baibei.hengjia.common.tool.utils.BeanUtil;
import com.baibei.hengjia.common.tool.utils.MobileUtils;
import com.github.pagehelper.PageHelper;
import com.google.common.collect.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import tk.mybatis.mapper.entity.Condition;
import tk.mybatis.mapper.entity.Example;

import java.util.Date;
import java.util.List;


/**
 * @author: wenqing
 * @date: 2019/07/19 14:30:09
 * @description: ShpOrder服务实现
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class ShpOrderServiceImpl extends AbstractService<ShpOrder> implements IShpOrderService {

    @Autowired
    private ShpOrderMapper shpOrderMapper;
    @Autowired
    private LookUpMapper lookUpMapper;

    @Override
    public MyPageInfo<ShopOrderVo> pageList(ShopOrderDto shopOrderDto) {
        PageHelper.startPage(shopOrderDto.getCurrentPage(), shopOrderDto.getPageSize());
        List<ShopOrderVo> pageList = shpOrderMapper.pageList(shopOrderDto);
        for (ShopOrderVo shopOrderVo : pageList) {
            String userName = MobileUtils.changeName(shopOrderVo.getUserName());
            String phone = MobileUtils.changeMobile(shopOrderVo.getPhone());
            shopOrderVo.setUserName(userName);
            shopOrderVo.setPhone(phone);
            String address = MobileUtils.hideAddress(shopOrderVo.getAddress());
            shopOrderVo.setAddress(address);
        }
        MyPageInfo<ShopOrderVo> pageInfo = new MyPageInfo<>(pageList);
        return pageInfo;
    }

    @Override
    public List<LookOrderDetailVo> lookInfo(String orderNo) {
        List<LookOrderDetailVo> lookOrderDetailVos = shpOrderMapper.lookInfo(orderNo);
        return lookOrderDetailVos;
    }

    @Override
    public String confirmSend(String orderNo, String name, String logisticsNo) {
        Condition condition = new Condition(ShpOrder.class);
        Example.Criteria criteria = condition.createCriteria();
        criteria.andEqualTo("orderNo", orderNo);
        criteria.andNotEqualTo("orderStatus", "received");//已发货状态
        List<ShpOrder> shpOrderList = shpOrderMapper.selectByCondition(condition);
        if (!CollectionUtils.isEmpty(shpOrderList)) {
            ShpOrder order = shpOrderList.get(0);
            order.setOrderStatus("delivered");
            order.setLogisticsCompany(name);
            order.setLogisticsNo(logisticsNo);
            order.setModifyTime(new Date());
            order.setDeliveredTime(new Date());
            Condition condition1 = new Condition(ShpOrder.class);
            Example.Criteria criteria1 = condition1.createCriteria();
            criteria1.andEqualTo("orderNo", order.getOrderNo());
            if (shpOrderMapper.updateByConditionSelective(order, condition1) == 1) {
                return "success";
            }
        }
        return "fail";
    }

    @Override
    public MyPageInfo<ShopOrderVo> list(ShopOrderDto shopOrderDto) {
        //查询主订单
        List<ShopOrderVo> pageList = shpOrderMapper.export(shopOrderDto);
        //查询订单详情
        for (ShopOrderVo shopOrderVo : pageList) {
            String userName = MobileUtils.changeName(shopOrderVo.getUserName());
            String phone = MobileUtils.changeMobile(shopOrderVo.getPhone());
            shopOrderVo.setUserName(userName);
            shopOrderVo.setPhone(phone);
        }
        MyPageInfo<ShopOrderVo> pageInfo = new MyPageInfo<>(pageList);
        return pageInfo;
    }

    @Override
    public List<LookUpVo> listData() {
        List<LookUpVo> lookUpVoList = Lists.newArrayList();
        Condition condition = new Condition(LookUp.class);
        Example.Criteria criteria = condition.createCriteria();
        criteria.andEqualTo("classcode", "logistics");
        List<LookUp> lookUpList = lookUpMapper.selectByCondition(condition);
        for (int i = 0; i < lookUpList.size(); i++) {
            LookUp lookUp = lookUpList.get(i);
            LookUpVo lookUpVo = BeanUtil.copyProperties(lookUp, LookUpVo.class);
            lookUpVoList.add(lookUpVo);
        }
        return lookUpVoList;
    }

    @Override
    public int selectByOrderNoExist(String orderNo) {

        return shpOrderMapper.selectByOrderNoExist(orderNo);
    }
}
