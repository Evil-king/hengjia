package com.baibei.hengjia.admin;

import com.alibaba.fastjson.JSONObject;
import com.baibei.hengjia.admin.modules.advisory.bean.dto.AdvisoryArticleDto;
import com.baibei.hengjia.admin.modules.advisory.bean.vo.AdvisoryArticleVo;
import com.baibei.hengjia.admin.modules.advisory.service.IAdvisoryArticleService;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@Slf4j
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = AdminWebServiceApplication.class)
public class AdvisoryArticleTest {

    @Autowired
    private IAdvisoryArticleService advisoryArticleService;

    @Test
    public void addAndEdit(){
        AdvisoryArticleDto advisoryArticleDto = new AdvisoryArticleDto();
        advisoryArticleDto.setId("9");
        advisoryArticleDto.setArticleContent("测试-1113");
        advisoryArticleDto.setArticleDisplay("show");
        advisoryArticleDto.setArticleImage("生dasd.jpg");
        advisoryArticleDto.setArticleTitle("文章85651");
        advisoryArticleDto.setArticleType("url");
        advisoryArticleDto.setArticleUrl("http://www.sina.com");
        advisoryArticleDto.setNavigationId("1,2");
        advisoryArticleService.addAndEdit(advisoryArticleDto);
    }

    @Test
    public void lookObj(){
        AdvisoryArticleVo advisoryArticleVo = advisoryArticleService.lookObj(1);
        log.info("advisoryArticleVo={}", JSONObject.toJSONString(advisoryArticleVo));
    }

    @Test
    public void batchOperator(){
        advisoryArticleService.batchOperator("1", "hidden");
    }
}
