package com.baibei.hengjia.api.modules.utils;

import com.jcraft.jsch.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.*;
import java.util.Properties;

/**
 * SFTP 工具类
 */
@Component
public class SFTPUtils {

    /**
     * SFTP 登录用户名
     */
    @Value("${sftp.username}")
    private String username;
    /**
     * SFTP 登录密码
     */
    @Value("${sftp.password}")
    private String password;
    /**
     * 私钥
     */
    private String privateKey;
    /**
     * SFTP 服务器地址IP地址
     */
    @Value("${sftp.host}")
    private String host;
    /**
     * SFTP 端口
     */
    @Value("${sftp.port}")
    private int port;

    @Value("${sftp.filePath}")
    private String filePath;

    private final Logger logger = LoggerFactory.getLogger(SFTPUtils.class);


    public Session getSession() throws JSchException {
        JSch jsch = new JSch();
        Session session;
        if (privateKey != null) {
            // 设置私钥
            jsch.addIdentity(privateKey);
        }
        session = jsch.getSession(username, host, port);
        if (password != null) {
            session.setPassword(password);
        }
        return session;
    }

    public ChannelSftp login(Session session) throws JSchException {
        ChannelSftp sftp;
        Properties config = new Properties();
        config.put("StrictHostKeyChecking", "no");
        session.setConfig(config);
        session.connect();
        Channel channel = session.openChannel("sftp");
        channel.connect();
        sftp = (ChannelSftp) channel;
        return sftp;
    }

    /**
     * 关闭连接 server
     */
    public void logout(ChannelSftp sftp, Session session) {
        if (sftp != null) {
            if (sftp.isConnected()) {
                sftp.disconnect();
            }
        }
        if (session != null) {
            if (session.isConnected()) {
                session.disconnect();
            }
        }
    }

    /**
     * 上传文件,多文件
     * 将输入流的数据上传到sftp作为文件
     *
     * @param sftpFileName sftp端文件名
     * @param input
     * @param sftp         sftp 通道
     * @throws SftpException
     */
    public void upload(String sftpFileName, InputStream input, ChannelSftp sftp) throws SftpException {
        try {
            sftp.cd(filePath);
        } catch (SftpException e) {
            //目录不存在，则创建文件夹
            sftp.mkdir(filePath);
            sftp.cd(filePath);
        }
        //上传文件
        sftp.put(input, sftpFileName);
    }

    /**
     * 支持单管道上传文件,如果要上传多个文件，占资源比较多,最好不要使用此方法,
     * 请使用 upload(String sftpFileName, InputStream input, ChannelSftp sftp)
     *
     * @param localFilePath 本地文件包含文件名
     */
    public Boolean upload(String localFilePath) {
        logger.info("当前文件开始上传..............................路径为{}", localFilePath);
        Session session = null;
        BufferedInputStream fileInputStream = null;
        ChannelSftp sftp = null;
        try {
            File file = new File(localFilePath);
            if (!file.exists()) {
                logger.info("当前文件{}不存在", localFilePath);
                return false;
            }
            fileInputStream = new BufferedInputStream(new FileInputStream(file));

            session = getSession();
            sftp = login(session);
            upload(file.getName(), fileInputStream, sftp);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (fileInputStream != null) {
                try {
                    fileInputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (session != null || sftp != null) {
                logout(sftp, session);
            }
        }
        return true;
    }

    /**
     * 下载文件。
     *
     * @param saveFile 存在本地的路径包含文件名
     */
    public void download(String saveFile, ChannelSftp sftp) {
        logger.info("当前sftp正在下载的文件............................路径为{}", saveFile);
        if (filePath != null && !"".equals(filePath)) {
            try {
                sftp.cd(filePath);
            } catch (SftpException e) {
                e.printStackTrace();
            }
        }
        File file = new File(saveFile);
        BufferedOutputStream fileOutputStream = null;
        try {
            fileOutputStream = new BufferedOutputStream(new FileOutputStream(file));
            sftp.get(file.getName(), fileOutputStream);
        } catch (SftpException | FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException ex) {
            ex.printStackTrace();
        } finally {
            if (fileOutputStream != null) {
                try {
                    fileOutputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * 支持单管道下载文件,在多文件操作，不建议此方法,比较占资源
     *
     * @param filePath
     * @return
     */
    public Boolean download(String filePath) {
        Session session = null;
        ChannelSftp sftp = null;
        try {
            session = getSession();
            sftp = login(session);
            download(filePath, sftp);
        } catch (Exception e) {
            logger.info("当前文件下载异常,异常信息为{}", e.getMessage());
            return false;
        } finally {
            if (session != null || sftp != null) {
                logout(sftp, session);
            }
        }
        return true;
    }


    /**
     * 判断远程SFTP服务器上是否存在某个文件
     *
     * @param directory 目录
     * @param fileName  文件名
     * @return 是否存在
     */
    public boolean isExists(String directory, String fileName, ChannelSftp sftp) {
        boolean isHave = false;
        try {
            sftp.cd(directory);
            SftpATTRS attrs = sftp.stat(fileName);
            if (attrs != null) {
                isHave = true;
            }
        } catch (Exception e) {
        }
        return isHave;
    }

    /**
     * 删除文件
     *
     * @param directory  要删除文件所在目录
     * @param deleteFile 要删除的文件
     */
    public void delete(String directory, String deleteFile, ChannelSftp sftp) throws SftpException {
        sftp.cd(directory);
        sftp.rm(deleteFile);
    }

}
