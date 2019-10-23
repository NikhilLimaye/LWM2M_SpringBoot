package com.cmpe273.bootstrapServer.resource;

import java.util.List;

import com.cmpe273.bootstrapServer.model.ClientObject;

public class ClientObjectList {

	List<ClientObject> objectList;

	public List<ClientObject> getObjectList() {
		return objectList;
	}

	public ClientObjectList(List<ClientObject> objectList) {
		super();
		this.objectList = objectList;
	}

	public void setObjectList(List<ClientObject> objectList) {
		this.objectList = objectList;
	}

	public  ClientObjectList() {
	
		
	}
	
}
