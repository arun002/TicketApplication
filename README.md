# TicketApplication

Implementation of a simple ticket service that facilitates the discovery, temporary hold, and final reservation of seats within a high-demand performance
venue.

Technology and Frameworks Used

1. Java 8 (jdk -1.8)
2. Spring Boot 4.0.0
3. Spring JDBC 
4. JMX with Spring Boot for dynamic configuration changes via JConsole
5. Database - PostgreSQL 9.5

Notes

1. Before run the build, download PostgreSQL 9.5 and set up the database in your local.Conection details are provided in application.yml (https://github.com/arun002/TicketApplication/blob/master/src/main/resources/application.yml) 
2. No need to set up the tables and insert the data. This will be taken care in the Build process.

Assumptions :

1. User does not have the ability to select particular seats, while holding. They only have the ability to select seating levels.
2. Application/system will find and hold the seats on behalf of the user.
3. No notification process implemented while expiring the hold ids.
4. Confirmation code is a random generated code and is not persisted in database.

Design Decisions :

1. User has the ability to see the total price as well as individual selected seat price,  while holding the seat. 
2. Seat Hold Id expiry time is JMX enabled and thus can be changed dynamically via JConsole.
3. For every service request,  there is a purge process happening to remove the expired hold ids.
4. Service health check implemented using spring boot actuator (http://localhost:8080/health)
5. Confirmation code after reservation is persisted to the table.


Steps to Build and Run the application via commandline:

1. git clone https://github.com/arun002/TicketApplication.git

2. cd TicketApplication

3. mvn package

4. java -jar target/TicketApp-0.0.1-SNAPSHOT.jar



#APIs

#Get Total Available Seats :

Request :

METHOD:GET

URL: http://localhost:8080/ticket-booking-app/v1/venue/seats

Response:

{
  "seatsAvailable": 6250
}


#Get Available Seats for a level

Request :

METHOD:GET

URL: http://localhost:8080/ticket-booking-app/v1/venue/seats/{{levelId}}

SAMPLE : http://localhost:8080/ticket-booking-app/v1/venue/seats/1

Response:
{
  "seatsAvailable": 1250
}

#Find and Hold Seats

Request

METHOD :POST

URL : http://localhost:8080/ticket-booking-app/v1/venue/seats/hold?showSeats=true

Query param : showSeats -  Give the individual seats information which are hold . Default value is false.

Request Header :

Content-Type : application/json

Request Body :
{
    "emailId":"arun.madhu@gmail.com",
    "minLevel":1,
    "maxLevel":3,
    "numSeats":4
}

Response :

With no query param - http://localhost:8080/ticket-booking-app/v1/venue/seats/hold

{
  "seatHold": {
    "id": 467,
    "email": "arun.madhu@gmail.com",
    "holdDate": "08-05-2016 12:48:30",
    "numSeats": 4,
    "totalSeatPrice": "$400.00"
  }
}

With query param value as true - http://localhost:8080/ticket-booking-app/v1/venue/seats/hold?showSeats=true

{
  "seatHold": {
    "id": 468,
    "email": "arun.madhu@gmail.com",
    "holdDate": "08-05-2016 12:48:44",
    "numSeats": 4,
    "seats": [
      {
        "levelId": 1,
        "rowNumber": 3,
        "seatNumber": 24,
        "price": "$100.00"
      },
      {
        "levelId": 1,
        "rowNumber": 3,
        "seatNumber": 25,
        "price": "$100.00"
      },
      {
        "levelId": 1,
        "rowNumber": 3,
        "seatNumber": 26,
        "price": "$100.00"
      },
      {
        "levelId": 1,
        "rowNumber": 3,
        "seatNumber": 27,
        "price": "$100.00"
      }
    ],
    "totalSeatPrice": "$400.00"
  }
}


Error Details:

---------------------
Error Code - 401

Error Message - Email Id is required

Scenario - Email id is not passed or value is empty in the request body

-----------------

Error Code - 409

Error Message - Email Id is not valid

Scenario - Email Id entered in the request body is not valid

------------------------------
Error Code - 410 

Error Message - numSeats is required

Scenario - numSeats is not passed in the request body

-----------------------------

Error Code - 402

Error Message - numSeats should be greater than 0

Scenario - numSeats passed in the request body is zero or negative number

-----------------------------------

Error Code - 403

Error Message - minLevel should be greater than 0

Scenario - minLevel passed in the request body is zero or negative number

-------------------------------------------

Error Code - 404

Error Message - maxLevel should be greater than 0

Scenario - maxLevel passed in the request body is zero or negative number

-----------------------------------

Error Code - 405

Error Message - minLevel should be less than or equal to maxLevel

Scenario - minLevel passed in the request body is greater than maxLevel passed

-----------------------------------------

Error Code - 406

Error Message - LevelIds are not valid

Scenario - minLevel or maxLevel or both passed in the request are not valid level ids

----------------------------------------------

Error Code - 413

Error Message - Requested Seats are not fully Available

Scenario - seatNums passed in the request are not fully available , while holding the seats.

---------------------------------------------------

Error Code - 413

Error Message - Requested Seats are not Available

Scenario - seatNums passed in the request are fully unavailable , while holding the seats. 

---------------------------------------------

#Reserve the hold seats

Request :

METHOD : POST

URL : http://localhost:8080/ticket-booking-app/v1/venue/seats/reserve

Request Header :

Content-Type : application/json

Request Body :

{
    "emailId":"arun.madhu@gmail.com",
    "seatHoldId":468
}

Response:

{
  "confirmationCd": "c4dafd0e-8bbf-485d-90ea-d5da6fa40666"
}

Error Details :

Error Code - 411

Error Message - Email is different from what entered while holding the seat

Scenario - Email Id passed in the requset body  is different from what entered while holding the seat

----------------------------------------------------

Error Code - 411

Error Message - Email is different from what entered while holding the seat

Scenario - Email Id passed in the requset body  is different from what entered while holding the seat

------------------------------------------

Error Code - 401

Error Message - Email Id is required

Scenario - Email id is not passed or value is empty in the request body

--------------------------------

Error Code - 409

Error Message - Email Id is not valid

Scenario - Email Id entered in the request body is not valid

---------------------------------------

Error Code - 408

Error Message - seatHoldId is required

Scenario - seatHoldId is not passed in the request body

-----------------------------------------

Error Code - 407

Error Message - seatHoldId should be greater than 0

Scenario - seatHoldId passed in the request body is zero or negative

----------------------------------------------------

Generic Error (applicable to all the service methods)

Error Code : 500

Error Message : Service is unavailable now 

Scenario : If there is any server side code error 

------------------------------------------------------------

#Testing Results

Tests are run during the build process. Run the below command, to run it seperately.

mvn test

#DB Schema



