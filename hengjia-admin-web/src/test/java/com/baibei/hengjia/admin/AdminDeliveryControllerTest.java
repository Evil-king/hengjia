package com.baibei.hengjia.admin;

import com.alibaba.fastjson.JSON;
import com.baibei.hengjia.admin.modules.tradingQuery.bean.dto.DeliveryDto;
import com.baibei.hengjia.admin.modules.tradingQuery.bean.vo.DeliveryVo;
import com.baibei.hengjia.admin.modules.tradingQuery.service.IDeliveryService;
import com.baibei.hengjia.common.tool.page.MyPageInfo;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@Slf4j
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = AdminWebServiceApplication.class)
public class AdminDeliveryControllerTest {

    @Autowired
    private IDeliveryService deliveryService;

    @Test
    public void pageList() {
        DeliveryDto deliveryDto = new DeliveryDto();
        deliveryDto.setHoldType("");
        deliveryDto.setCurrentPage(1);
        deliveryDto.setPageSize(10);
        MyPageInfo<DeliveryVo> deliveryVoMyPageInfo = deliveryService.pageList(deliveryDto);
        log.info("deliveryVoMyPageInfo={}", JSON.toJSONString(deliveryVoMyPageInfo));
    }
}