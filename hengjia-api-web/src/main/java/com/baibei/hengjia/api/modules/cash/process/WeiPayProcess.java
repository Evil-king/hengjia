package com.baibei.hengjia.api.modules.cash.process;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baibei.hengjia.api.modules.account.service.IAccountService;
import com.baibei.hengjia.api.modules.cash.bean.vo.WeiXinVo;
import com.baibei.hengjia.api.modules.cash.dao.TempDepositMapper;
import com.baibei.hengjia.api.modules.cash.model.TempDeposit;
import com.baibei.hengjia.api.modules.cash.service.IProcessService;
import com.baibei.hengjia.api.modules.cash.template.PayTemplate;
import com.baibei.hengjia.api.modules.cash.weiPay.WeiAppPayUtil;
import com.baibei.hengjia.api.modules.cash.weiPay.XMLMethods;
import com.baibei.hengjia.api.modules.sms.core.PropertiesVal;
import com.baibei.hengjia.api.modules.user.model.Customer;
import com.baibei.hengjia.api.modules.user.service.ICustomerService;
import com.baibei.hengjia.common.tool.api.ApiResult;
import com.baibei.hengjia.common.tool.enumeration.FundTradeTypeEnum;
import com.baibei.hengjia.common.tool.exception.ServiceException;
import lombok.extern.slf4j.Slf4j;
import org.dom4j.DocumentException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import tk.mybatis.mapper.entity.Condition;
import tk.mybatis.mapper.entity.Example;

import javax.servlet.http.HttpServletRequest;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

/**
 * @author hwq
 * @date 2019/06/22
 * <p>
 * 微信操作类
 * </p>
 */
@Slf4j
@Component
public class WeiPayProcess extends PayTemplate {

    @Autowired
    private IProcessService processService;
    @Autowired
    private ICustomerService customerService;
    @Autowired
    private PropertiesVal propertiesVal;
    @Autowired
    private TempDepositMapper depositMapper;
    @Autowired
    private IAccountService accountService;

    @Override
    protected ApiResult validate(JSONObject o) {
        return processService.validate(o);
    }

    @Override
    protected TempDeposit createOrder(JSONObject o) {
        return processService.createOrder(o);
    }

    @Override
    protected ApiResult sendToChannel(TempDeposit deposit, String templeStr) {
        ApiResult apiResult = new ApiResult();
        //获取用户信息
        Customer customer = customerService.findByCustomerNo(deposit.getCustomerNo());
        log.info("获取用户信息,customerVo={}", JSON.toJSONString(customer));
        if (customer == null) {
            return ApiResult.error("该用户不存在");
        }
        log.info("微信支付入参，deposit={},openid={},spbill_create_ip={}", JSON.toJSONString(deposit), customer.getOpenid(), templeStr);
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
            parameters.put("spbill_create_ip", templeStr);//终端IP
            parameters.put("notify_url", propertiesVal.getNotify_url());
            parameters.put("trade_type", "JSAPI");
            //oppenid是在session里面获得的，所以才有了上面的授权页面了
            parameters.put("openid", customer.getOpenid());
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
                parameters1.put("appId", appid);
                parameters1.put("nonceStr", nonce_str);
                parameters1.put("package", "prepay_id=" + prepay_id);
                parameters1.put("timeStamp", timestamp);
                parameters1.put("signType", signType);

                String resultSign = WeiAppPayUtil.createSign("UTF-8", parameters1, propertiesVal.getMch_key());

                log.info("最终返回签名,resultSign={}", resultSign);

                weiXinVo.setAppid(appid);
                weiXinVo.setNonce_str(nonce_str);
                weiXinVo.setSign(resultSign);
                weiXinVo.setTimeStamp(timestamp);
                weiXinVo.setSignType("MD5");
                weiXinVo.setPrepay_id("prepay_id=" + prepay_id);
                apiResult.setData(weiXinVo);
                return apiResult;
            } else {
                log.info("返回数据为空,weiXinVo={}", weiXinVo);
                return apiResult;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected String payCallback(HttpServletRequest request) {
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

            BigDecimal amount = new BigDecimal(totalFee).divide(BigDecimal.valueOf(100));


            Condition condition = new Condition(TempDeposit.class);
            Example.Criteria criteria = condition.createCriteria();
            criteria.andEqualTo("depositNo",orderNo);
            List<TempDeposit> depositList = depositMapper.selectByCondition(condition);
            if(CollectionUtils.isEmpty(depositList)){
                throw new ServiceException("用户订单信息不存在");
            }
            TempDeposit deposit = depositList.get(0);
            //微信端已支付，处理订单业务
            if (map.get("result_code").toString().equalsIgnoreCase("SUCCESS")) {
                //处理自己的业务逻辑
                //发送信息给微信知道当前处理完业务逻辑、返回成功标识、(更新订单、处理金额)
                if (processService.updateOrder(deposit.getCustomerNo(), orderNo, outOrderNo, "success") != 1) {
                    throw new ServiceException("订单状态更新失败");
                }
                accountService.updateWithDraw(deposit.getCustomerNo(), amount, orderNo,
                        FundTradeTypeEnum.MONEY_INTO.getCode(), (byte) 2);
                //返回给微信状态码
                return "success";
            } else {
                processService.updateOrder(deposit.getCustomerNo(), orderNo, outOrderNo, "fail");
                return "success";
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (DocumentException e) {
            e.printStackTrace();
        }
        return null;
    }
}
