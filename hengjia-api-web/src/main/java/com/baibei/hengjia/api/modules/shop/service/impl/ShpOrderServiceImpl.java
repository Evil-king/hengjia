package com.baibei.hengjia.api.modules.shop.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.baibei.hengjia.api.modules.account.bean.dto.ChangeIntegralDto;
import com.baibei.hengjia.api.modules.account.bean.dto.IntegralDetailDto;
import com.baibei.hengjia.api.modules.account.bean.vo.IntegralDetailVo;
import com.baibei.hengjia.api.modules.account.service.ICustomerIntegralService;
import com.baibei.hengjia.api.modules.product.dao.ProductMapper;
import com.baibei.hengjia.api.modules.product.model.Product;
import com.baibei.hengjia.api.modules.product.service.IProductService;
import com.baibei.hengjia.api.modules.shop.bean.dto.*;
import com.baibei.hengjia.api.modules.shop.bean.vo.OrderDetailsVO;
import com.baibei.hengjia.api.modules.shop.bean.vo.OrderListVO;
import com.baibei.hengjia.api.modules.shop.dao.ShpOrderDetailsMapper;
import com.baibei.hengjia.api.modules.shop.dao.ShpOrderMapper;
import com.baibei.hengjia.api.modules.shop.model.ShpOrder;
import com.baibei.hengjia.api.modules.shop.model.ShpOrderDetails;
import com.baibei.hengjia.api.modules.shop.service.IShpOrderService;
import com.baibei.hengjia.api.modules.user.model.CustomerAddress;
import com.baibei.hengjia.api.modules.user.service.ICustomerAddressService;
import com.baibei.hengjia.common.core.mybatis.AbstractService;
import com.baibei.hengjia.common.tool.api.ApiResult;
import com.baibei.hengjia.common.tool.bean.CustomerBaseDto;
import com.baibei.hengjia.common.tool.enumeration.ChannelTypeEnum;
import com.baibei.hengjia.common.tool.enumeration.IntegralTradeTypeEnum;
import com.baibei.hengjia.common.tool.enumeration.PointOrderTypeEnum;
import com.baibei.hengjia.common.tool.exception.ServiceException;
import com.baibei.hengjia.common.tool.page.MyPageInfo;
import com.baibei.hengjia.common.tool.page.PageParam;
import com.baibei.hengjia.common.tool.page.PageUtil;
import com.baibei.hengjia.common.tool.utils.CodeUtils;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;
import tk.mybatis.mapper.entity.Condition;
import tk.mybatis.mapper.entity.Example;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;


/**
 * @author: wenqing
 * @date: 2019/06/03 15:49:31
 * @description: ShpOrder服务实现
 */
@Slf4j
@Service
@Transactional(rollbackFor = Exception.class)
public class ShpOrderServiceImpl extends AbstractService<ShpOrder> implements IShpOrderService {

    @Autowired
    private ShpOrderMapper tblShpOrderMapper;
    @Autowired
    private ShpOrderDetailsMapper orderDetailsMapper;
    @Autowired
    private ProductMapper productMapper;
    @Autowired
    private ICustomerIntegralService customerIntegralService;
    @Autowired
    private IProductService productService;
    @Autowired
    private ICustomerAddressService customerAddressService;


    @Override
    public void exchangePoint(ExchangePointDTO exchangePointDTO) {
        log.info("exchangePointDTO={}", JSONObject.toJSONString(exchangePointDTO));
        //查询收货地址
        CustomerAddress address = customerAddressService.findDefaultByNo(exchangePointDTO.getCustomerNo());
        if(address == null){
            throw new ServiceException("用户收货地址不存在");
        }
        //1、生成主订单
        ShpOrder order = new ShpOrder();
        String orderNum = CodeUtils.generateTreeOrderCode();
        order.setOrderNo(orderNum);
        order.setCustomerNo(exchangePointDTO.getCustomerNo());
        order.setLogisticsNo("");
        order.setOrderStatus(String.valueOf(PointOrderTypeEnum.POINT_ORDER_SUCCESS.getCode()));
        order.setPayChannel(String.valueOf(ChannelTypeEnum.CHANNEL_POINT.getCode()));
        order.setOutOrdernum("");
        order.setCreateTime(new Date());
        order.setModifyTime(new Date());
        order.setFlag((byte) 1);
        order.setLogisticsCompany("");
        order.setLogisticsNo("");
        order.setAddressId(address.getId());
        order.setSumPoint(new BigDecimal(exchangePointDTO.getSumPoint()));
        //2、生成订单详情
        for (OrderDetailsDTO orderDetailsDTO : exchangePointDTO.getOrderDetailsDTOList()) {
            ShpOrderDetails orderDetails = new ShpOrderDetails();
            orderDetails.setOrderNo(orderNum);
            orderDetails.setSpuNo(orderDetailsDTO.getSpuNo());
            orderDetails.setCount(Integer.valueOf(orderDetailsDTO.getNum()));
            orderDetails.setPrice(new BigDecimal(orderDetailsDTO.getSellPrice()));
            orderDetails.setFlag((byte) 1);
            orderDetails.setPrice(new BigDecimal(orderDetailsDTO.getSellPrice()));
            orderDetails.setCreateTime(new Date());
            orderDetails.setModifyTime(new Date());
            orderDetailsMapper.insert(orderDetails);
            //3、扣除商品库存
            productService.cutStock(orderDetailsDTO.getSpuNo(), Integer.valueOf(orderDetailsDTO.getNum()), "integral");
        }
        tblShpOrderMapper.insert(order);
        //4、调用账户服务扣减用户的积分
        ChangeIntegralDto changeIntegralDto = new ChangeIntegralDto();
        changeIntegralDto.setOrderNo(orderNum);
        changeIntegralDto.setIntegralNo(101L);
        changeIntegralDto.setCustomerNo(exchangePointDTO.getCustomerNo());
        changeIntegralDto.setChangeAmount(new BigDecimal(exchangePointDTO.getSumPoint()));
        changeIntegralDto.setTradeType((byte) IntegralTradeTypeEnum.EXCHANGE_INTEGRAL_PRODUCT.getCode());
        changeIntegralDto.setReType((byte) 1);
        customerIntegralService.changeIntegral(changeIntegralDto);
    }

    @Override
    public IntegralDetailVo getUserInfo(CustomerBaseDto customerBaseDto) {
        IntegralDetailDto integralDetailDto = new IntegralDetailDto();
        integralDetailDto.setIntegralNo(101L);
        integralDetailDto.setCustomerNo(customerBaseDto.getCustomerNo());
        IntegralDetailVo detailVo = customerIntegralService.findIntegralDetailByCustomer(integralDetailDto);
        if (ObjectUtils.isEmpty(detailVo)) {
            throw new ServiceException("查询用户相关信息报错");
        }
        return detailVo;
    }

    @Override
    public MyPageInfo<OrderListVO> exchangeList(ExchangeListDTO exchangeListDTO) {
        log.info("exchangeListDTO={}", JSONObject.toJSONString(exchangeListDTO));
        OrderListVO orderListVO = null;

        List<OrderDetailsVO> detailsVOList = null;

        List<OrderListVO> resultList = Lists.newArrayList();

        MyPageInfo<ShpOrderDetails> transform = null;

        MyPageInfo<OrderListVO> resultPage = null;

        Condition condition = new Condition(ShpOrder.class);
        Example.Criteria criteria = condition.createCriteria();
        criteria.andEqualTo("customerNo", exchangeListDTO.getCustomerNo());

        if (exchangeListDTO.getStartTime() != null && !"".equals(exchangeListDTO.getStartTime())) {
            String startTime = stampToDate(exchangeListDTO.getStartTime());
            criteria.andGreaterThanOrEqualTo("createTime", startTime + " 00:00:00");
        }
        if (exchangeListDTO.getEndTime() != null && !"".equals(exchangeListDTO.getEndTime())) {
            String endTime = stampToDate(exchangeListDTO.getEndTime());
            criteria.andLessThanOrEqualTo("createTime", endTime + " 23:59:59");
        }
        MyPageInfo<ShpOrder> pageInfo = pageList(condition, PageParam.buildWithDefaultSort(exchangeListDTO.getCurrentPage(), exchangeListDTO.getPageSize()));
        if (pageInfo == null) {
            log.error("该用户没有订单");
            return resultPage;
        }
        if (CollectionUtils.isEmpty(pageInfo.getList())) {
            log.error("该用户没有订单");
            return resultPage;
        }
        for (ShpOrder order : pageInfo.getList()) {
            orderListVO = new OrderListVO();
            detailsVOList = Lists.newArrayList();
            //封装订单参数
            orderListVO.setCreateTime(order.getCreateTime());
            orderListVO.setOrderNum(order.getOrderNo());
            orderListVO.setSumPoint(order.getSumPoint());
            orderListVO.setLogisticsNo(order.getLogisticsNo());
            orderListVO.setLogisticsCompany(order.getLogisticsCompany());
            if(order.getOrderStatus().equals(PointOrderTypeEnum.POINT_ORDER_WAITING.getCode())){
                orderListVO.setOrderStatus(order.getOrderStatus());
                orderListVO.setStatusRemark(PointOrderTypeEnum.POINT_ORDER_WAITING.getMsg());
            }
            if(order.getOrderStatus().equals(PointOrderTypeEnum.POINT_ORDER_SUCCESS.getCode())){
                orderListVO.setOrderStatus(order.getOrderStatus());
                orderListVO.setStatusRemark(PointOrderTypeEnum.POINT_ORDER_SUCCESS.getMsg());
            }
            if(order.getOrderStatus().equals(PointOrderTypeEnum.POINT_ORDER_RECEIVED.getCode())){
                orderListVO.setOrderStatus(order.getOrderStatus());
                orderListVO.setStatusRemark(PointOrderTypeEnum.POINT_ORDER_RECEIVED.getMsg());
            }

            Condition condition1 = new Condition(ShpOrderDetails.class);
            Example.Criteria criteria1 = condition1.createCriteria();
            criteria1.andEqualTo("orderNo", order.getOrderNo());
            List<ShpOrderDetails> orderDetailsList = orderDetailsMapper.selectByCondition(condition1);
            transform = PageUtil.transform(pageInfo, ShpOrderDetails.class);
            transform.setList(orderDetailsList);
            for (ShpOrderDetails shpOrderDetails : transform.getList()) {
                //查询商品信息
                Condition productCondition = new Condition(Product.class);
                Example.Criteria productCri = productCondition.createCriteria();
                productCri.andEqualTo("spuNo", shpOrderDetails.getSpuNo());
                productCri.andEqualTo("productType","integral");
                List<Product> products = productMapper.selectByCondition(productCondition);
                if (CollectionUtils.isEmpty(products)) {
                    throw new ServiceException("该商品不存在");
                }
                //封装商品详情参数
                OrderDetailsVO orderDetailsVO = new OrderDetailsVO();
                orderDetailsVO.setCount(shpOrderDetails.getCount());
                orderDetailsVO.setImageUrl(products.get(0).getImgUrl());
                orderDetailsVO.setProductName(products.get(0).getProductName());
                orderDetailsVO.setSellPrice(shpOrderDetails.getPrice());
                orderDetailsVO.setSpuNo(shpOrderDetails.getSpuNo());

                detailsVOList.add(orderDetailsVO);
            }
            orderListVO.setList(detailsVOList);
            resultList.add(orderListVO);
            //加入到分页中
            resultPage = PageUtil.transform(transform, OrderListVO.class);
            resultPage.setList(resultList);
        }
        return resultPage;
    }

    @Override
    public ApiResult confirmReceipt(ConfirmReceiptDTO confirmReceiptDTO) {
        Condition condition = new Condition(ShpOrder.class);
        Example.Criteria criteria = condition.createCriteria();
        criteria.andEqualTo("customerNo",confirmReceiptDTO.getCustomerNo());
        criteria.andEqualTo("orderNo",confirmReceiptDTO.getOrderNum());
        List<ShpOrder> orderList = tblShpOrderMapper.selectByCondition(condition);
        if(CollectionUtils.isEmpty(orderList)){
            return ApiResult.error("该用户没有订单");
        }
        ShpOrder order = orderList.get(0);
        order.setOrderStatus(PointOrderTypeEnum.POINT_ORDER_RECEIVED.getCode());//将已发货(待收货)改为已收货
        order.setModifyTime(new Date());
        order.setReceivedTime(new Date());
        if(tblShpOrderMapper.updateByPrimaryKeySelective(order) != 1){
            return ApiResult.error("订单更新状态失败");
        }
        return ApiResult.success();
    }

    @Override
    public int longDayConfirmSend() {
        Date currentDate = new Date();
        Condition condition = new Condition(ShpOrder.class);
        Example.Criteria criteria = condition.createCriteria();
        criteria.andEqualTo("orderStatus","delivered");
        List<ShpOrder> shpOrderList = tblShpOrderMapper.selectByCondition(condition);
        if(!org.springframework.util.CollectionUtils.isEmpty(shpOrderList)){
            for(ShpOrder shpOrder : shpOrderList){
                if(!ObjectUtils.isEmpty(shpOrder.getDeliveredTime())){
                    if(daysBetween(currentDate, shpOrder.getDeliveredTime()) >= 14){
                        Condition condition1 = new Condition(ShpOrder.class);
                        Example.Criteria criteria1 = condition1.createCriteria();
                        criteria1.andEqualTo("orderNo",shpOrder.getOrderNo());
                        shpOrder.setOrderStatus("received");
                        shpOrder.setReceivedTime(new Date());
                        return tblShpOrderMapper.updateByPrimaryKeySelective(shpOrder);
                    }
                }
            }
        }
        return 0;
    }

    @Override
    public BigDecimal sumAmount(Date beginTime, Date endTime) {
        Map<String,Object> params = new HashMap<>();
        params.put("beginTime",beginTime);
        params.put("endTime",endTime);
        return tblShpOrderMapper.sumAmount(params);
    }

    @Override
    public ApiResult cancelPointOrder(CancelPointOrderDto cancelPointOrderDto) {
        cancelPointOrderDto.getOrderNo().stream().forEach(orderNo ->{
            Condition condition = new Condition(ShpOrder.class);
            Example.Criteria criteria = condition.createCriteria();
            criteria.andEqualTo("orderStatus","success");
            criteria.andEqualTo("orderNo",orderNo);
            List<ShpOrder> shpOrderList = tblShpOrderMapper.selectByCondition(condition);
            shpOrderList.stream().forEach(shpOrder -> {
                ChangeIntegralDto changeIntegralDto = new ChangeIntegralDto();
                changeIntegralDto.setOrderNo(shpOrder.getOrderNo());
                changeIntegralDto.setIntegralNo(101L);
                changeIntegralDto.setCustomerNo(shpOrder.getCustomerNo());
                changeIntegralDto.setChangeAmount(shpOrder.getSumPoint());
                changeIntegralDto.setTradeType((byte) IntegralTradeTypeEnum.INTEGRAL_BACK.getCode());
                changeIntegralDto.setReType((byte) 2);

                Condition condition1 = new Condition(ShpOrder.class);
                Example.Criteria criteria1 = condition1.createCriteria();
                criteria1.andEqualTo("orderNo",shpOrder.getOrderNo());
                shpOrder.setOrderStatus("fail");
                shpOrder.setRemark("积分回退");
                shpOrder.setModifyTime(new Date());
                if( tblShpOrderMapper.updateByPrimaryKeySelective(shpOrder) > 0){
                    customerIntegralService.changeIntegral(changeIntegralDto);
                }
            });
        });
        return ApiResult.success();
    }

    protected String stampToDate(String s) {
        String res;
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        long lt = new Long(s);
        Date date = new Date(lt);
        res = simpleDateFormat.format(date);
        return res;
    }


    public static int daysBetween(Date smdate,Date bdate){
        SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
        try {
            smdate=sdf.parse(sdf.format(smdate));
            bdate=sdf.parse(sdf.format(bdate));
            Calendar cal = Calendar.getInstance();
            cal.setTime(smdate);
            long time1 = cal.getTimeInMillis();
            cal.setTime(bdate);
            long time2 = cal.getTimeInMillis();
            log.info("time1={},time2={}",time1,time2);
            long between_days=(time2-time1)/(1000*3600*24);
            return Integer.parseInt(String.valueOf(between_days).replace("-", ""));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return 0;
    }
}
