package com.baibei.hengjia.api.modules.cash.process;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.domain.AlipayTradeWapPayModel;
import com.alipay.api.internal.util.AlipaySignature;
import com.alipay.api.request.AlipayTradeWapPayRequest;
import com.baibei.hengjia.api.modules.account.service.IAccountService;
import com.baibei.hengjia.api.modules.cash.aliPay.AlipayConfig;
import com.baibei.hengjia.api.modules.cash.dao.TempDepositMapper;
import com.baibei.hengjia.api.modules.cash.model.TempDeposit;
import com.baibei.hengjia.api.modules.cash.service.IProcessService;
import com.baibei.hengjia.api.modules.cash.template.PayTemplate;
import com.baibei.hengjia.api.modules.user.model.Customer;
import com.baibei.hengjia.api.modules.user.service.ICustomerService;
import com.baibei.hengjia.common.tool.api.ApiResult;
import com.baibei.hengjia.common.tool.enumeration.FundTradeTypeEnum;
import com.baibei.hengjia.common.tool.exception.ServiceException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import tk.mybatis.mapper.entity.Condition;
import tk.mybatis.mapper.entity.Example;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.UnsupportedEncodingException;
import java.util.*;

/**
 * @author hwq
 * @date 2019/06/22
 * <p>
 *     支付宝操作类
 * </p>
 */
@Slf4j
@Component
public class AliPayProcess extends PayTemplate {
    @Autowired
    private IProcessService prosscessService;
    @Autowired
    private AlipayConfig alipayConfig;
    @Autowired
    private ICustomerService customerService;
    @Autowired
    private IAccountService accountService;
    @Autowired
    private TempDepositMapper depositMapper;

    @Override
    protected ApiResult validate(JSONObject o) {
        return prosscessService.validate(o);
    }

    @Override
    protected TempDeposit createOrder(JSONObject o) {
        return prosscessService.createOrder(o);
    }

    @Override
    protected ApiResult sendToChannel(TempDeposit deposit, String templeStr) {
        ApiResult apiResult = new ApiResult();
        log.info("支付出参,deposit={}", JSON.toJSONString(deposit));

        //获取用户信息
        Customer customer = customerService.findByCustomerNo(deposit.getCustomerNo());
        log.info("获取用户信息,customerVo={}", JSON.toJSONString(customer));
        if (customer == null) {
            return ApiResult.error("该用户不存在");
        }

        //向支付宝发送支付请求
        AlipayClient alipayClient = new DefaultAlipayClient(alipayConfig.getURL(), alipayConfig.getAPPID()
                , alipayConfig.getPRIVATE_KEY(), alipayConfig.getFORMAT(), alipayConfig.getCHARSET()
                , alipayConfig.getPUBLIC_KEY(), alipayConfig.getSIGNTYPE());

        //创建Alipay支付请求对象
        AlipayTradeWapPayModel model = new AlipayTradeWapPayModel();
        model.setOutTradeNo(deposit.getDepositNo());
        model.setSubject(alipayConfig.getSubject());
        model.setProductCode("QUICK_WAP_WAY");
        model.setTimeoutExpress("5m");
        model.setEnablePayChannels("balance,moneyFund,debitCardExpress");
        model.setTotalAmount(deposit.getAmount().toString());

        AlipayTradeWapPayRequest request = new AlipayTradeWapPayRequest();
        log.info("decodeUrl={}", templeStr);
        request.setReturnUrl(templeStr); //同步通知url
        request.setNotifyUrl(alipayConfig.getNotify_url());//异步通知url
        request.setBizModel(model);//设置参数

        String form = "";//输出页面的表单
        try {
            form = alipayClient.pageExecute(request).getBody(); //调用SDK生成表单
            log.info("form={}", form);
            apiResult.setData(form);
        } catch (Exception e) {
            log.info("支付请求发送失败");
        }
        return apiResult;
    }

    @Override
    protected String payCallback(HttpServletRequest request) {
        // 一定要验签，防止黑客篡改参数
        Map<String, String[]> parameterMap = request.getParameterMap();
        StringBuilder notifyBuild = new StringBuilder("######【alipay notify】######");
        parameterMap.forEach((key, value) -> notifyBuild.append(key + "=" + value[0] + "\n"));
        log.info(notifyBuild.toString());
        // 获取支付宝POST过来反馈信息
        Map<String, String> params = new HashMap<>();
        Map requestParams = request.getParameterMap();
        log.info("支付宝回调返回对象：map={}", JSONObject.toJSONString(requestParams));
        for (Iterator iter = requestParams.keySet().iterator(); iter.hasNext(); ) {
            String name = (String) iter.next();
            String[] values = (String[]) requestParams.get(name);
            String valueStr = "";
            for (int i = 0; i < values.length; i++) {
                valueStr = (i == values.length - 1) ? valueStr + values[i]
                        : valueStr + values[i] + ",";
            }
            params.put(name, valueStr);
        }

        boolean flag = false;
        try {
            flag = AlipaySignature.rsaCheckV1(params,
                    alipayConfig.getPUBLIC_KEY(),
                    alipayConfig.getCHARSET(),
                    alipayConfig.getSIGNTYPE());

            if (flag) {
                //商户订单号
                String orderNo = null;
                orderNo = new String(request.getParameter("out_trade_no").getBytes("ISO-8859-1"), "UTF-8");
                //支付宝交易号
                String outOrderNum = new String(request.getParameter("trade_no").getBytes("ISO-8859-1"), "UTF-8");
                //交易状态
                String tradeStatus = new String(request.getParameter("trade_status").getBytes("ISO-8859-1"), "UTF-8");

                Condition condition = new Condition(TempDeposit.class);
                Example.Criteria criteria = condition.createCriteria();
                criteria.andEqualTo("depositNo", orderNo);
                List<TempDeposit> depositList = depositMapper.selectByCondition(condition);
                if (CollectionUtils.isEmpty(depositList)) {
                    throw new ServiceException("用户订单信息不存在");
                }
                TempDeposit deposit = depositList.get(0);


                // TRADE_FINISHED(表示交易已经成功结束，并不能再对该交易做后续操作);
                // TRADE_SUCCESS(表示交易已经成功结束，可以对该交易做后续操作，如：分润、退款等);
                if (tradeStatus.equals("TRADE_SUCCESS")) {
                    if (prosscessService.updateOrder(deposit.getCustomerNo(), deposit.getDepositNo(), outOrderNum, "success") != 1) {
                        throw new ServiceException("订单状态更新失败");
                    }
                    accountService.updateWithDraw(deposit.getCustomerNo(), deposit.getAmount(), orderNo,
                            FundTradeTypeEnum.MONEY_INTO.getCode(), (byte) 2);
                    return "success";
                } else if (tradeStatus.equals("TRADE_CLOSED")) {
                    if (prosscessService.updateOrder(deposit.getCustomerNo(), deposit.getDepositNo(), outOrderNum, "fail") != 1) {
                        throw new ServiceException("订单状态更新失败");
                    }
                    return "fail";
                }
            }
        } catch (AlipayApiException e) {
            log.error("调用支付宝接口发生异常", e.getMessage());
            return "fail";
        } catch (UnsupportedEncodingException e) {
            log.error("URLDecoderf发生异常", e.getMessage());
            return "fail";
        }
        return null;
    }
}
