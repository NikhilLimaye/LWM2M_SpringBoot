package com.cmpe273.bootstrapServer.resource;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.cmpe273.bootstrapServer.model.BootstrapInfo;
import com.cmpe273.bootstrapServer.model.ClientObject;

@RestController
@RequestMapping("/bootstrap")
public class BootstrapServerResource {

	@Autowired
	public RestTemplate restTemplate;
	
	/*
	 * BOOSTRAP - WRITE - SERVER URI
	 */
	@RequestMapping(method = RequestMethod.GET , path = "/{deviceId}")
	public BootstrapInfo getBootstrapInfo(@PathVariable("deviceId") String deviceId) {
				
		return new BootstrapInfo("http://localhost:8082/server" , deviceId);
				
	}
	
	
	
	/*
	 * BOOTSTRAP - READ
	 */
	@RequestMapping(method = RequestMethod.POST , path = "read/{deviceId}/{objectId}")
	public ClientObject readObject(@PathVariable("deviceId") String deviceId , 
								@PathVariable("objectId") String objectId) {
		
		ClientObject res =  restTemplate.getForObject("http://localhost:8080/client/read/"+deviceId+"/"+objectId, ClientObject.class);
		return res;	
	}
	
	
	/*
	 * BOOTSTRAP - DISCOVER
	 */
	@RequestMapping(method = RequestMethod.POST , path = "discover/{deviceId}")
	public Object discoverObject(@PathVariable("deviceId") String deviceId ) {
		
		return restTemplate.getForObject("http://localhost:8080/client/discover/"+deviceId, Object.class);
				
	}
	
	
	/*
	 * BOOSTRAP - WRITE - CLIENT OBJECTS
	 */
	@RequestMapping(method = RequestMethod.GET , path = "clientObjects/{deviceId}")
	public ClientObjectList getClientObjectList(@PathVariable("deviceId") String deviceId) {
		
		
		List<ClientObject> clientObjs = new  ArrayList<ClientObject>();
		
		ClientObject accessControl = new ClientObject(deviceId, 
														"LwM2M Access Control", 
														"2", 
														"Multiple", 
														"Optional", 
														"urn:oma:lwm2m:oma:2");
		clientObjs.add(accessControl);
		
		ClientObject connectivityMonitoring = new ClientObject(deviceId, 
																"Connectivity Monitoring", 
																"4", 
																"Single", 
																"Optional", 
																"urn:oma:lwm2m:oma:4");
		
		clientObjs.add(connectivityMonitoring);
		
		
		ClientObject serverObject = new ClientObject(deviceId, 
														"LwM2M Server", 
														"1", 
														"Multiple", 
														"Optional", 
														"urn:oma:lwm2m:oma:1");
		clientObjs.add(serverObject);
		
		return new ClientObjectList(clientObjs);
	}
	
	
	
	
}
