package com.baibei.hengjia.common.tool.api;

import java.io.Serializable;

/**
 * @author 会跳舞的机器人
 * @version 1.0
 * @Description Controller层返回的统一结果对象
 * @date 2015年8月31日 下午11:57:43
 */
public final class ApiResult<T> implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * code码
     */
    private Integer code = ResultEnum.SUCCESS.getCode();

    /**
     * msg信息
     */
    private String msg = null;

    /**
     * 返回结果实体
     */
    private T data = null;

    public ApiResult() {
    }

    public ApiResult(ResultEnum resultEnum) {
        this.code = resultEnum.getCode();
        this.msg = resultEnum.getMsg();
    }

    public ApiResult(Integer code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public ApiResult(Integer code, String msg, T data) {
        this.code = code;
        this.msg = msg;
        this.data = data;
    }

    /**
     * 构建
     *
     * @param resultEnum
     * @return
     */
    public ApiResult build(ResultEnum resultEnum) {
        return new ApiResult(resultEnum.getCode(), resultEnum.getMsg());
    }

    /**
     * 是否成功
     *
     * @return
     */
    public boolean hasSuccess() {
        return this.getCode() != null && ResultEnum.SUCCESS.getCode() == this.getCode().intValue();
    }

    /**
     * 成功,带结果数据
     *
     * @return
     */
    public static <T> ApiResult<T> success() {
        return new ApiResult<>(ResultEnum.SUCCESS.getCode(), ResultEnum.SUCCESS.getMsg(), null);
    }

    /**
     * 成功,带结果数据
     *
     * @param data
     * @param <T>
     * @return
     */
    public static <T> ApiResult<T> success(T data) {
        return new ApiResult<>(ResultEnum.SUCCESS.getCode(), ResultEnum.SUCCESS.getMsg(), data);
    }

    /**
     * 系统错误,默认提示
     *
     * @param <T>
     * @return
     */
    public static <T> ApiResult<T> error() {
        return new ApiResult<>(ResultEnum.ERROR.getCode(), ResultEnum.ERROR.getMsg(), null);
    }

    /**
     * 系统错误,自定义提示信息
     *
     * @param msg
     * @param <T>
     * @return
     */
    public static <T> ApiResult<T> error(String msg) {
        return new ApiResult<>(ResultEnum.ERROR.getCode(), msg, null);
    }

    /**
     * 服务调用失败
     *
     * @param <T>
     * @return
     */
    public static <T> ApiResult<T> serviceFail() {
        return new ApiResult<>(ResultEnum.SERVIE_FAIL.getCode(), ResultEnum.SERVIE_FAIL.getMsg(), null);
    }

    /**
     * 参数错误
     *
     * @param <T>
     * @return
     */
    public static <T> ApiResult<T> badParam(String msg) {
        return new ApiResult<>(ResultEnum.BAD_PARAM.getCode(), msg, null);
    }

    /**
     * 参数错误
     *
     * @param <T>
     * @return
     */
    public static <T> ApiResult<T> badParam() {
        return new ApiResult<>(ResultEnum.BAD_PARAM.getCode(), ResultEnum.BAD_PARAM.getMsg(), null);
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "ApiResult [code=" + code + ", msg=" + msg + ", data=" + data + "]";
    }

}
