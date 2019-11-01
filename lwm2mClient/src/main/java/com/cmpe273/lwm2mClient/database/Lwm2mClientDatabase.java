package com.cmpe273.lwm2mClient.database;

import java.util.Iterator;
import java.util.List;

import org.bson.Document;
import org.bson.conversions.Bson;

import com.cmpe273.lwm2mClient.model.ClientObject;
import com.cmpe273.lwm2mClient.model.Device;
import com.google.gson.Gson;
import com.mongodb.BasicDBObject;
import com.mongodb.DBCursor;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.MongoIterable;
import com.mongodb.client.model.BsonField;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Updates;

import ch.qos.logback.core.net.server.Client;

public class Lwm2mClientDatabase {
	
	private static MongoClient mongoClient;
	private static MongoDatabase projectdb;
	private static Lwm2mClientDatabase instance;
	
	private Lwm2mClientDatabase(String uri, String database) {
		
		mongoClient = MongoClients.create(uri);
		projectdb = mongoClient.getDatabase(database);
		
	}
	
	
	
	public static Lwm2mClientDatabase getInstance() {
		if(instance == null)
			instance =  new Lwm2mClientDatabase("mongodb://localhost:27017", "cmpe273_local");
		
		return instance;

	}
	
	public void testInsert() {
				
		MongoCollection<Document> objects = projectdb.getCollection("resourceObjects");
		Gson g = new Gson();
			
//		Document temp = new Document("id","7")
//				.append("name", "Connectivity Monitoring");
//		
//		Bson filter = Filters.eq("deviceId","1");
//		Bson setUpdate = Updates.push("objectList", temp);
//		devices.updateOne(filter,setUpdate);	
//		
//		Document d = devices.find(Filters.eq("deviceId", "1")).first();		
//		System.out.println(d);
				
	}

	public void addDevice(Device smartLight) {
		
		MongoCollection<Document> devices = projectdb.getCollection("devices");
		Document doc = new Document();
		doc.append("deviceName", smartLight.getName());
		doc.append("deviceId", smartLight.getId());
		doc.append("deviceType", smartLight.getType());
		doc.append("bootstrapServerURI", smartLight.getBoostrapServerURI());
		doc.append("lwm2mServerURI", null);
		devices.insertOne(doc);
	}

	public void updateDevice(String deviceId, String lwm2mServerURL, List<ClientObject> bootstrapObjs) {
		
		MongoCollection<Document> devices = projectdb.getCollection("devices");
		
		devices.updateOne(Filters.eq("deviceId", deviceId), 
				new Document("$set", new Document("lwm2mServerURI", lwm2mServerURL)));
		
		for( int i = 0 ; i < bootstrapObjs.size() ; i++) {
			ClientObject o = bootstrapObjs.get(i);
			Document temp =  new Document("deviceId", o.getDeviceId() )
			 .append("name", o.getName())
			 .append("id", o.getId())
			 .append("instances", o.getInstances())
			 .append("mandatory", o.getMandatory())
			 .append("urn", o.getUrn());
			
			Bson filter = Filters.eq("deviceId",deviceId);
			Bson setUpdate = Updates.push("objectList", temp);
			devices.updateOne(filter,setUpdate);
			
		}			
	}

	public Device getDevice(String deviceId) {
		
		MongoCollection<Document> devices = projectdb.getCollection("devices");
		
		Document d = devices.find(Filters.eq("deviceId", deviceId)).first();
		
		return new Device(d.getString("deviceId"),
				d.getString("deviceName"),
				d.getString("deviceType"),
				d.getString("bootstrapServerURI"),
				(List)d.get("objectList"),
				d.getString("lwm2mServerURI"));			
	}

	public void addClientObject(String deviceId, List<ClientObject> bootstrapObjs) {
		
		MongoCollection<Document> objects = projectdb.getCollection("clientObjects");
		
	     for( int i = 0 ; i < bootstrapObjs.size() ; i++) {
	    	 ClientObject o = bootstrapObjs.get(i);  	 
			 Document doc = new Document();
			 doc.append("deviceId", o.getDeviceId() );
			 doc.append("name", o.getName());
			 doc.append("id", o.getId());
			 doc.append("instances", o.getInstances());
			 doc.append("mandatory", o.getMandatory());
			 doc.append("urn", o.getUrn());
			 objects.insertOne(doc);
		}
	}



	public String readDeviceDetails(String objectId) {
		
		MongoCollection<Document> objects = projectdb.getCollection("resourceObjects");
		BasicDBObject query = new BasicDBObject();
		query.put("objectId",objectId);
		StringBuilder res = new StringBuilder();
		for(Document a : objects.find(query)) {
			res.append(a.toJson());
			res.append("\n");
		}

		return res.toString();
	}
	
	public String readDeviceDetails(String objectId, String resourceId) {
		
		MongoCollection<Document> objects = projectdb.getCollection("resourceObjects");
		BasicDBObject query = new BasicDBObject();
		query.put("objectId",objectId);
		int count = 0;
		for(Document a : objects.find(query)) {
			if(count == Integer.parseInt(resourceId))
				return a.toJson();	
			count++;
		}

		return null;
	}



	public void writeDeviceObject(String objectId, String resourceId , String newValue) {
		
		MongoCollection<Document> objects = projectdb.getCollection("resourceObjects");
		objects.updateOne(Filters.eq("resourceId", resourceId), 
				new Document("$set", new Document("value", newValue)));
		
		
	}



	public void createDeviceObject(String obj) {
		
		MongoCollection<Document> objects = projectdb.getCollection("resourceObjects");
		String[] elements = obj.split(",");
		Document doc = new Document();
		doc.append("deviceId",elements[0]);
		doc.append("objectId",elements[1]);
		doc.append("objectInstanceId",elements[2]);
		doc.append("resourceId",elements[3]);
		doc.append("name",elements[4]);
		doc.append("value",elements[5]);
		doc.append("details",elements[6]);
		objects.insertOne(doc);
		
		
	}



	public String discoverDevice(String deviceId, String objectId) {
		
		MongoCollection<Document> objects = projectdb.getCollection("resourceObjects");
		BasicDBObject query = new BasicDBObject();
		query.put("deviceId",deviceId);
		Document doc = objects.find(query).first();
		StringBuilder res = new StringBuilder();
		res.append("pmax="+doc.getString("pmax"));
		res.append("\n");
		res.append("dim="+doc.getString("dim"));
		return res.toString();
				
	}



	public void writeDeviceObjectAttribute(String deviceId, String objectId, String newpmax) {
		
		MongoCollection<Document> objects = projectdb.getCollection("resourceObjects");
		BasicDBObject query = new BasicDBObject();
		query.put("deviceId",deviceId);
		
		objects.updateOne(Filters.eq("deviceId", deviceId), 
				new Document("$set", new Document("pmax", newpmax)));
		
		
	}



	
	

}
