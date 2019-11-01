package com.cmpe273.lwm2mServer.resource;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.bson.Document;
import org.bson.conversions.Bson;
import org.hibernate.validator.constraints.ParameterScriptAssert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.cmpe273.lwm2mServer.database.Lwm2mServerDatabase;
import com.cmpe273.lwm2mServer.model.Device;
import com.google.gson.Gson;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Updates;


@RestController
@RequestMapping("/server")
public class lwm2mServerResource {
	
	@Autowired
	public RestTemplate restTemplate;
	
	
	/*
	 * INFORMATION REPORTING - OBSERVER
	 */
	@PostMapping(value = "/observe/{deviceId}{objectId}" , consumes = "text/plain" , produces = "text/plain")
	public String observeDevice(@PathVariable("objectId") String objectId , 
								@PathVariable("deviceId") String deviceId) { 
		
		return "OBSERVE operation for Device:"+deviceId+"/"+objectId+" Resource object: Maximum Period";
			
	}
	
	
	
	//------------------------------------------------------------------------------------------
	
	/*
	 * DEVICE MANAGEMENT - READ
	 */
	@RequestMapping(method = RequestMethod.GET , path = "/dvmgt/{deviceId}/{objectId}")
	public String readDevice(@PathVariable("objectId") String objectId , @PathVariable("deviceId") String deviceId) {
		
		return restTemplate.getForObject("http://localhost:8080/client/dvmgt/"+deviceId+"/"+objectId
												, String.class);

		
	}
	
	@RequestMapping(method = RequestMethod.GET , path = "/dvmgt/{deviceId}/{objectId}/{resourceId}")
	public String readDevice(@PathVariable("objectId") String objectId , @PathVariable("deviceId") String deviceId , 
							@PathVariable("resourceId") String resourceId) {
		
		return restTemplate.getForObject("http://localhost:8080/client/dvmgt/"+deviceId+"/"+objectId+"/"+resourceId
												, String.class);

		
	}
	
	/*
	 * DEVICE MANAGEMENT - DISCOVER 
	 */
	@RequestMapping(method = RequestMethod.GET , path = "/dvmgt/discover/{deviceId}/{objectId}" )
	public String discoverDevice(@PathVariable("objectId") String objectId , @PathVariable("deviceId") String deviceId) {
		
		return restTemplate.getForObject("http://localhost:8080/client/dvmgt/discover/"+deviceId+"/"+objectId
												, String.class);
	}
	
	
	
	/*
	 * DEVICE MANAGEMENT - WRITE
	 */
	@PutMapping(value="dvmgt/{deviceId}/{objectId}/{resourceId}" , consumes = "text/plain" , produces = "text/plain")
	public String writeDevice(@PathVariable("objectId") String objectId , @PathVariable("deviceId") String deviceId , 
			@PathVariable("resourceId") String resourceId) {
		
		String newACL = "0b0000000000001110";
		HttpHeaders headers = new HttpHeaders();
	    headers.setContentType(MediaType.TEXT_PLAIN);
	    HttpEntity<String> updatedACL = new HttpEntity<String>(newACL , headers);
	    restTemplate.put("http://localhost:8080/client/dvmgt/"+deviceId+"/"+objectId+"/"+resourceId, updatedACL);
		
		return newACL;
	}
	
	/*
	 * DEVICE MANAGEMENT - WRITE ATTRIBUTE
	 */
	@PutMapping(value="attribute/{deviceId}/{objectId}/" , consumes = "text/plain" , produces = "text/plain")
	public String writeAttributeDevice(@PathVariable("objectId") String objectId , @PathVariable("deviceId") String deviceId) {
		
		String newpmax = "180";
		HttpHeaders headers = new HttpHeaders();
	    headers.setContentType(MediaType.TEXT_PLAIN);
	    HttpEntity<String> updatedpmax = new HttpEntity<String>(newpmax , headers);
	    restTemplate.put("http://localhost:8080/client/attribute/"+deviceId+"/"+objectId, updatedpmax);
		
		return newpmax;
	}
	

	/*
	 * DEVICE MANAGEMENT - CREATE
	 */
	@PostMapping(value = "dvmgt/{deviceId}/{objectId}", consumes = "text/plain" , produces = "text/plain")
	public String createObject(@PathVariable("objectId") String objectId , @PathVariable("deviceId") String deviceId ) {
		
		String newObject = deviceId+","+objectId+",0,2,ACL,0b000000000001101,LWM2M Server Access Rights";
		HttpHeaders headers = new HttpHeaders();
	    headers.setContentType(MediaType.TEXT_PLAIN);
	    HttpEntity<String> newObjectString = new HttpEntity<String>(newObject , headers);
	    return restTemplate.postForObject("http://localhost:8080/client/dvmgt/create", newObjectString, String.class);
	}
	
	
	

	
	//------------------------------------------------------------------------------------------------------
	
	
	
	@PostMapping(value="register" , consumes = "application/json" , produces = "application/json")
	public String registerDevice(@RequestBody String d, HttpServletResponse response) {
		
		Gson g = new Gson();
		Device p = g.fromJson(d, Device.class);
		Lwm2mServerDatabase db = Lwm2mServerDatabase.getInstance();
		db.addDevice(p);		
		return p.getName();
				
	}
	
	@PutMapping(value="update/{deviceId}" , consumes = "text/plain" , produces = "text/plain")
	public String updateDevice(@PathVariable("deviceId") String deviceId, @RequestBody String d, HttpServletResponse response) {
		
		MongoDatabase db = Lwm2mServerDatabase.getInstance().getProjectdb();
		MongoCollection<Document> coll = db.getCollection("devicesRegistered");
		Bson filter = Filters.eq("deviceId", deviceId);
		Bson setUpdate = Updates.set("SMSNumber", d);
		coll.updateOne(filter,setUpdate);
		return "Updation successful";

	}
	
	@DeleteMapping(value = "deregister/{deviceId}") 
	public String deleteDevice(@PathVariable("deviceId") String deviceId) {
		
		MongoDatabase db = Lwm2mServerDatabase.getInstance().getProjectdb();
		MongoCollection<Document> coll = db.getCollection("devicesRegistered");
		Bson filter = Filters.eq("deviceId", deviceId);
		coll.findOneAndDelete(filter);
		return deviceId;
		
	}
	
	
	
}
 