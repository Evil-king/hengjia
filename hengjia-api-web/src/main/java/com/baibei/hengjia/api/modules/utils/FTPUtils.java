package com.baibei.hengjia.api.modules.utils;

import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;
import org.apache.commons.net.ftp.FTPReply;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.*;

/**
 * FTP 工具类
 */
@Component
public class FTPUtils {

    private final Logger logger = LoggerFactory.getLogger(FTPUtils.class);

    @Value("${ftp.host}")
    private String ftpHost;

    @Value("${ftp.port}")
    private int ftpPort;

    @Value("${ftp.name}")
    private String ftpName;

    @Value("${ftp.password}")
    private String ftpPwd;

    @Value("${ftp.controlEncoding}")
    private String controlEncoding;  //设置文件传输的编码

    @Value("${ftp.path}")
    private String remotePath; //ftp上传和下载文件目录


    /**
     * 初始化FTP 连接
     *
     * @return
     */
    public FTPClient initFtp() {
        int reply;
        FTPClient ftp = new FTPClient();
        try {
            // 1.连接服务器
            ftp.setControlEncoding(controlEncoding);
            ftp.connect(ftpHost, ftpPort);
            // 2.登录服务器 如果采用默认端口，可以使用ftp.connect(url)的方式直接连接FTP服务器
            ftp.login(ftpName, ftpPwd);
            // 3.判断登陆是否成功
            reply = ftp.getReplyCode();
            if (!FTPReply.isPositiveCompletion(reply)) {
                ftp.abort();
                ftp.disconnect();
                logger.error("当前登录失败");
            }
        } catch (Exception e) {
            e.printStackTrace();
            new Exception("服务器连接失败");
        }
        return ftp;
    }

    /**
     * 文件上传Ftp,如果成功获取结果
     * 其中file如果是目录,则会把目录中所有的文件上传到ftp上
     *
     * @param file 本地文件路径
     * @param ftp  启动ftp客户端
     */
    public Boolean uploadFile(FTPClient ftp, File file) {
        String relativeRemotePath = remotePath + "/" + file.getName();
        logger.info("当前ftp上传文件目录为{}", relativeRemotePath);
        if (!ftp.isConnected() || !ftp.isAvailable()) {
            logger.info(">>>>>FTP服务器连接已经关闭或者连接无效*****放弃文件上传****");
            return false;
        }
        if (file == null && !file.exists()) {
            logger.info(">>>>>待上传文件为空或者文件不存在*****放弃文件上传****");
            return false;
        }
        try {
            if (file.isDirectory()) {
                // 如果目录不存在,则会在ftp中创建此目录
                ftp.makeDirectory(relativeRemotePath);
                ftp.changeWorkingDirectory(relativeRemotePath); //切换ftp上传文件的目录
                File[] listFiles = file.listFiles();
                for (int i = 0; i < listFiles.length; i++) {
                    File loopFile = listFiles[i];
                    if (loopFile.isDirectory()) {
                        uploadFile(ftp, file);
                        // 这一步细节很关键，子目录上传完成后，必须将工作目录返回上一层，否则容易导致文件上传后，目录不一致
                        ftp.changeToParentDirectory();
                    } else {
                        /**如果目录中全是文件，则直接上传*/
                        BufferedInputStream fileInputStream = new BufferedInputStream(new FileInputStream(loopFile));
                        ftp.storeFile(loopFile.getName(), fileInputStream);
                        fileInputStream.close();
                        logger.info(">>>>>文件上传成功****", loopFile.getParentFile());
                    }
                }
                return true;
            } else {
                // 本地的LocalFile
                BufferedInputStream input = new BufferedInputStream(new FileInputStream(file));
                boolean result = ftp.storeFile(relativeRemotePath, input);
                if (!result) {
                    logger.error("上传文件失败");
                }
                input.close();
                return result;
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return false;
    }

    /**
     * 在ftp下载文件到本地中,成功以后，进行逻辑处理
     *
     * @param file 本地文件路径
     * @param ftp  ftp客户端
     * @return
     */
    public Boolean downloadFile(FTPClient ftp, File file) {
        if (!ftp.isConnected() || !ftp.isAvailable()) {
            System.out.println(">>>>>FTP服务器连接已经关闭或者连接无效*********");
            return false;
        }
        // stop 1 获取 ftp 在文件中位置
        String relativeRemotePath = remotePath + "/" + file.getName();
        logger.info("当前在ftp下载文件时路径为{}", relativeRemotePath);
        try {
            /**没有对应路径时，FTPFile[] 大小为0，不会为null*/
            FTPFile[] ftpFiles = ftp.listFiles(relativeRemotePath);
            FTPFile ftpFile = null;
            if (ftpFiles.length >= 1) {
                ftpFile = ftpFiles[0];
            } else {
                logger.error("当前{}文件找不到", relativeRemotePath);
            }
            // stop 2 把文件下载到本地
            if (ftpFile != null && ftpFile.isFile()) {
                if (file.getParentFile().exists()) {
                    logger.info("本地路径的父目录不存在,正在创建................");
                    file.getParentFile().mkdirs();
                }
                BufferedOutputStream  outputStream = new BufferedOutputStream(new FileOutputStream(file));
                // stop 3 切换ftp文件所在目录
                ftp.changeWorkingDirectory(remotePath);
                // stop 4 下载文件到本地目录
                Boolean result = ftp.retrieveFile(ftpFile.getName(), outputStream);
                if (!result) {
                    logger.error("当前下载文件错误");
                }
                // stop 5 关闭输出流
                outputStream.flush();
                outputStream.close();
                return result;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * 在使用完以后,必须要关闭FTpClietn流通道
     *
     * @param ftpClient
     * @return
     */
    public FTPClient closeFTPConnect(FTPClient ftpClient) {
        try {
            if (ftpClient != null && ftpClient.isConnected()) {
                ftpClient.abort();
                ftpClient.disconnect();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return ftpClient;
    }

}
