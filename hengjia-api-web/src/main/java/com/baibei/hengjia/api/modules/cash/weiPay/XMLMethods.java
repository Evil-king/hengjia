package com.baibei.hengjia.api.modules.cash.weiPay;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;

import java.io.IOException;
import java.util.*;





/**
 * 微信XML操作
 * @author Mr.zhou
 *
 */
public class XMLMethods {

    /**
     * 解析xml,返回第一级元素键值对。如果第一级元素有子节点，则此节点的值是子节点的xml数据。
     * @param strxml
     * @return
     * @throws DocumentException 
     * @throws JDOMException
     * @throws IOException
     */
    @SuppressWarnings("unchecked")
	public static Map doXMLParse(String strxml) throws DocumentException{
        strxml = strxml.replaceFirst("encoding=\".*\"", "encoding=\"UTF-8\"");
        if(null == strxml || "".equals(strxml)) {
            return null;
        }
        Map m = new HashMap();
        Document document = DocumentHelper.parseText(strxml);
        Element root = document.getRootElement();
     // 遍历根节点下所有子节点  
        Iterator<?> it = root.elementIterator();  
        while(it.hasNext()) {
        	Element e = (Element) it.next();
        	if(!e.isTextOnly()){
        		System.out.println("有多个子节点");
        	}
        	String k = e.getName();
        	String v = e.getText();
//        	System.out.println(k+" "+v);
        	m.put(k, v);
        } 
        return m;
    }
    
    /**
	 * 将map封装成XML
	 * @param parameters
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static String getRequestXml(SortedMap<Object,Object> parameters){
        StringBuffer sb = new StringBuffer();
        sb.append("<xml>");
        Set es = parameters.entrySet();
        Iterator it = es.iterator();
        while(it.hasNext()) {
            Map.Entry entry = (Map.Entry)it.next();
            String k = (String)entry.getKey();
            String v = (String)entry.getValue();
            //注意，有一些值要用![CDATA[value]]来表示
            if ("attach".equalsIgnoreCase(k)||"body".equalsIgnoreCase(k)||"sign".equalsIgnoreCase(k)) {
                sb.append("<"+k+">"+"<![CDATA["+v+"]]></"+k+">");
            }else {
                sb.append("<"+k+">"+v+"</"+k+">");
            }
        }
        sb.append("</xml>");
        return sb.toString();
    }
	
	/**
	 * 生成一个XML返回给微信的通知
	 * @param return_code
	 * @param return_msg
	 * @return
	 */
	public static String setXML(String return_code, String return_msg) {
        return "<xml><return_code><![CDATA[" + return_code
                + "]]></return_code><return_msg><![CDATA[" + return_msg
                + "]]></return_msg></xml>";
	}

	/**
     * 封装微信text类的返回消息
     * @param to
     * @param from
     * @param content
     * @return
     */
    public static String formatXmlAnswer(String to, String from, String content) {
        StringBuffer sb = new StringBuffer();
        Date date = new Date();
        sb.append("<xml><ToUserName><![CDATA[");
        sb.append(to);
        sb.append("]]></ToUserName><FromUserName><![CDATA[");
        sb.append(from);
        sb.append("]]></FromUserName><CreateTime>");
        sb.append(date.getTime());
        sb.append("</CreateTime><MsgType><![CDATA[text]]></MsgType><Content><![CDATA[");
        sb.append(content);
        sb.append("]]></Content><FuncFlag>0</FuncFlag></xml>");
        return sb.toString();
    }


}
