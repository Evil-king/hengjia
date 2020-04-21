package com.baibei.hengjia.api.modules.cash.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baibei.hengjia.api.modules.account.service.IAccountService;
import com.baibei.hengjia.api.modules.cash.bean.dto.WeiPayDto;
import com.baibei.hengjia.api.modules.cash.bean.vo.WeiXinVo;
import com.baibei.hengjia.api.modules.cash.dao.TempDepositMapper;
import com.baibei.hengjia.api.modules.cash.model.TempDeposit;
import com.baibei.hengjia.api.modules.cash.service.IWeiXinPayService;
import com.baibei.hengjia.api.modules.cash.weiPay.WeiAppPayUtil;
import com.baibei.hengjia.api.modules.cash.weiPay.XMLMethods;
import com.baibei.hengjia.api.modules.sms.core.PropertiesVal;
import com.baibei.hengjia.api.modules.user.bean.vo.CustomerVo;
import com.baibei.hengjia.api.modules.user.model.Customer;
import com.baibei.hengjia.api.modules.user.service.ICustomerService;
import com.baibei.hengjia.common.tool.api.ApiResult;
import com.baibei.hengjia.common.tool.enumeration.FundTradeTypeEnum;
import com.baibei.hengjia.common.tool.exception.ServiceException;
import com.baibei.hengjia.common.tool.utils.BigDecimalUtil;
import com.baibei.hengjia.common.tool.utils.CodeUtils;
import lombok.extern.slf4j.Slf4j;
import org.dom4j.DocumentException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import tk.mybatis.mapper.entity.Condition;
import tk.mybatis.mapper.entity.Example;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.util.*;

@Slf4j
@Service
public class WeiXinPayServiceImpl implements IWeiXinPayService {

    @Autowired
    private ICustomerService customerService;
    @Autowired
    private PropertiesVal propertiesVal;
    @Autowired
    private TempDepositMapper depositMapper;
    @Autowired
    private IAccountService accountService;

    @Override
    public ApiResult weiH5Pay(WeiPayDto weiPayDto) {
        log.info("支付入口进参,weiPayDto={}", JSON.toJSONString(weiPayDto));
        ApiResult apiResult = new ApiResult();

        ApiResult validate = validate(weiPayDto);
        if (!validate.hasSuccess()) {
            return validate;
        }

        //获取用户信息
        Customer customer = customerService.findByCustomerNo(weiPayDto.getCustomerNo());
        log.info("获取用户信息,customerVo={}", JSON.toJSONString(customer));
        if (customer == null) {
            return ApiResult.error("该用户不存在");
        }

        String depositNo = "D" + CodeUtils.generateTreeOrderCode();
        //创建出金订单
        TempDeposit deposit = new TempDeposit();
        deposit.setCustomerNo(weiPayDto.getCustomerNo());
        deposit.setAmount(weiPayDto.getAmount());
        deposit.setFlag((byte) 1);
        deposit.setChannel("001");
        deposit.setStatus("init");
        deposit.setOutorderNo("");
        deposit.setDepositNo(depositNo);
        deposit.setCreateTime(new Date());
        deposit.setModifyTime(new Date());

        depositMapper.insert(deposit);
        //发送到微信
        WeiXinVo weiXinVo = sendWeiXin(deposit, customer.getOpenid(), weiPayDto.getIp());
        apiResult.setData(weiXinVo);
        return apiResult;
    }

    @Override
    public void weiH5PayCallBack(HttpServletRequest request, HttpServletResponse response) {
        InputStream inStream = null;
        try {
            inStream = request.getInputStream();
            ByteArrayOutputStream outSteam = new ByteArrayOutputStream();
            byte[] buffer = new byte[1024];
            int len = 0;
            while ((len = inStream.read(buffer)) != -1) {
                outSteam.write(buffer, 0, len);
            }
            outSteam.close();
            inStream.close();
            String result = new String(outSteam.toByteArray(), "utf-8");   //获取微信调用我们notify_url的返回信息
            Map<Object, Object> map = XMLMethods.doXMLParse(result);
            log.info("微信回调返回对象：map={}", JSONObject.toJSONString(map));
            String orderNo = (String) map.get("out_trade_no");//返回商户订单号
            String outOrderNo = (String) map.get("transaction_id");//微信支付订单号
            String totalFee = (String) map.get("total_fee");//订单金额
            String openid = (String) map.get("openid");//用户标识

            BigDecimal amount = new BigDecimal(totalFee).divide(BigDecimal.valueOf(100));


            Condition condition = new Condition(TempDeposit.class);
            Example.Criteria criteria = condition.createCriteria();
            criteria.andEqualTo("depositNo",orderNo);
            List<TempDeposit> depositList = depositMapper.selectByCondition(condition);
            if(CollectionUtils.isEmpty(depositList)){
                throw new ServiceException("用户订单信息不存在");
            }
            TempDeposit deposit = depositList.get(0);
//            log.info("调用用户订单信息接口,depositList={}", JSON.toJSONString(deposit));

            //微信端已支付，处理订单业务
            if (map.get("result_code").toString().equalsIgnoreCase("SUCCESS")) {
                //处理自己的业务逻辑
                //发送信息给微信知道当前处理完业务逻辑、返回成功标识、(更新订单、处理金额)
                if (updateOrder(deposit.getCustomerNo(), orderNo, outOrderNo, "success") != 1) {
                    throw new ServiceException("订单状态更新失败");
                }
                accountService.updateWithDraw(deposit.getCustomerNo(), amount, orderNo,
                        FundTradeTypeEnum.MONEY_INTO.getCode(), (byte) 2);
                //返回给微信状态码
                response.getWriter().write(WeiAppPayUtil.setXML("SUCCESS", "OK"));   //告诉微信服务器，我收到信息了，不要在通知我了
            } else {
                updateOrder(deposit.getCustomerNo(), orderNo, outOrderNo, "fail");
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (DocumentException e) {
            e.printStackTrace();
        }
    }


    private WeiXinVo sendWeiXin(TempDeposit deposit, String openid, String spbill_create_ip) {
        log.info("微信支付入参，deposit={},openid={},spbill_create_ip={}", JSON.toJSONString(deposit), openid, spbill_create_ip);
        try {
            SortedMap<Object, Object> parameters = new TreeMap<Object, Object>();
            parameters.put("appid", propertiesVal.getAppid());//Appid
            parameters.put("mch_id", propertiesVal.getMch_id());//商户号
            parameters.put("device_info", "WEB");
            parameters.put("nonce_str", WeiAppPayUtil.CreateNoncestr(32));
            parameters.put("body", propertiesVal.getBody());//商品描述
            parameters.put("out_trade_no", deposit.getDepositNo());
            System.out.println(deposit.getAmount());
            //测试填写1，微信默认为整形数字符串1为1分钱、10为1毛钱如此类推
            parameters.put("total_fee", WeiAppPayUtil.getMoney(deposit.getAmount().toString()));
            parameters.put("spbill_create_ip", spbill_create_ip);//终端IP
            parameters.put("notify_url", propertiesVal.getNotify_url());
            parameters.put("trade_type", "JSAPI");
            //oppenid是在session里面获得的，所以才有了上面的授权页面了
            parameters.put("openid", openid);
            parameters.put("limit_pay", "no_credit");//限制信用卡
            String sign = WeiAppPayUtil.createSign("UTF-8", parameters, propertiesVal.getMch_key());
            log.info("签名，sign={}", sign);
            //把以上的信息进行键值对加密‘MD5’
            parameters.put("sign", sign);
            //把map里面的map对应的键值对变成MXL格式的字符串
            String requestXML = XMLMethods.getRequestXml(parameters);
            //调用微信接口下单，返回
            String result = WeiAppPayUtil.httpsRequest(propertiesVal.getUrlStr(), "POST", requestXML);
            log.info("result={}", result);
            //将解析结果存储在HashMap中
            Map map = WeiAppPayUtil.doXMLParse(result);
            log.info("map={}", map);
            WeiXinVo weiXinVo = new WeiXinVo();
            String return_code = (String) map.get("return_code");//返回状态码
            if (return_code == "SUCCESS" || return_code.equals(return_code)) {
                String appid = map.get("appid").toString();
                String nonce_str = map.get("nonce_str").toString();
                String prepay_id = map.get("prepay_id").toString();
                long timestamp = System.currentTimeMillis() / 1000;
                String signType = "MD5";

                SortedMap<Object, Object> parameters1 = new TreeMap<Object, Object>();
                parameters1.put("appId",appid);
                parameters1.put("nonceStr", nonce_str);
                parameters1.put("package", "prepay_id="+prepay_id);
                parameters1.put("timeStamp", timestamp);
                parameters1.put("signType", signType);

                String resultSign = WeiAppPayUtil.createSign("UTF-8", parameters1, propertiesVal.getMch_key());

                log.info("最终返回签名,resultSign={}", resultSign);

                weiXinVo.setAppid(appid);
                weiXinVo.setNonce_str(nonce_str);
                weiXinVo.setSign(resultSign);
                weiXinVo.setTimeStamp(timestamp);
                weiXinVo.setSignType("MD5");
                weiXinVo.setPrepay_id("prepay_id="+prepay_id);
                return weiXinVo;
            } else {
                log.info("返回数据为空,weiXinVo={}", weiXinVo);
                return weiXinVo;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    protected ApiResult validate(WeiPayDto weiPayDto) {
        //判断用户状态
        CustomerVo customerVo = customerService.findUserByCustomerNo(weiPayDto.getCustomerNo());
        if ("102".indexOf(customerVo.getCustomerStatus()) != -1) {
            return ApiResult.error("该用户已经被限制充值");
        }
        //判断入金金额上下限
        int minAmount = BigDecimalUtil.compareToBigDecimal(weiPayDto.getAmount(), propertiesVal.getMinDeposit());
        int maxAmount = BigDecimalUtil.compareToBigDecimal(weiPayDto.getAmount(), propertiesVal.getMaxDeposit());
        if (minAmount == -1) {
            return ApiResult.error("该用户入金下限不能小于1元");
        }
        if (maxAmount == 1) {
            return ApiResult.error("该用户入金上限不能大于20000元");
        }
        return ApiResult.success();
    }

    private int updateOrder(String customerNo, String orderNo, String outOrderNo, String type) {
        Condition condition = new Condition(TempDeposit.class);
        Example.Criteria criteria = condition.createCriteria();
        criteria.andEqualTo("customerNo", customerNo);
        criteria.andEqualTo("depositNo", orderNo);
        TempDeposit deposit = new TempDeposit();
        deposit.setOutorderNo(outOrderNo);
        deposit.setModifyTime(new Date());
        if ("success".equals(type)) {
            deposit.setStatus("success");
        }
        if ("fail".equals(type)) {
            deposit.setStatus("fail");
        }
        return depositMapper.updateByConditionSelective(deposit, condition);
    }
}
