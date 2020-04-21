package com.baibei.hengjia;

import com.alibaba.fastjson.JSONObject;
import com.baibei.hengjia.api.ApiApplication;
import com.baibei.hengjia.api.modules.advisory.bean.dto.AdvisoryNavigationDetailsDto;
import com.baibei.hengjia.api.modules.advisory.bean.vo.AdvisoryNavigationDetailsVo;
import com.baibei.hengjia.api.modules.advisory.bean.vo.AdvisoryNavigationListVo;
import com.baibei.hengjia.api.modules.advisory.service.IAdvisoryNavigationDetailsService;
import com.baibei.hengjia.api.modules.advisory.service.IAdvisoryNavigationService;
import com.baibei.hengjia.common.tool.page.MyPageInfo;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.List;

@Slf4j
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = ApiApplication.class)
public class AdvisoryNavigationTest {

    @Autowired
    private IAdvisoryNavigationService advisoryNavigationService;
    @Autowired
    private IAdvisoryNavigationDetailsService advisoryNavigationDetailsService;

    @Test
    public void navigationList(){
        List<AdvisoryNavigationListVo> advisoryNavigationListVos = advisoryNavigationService.navigationList();
        log.info("advisoryNavigationListVos={}", JSONObject.toJSONString(advisoryNavigationListVos));
    }

    @Test
    public void navigationDetails(){
        AdvisoryNavigationDetailsDto advisoryNavigationDetailsDto = new AdvisoryNavigationDetailsDto();
        advisoryNavigationDetailsDto.setId(1);
        advisoryNavigationDetailsDto.setCurrentPage(1);
        advisoryNavigationDetailsDto.setPageSize(10);
        MyPageInfo<AdvisoryNavigationDetailsVo> advisoryNavigationDetalsVoMyPageInfo = advisoryNavigationDetailsService.navigationDetails(advisoryNavigationDetailsDto);
        log.info("advisoryNavigationDetalsVoMyPageInfo={}", JSONObject.toJSONString(advisoryNavigationDetalsVoMyPageInfo));
    }
}
