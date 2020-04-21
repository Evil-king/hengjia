package com.baibei.hengjia.common.tool.utils;

import com.aliyun.oss.OSSClient;
import com.aliyun.oss.model.ObjectMetadata;
import com.google.zxing.*;
import com.google.zxing.client.j2se.BufferedImageLuminanceSource;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.common.HybridBinarizer;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

/**
 * @author: hyc
 * @date: 2019/6/4 14:23
 * @description:
 */
public class QRcodeUtils {
//    public static void main(String[] args) {
//        String url = "www.baidu.com";
//        System.out.println(getQRCode(url, "upload/QRcode/", "52db667ace5a4f2dae503a9572efb989998"));;
//    }

    //private static PropertisComon propertisComon = (PropertisComon) SpringContextUtil.getBean("propertisComon");


    /**
     * 获取二维码
     *
     * @param url
     * @param savePath
     * @param wid
     * @return
     */
    public static String getQRCode(String url, String savePath, String wid,String ossUrl) {
        // 先生成二维码图片
        ByteArrayOutputStream byteArrayOutputStream = createQrCode(url);
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(byteArrayOutputStream.toByteArray());

        String accessKeyId = "SpfG9gNecmI1aQ2D";
        String accessKeySecret = "AaSD0ptu7jdSpDlLzBjRpyIpJtfEpp";
        String endpoint = "http://oss-cn-shenzhen.aliyuncs.com";
        // 初始化一个OSSClient
        OSSClient client = new OSSClient(endpoint, accessKeyId, accessKeySecret);
        String bucketName = ossUrl;
        ObjectMetadata meta = new ObjectMetadata();
        // 必须设置ContentLength
        meta.setContentType("image/jpeg");

        meta.setCacheControl("max-age=8640000");
        long file_size = byteArrayInputStream.available();
        meta.setContentLength(file_size);
        // 上传Object.
        String filePath = savePath + wid + ".jpg";
        client.putObject(bucketName, filePath, byteArrayInputStream, meta);
        return filePath;
    }

    public static ByteArrayOutputStream createQrCode(String url) {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        try {
            Map<EncodeHintType, String> hints = new HashMap<>();
            hints.put(EncodeHintType.CHARACTER_SET, "UTF-8");
            BitMatrix bitMatrix = new MultiFormatWriter().encode(url, BarcodeFormat.QR_CODE, 400, 400, hints);
            writeToStream(bitMatrix, "jpg", outputStream);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return outputStream;
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

    static void writeToFile(BitMatrix matrix, String format, File file) throws IOException {
        BufferedImage image = toBufferedImage(matrix);
        if (!ImageIO.write(image, format, file)) {
            throw new IOException("Could not write an image of format " + format + " to " + file);
        }
    }

    static void writeToStream(BitMatrix matrix, String format, OutputStream stream) throws IOException {
        BufferedImage image = toBufferedImage(matrix);
        if (!ImageIO.write(image, format, stream)) {
            throw new IOException("Could not write an image of format " + format);
        }
    }

    private static final int BLACK = 0xFF000000;
    private static final int WHITE = 0xFFFFFFFF;

    private static BufferedImage toBufferedImage(BitMatrix matrix) {
        int width = matrix.getWidth();
        int height = matrix.getHeight();
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                image.setRGB(x, y, matrix.get(x, y) ? BLACK : WHITE);
            }
        }
        return image;
    }
}
