package com.yzz.util;

import java.util.Properties;

public class AlipayConfig {
	
	private String appid;
	private String gatewayUrl;
	private String signType;
	private String privateKey;
	private String format;
	private String charset;
	private String alipayPulicKey;
	private String notifyUrl;
	private String returnUrl;
	
	private static AlipayConfig instance = new AlipayConfig();
	
	private AlipayConfig(){
		String filePath = "alipay.properties";
		Properties props = PropertiesLoader.loadProperties(filePath);
		appid = props.getProperty("alipay.appid");
		gatewayUrl = props.getProperty("alipay.gatewayurl");
		signType = props.getProperty("alipay.signtype");
		privateKey = props.getProperty("alipay.privatekey");
		format = props.getProperty("alipay.format");
		charset = props.getProperty("alipay.charset");
		alipayPulicKey = props.getProperty("alipay.alipaypublickey");
		notifyUrl = props.getProperty("alipay.notifyurl");
		returnUrl = props.getProperty("alipay.returnurl");
	}
	
	public static AlipayConfig getInstance(){
		
		return instance;
	}

	public String getAppid() {
		return appid;
	}

	public void setAppid(String appid) {
		this.appid = appid;
	}

	public String getGatewayUrl() {
		return gatewayUrl;
	}

	public void setGatewayUrl(String gatewayUrl) {
		this.gatewayUrl = gatewayUrl;
	}

	public String getSignType() {
		return signType;
	}

	public void setSignType(String signType) {
		this.signType = signType;
	}

	public String getPrivateKey() {
		return privateKey;
	}

	public void setPrivateKey(String privateKey) {
		this.privateKey = privateKey;
	}

	public String getFormat() {
		return format;
	}

	public void setFormat(String format) {
		this.format = format;
	}

	public String getCharset() {
		return charset;
	}

	public void setCharset(String charset) {
		this.charset = charset;
	}

	public String getAlipayPulicKey() {
		return alipayPulicKey;
	}

	public void setAlipayPulicKey(String alipayPulicKey) {
		this.alipayPulicKey = alipayPulicKey;
	}

	public static void setInstance(AlipayConfig instance) {
		AlipayConfig.instance = instance;
	}

	public String getNotifyUrl() {
		return notifyUrl;
	}

	public void setNotifyUrl(String notifyUrl) {
		this.notifyUrl = notifyUrl;
	}

	public String getReturnUrl() {
		return returnUrl;
	}

	public void setReturnUrl(String returnUrl) {
		this.returnUrl = returnUrl;
	}

}
