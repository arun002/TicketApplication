# TicketApplication
Ticket application services

Implementation of a simple ticket service that facilitates the discovery, temporary hold, and final reservation of seats within a high-demand performance
venue.

#APIs

#Get Available Seats :

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
    "id": 4,
    "email": "arun.madhu@gmail.com",
    "holdDate": "08-03-2016 12:24:52",
    "numSeats": 10
  }
}

With query param value as true - http://localhost:8080/ticket-booking-app/v1/venue/seats/hold?showSeats=true

{
  "seatHold": {
    "id": 6,
    "email": "arun.madhu@gmail.com",
    "holdDate": "08-03-2016 12:25:42",
    "numSeats": 4,
    "seats": [
      {
        "levelId": 1,
        "rowNumber": 1,
        "seatNumber": 25
      },
      {
        "levelId": 1,
        "rowNumber": 1,
        "seatNumber": 26
      },
      {
        "levelId": 1,
        "rowNumber": 1,
        "seatNumber": 27
      },
      {
        "levelId": 1,
        "rowNumber": 1,
        "seatNumber": 28
      }
    ]
  }
}

#Reserve the hold seats

Request :

METHOD : POST

URL : http://localhost:8080/ticket-booking-app/v1/venue/seats/reserve

Request Header :

Content-Type : application/json

Request Body :

{
    "emailId":"arun.madhu@gmail.com",
    "seatHoldId":8
}

Response:

{
  "confirmationCd": "c4dafd0e-8bbf-485d-90ea-d5da6fa40666"
}

