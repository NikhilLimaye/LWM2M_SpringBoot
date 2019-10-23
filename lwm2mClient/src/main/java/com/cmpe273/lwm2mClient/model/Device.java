package com.cmpe273.lwm2mClient.model;

import java.util.List;

public class Device {
	
	String id;
	String name;
	String type;
	String boostrapServerURI;
	List<ClientObject> objectList;
	String lwm2mServerURI;
	Boolean registered = false;
	
	public String getLwm2mServerURI() {
		return lwm2mServerURI;
	}

	public Boolean getRegisteredStatus() {
		return registered;
	}

	public void setRegistered(Boolean registered) {
		this.registered = registered;
	}

	public void setLwm2mServerURI(String lwm2mServerURI) {
		this.lwm2mServerURI = lwm2mServerURI;
	}

	public Device() {	
		
	}
		
	public Device(String id, String name, String type, String boostrapServerURI, List<ClientObject> objectList, String lwm2mServerURI) {
		super();
		this.id = id;
		this.name = name;
		this.type = type;
		this.boostrapServerURI = boostrapServerURI;
		this.objectList = objectList;
		this.lwm2mServerURI = lwm2mServerURI;
	}
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getBoostrapServerURI() {
		return boostrapServerURI;
	}
	public void setBoostrapServerURI(String boostrapServerURI) {
		this.boostrapServerURI = boostrapServerURI;
	}
	public List<ClientObject> getObjectList() {
		return objectList;
	}
	public void setObjectList(List<ClientObject> objectList) {
		this.objectList = objectList;
	}

}
