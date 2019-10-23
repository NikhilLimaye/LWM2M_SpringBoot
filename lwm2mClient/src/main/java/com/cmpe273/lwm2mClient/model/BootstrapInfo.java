package com.cmpe273.lwm2mClient.model;

import com.cmpe273.lwm2mClient.resource.ClientObjectList;

public class BootstrapInfo {

	String lwm2mServerURL;
	String deviceId;
	ClientObjectList objList;
	

	public ClientObjectList getObjList() {
		return objList;
	}

	public void setObjList(ClientObjectList objList) {
		this.objList = objList;
	}

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
