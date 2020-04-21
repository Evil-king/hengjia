package com.baibei.hengjia.gateway.dto;

import lombok.Data;

import java.io.Serializable;

/**
 * @author: 会跳舞的机器人
 * @date: 2019/4/29 10:01 PM
 * @description:
 */
@Data
public class JsonResponse implements Serializable {
    private String requestId;

    private int code;

    private String msg;

    private Object data;

}
