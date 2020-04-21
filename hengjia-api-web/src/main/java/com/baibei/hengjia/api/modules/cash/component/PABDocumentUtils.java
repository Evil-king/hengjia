package com.baibei.hengjia.api.modules.cash.component;

import com.sdb.util.Base64;
import com.sdb.util.SignUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.security.SecureRandom;
import java.util.Random;

/**
 * 平安银行文件加密和解密，上传和下载工具类
 * 1、使用ftp 工具类上传的注意点
 * 把文件在本地进行加密,如使用ftp工具类上传到银行的ftp指定的路径下,不过要申请平安银行IP地址放行才行
 * 要不然无法上传文件！
 * <p>
 * 2、使用当前工具类上传文件和下载文件的方法，可以不需要申请平安银行IP地址开发，通过B2B客户端直接上传
 * 无需要加密和解密。
 */
@Component
public class PABDocumentUtils {

    private final Logger logger = LoggerFactory.getLogger(PABDocumentUtils.class);

   @Value("${ALG:DesEde/CBC/PKCS5Padding}")
    private String ALG; // 加密的方式



    /**
     * @param filePath 文件在本地的路径,包含文件名
     * @return
     * @throws Exception
     */
    public String FileEncrypt(String filePath) throws Exception {
        Random random = new SecureRandom();
        byte[] bkey = new byte[24];
        random.nextBytes(bkey);
        // stop 1 生成文件加密的密码
        String password = new String(Base64.encode(bkey));

        // stop 2 生成加密的文件
        String srcFile = filePath;
        String zipFile = srcFile + ".zip";
        String encFile = srcFile + ".enc";
        // stop 3 加压
        SignUtil.compress(srcFile, zipFile);
        // stop 4 加密
        SignUtil.encrypt(zipFile, encFile, bkey, ALG, "DesEde", null);
        return password;
    }

    /**
     * @param filePath 文件在本地的路径,包含文件名
     * @param password 文件解密需要的密码
     * @return
     */
    public void FileDecryption(String filePath, String password) throws Exception {
        String desFile = filePath;
        String srcFile = filePath + ".enc";
        String zipFile = filePath + ".zip";
        // stop 1解密需要加密的密码，如果没有key则解密失败
        byte[] bkey = Base64.decode(password.getBytes());
        // stop 2 解密
        SignUtil.decrypt(srcFile, zipFile, bkey, ALG, "DesEde", null);
        // stop 3 解压
        SignUtil.uncompress(zipFile, desFile);
    }

}
