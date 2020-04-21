package com.baibei.hengjia.admin.modules.advisory.utils;

import com.aliyun.oss.OSSClient;
import com.aliyun.oss.model.ObjectMetadata;
import com.baibei.hengjia.common.tool.enumeration.FileTypeEnum;
import com.baibei.hengjia.common.tool.utils.FileTypeUtil;
import lombok.extern.slf4j.Slf4j;
import net.coobird.thumbnailator.Thumbnails;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.UUID;

/**
 * 获取图片,可以通过阿里云的网址直接访问,格式如下
 * 其中endpoint要去掉http前缀
 * filePath,是uploadFilePath+自定义文件夹+文件名字
 * http://${bucketName}.${endpoint}/${filePath}
 * 参考如下:
 * http://cloudtree-test.oss-cn-shenzhen.aliyuncs.com/data/UploadFile/backimg/20190805/test.jpg
 */
@Component
@Slf4j
public class UploadUtils {

    @Value("${oss.accessKeyId}")
    private String accessKeyId;
    @Value("${oss.accessKeySecret}")
    private String accessKeySecret;
    @Value("${oss.endpoint}")
    private String endpoint;
    @Value("${oss.bucketName}")
    private String bucketName;

    @Value("${backend.hpme.upload.file.path}")
    private String uploadFilePath;

    @Value("${frontend.qrcode_url}")
    private String ossPath;


    private final List<FileTypeEnum> fileTypeEnumList = Arrays.asList(FileTypeEnum.JPEG, FileTypeEnum.PNG, FileTypeEnum.GIF);

    public String uploadImg(InputStream input) throws IOException {

        // 初始化一个OSSClient
        OSSClient client = new OSSClient(endpoint, accessKeyId, accessKeySecret);
        ObjectMetadata meta = new ObjectMetadata();

        // 必须设置ContentLength
        meta.setContentType("image/jpeg");

        meta.setCacheControl("max-age=8640000");
        // 把图片读入到内存中
        BufferedImage bufImg = ImageIO.read(input);
        // 压缩代码
        bufImg = Thumbnails.of(bufImg).width(1024).keepAspectRatio(true).outputQuality(0.9).asBufferedImage();
        ByteArrayOutputStream bos = new ByteArrayOutputStream();// 存储图片文件byte数组

        //防止图片变红
        BufferedImage newBufferedImage = new BufferedImage(bufImg.getWidth(), bufImg.getHeight(), BufferedImage.TYPE_INT_RGB);
        newBufferedImage.createGraphics().drawImage(bufImg, 0, 0, Color.WHITE, null);

        ImageIO.write(bufImg, "jpg", bos); // 图片写入到 ImageOutputStream
        input = new ByteArrayInputStream(bos.toByteArray());

        long file_size = input.available();
        meta.setContentLength(file_size);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        String ymd = sdf.format(new Date()) + "/";
        // 上传Object.
        String filePath = uploadFilePath + ymd + UUID.randomUUID().toString() + ".jpg";
        client.putObject(bucketName, filePath, input, meta);
        return filePath;
    }


    public String uploadImg2(InputStream input, MultipartFile filedata) throws Exception {

//        if (filedata.getSize() > 10 * 1024 * 1024) {
//            throw new Exception("outSize");
//        }
        // 初始化一个OSSClient
        OSSClient client = new OSSClient(endpoint, accessKeyId, accessKeySecret);
        ObjectMetadata meta = new ObjectMetadata();

        // 必须设置ContentLength
        meta.setContentType("image/jpeg");

        meta.setCacheControl("max-age=8640000");

        long file_size = input.available();

        meta.setContentLength(file_size);

        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        String ymd = sdf.format(new Date()) + "/";
        // 上传Object.
        String originalFilename = filedata.getOriginalFilename();
        int fileIndex = originalFilename.lastIndexOf(".");
        String suffix = originalFilename.substring(fileIndex);
        String filePath = uploadFilePath + ymd + UUID.randomUUID().toString() + suffix;
        client.putObject(bucketName, filePath, input, meta);
        return filePath;
    }


    public String uploadVedio(InputStream input, MultipartFile filedata) throws Exception {

        // 初始化一个OSSClient
        OSSClient client = new OSSClient(endpoint, accessKeyId, accessKeySecret);
        ObjectMetadata meta = new ObjectMetadata();

        // 必须设置ContentLength
        meta.setContentType("application/octet-stream");

        meta.setCacheControl("max-age=8640000");

        long file_size = input.available();

        meta.setContentLength(file_size);

        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        String ymd = sdf.format(new Date()) + "/";
        // 上传Object.
        String originalFilename = filedata.getOriginalFilename();
        int fileIndex = originalFilename.lastIndexOf(".");
        String suffix = originalFilename.substring(fileIndex);
        String filePath = uploadFilePath + ymd + UUID.randomUUID().toString() + suffix;
        client.putObject(bucketName, filePath, input, meta);
        return filePath;
    }

    public String uploadImg3(MultipartFile filedata) throws Exception {
        InputStream input = filedata.getInputStream();
        return uploadImg2(input, filedata);
    }

    public String uploadVedio(MultipartFile filedata) throws Exception {
        InputStream input = filedata.getInputStream();
        return uploadVedio(input, filedata);
    }

    /**
     * 图片仅支持jpeg,png,gif格式
     *
     * @param inputStream
     * @return
     * @throws IOException
     */
    public Boolean checkImageType(InputStream inputStream) throws IOException {
        FileTypeEnum fileTypeEnum = FileTypeUtil.getType(inputStream);
        if (!fileTypeEnumList.contains(fileTypeEnum)) {
            log.info("图片后缀为{}", fileTypeEnum);
            return false;
        }
        return true;
    }
}