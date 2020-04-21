package com.baibei.hengjia;

import com.baibei.hengjia.api.ApiApplication;
import com.baibei.hengjia.api.modules.utils.FTPUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.io.File;


@SpringBootTest(classes = ApiApplication.class)
@RunWith(SpringJUnit4ClassRunner.class)
public class FTPToolTest {

    @Autowired
    private FTPUtils ftpUtils;

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Test
    public void testFileUpload() {
        File file = new File("C:\\Users\\Administrator\\Desktop\\签约报文.enc");//把本地的文件的地址
        Long startTime = System.currentTimeMillis();
        ftpUtils.uploadFile(ftpUtils.initFtp(), file);
        Long endTime = System.currentTimeMillis();
        Long differenceTime = (endTime - startTime) / 1000;
        logger.info("当前时间总消耗时间为{}", differenceTime);
    }

    @Test
    public void testDownloadFile() {
        File file = new File("C:\\Users\\Administrator\\Desktop\\签约报文.enc");// 下载的文件名
        Long startTime = System.currentTimeMillis();
        ftpUtils.downloadFile(ftpUtils.initFtp(), file);
        long endTime = System.currentTimeMillis();
        Long differenceTime = (endTime - startTime) / 1000;
        logger.info("当前时间总消耗时间为{}", differenceTime);
    }

}
