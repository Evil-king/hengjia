package com.baibei.hengjia.api.modules.sms.core;

public class SmsBean {

	private String url;	// 短信渠道发送短信的url
	private String mobile;//手机号
	private String account;//短信账号
	private String password;//短信密码
	private String verifyCode;//验证码
	private Integer smsType;//短信模版的kye
	private Integer type;//模板类型
	private String content;//短信内容
	private String smsSignature;//短信签名
	
	
	public SmsBean(String url, String mobile, String account, String password, String verifyCode, Integer smsType,
                   Integer type, String smsSignature) {
		this.url = url;
		this.mobile=mobile;
		this.account=account;
		this.password=password;
		this.verifyCode=verifyCode;
		this.smsType=smsType;
		this.type=type;
		this.smsSignature=smsSignature;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getMobile() {
		return mobile;
	}
	public void setMobile(String mobile) {
		this.mobile = mobile;
	}
	
	public String getAccount() {
		return account;
	}
	public void setAccount(String account) {
		this.account = account;
	}
	
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.mobile = password;
	}


	public String getVerifyCode() {
		return verifyCode;
	}
	public void setVerifyCode(String verifyCode) {
		this.verifyCode = verifyCode;
	}

	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	
	public String getSmsSignature() {
		return smsSignature;
	}
	public void setSmsSignature(String smsSignature) {
		this.smsSignature = smsSignature;
	}
	public Integer getSmsType() {
		return smsType;
	}
	public void setSmsType(Integer smsType) {
		this.smsType = smsType;
	}
	public Integer getType() {
		return type;
	}
	public void setType(Integer type) {
		this.type = type;
	}

	@Override
	public String toString() {
		return "SmsBean{" +
				"url='" + url + '\'' +
				", mobile='" + mobile + '\'' +
				", account='" + account + '\'' +
				", password='" + password + '\'' +
				", verifyCode='" + verifyCode + '\'' +
				", smsType=" + smsType +
				", type=" + type +
				", content='" + content + '\'' +
				", smsSignature='" + smsSignature + '\'' +
				'}';
	}
}
