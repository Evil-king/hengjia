/* ***********************************************************************
 * Copyright (C) 1992-2018 AirSky.
 *
 * Created [2018年5月16日 上午10:33:43] by AS Team
 * ***********************************************************************
 *
 * payment-service
 * ***********************************************************************
 */
package com.baibei.hengjia.common.tool.utils;

import com.baibei.hengjia.common.tool.enumeration.FileTypeEnum;

import java.io.IOException;
import java.io.InputStream;


/**
 * 文件类型工具类
 */
public class FileTypeUtil {
	
    
    private static String getFileContent(InputStream inputStream) throws IOException {
        
        byte[] b = new byte[28];
        try {
            inputStream.read(b, 0, 28);
        } catch (IOException e) {
            e.printStackTrace();
            throw e;
        } finally {
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                    throw e;
                }
            }
        }
        return bytesToHexString(b);
    }
    
    private static String bytesToHexString(byte[] b){
        
        StringBuilder stringBuilder = new StringBuilder();   
        if (b == null || b.length <= 0) {   
            return null;   
        }   
        for (int i = 0; i < b.length; i++) {   
            int v = b[i] & 0xFF;   
            String str = Integer.toHexString(v);   
            if (str.length() < 2) {   
                stringBuilder.append(0);   
            }   
            stringBuilder.append(str);   
        }   
        return stringBuilder.toString();   
    }
    
    /**
     * 
      * getType(判断文件类型)
      * @param fileInputStream 根据输入流的字节，获取文件类型，防止恶意的文件提交
      * @return
      * @throws IOException
     */
    public static FileTypeEnum getType(InputStream fileInputStream) throws IOException {
        
        String fileHead = getFileContent(fileInputStream);
        if (fileHead == null || fileHead.length() == 0) {
            return null;
        }
         
        fileHead = fileHead.toUpperCase();

        FileTypeEnum[] fileTypes = FileTypeEnum.values();
        for (FileTypeEnum type : fileTypes) {
            if (fileHead.startsWith(type.getValue())) {
            	return type;
            }
        }
        return null;
    }
    
}
    
    

