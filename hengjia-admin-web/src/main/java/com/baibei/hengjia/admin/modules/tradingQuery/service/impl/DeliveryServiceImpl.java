package com.baibei.hengjia.admin.modules.tradingQuery.service.impl;

import com.baibei.hengjia.admin.modules.tradingQuery.bean.dto.DeliveryDto;
import com.baibei.hengjia.admin.modules.tradingQuery.bean.vo.DeliveryExportVo;
import com.baibei.hengjia.admin.modules.tradingQuery.bean.vo.DeliveryVo;
import com.baibei.hengjia.admin.modules.tradingQuery.dao.DeliveryMapper;
import com.baibei.hengjia.admin.modules.tradingQuery.model.Delivery;
import com.baibei.hengjia.admin.modules.tradingQuery.service.IDeliveryService;
import com.baibei.hengjia.common.core.mybatis.AbstractService;
import com.baibei.hengjia.common.tool.api.ApiResult;
import com.baibei.hengjia.common.tool.constants.Constants;
import com.baibei.hengjia.common.tool.exception.ServiceException;
import com.baibei.hengjia.common.tool.page.MyPageInfo;
import com.baibei.hengjia.common.tool.utils.MobileUtils;
import com.github.pagehelper.PageHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import tk.mybatis.mapper.entity.Condition;
import tk.mybatis.mapper.entity.Example;

import java.util.Date;
import java.util.List;


/**
 * @author: Longer
 * @date: 2019/07/19 13:51:38
 * @description: Delivery服务实现
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class DeliveryServiceImpl extends AbstractService<Delivery> implements IDeliveryService {

    @Autowired
    private DeliveryMapper deliveryMapper;

    @Override
    public MyPageInfo<DeliveryVo> pageList(DeliveryDto deliveryDto) {
        PageHelper.startPage(deliveryDto.getCurrentPage(), deliveryDto.getPageSize());
        if("exchange".equals(deliveryDto.getHoldType())){
            deliveryDto.setSource("exchange");
            deliveryDto.setHoldType("");
        }
        if("match".equals(deliveryDto.getHoldType())){
            deliveryDto.setSource("unExchange");
            deliveryDto.setHoldType("");
        }
        if("main".equals(deliveryDto.getHoldType())){
            deliveryDto.setHoldType("main");
        }
        List<DeliveryVo> entrustOrderVoList = deliveryMapper.myList(deliveryDto);
        entrustOrderVoList.stream().forEach(deliveryVo -> {
            deliveryVo.setMobile(MobileUtils.changeMobile(deliveryVo.getMobile()));
            deliveryVo.setReceivingMobile(MobileUtils.changeMobile(deliveryVo.getReceivingMobile()));
            deliveryVo.setReceivingAddress(MobileUtils.hideAddress(deliveryVo.getReceivingAddress()));
        });
        MyPageInfo<DeliveryVo> pageInfo = new MyPageInfo<>(entrustOrderVoList);
        return pageInfo;
    }

    @Override
    public ApiResult audit(DeliveryDto deliveryDto) {
        if (StringUtils.isEmpty(deliveryDto.getDeliveryNo())) {
            throw new ServiceException("审核失败，未指定提货订单号");
        }
        if (StringUtils.isEmpty(deliveryDto.getDeliveryStatus()) || (!Constants.DeliveryStatus.UNSEND.equals(deliveryDto.getDeliveryStatus())
                && !Constants.DeliveryStatus.REJECT.equals(deliveryDto.getDeliveryStatus()))) {
            throw new ServiceException("审核失败，状态异常");
        }
        Delivery delivery = new Delivery();
        delivery.setDeliveryStatus(Integer.parseInt(deliveryDto.getDeliveryStatus()));
        Condition condition = new Condition(Delivery.class);
        Example.Criteria criteria = condition.createCriteria();
        criteria.andEqualTo("deliveryNo", deliveryDto.getDeliveryNo());
        deliveryMapper.updateByConditionSelective(delivery, condition);
        return ApiResult.success();
    }

    @Override
    public ApiResult send(DeliveryDto deliveryDto) {
        if (StringUtils.isEmpty(deliveryDto.getDeliveryNo())) {
            throw new ServiceException("审核失败，未指定提货订单号");
        }
        if (StringUtils.isEmpty(deliveryDto.getLogisticsCompany())) {
            throw new ServiceException("发货失败，未指定物流公司");
        }
        if (StringUtils.isEmpty(deliveryDto.getLogisticsNo())) {
            throw new ServiceException("发货失败，未指定物流单号");
        }
        Delivery delivery = new Delivery();
        delivery.setDeliveryStatus(Integer.parseInt(Constants.DeliveryStatus.SENT));//状态
        delivery.setLogisticsCompany(deliveryDto.getLogisticsCompany());//物流公司
        delivery.setLogisticsNo(deliveryDto.getLogisticsNo());//物流单号
        Condition condition = new Condition(Delivery.class);
        Example.Criteria criteria = condition.createCriteria();
        criteria.andEqualTo("deliveryNo", deliveryDto.getDeliveryNo());
        deliveryMapper.updateByConditionSelective(delivery, condition);
        return ApiResult.success();
    }

    @Override
    public ApiResult autoReceive() {
        //查询当前状态为“待收货(已发货)”的提货订单
        List<Delivery> deliverieList = getSendDeliverys();
        if (deliverieList == null || deliverieList.size() == 0) {
            return ApiResult.error("暂无订单需要更新");
        }
        //更新状态
        Date currentDate = new Date();
        for (Delivery delivery : deliverieList) {
            Condition condition = new Condition(Delivery.class);
            Example.Criteria criteria = condition.createCriteria();
            criteria.andEqualTo("deliveryStatus", Constants.DeliveryStatus.SENT);//乐观锁
            delivery.setDeliveryStatus(Integer.parseInt(Constants.DeliveryStatus.RECEIVED));
            delivery.setModifyTime(currentDate);
            deliveryMapper.updateByCondition(delivery, condition);
        }
        return ApiResult.success();
    }

    @Override
    public void addLogisticsInfo(DeliveryDto deliveryDto) {
        Condition condition = new Condition(Delivery.class);
        Example.Criteria criteria = condition.createCriteria();
        criteria.andEqualTo("deliveryNo", deliveryDto.getDeliveryNo());
        Delivery delivery = this.findOneByCondition(condition);
        delivery.setLogisticsCompany(deliveryDto.getLogisticsCompany());
        delivery.setLogisticsNo(deliveryDto.getLogisticsNo());
        delivery.setPendingTime(new Date()); // 发货时间
        delivery.setDeliveryStatus(Integer.valueOf(Constants.DeliveryStatus.SENT)); //状态
        delivery.setModifyTime(new Date());
        this.update(delivery);
    }

    @Override
    public List<DeliveryExportVo> list(DeliveryDto deliveryDto) {
        List<DeliveryExportVo> entrustOrderVoList = deliveryMapper.exportList(deliveryDto);
        entrustOrderVoList.stream().forEach(deliveryExportVo -> {
            deliveryExportVo.setReceivingMobile(MobileUtils.changeMobile(deliveryExportVo.getReceivingMobile()));
            deliveryExportVo.setMobile(MobileUtils.changeMobile(deliveryExportVo.getMobile()));
        });
        return entrustOrderVoList;
    }

    @Override
    public String send(String deliveryNo, String logisticsCompany, String logisticsNo) {
        Condition condition = new Condition(Delivery.class);
        Example.Criteria criteria = condition.createCriteria();
        criteria.andEqualTo("deliveryNo", deliveryNo);
        criteria.andEqualTo("deliveryStatus", "20");
        List<Delivery> deliveryList = deliveryMapper.selectByCondition(condition);
        if (!CollectionUtils.isEmpty(deliveryList)) {
            Delivery delivery = deliveryList.get(0);
            delivery.setDeliveryNo(deliveryNo);
            delivery.setDeliveryStatus(Integer.parseInt(Constants.DeliveryStatus.SENT));//状态
            delivery.setLogisticsCompany(logisticsCompany);//物流公司
            delivery.setLogisticsNo(logisticsNo);//物流单号
            delivery.setPendingTime(new Date());//发货时间
            Condition condition1 = new Condition(Delivery.class);
            Example.Criteria criteria1 = condition1.createCriteria();
            criteria1.andEqualTo("deliveryNo", deliveryNo);
            if (deliveryMapper.updateByConditionSelective(delivery, condition1) > 0) {
                return "success";
            }
        }
        return "fail";
    }

    @Override
    public int selectByOrderNoExist(String deliveryNo) {
        return deliveryMapper.selectByOrderNoExist(deliveryNo);
    }

    public List<Delivery> getSendDeliverys() {
        Condition condition = new Condition(Delivery.class);
        Example.Criteria criteria = condition.createCriteria();
        criteria.andEqualTo("deliveryStatus", Constants.DeliveryStatus.SENT);
        criteria.andEqualTo("flag", Constants.Flag.VALID);
        List<Delivery> deliverieList = deliveryMapper.selectByCondition(condition);
        return deliverieList;
    }

}
