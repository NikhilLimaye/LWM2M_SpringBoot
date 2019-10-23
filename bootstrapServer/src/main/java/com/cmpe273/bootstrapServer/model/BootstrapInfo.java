package com.cmpe273.bootstrapServer.model;

public class BootstrapInfo {

	String lwm2mServerURL;
	String deviceId;
	

	public BootstrapInfo(String lwm2mServerURL, String deviceId) {
		super();
		this.lwm2mServerURL = lwm2mServerURL;
		this.deviceId = deviceId;
	}
	
	public BootstrapInfo() {
		
	}
	
	public String getLwm2mServerURL() {
		return lwm2mServerURL;
	}
	public void setLwm2mServerURL(String lwm2mServerURL) {
		this.lwm2mServerURL = lwm2mServerURL;
	}
	public String getDeviceId() {
		return deviceId;
	}
	public void setDeviceId(String deviceId) {
		this.deviceId = deviceId;
	}
	
}


