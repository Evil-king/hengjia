package com.baibei.hengjia.gateway.filters.pre;

import com.baibei.hengjia.gateway.dto.JsonRequest;
import com.netflix.zuul.context.RequestContext;
import com.netflix.zuul.http.HttpServletRequestWrapper;
import com.netflix.zuul.http.ServletInputStreamWrapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.netflix.zuul.filters.pre.FormBodyWrapperFilter;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpOutputMessage;
import org.springframework.http.MediaType;
import org.springframework.http.converter.FormHttpMessageConverter;
import org.springframework.http.converter.json.AbstractJackson2HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.util.StringUtils;

import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Map;

/**
 * 功能描述：HTTP POST BODY包装处理
 *
 * @作者 luozhh 创建时间： 2017年11月19日 上午1:53:59
 * @see FormBodyWrapperFilter.FormBodyRequestWrapper
 * @see FormHttpMessageConverter
 */
@Slf4j
public class PostBodyRequestWrapper extends HttpServletRequestWrapper {

    private HttpServletRequest request;
    private byte[] contentData;
    private MediaType contentType;
    private int contentLength;

    public PostBodyRequestWrapper(HttpServletRequest request) {
        super(request);
        this.request = request;
    }

    @Override
    public String getContentType() {
        if (this.contentData == null) {
            buildContentData();
        }
        return this.contentType.toString();
    }

    @Override
    public int getContentLength() {
        if (super.getContentLength() <= 0) {
            return super.getContentLength();
        }
        if (this.contentData == null) {
            buildContentData();
        }
        return this.contentLength;
    }

    @Override
    public long getContentLengthLong() {
        return getContentLength();
    }

    @Override
    public ServletInputStream getInputStream() throws IOException {
        if (this.contentData == null) {
            buildContentData();
        }
        return new ServletInputStreamWrapper(this.contentData);
    }

    private synchronized void buildContentData() {
        try {
            RequestContext ctx = RequestContext.getCurrentContext();
            JsonRequest jsonRequest = (JsonRequest) ctx.get("jsonRequest");
            this.contentType = MediaType.valueOf(this.request.getContentType());
            PostHttpOutputMessage outputMessage = new PostHttpOutputMessage();
            outputMessage.getHeaders().setContentType(this.contentType);
            Map<String, Object> dataParam = jsonRequest.getData();
            // 设置鉴权过后的客户ID数据
            if (ctx.get("customerId") != null) {
                dataParam.put("customerId", ctx.get("customerId"));
            }
            // 设置鉴权过后的客户编号数据
            if (ctx.get("customerNo") != null) {
                dataParam.put("customerNo", ctx.get("customerNo"));
            }
            // 设置分页信息
            if (jsonRequest.getPage() != null) {
                dataParam.put("currentPage", jsonRequest.getPage().getCurrentPage());
                dataParam.put("pageSize", jsonRequest.getPage().getPageSize());
            }
            // 设置请求ID
            if (!StringUtils.isEmpty(jsonRequest.getRequestId())) {
                dataParam.put("requestId", jsonRequest.getRequestId());
            }
            // 设置转发IP
            if (ctx.get("ip") != null) {
                dataParam.put("ip", ctx.get("ip").toString());
            }
            AbstractJackson2HttpMessageConverter converter = new MappingJackson2HttpMessageConverter();
            converter.write(dataParam, contentType, outputMessage);
            this.contentType = outputMessage.getHeaders().getContentType();
            this.contentData = outputMessage.getInput();
            this.contentLength = this.contentData.length;
        } catch (Exception e) {
            log.info(e.getMessage(), e);
            throw new IllegalStateException("Cannot convert form data", e);
        }
    }

    @Override
    public HttpServletRequest getRequest() {
        return this.request;
    }

    private class PostHttpOutputMessage implements HttpOutputMessage {

        private HttpHeaders headers = new HttpHeaders();
        private ByteArrayOutputStream output = new ByteArrayOutputStream();

        @Override
        public HttpHeaders getHeaders() {
            return this.headers;
        }

        @Override
        public OutputStream getBody() throws IOException {
            return this.output;
        }

        public byte[] getInput() throws IOException {
            this.output.flush();
            return this.output.toByteArray();
        }

    }

}
