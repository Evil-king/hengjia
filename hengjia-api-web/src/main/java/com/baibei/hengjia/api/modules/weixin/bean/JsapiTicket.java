package com.baibei.hengjia.api.modules.weixin.bean;

/**
 * JsapiTicket 实体
 *
 * @author zhongwen ho
 * @createDate 2015-3-3
 */
public class JsapiTicket {
	// 获取到的凭证
	private String ticket;
	// 凭证有效时间，单位：秒
	private int expiresIn;
	public String getTicket() {
		return ticket;
	}
	public void setTicket(String ticket) {
		this.ticket = ticket;
	}
	public int getExpiresIn() {
		return expiresIn;
	}
	public void setExpiresIn(int expiresIn) {
		this.expiresIn = expiresIn;
	}
}
