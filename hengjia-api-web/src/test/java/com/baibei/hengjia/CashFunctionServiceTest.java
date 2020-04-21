package com.baibei.hengjia;

import com.baibei.hengjia.api.ApiApplication;
import com.baibei.hengjia.api.modules.cash.bean.dto.FilePlannedSpeedDto;
import com.baibei.hengjia.api.modules.cash.bean.dto.ReconciliationMatchingDto;
import com.baibei.hengjia.api.modules.cash.component.BankBackMessageAnalysis;
import com.baibei.hengjia.api.modules.cash.bean.dto.SignInBackDto;
import com.baibei.hengjia.api.modules.cash.enumeration.CashFunctionType;
import com.baibei.hengjia.api.modules.cash.service.ICashFunctionService;
import com.baibei.hengjia.common.tool.api.ApiResult;
import com.baibei.hengjia.common.tool.api.ResultEnum;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.util.Map;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = ApiApplication.class)
public class CashFunctionServiceTest {

    @Autowired
    private List<ICashFunctionService> cashFunctionServiceList;

    /**
     * 报文解析
     */
    @Autowired
    private BankBackMessageAnalysis bankInterfaceBackMessage;


    @Value("${cash.Qydm}")
    private String Qydm;

    @Value("${cash.socketIP}")
    protected String socketIP;

    @Value("${cash.socketPort}")
    protected String socketPort;

    private final Logger logger = LoggerFactory.getLogger(this.getClass());


    /**
     * 签约报文测试
     */
    @Test
    public void testSigningService() {
        String message= "A0010301019203                0000000273000000EB001012019070914543119070996770739      999999                                                                                                    00000000000000000000000000000130301                20190709145431999999                                          000000151EB00119070996770739      92031&15000098413560&888800526513811&柴方成&1765&1&220225196512191418&6222023500018391447&3&3&柴方成&102261032074&中国工商银行股份有限公司哈尔滨中山支行&&&";
        Map<String, String> result = bankInterfaceBackMessage.parsingTranMessageString(message);
        String tranFunc = result.get("TranFunc");
        if (tranFunc != null) {
            ICashFunctionService iCashFunctionService = cashFunctionServiceList.stream()
                    .filter(function -> function.getType().getIndex() == Integer.valueOf(tranFunc))
                    .findFirst().orElse(null);
            String bodyMessages = iCashFunctionService.response(result);
            logger.info("当前的报文结果为:{}", bodyMessages);
        }
    }

    /**
     * 入金报文测试
     */
    @Test
    public void testDepositServer() {
        String message = "A0010301019203                0000000209000000LX01 012019070417465218121020572829      999999                                                                                                    000000000000000000000000000001310010               20190704174652999999                                          000000087LX01 18121020572829      920315000098413560&888800120689700&1000.00&6225881207235218&杨智远&RMB&20190704&0015670994&";
        Map<String, String> result = bankInterfaceBackMessage.parsingTranMessageString(message);
        String tranFunc = result.get("TranFunc");
        if (tranFunc != null) {
            ICashFunctionService iCashFunctionService = cashFunctionServiceList.stream()
                    .filter(function -> function.getType().getIndex() == Integer.valueOf(tranFunc))
                    .findFirst().orElse(null);
            String bodyMessages = iCashFunctionService.response(result);
            logger.info("当前的报文结果为:{}", bodyMessages);
            // 获取通讯报文头+业务报文体
            String qydm = bodyMessages.substring(344 - Qydm.length(), 344);
            Assert.assertEquals(qydm, Qydm);
        }
    }

    /**
     * 签到或签退测试
     */
    @Test
    public void testSigningInBack() {
        ICashFunctionService cashFunctionService = cashFunctionServiceList.stream().
                filter(function -> function.getType().getIndex() == CashFunctionType.SIGN_IN_BACK.getIndex()).findFirst().orElse(null);
        if (cashFunctionService != null) {
            SignInBackDto signInBackDto = new SignInBackDto();
            signInBackDto.setFuncFlag("1"); //1：签到 2：签退
            ApiResult request = cashFunctionService.request(signInBackDto);
            Assert.assertEquals(request.getCode(), Integer.valueOf(ResultEnum.SUCCESS.getCode()));
        }
    }


    /**
     * 交易网发起出入金流水对账及会员开销户流水匹配
     * 开始时间
     * 结束时间
     */
    @Test
    public void testReconciliationMatching() throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        ICashFunctionService cashFunctionService = cashFunctionServiceList.stream().
                filter(function -> function.getType() == CashFunctionType.DEPOSIT_WITHDRAW_MEMBERS_DETAIL).findFirst().orElse(null);
        ReconciliationMatchingDto reconciliationMatchingDto = new ReconciliationMatchingDto();
        reconciliationMatchingDto.setFuncFlag("2"); // 会员开销户对账流水
        /*   reconciliationMatchingDto.setFuncFlag("1"); // 出入金流水对账*/
        LocalDate nowDate = LocalDate.now();
        LocalDate endDate = nowDate.plusDays(1);
        reconciliationMatchingDto.setBeginDateTime(sdf.parse(nowDate.toString()));
        reconciliationMatchingDto.setEndDateTime(sdf.parse(endDate.toString()));
        ApiResult result = cashFunctionService.request(reconciliationMatchingDto);
        Assert.assertEquals(result.getCode(), Integer.valueOf(ResultEnum.SUCCESS.getCode()));
    }


    /**
     * 查看文件进度
     */
    @Test
    public void testFilePlannedSpeed() {
        ICashFunctionService cashFunctionService = cashFunctionServiceList.stream().filter(function -> function.getType() == CashFunctionType.FIND_FILE_PLANNED_SPEED)
                .findFirst().orElse(null);
        Assert.assertNotNull(cashFunctionService);
        FilePlannedSpeedDto filePlannedSpeedDto = new FilePlannedSpeedDto();
        //filePlannedSpeedDto.setFuncFlag("4"); //出入金流水
        filePlannedSpeedDto.setFuncFlag("1"); //清算
        //filePlannedSpeedDto.setFuncFlag("2"); // 余额对账
        //filePlannedSpeedDto.setFuncFlag("5"); // 开销户流水对账
        filePlannedSpeedDto.setBeginDate(new Date());
        filePlannedSpeedDto.setEndDate(new Date());
        ApiResult result = cashFunctionService.request(filePlannedSpeedDto);
        Assert.assertEquals(result.getCode(), Integer.valueOf(ResultEnum.SUCCESS.getCode()));
    }


}
