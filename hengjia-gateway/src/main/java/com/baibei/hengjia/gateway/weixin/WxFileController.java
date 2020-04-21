package com.baibei.hengjia.gateway.weixin;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author: 会跳舞的机器人
 * @date: 2019/6/19 10:54 AM
 * @description: 微信域名授权文件存放
 */
@Controller
@RequestMapping("/")
public class WxFileController {


    /**
     * 生产环境
     *
     * @param response
     * @throws IOException
     */
    @RequestMapping({"MP_verify_GEUIW37hHOduNxFP.txt"})
    private void returnConfigFile1(HttpServletResponse response) throws IOException {

        response.getWriter().write("GEUIW37hHOduNxFP");
    }

    /**
     * 生产环境
     *
     * @param response
     * @throws IOException
     */
    @RequestMapping({"MP_verify_JrkJqNXDZsk2nW8M.txt"})
    private void returnConfigFile2(HttpServletResponse response) throws IOException {

        response.getWriter().write("JrkJqNXDZsk2nW8M");
    }

    /**
     * 测试环境
     *
     * @param response
     * @throws IOException
     */
    @RequestMapping({"MP_verify_NSMgDOmQRmTkRJxA.txt"})
    private void returnConfigFile3(HttpServletResponse response) throws IOException {

        response.getWriter().write("NSMgDOmQRmTkRJxA");
    }


}
