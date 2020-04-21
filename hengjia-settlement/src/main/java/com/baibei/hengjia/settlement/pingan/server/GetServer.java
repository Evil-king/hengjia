package com.baibei.hengjia.settlement.pingan.server;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Arrays;

public class GetServer {


    public String sendAndGetMessage(String Ip, String Port, String tranMessage) {
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

            int headcount = 222;
            byte[] headMessage = new byte[headcount];
            int headMessageReadCount = 0;
            while (headMessageReadCount < headcount) {
                headMessageReadCount += is.read(headMessage, headMessageReadCount, headcount - headMessageReadCount);
            }
            // ????????
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
