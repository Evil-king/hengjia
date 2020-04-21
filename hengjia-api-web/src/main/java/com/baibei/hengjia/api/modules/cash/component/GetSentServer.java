package com.baibei.hengjia.api.modules.cash.component;

import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Arrays;
/**
 *
 * @author 林英男
 * @version 1.0
 * @see 银商结算通建立与服务端通信，发送请求报文并接受返回报文
 *
 */
@Component
public class GetSentServer {

    /**
     * @author 林英男
     * @see 发送请求报文并接收返回
     * @param socketModel
     *            通讯地址实体类对象
     * @param tranMessage
     *            请求报文
     */
    public String sendAndGetMessage(String Ip,String Port,String tranMessage) {
        String tranBackMessage = "";
        Socket socket = null;
        InputStream is = null;
        OutputStream os = null;
        String ip = Ip.trim();
        try {
            socket = new Socket(ip, Integer.parseInt(Port));
            socket.setSendBufferSize(4096);
            socket.setTcpNoDelay(true);
            socket.setSoTimeout(60000);
            socket.setKeepAlive(true);

            is = socket.getInputStream();
            os = socket.getOutputStream();

            os.write(tranMessage.getBytes("gbk"));
            os.flush();

            // 读取222字节报文头
            int headcount = 222;
            byte[] headMessage = new byte[headcount];
            int headMessageReadCount = 0;
            while (headMessageReadCount < headcount) {
                headMessageReadCount += is.read(headMessage, headMessageReadCount, headcount - headMessageReadCount);
            }
            // 读取业务报文
            byte[] bodyMessageLength = Arrays.copyOfRange(headMessage, 30, 40);
            int bodyLength = Integer.parseInt(new String(bodyMessageLength, "GBK"));
            byte[] bodyMessage = new byte[bodyLength];
            int bodycount = 0;
            while (bodycount < bodyLength) {
                bodycount += is.read(bodyMessage, bodycount, bodyLength - bodycount);
            }

            tranBackMessage = new String(headMessage, "GBK") + new String(bodyMessage, "GBK");
            os.close();
            is.close();
            socket.close();
        } catch (NumberFormatException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (UnknownHostException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return tranBackMessage;
    }

}
