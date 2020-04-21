package com.baibei.hengjia.admin.modules.advisory.web;

import com.baibei.hengjia.admin.modules.advisory.utils.UploadUtils;
import com.baibei.hengjia.common.tool.api.ApiResult;
import com.baibei.hengjia.common.tool.enumeration.FileTypeEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Arrays;

/**
 * 上传图片
 */
@RestController
@RequestMapping("/api/advisory/tool")
public class UploadImageController{


    @Autowired
    private UploadUtils uploadUtils;

    @Value("${oos.path.prefix}")
    private String oosPathPrefix;

    @PostMapping(value = "/uploadImage", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ApiResult<String> uploadImage(@RequestPart("file") MultipartFile file) {
        String filePath;
        if(file.isEmpty()){
            return ApiResult.error("上传图片参数有问题");
        }
        if (file.getSize() > 10 * 1024 * 1024) {
            return ApiResult.error("上传图片不能大于10MB");
        }
        try {
            if (!uploadUtils.checkImageType(file.getInputStream())) {
                return ApiResult.error("图片仅支持jpeg,png,gif格式");
            }
            filePath = uploadUtils.uploadImg3(file);
        } catch (Exception e) {
            e.printStackTrace();
            return ApiResult.error();
        }
        return ApiResult.success(oosPathPrefix+filePath);
    }

    @PostMapping(value = "/uploadVideo", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ApiResult<String> uploadVideo(@RequestPart("file") MultipartFile file) {
        String filePath;
        try {
            if(file.isEmpty()){
                return ApiResult.error("上传音频参数有问题");
            }
            String fileName = file.getOriginalFilename();
            String suffix = fileName.substring(fileName.lastIndexOf(".") + 1);
            if (!suffix.equals(FileTypeEnum.MP3.getValue()) && !suffix.equals(FileTypeEnum.MP4.getValue())) {
                return ApiResult.error("咨询仅支持mp3、mp4格式");
            }
            filePath = uploadUtils.uploadVedio(file);
        } catch (Exception e) {
            e.printStackTrace();
            return ApiResult.error();
        }
        return ApiResult.success(oosPathPrefix+filePath);
    }

    @PostMapping(value = "/uploadImages", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ApiResult<String> uploadImages(@RequestParam("files") MultipartFile[] files) {
        if(files.length == 0){
            return ApiResult.error("上传图片参数有问题");
        }
        Long count = Arrays.stream(files).filter(file -> file.getSize() > 10 * 1024 * 1024).count();
        if (count > 0) {
            return ApiResult.error("上传图片不能大于10MB");
        }
        Long imagesError = Arrays.stream(files).filter(file -> {
            try {
                if (!uploadUtils.checkImageType(file.getInputStream())) {
                    return true;
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            return false;
        }).count();
        if (imagesError > 0) {
            return ApiResult.error("上传图片仅支持jpeg,png,gif格式");
        }
        StringBuilder filePathStr = new StringBuilder();
        try {
            for (MultipartFile file : files) {
                filePathStr.append(oosPathPrefix+uploadUtils.uploadImg3(file));
                filePathStr.append(",");
            }
        } catch (Exception e) {
            e.printStackTrace();
            ApiResult.error();
        }
        String filePath = filePathStr.toString();
        if (!StringUtils.isEmpty(filePath)) {
            filePath = filePath.substring(0, filePath.length() - 1);
        }
        return ApiResult.success(filePath);
    }
}
