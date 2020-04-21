package com.baibei.hengjia.api.modules.cash.web;

import com.baibei.hengjia.api.modules.utils.SFTPUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/api/sftp")
public class ApiSftpUploadController {

    @Autowired
    private SFTPUtils sftpUtils;


    @GetMapping("/uploadFile")
    public boolean uploadFile(String filePath) {
        return sftpUtils.upload(filePath);
    }

    /**
     * 下载到本地
     *
     * @param filePath
     */
    @GetMapping("/download")
    public Boolean download(String filePath) {
        return sftpUtils.download(filePath);
    }
}
