package com.baibei.hengjia.admin;

import com.alibaba.fastjson.JSONObject;
import com.baibei.hengjia.admin.modules.advisory.bean.dto.AdvisoryNavigationDetailsDto;
import com.baibei.hengjia.admin.modules.advisory.bean.dto.AdvisoryNavigationDto;
import com.baibei.hengjia.admin.modules.advisory.bean.dto.AdvisoryNavigationListDto;
import com.baibei.hengjia.admin.modules.advisory.bean.dto.EditSortDto;
import com.baibei.hengjia.admin.modules.advisory.bean.vo.AdvisoryNavigationDetailVo;
import com.baibei.hengjia.admin.modules.advisory.bean.vo.AdvisoryNavigationListVo;
import com.baibei.hengjia.admin.modules.advisory.bean.vo.AdvisoryNavigationVo;
import com.baibei.hengjia.admin.modules.advisory.service.IAdvisoryNavigationDetailsService;
import com.baibei.hengjia.admin.modules.advisory.service.IAdvisoryNavigationService;
import com.baibei.hengjia.common.tool.api.ApiResult;
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
@SpringBootTest(classes = AdminWebServiceApplication.class)
public class AdvisoryNavigationTest {

    @Autowired
    private IAdvisoryNavigationService advisoryNavigationService;
    @Autowired
    private IAdvisoryNavigationDetailsService advisoryNavigationDetailsService;

    @Test
    public void addAdvisoryNavigation(){
        AdvisoryNavigationDto advisoryNavigationDto = new AdvisoryNavigationDto();
        advisoryNavigationDto.setNavigationName("导航3");
        advisoryNavigationDto.setNavigationDisplay("show");
        advisoryNavigationDto.setNavigationSort("3");
        ApiResult apiResult = advisoryNavigationService.addObj(advisoryNavigationDto);
        log.info("apiResult={}", apiResult);
    }

    @Test
    public void navigationList(){
        List<AdvisoryNavigationListVo> advisoryNavigationListVos = advisoryNavigationService.navigationList();
        log.info("advisoryNavigationListVos={}", JSONObject.toJSONString(advisoryNavigationListVos));
    }

    @Test
    public void navigationPageList(){
        AdvisoryNavigationListDto advisoryNavigationListDto = new AdvisoryNavigationListDto();
        advisoryNavigationListDto.setCurrentPage(1);
        advisoryNavigationListDto.setPageSize(10);
//        advisoryNavigationListDto.setNavigationName("导航一");
        MyPageInfo<AdvisoryNavigationVo> pageInfo = advisoryNavigationService.objList(advisoryNavigationListDto);
        log.info("pageInfo={}",JSONObject.toJSONString(pageInfo));
    }

    @Test
    public void navigationDetailsList(){
        AdvisoryNavigationDetailsDto advisoryNavigationDetailsDto = new AdvisoryNavigationDetailsDto();
        advisoryNavigationDetailsDto.setCurrentPage(1);
        advisoryNavigationDetailsDto.setPageSize(10);
        MyPageInfo<AdvisoryNavigationDetailVo> pageInfo = advisoryNavigationDetailsService.objList(advisoryNavigationDetailsDto);
        log.info("pageInfo={}",JSONObject.toJSONString(pageInfo));
    }

    @Test
    public void editSort(){
        EditSortDto editSortDto = new EditSortDto();
        editSortDto.setId("7");
        editSortDto.setSort("1");
        advisoryNavigationDetailsService.editSort(editSortDto);
    }

    @Test
    public void batchDelete(){
        advisoryNavigationService.batchDelete("4");
    }

    @Test
    public void deleteDetails(){
        advisoryNavigationDetailsService.deleteDetails("6");
    }
}
