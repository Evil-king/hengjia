package com.baibei.hengjia;

import com.baibei.hengjia.api.ApiApplication;
import com.baibei.hengjia.api.modules.settlement.biz.CleanBiz;
import com.baibei.hengjia.api.modules.trade.service.ITradeDayService;
import com.baibei.hengjia.common.tool.utils.DateUtil;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;

import java.util.Date;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = ApiApplication.class)
@AutoConfigureMockMvc
@Transactional()
public class TradeDayTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private WebApplicationContext webApplicationContext;

    private Logger logger = LoggerFactory.getLogger(getClass());
    @Autowired
    private ITradeDayService tradeDayService;
    @Autowired
    private CleanBiz cleanBiz;


    @Test
    @Rollback(false)
    public void tradeDay() {
        Date date = tradeDayService.getAddNTradeDay(10);
        System.out.println("十天之后的交易日为：" + DateUtil.yyyyMMddWithLine.get().format(date));


        Date date2 = tradeDayService.getAddNTradeDay(-10);
        System.out.println("十天之前的交易日为：" + DateUtil.yyyyMMddWithLine.get().format(date2));
    }
}

