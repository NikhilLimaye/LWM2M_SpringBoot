Technologies used : Spring Boot, MongoDB 
Databases : Server MongoDB database = cmpe273 collection = devicesRegistered
			Client MongoDB database = cmpe273_local collections = devices , clientObjects

Client database already has a device document : 
{"_id":"5dadf72b23d99c71cc46c517","deviceName":"SmartLight_Garage","deviceId":"1","deviceType":"Lights",
"bootstrapServerURI":"http://localhost:8081/bootstrap"}

Unzip all project folders
Import the into Eclipse as Maven Projects
To start all services - lwm2mClient , lwm2mServer and bootstrapServer run all three main methods in respective projects. 
All 3 services should be up - 
	LWM2M Client - http://localhost:8080
	Bootstrap Server - http://localhost:8081
	LWM2M Server - http://localhost:8082


Run these HTTP methods on Postman to view results - 

BOOTSTRAP REQUEST 
POST - http://localhost:8080/client/request/1  - client device should be populated with lwm2mServerURL & Object list

BOOTSTRAP DISCOVER
POST - http://localhost:8081/bootstrap/discover/1 - All objects should be returned

BOOTSTRAP READ
POST - http://localhost:8081/bootstrap/read/1/4 - Object with id = 4 should be returned

CLIENT REGISTER
POST - http://localhost:8080/client/register/1 - Client device will be registered in server database

CLIENT UPDATE
PUT - http://localhost:8080/client/update/SMS/1 - SMS number of registered device will be updated

CLIENT DEREGISTER
DELETE - http://localhost:8080/client/deregister/1 - Device will be removed from server database

----------------------------------------------------------------
DEVICE MANAGEMEMT - 
READ 
GET - http://localhost:8082/server/dvmgt/1/2
GET - http://localhost:8082/server/dvmgt/1/2/2

WRITE
PUT - http://localhost:8082/server/dvmgt/1/2/2 - Updated ACL

CREATE 
POST - http://localhost:8082/server/dvmgt/6/0 - New Object Instance

DISCOVER
GET - http://localhost:8082/server/dvmgt/discover/6/1

----------------------------------------------------------------
INFORMATION REPORTING 

OBSERVE 
POST - http://localhost:8082/server/observe/1/2

WRITE ATTRIBUTE
PUT - http://localhost:8080/client/attribute/6/1 - Updated pmax

NOTIFY 
POST - http://localhost:8080/client/notification/1/2



