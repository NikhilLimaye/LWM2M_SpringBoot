package com.cmpe273.bootstrapServer.model;

public class ClientObject {

	String name;
	String id;
	String instances;
	String mandatory;
	String urn;
	String deviceId;
	
	
	public String getDeviceId() {
		return deviceId;
	}


	public void setDeviceId(String deviceId) {
		this.deviceId = deviceId;
	}


	public ClientObject(String deviceId, String name, String id, String instances, String mandatory, String urn) {
		this.deviceId = deviceId;
		this.name = name;
		this.id = id;
		this.instances = instances;
		this.mandatory = mandatory;
		this.urn = urn;
	}


	public ClientObject() {
		
	}


	public String getName() {
		return name;
	}


	public void setName(String name) {
		this.name = name;
	}


	public String getId() {
		return id;
	}


	public void setId(String id) {
		this.id = id;
	}


	public String getInstances() {
		return instances;
	}


	public void setInstances(String instances) {
		this.instances = instances;
	}


	public String getMandatory() {
		return mandatory;
	}


	public void setMandatory(String mandatory) {
		this.mandatory = mandatory;
	}


	public String getUrn() {
		return urn;
	}


	public void setUrn(String urn) {
		this.urn = urn;
	}
	
		
}
