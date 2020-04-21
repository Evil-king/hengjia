package com.baibei.hengjia;

import com.baibei.hengjia.api.ApiApplication;
import com.baibei.hengjia.api.modules.cash.component.PABDocumentUtils;
import com.baibei.hengjia.api.modules.cash.service.ISignInBackService;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = ApiApplication.class)
public class DocumentsEncrypt {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    @Autowired
    private PABDocumentUtils pabDocumentUtils;


    @Test
    public void testFileEncrypt() throws Exception {

        String fsrc = "C:\\Users\\Administrator\\Desktop\\签约报文"; //文件目录

        //文件加密,生成加密文件
        String filePassword = pabDocumentUtils.FileEncrypt(fsrc);
        File file = new File(fsrc);
        if (file.exists()) { // 删除原始的文件,
            file.delete();
        }
        // 文件解密
        if (!file.exists()) { // 文件不存在可以解密
            logger.info("文件不存在");
            pabDocumentUtils.FileDecryption(fsrc, filePassword);
        }
    }



    @Test
    public void testFileOut() {
        File file = new File("C:\\Users\\Administrator\\Desktop\\test.txt");
        try {
            file.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
        String contenxt = "3&-100&\r\n";
        byte[] bytes;
        bytes = contenxt.getBytes();
        FileOutputStream fileOutputStream = null;
        BufferedOutputStream out = null;
        try {
            fileOutputStream = new FileOutputStream(file);
            out = new BufferedOutputStream(fileOutputStream);
            out.write(bytes);
            String ff = "1&20100104110551&EB001&11000092379502&31&888800000005986&测试十三&62304&1600002&RMB&500.0&100.0&0.00&0.00&0.0&0.0&10.0&2&30000.00&125.00&会员日终扎差清算&&\r\n";
            for (int i = 0; i < 10; i++) {
                out.write(ff.getBytes());
            }
            out.flush();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (fileOutputStream != null) {
                try {
                    fileOutputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (out != null) {
                try {
                    out.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

    }
}
