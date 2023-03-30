# DroneController  
This class contains endpoints to register drones, load medications into drones, get loaded medications, available drones for loading medications, and drone battery level.  

## Technologies:  
• Spring Boot  
• Spring Data JPA  
• H2 Database  
• JUnit Jupiter  
• Mockito  
• Gradle    
• Liquibase  
• REST API

## Endpoints:  
### Register Drone
URL: POST /drone-service/register-drone

Request Body: DroneEntity  
Example:  
{
"serialNumber": "DRONE017",  
"model": "LIGHTWEIGHT",  
"weightLimit": 500,  
"batteryCapacity": 100,  
"state": "IDLE",  
"loadedMedications": []
}

Response:  
201 CREATED if drone successfully registered  
400 BAD REQUEST if the drone with the given serial number already exists or if the request is invalid  
500 INTERNAL SERVER ERROR if an unexpected error occurs while registering the drone  
### Load Drone  
URL: POST /drone-service/load-drone?serialNumber={serialNumber}  

Request Params:
serialNumber: String (required)  
Request Body: List of MedicationEntity  

[
{
"id": 1,  
"name": "MedicationA",  
"weight": 10.5,  
"code": "ASDFGH",  
"image": [1, 2, 3],  
"droneEntity": null
},  
{
"id": 2,  
"name": "MedicatioB",  
"weight": 5.2,  
"code": "XCVBNM",  
"image": [4, 5, 6],  
"droneEntity": null  
},  
{
"id": 3,  
"name": "MedicationC",  
"weight": 7.8,  
"code": "ERTYUIO",  
"image": [7, 8, 9],  
"droneEntity": null
}
]  

Response:  
200 OK if the drone was successfully loaded with medications  
400 BAD REQUEST if the drone with the given serial number is not found, is not in the idle state, or the request is invalid  
404 NOT FOUND if the drone with the given serial number is not found  
409 CONFLICT if the drone with the given serial number is already loaded with medications and cannot be loaded with new medications  
500 INTERNAL SERVER ERROR if an unexpected error occurs while loading the drone with medications  
### Get Loaded Medications  
URL: GET /drone-service/loaded-medications?serialNumber={serialNumber}  
Request Params:  
serialNumber: String (required)  
Response:  
200 OK with a list of Medication objects if the drone is loaded with medications  
403 FORBIDDEN if the drone is not in the loaded state  
404 NOT FOUND if the drone with the given serial number is not found  
### Get Available Drones For Loading Medications  
URL: GET /drone-service/drones?loadedMedications={loadedMedications}  
Request Params:  
loadedMedications: boolean (optional)  
Response:  
200 OK with a list of Drone objects that are available for loading medications if the request is successful  
500 INTERNAL SERVER ERROR if an unexpected error occurs while getting available drones  
### Get Drone Battery Level  
URL: GET /drone-service/drone-battery-level?serialNumber={serialNumber}  
Request Params:  
serialNumber: String (required)  
Response:  
200 OK with the battery level percentage of the drone if the drone is found and in the loaded state  
400 BAD REQUEST if the drone with the given serial number is not in the loaded state  
404 NOT FOUND if the drone with the given serial number is not found  
### Build and Run  
To build the project, run ./gradlew build  
To run the project, run ./gradlew bootRun  
The application will run on localhost:8080  