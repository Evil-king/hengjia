package com.baibei.hengjia.common.tool.utils;

import com.aliyun.oss.OSSClient;
import com.aliyun.oss.model.ObjectMetadata;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.common.BitMatrix;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.HashMap;
import java.util.Map;

/**
 * @author: hyc
 * @date: 2019/6/5 11:16
 * @description:
 */
public class ImageUtils {
    /**
     * 获取二维码
     *
     * @param inputStream 文件流
     * @param savePath
     * @param wid
     * @return
     */
    public static String setImagge(InputStream inputStream, String savePath, String wid) {

        String accessKeyId = "SpfG9gNecmI1aQ2D";
        String accessKeySecret = "AaSD0ptu7jdSpDlLzBjRpyIpJtfEpp";
        String endpoint = "http://oss-cn-shenzhen.aliyuncs.com";
        // 初始化一个OSSClient
        OSSClient client = new OSSClient(endpoint, accessKeyId, accessKeySecret);
        String bucketName = "cloudtreeoss-prod";
        ObjectMetadata meta = new ObjectMetadata();
        // 必须设置ContentLength
        meta.setContentType("image/jpeg");

        meta.setCacheControl("max-age=8640000");
        long file_size = 0;
        try {
            file_size = inputStream.available();
        } catch (IOException e) {
            e.printStackTrace();
        }
        meta.setContentLength(file_size);
        // 上传Object.
        String filePath = savePath + wid + ".jpg";
        client.putObject(bucketName, filePath, inputStream, meta);
        return filePath;
    }


    /*public static String createQrCode(String url, String path, String fileName) {
        try {
            Map<EncodeHintType, String> hints = new HashMap<>();
            hints.put(EncodeHintType.CHARACTER_SET, "UTF-8");
            BitMatrix bitMatrix = new MultiFormatWriter().encode(url, BarcodeFormat.QR_CODE, 400, 400, hints);
            File file = new File(path, fileName);
            if (file.exists() || ((file.getParentFile().exists() || file.getParentFile().mkdirs()) && file.createNewFile())) {
                writeToFile(bitMatrix, "jpg", file);
                System.out.println("搞定：" + file);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }*/
    public static void main(String[] args) {
        InputStream inputStream= null;
        try {
            inputStream = new FileInputStream(new File("D:/qRCode/860e8505e3abcd6440f0f851365df2b.png"));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        System.out.println(setImagge(inputStream, "upload/idcard/", "1111111111"));
    }
}
