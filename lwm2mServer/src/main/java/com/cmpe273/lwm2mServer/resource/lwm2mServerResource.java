package com.cmpe273.lwm2mServer.resource;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.bson.Document;
import org.bson.conversions.Bson;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

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
 