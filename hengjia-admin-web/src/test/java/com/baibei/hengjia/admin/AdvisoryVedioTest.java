package com.baibei.hengjia.admin;

import com.alibaba.fastjson.JSONObject;
import com.baibei.hengjia.admin.modules.advisory.bean.dto.AdvisoryVideoDto;
import com.baibei.hengjia.admin.modules.advisory.bean.dto.AdvisoryVideoListDto;
import com.baibei.hengjia.admin.modules.advisory.bean.vo.AdvisoryVideoVo;
import com.baibei.hengjia.admin.modules.advisory.service.IAdvisoryVideoService;
import com.baibei.hengjia.common.tool.api.ApiResult;
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
public class AdvisoryVedioTest {

    @Autowired
    private IAdvisoryVideoService advisoryVideoService;

    @Test
    public void addAndEdit() {
        AdvisoryVideoDto advisoryVideoDto = new AdvisoryVideoDto();
        advisoryVideoDto.setNavigationId("1,2");
        advisoryVideoDto.setVideoDisplay("hidden");
        advisoryVideoDto.setVideoIndex("boy.jpg");
        advisoryVideoDto.setVideoTitle("hi");
        advisoryVideoDto.setVideoType("vedio");
        advisoryVideoDto.setVideoUrl("http://www.baidu.com");
        advisoryVideoDto.setVideoSort("2");

        ApiResult apiResult = advisoryVideoService.addAndEdit(advisoryVideoDto);
        log.info("apiResult={}", JSONObject.toJSONString(apiResult));
    }

    @Test
    public void pageList() {
        AdvisoryVideoListDto advisoryVideoListDto = new AdvisoryVideoListDto();
        advisoryVideoListDto.setCurrentPage(1);
        advisoryVideoListDto.setPageSize(10);
        MyPageInfo<AdvisoryVideoVo> pageInfo = advisoryVideoService.objList(advisoryVideoListDto);
        log.info("pageInfo={}", JSONObject.toJSONString(pageInfo));
    }

    @Test
    public void lookObj() {
        AdvisoryVideoVo advisoryVideoVo = advisoryVideoService.lookObj(2);
        log.info("advisoryVideoVo={}", JSONObject.toJSONString(advisoryVideoVo));
    }

    public static void main(String[] args) {
        String str = "3,2,5,6";
        String navigationId = "3";
        String[] strArry = str.split(",");
            if(!strArry[0].equals(navigationId)){
                str = str.replaceAll(","+navigationId,"");
            } else {
                str = str.replaceAll(navigationId+",","");
            }
        log.info("str={}", str);
    }
}
