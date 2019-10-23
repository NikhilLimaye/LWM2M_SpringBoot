package com.cmpe273.lwm2mClient;

import org.bson.Document;

import com.cmpe273.lwm2mClient.database.Lwm2mClientDatabase;
import com.mongodb.client.MongoCollection;

public class dbtest {

	public static void main(String[] args) {
		
		
		Lwm2mClientDatabase db = Lwm2mClientDatabase.getInstance();
		db.testInsert();

	}

}
