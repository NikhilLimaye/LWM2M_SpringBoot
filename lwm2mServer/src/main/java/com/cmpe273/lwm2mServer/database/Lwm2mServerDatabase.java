package com.cmpe273.lwm2mServer.database;

import java.util.List;

import org.bson.Document;
import org.bson.conversions.Bson;

import com.cmpe273.lwm2mServer.model.ClientObject;
import com.cmpe273.lwm2mServer.model.Device;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Updates;

public class Lwm2mServerDatabase {
	
	private static MongoClient mongoClient;
	private static MongoDatabase projectdb;
	
	
	
	private static Lwm2mServerDatabase instance;
	
	private Lwm2mServerDatabase(String uri, String database) {
		
		mongoClient = MongoClients.create(uri);
		projectdb = mongoClient.getDatabase(database);
		
	}
	
	public MongoDatabase getProjectdb() {
		return projectdb;
	}

	
	
	public static Lwm2mServerDatabase getInstance() {		
		if(instance == null)
			instance =  new Lwm2mServerDatabase("mongodb+srv://nikhil10:nikhil10@cluster0-8rf4e.mongodb.net/test?retryWrites=true&w=majority", "cmpe273");
		
		return instance;

	}
	
	public void testInsert() {
				
		MongoCollection<Document> devices = projectdb.getCollection("devices");
			
		Document temp = new Document("id","7")
				.append("name", "Connectivity Monitoring");
		
		Bson filter = Filters.eq("deviceId","1");
		Bson setUpdate = Updates.push("objectList", temp);
		devices.updateOne(filter,setUpdate);	
		
		Document d = devices.find(Filters.eq("deviceId", "1")).first();		
		System.out.println(d);
				
	}

	public void addDevice(Device device) {
		
		MongoCollection<Document> devices = projectdb.getCollection("devicesRegistered");
		Document doc = new Document();
		doc.append("deviceName", device.getName());
		doc.append("deviceId", device.getId());
		doc.append("deviceType", device.getType());
		doc.append("endpointURI", "http://localhost:8080/client");
		doc.append("registered", "true");
		doc.append("SMSNumber", "(123)345-4564");
		devices.insertOne(doc);
		
		List<ClientObject> bootstrapObjs = device.getObjectList();
		for( int i = 0 ; i < bootstrapObjs.size() ; i++) {			
			ClientObject o = bootstrapObjs.get(i);
			Document temp =  new Document("deviceId", o.getDeviceId() )
			 .append("name", o.getName())
			 .append("id", o.getId())
			 .append("instances", o.getInstances())
			 .append("mandatory", o.getMandatory())
			 .append("urn", o.getUrn());
			
			Bson filter = Filters.eq("deviceId",device.getId());
			Bson setUpdate = Updates.push("objectList", temp);
			devices.updateOne(filter,setUpdate);
			
		}			
	}

	

}
