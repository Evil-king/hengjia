package com.baibei.hengjia;

import com.baibei.hengjia.api.ApiApplication;
import com.baibei.hengjia.api.modules.cash.model.SigningRecord;
import com.baibei.hengjia.api.modules.cash.service.ISigningRecordService;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.List;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = ApiApplication.class)
public class SigningRecordTest {

    @Autowired
    private ISigningRecordService signingRecordService;

    /**
     * 测试身份证的唯一性
     */
    @Test
    public void testSigningOnlyIdCode() {
        List<SigningRecord> signingRecordList = signingRecordService.findAll();
        SigningRecord signingRecord = signingRecordList.get(0);
        Boolean result = signingRecordService.isOnlyIdCode(signingRecord.getIdCode());
        Assert.assertEquals(result, Boolean.TRUE);
    }
}
