package com.cmpe273.lwm2mClient.resource;


import java.util.Iterator;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jackson.JsonObjectSerializer;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.function.ServerRequest.Headers;

import com.cmpe273.lwm2mClient.database.Lwm2mClientDatabase;
import com.cmpe273.lwm2mClient.model.BootstrapInfo;
import com.cmpe273.lwm2mClient.model.ClientObject;
import com.cmpe273.lwm2mClient.model.Device;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.mongodb.util.JSON;

@RestController
@RequestMapping("/client")
public class lwm2mClientResource {
	
	@Autowired
	public RestTemplate restTemplate;
	
	
	/*
	 * INFORMATION REPORTING - NOTIFY 
	 */
	
	
	
	
	/*
	 * DEVICE MANAGEMENT - READ
	 */
	@RequestMapping(method = RequestMethod.GET, path="dvmgt/{deviceId}/{objectId}")
	public String readDevice_dvmgt(@PathVariable("objectId") String objectId , @PathVariable("deviceId") String deviceId) {
		
		Lwm2mClientDatabase db = Lwm2mClientDatabase.getInstance();
		return db.readDeviceDetails(objectId);
		
	}
	
	@RequestMapping(method = RequestMethod.GET, path="dvmgt/{deviceId}/{objectId}/{resourceId}")
	public String readDevice_dvmgt(@PathVariable("objectId") String objectId , @PathVariable("deviceId") String deviceId ,
			@PathVariable("resourceId") String resourceId) {
		
		Lwm2mClientDatabase db = Lwm2mClientDatabase.getInstance();
		return db.readDeviceDetails(objectId,resourceId);
		
	}
	
	@RequestMapping(method = RequestMethod.GET, path="dvmgt/discover/{deviceId}/{objectId}")
	public String discoverDevice_dvmgt(@PathVariable("objectId") String objectId , @PathVariable("deviceId") String deviceId) {
		
		Lwm2mClientDatabase db = Lwm2mClientDatabase.getInstance();
		return db.discoverDevice(deviceId,objectId);
	}
	
	
	/*
	 * DEVICE MANAGEMENT - WRITE 
	 */
	@PutMapping(value="dvmgt/{deviceId}/{objectId}/{resourceId}" , consumes = "text/plain" , produces = "text/plain")
	public void writeDevice_dvmgt(@RequestBody String newValue , @PathVariable("objectId") String objectId , @PathVariable("deviceId") String deviceId ,
			@PathVariable("resourceId") String resourceId) {
				
		Lwm2mClientDatabase db = Lwm2mClientDatabase.getInstance();
		db.writeDeviceObject(objectId,resourceId,newValue);
					
	}
	
	/*
	 * DEVICE MANAGEMENT - WRITE ATTRIBUTE	
	 */
	@PutMapping(value="attribute/{deviceId}/{objectId}" , consumes = "text/plain" , produces = "text/plain")
	public String writeDeviceAttribute_dvmgt(@RequestBody String newpmax , @PathVariable("objectId") String objectId , @PathVariable("deviceId") String deviceId) {
		
		
		Lwm2mClientDatabase db = Lwm2mClientDatabase.getInstance();
		db.writeDeviceObjectAttribute(deviceId, objectId, newpmax);
		
		if(Integer.parseInt(newpmax) > 200) {
			return "NOTIFICATION UPDATE - pmax = "+newpmax;
		}
		
		return newpmax;
					
	}
	

	/*
	 * DEVICE MANAGEMENT - CREATE 
	 */
	@PostMapping(value = "dvmgt/create", consumes = "text/plain" , produces = "text/plain")
	public String createDeviceObject_dvmgt(@RequestBody String obj) {
		
		Lwm2mClientDatabase db = Lwm2mClientDatabase.getInstance();
		db.createDeviceObject(obj);
		return obj;
		
	}
	
	
	/*
	 * BOOSTRAP - REQUEST
	 */
	@RequestMapping(method = RequestMethod.POST , path = "request/{deviceId}")
	public BootstrapInfo requestBootstrap(@PathVariable("deviceId") String deviceId) {
		
		//Get boostrap server URL for that device
		Lwm2mClientDatabase db = Lwm2mClientDatabase.getInstance();
		String bootStrapServerURL = db.getDevice(deviceId).getBoostrapServerURI();
				
		
		BootstrapInfo bInfo = restTemplate.getForObject(bootStrapServerURL+"/"+deviceId, BootstrapInfo.class);
		ClientObjectList cInfo = restTemplate.getForObject(bootStrapServerURL+"/clientObjects/"+deviceId, ClientObjectList.class);
		bInfo.setObjList(cInfo);
		
		writeDeviceInfo(deviceId, bInfo, cInfo);
		
		return bInfo;
		
	}
	

	/*
	 * BOOTSTRAP - DISCOVER
	 */
	@RequestMapping(method = RequestMethod.GET , path = "discover/{deviceId}")
	public List<ClientObject> discoverDevice(@PathVariable("deviceId") String deviceId) {
		
		Lwm2mClientDatabase db = Lwm2mClientDatabase.getInstance();

		return db.getDevice(deviceId).getObjectList();
		
	}
	
	
	/*
	 * BOOTSTRAP - READ
	 */
	@RequestMapping(method = RequestMethod.GET , path = "read/{deviceId}/{objectId}")
	public Object readDevice(@PathVariable("deviceId") String deviceId , 
											@PathVariable("objectId") String objectId) {
		
		Lwm2mClientDatabase db = Lwm2mClientDatabase.getInstance();
		List<ClientObject> oList =  db.getDevice(deviceId).getObjectList();
		
		Iterator itr = oList.iterator();
		while(itr.hasNext()) {
			Object i = itr.next();
			System.out.println(i.toString());
			if(i.toString().contains("id="+objectId))
				return i;
		}
		
//		for(int i = 0 ; i < oList.size() ; i++) {
//			if(oList.get(i).getId() == objectId)
//				return oList.get(i);
//		}
		
		return null;
		
	}
	
	
	/*
	 * BOOTSTRAP - WRITE
	 */
	public void writeDeviceInfo(String deviceId, BootstrapInfo bInfo , ClientObjectList cInfo) {
		
		Lwm2mClientDatabase db = Lwm2mClientDatabase.getInstance();
		db.updateDevice(deviceId , bInfo.getLwm2mServerURL() , cInfo.getObjectList());
		
	}
	
	

	/*
	 * CLIENT - REGISTER
	 */
	@RequestMapping(method = RequestMethod.POST , path = "register/{deviceId}")
	public String registerDevice(@PathVariable("deviceId") String deviceId) throws JsonProcessingException {
		
		Lwm2mClientDatabase db = Lwm2mClientDatabase.getInstance();
		Device d = db.getDevice(deviceId);		
		Gson g = new Gson();
		String devJson = g.toJson(d).toString();
		System.out.println("devJson = "+devJson);
		
		HttpHeaders headers = new HttpHeaders();
	    headers.setContentType(MediaType.APPLICATION_JSON);
		HttpEntity<String> registerDevice = new HttpEntity<String>(devJson , headers);
		String result = restTemplate.postForObject("http://localhost:8082/server/register", registerDevice, String.class);
		
		return result.toString();
	}
	
	
	/*
	 * CLIENT - UPDATE - SMS Number
	 */
	@RequestMapping(method = RequestMethod.PUT , path = "update/SMS/{deviceId}")
	public String updateDevice(@PathVariable("deviceId") String deviceId) {
		
		String newSMSNumber = "(343)-321-3434";
		
		HttpHeaders headers = new HttpHeaders();
	    headers.setContentType(MediaType.TEXT_PLAIN);
	    HttpEntity<String> updateSMSNumber = new HttpEntity<String>(newSMSNumber , headers);
	    restTemplate.put("http://localhost:8082/server/update/"+deviceId, updateSMSNumber);
	    
	    return "Update Success";
	}
	
	
	/*
	 * CLIENT - DEREGISTER
	 */
	@RequestMapping(method = RequestMethod.DELETE , path = "deregister/{deviceId}")
	public String deleteDevice(@PathVariable("deviceId") String deviceId) {

	    restTemplate.delete("http://localhost:8082/server/deregister/"+deviceId);
	    return "Delete Success";
	}
	
	
	
	
	
}
