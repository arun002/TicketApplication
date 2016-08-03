package com.ticket;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Date;

import com.ticket.beans.Seat;
import com.ticket.beans.SeatHold;
import com.ticket.util.TicketApplicationUtil;

public class TestDBConnection {

	public static void main(String[] args) {
		
		//String filedsForInsert = "EMAIL,HOLD_DATE,NUM_SEATS";
		String filedsForInsert = "HOLD_ID,LEVEL_ID,ROW_NUM,SEAT_NUM,RESERVATION_ID";
		
		System.out.println("-------- PostgreSQL "
				+ "JDBC Connection Testing ------------");

		try {

			//Class.forName("org.postgresql.Driver");
			Class.forName("org.h2.Driver");

		} catch (ClassNotFoundException e) {

			System.out.println("Where is your PostgreSQL JDBC Driver? "
					+ "Include in your library path!");
			e.printStackTrace();
			return;

		}

		System.out.println("PostgreSQL JDBC Driver Registered!");

		Connection connection = null;

		try {

//			connection = DriverManager.getConnection(
//					"jdbc:postgresql://localhost:5432/ticketdb", "postgres",
//					"postgres");
			
//			SeatHold seatHold = new SeatHold();
//			seatHold.setEmail("arun.madhu@gmail.com");
//			seatHold.setHoldDate(new Date());
//			seatHold.setNumSeats(10);
//			//String SQL = "INSERT INTO SEAT_HOLD ("+filedsForInsert+") VALUES (?,?,?)";
//			String SQL = "INSERT INTO SEATS ("+filedsForInsert+") VALUES (?,?,?)";
//			PreparedStatement statement = connection.prepareStatement(SQL);
//			int i=0;
//			statement.setString(++i, seatHold.getEmail());
//			statement.setTimestamp(++i,  TicketApplicationUtil.covertToSQLTimestamp(seatHold.getHoldDate()));
//			statement.setInt(++i, seatHold.getNumSeats());
			
			
			connection = DriverManager.getConnection(
					"jdbc:h2:~/test", "sa",
					"password");
			
			Seat seat = new Seat();
			seat.setSeatHoldId(100);
			seat.setLevelId(1);
			seat.setRowNumber(1);
			seat.setSeatNumber(1);
			
			String SQL = "INSERT INTO SEATS ("+filedsForInsert+") VALUES (?,?,?,?)";
			PreparedStatement statement = connection.prepareStatement(SQL);
			int i=0;
			statement.setLong(++i, seat.getSeatHoldId());
			statement.setInt(++i,  seat.getLevelId());
			statement.setInt(++i, seat.getSeatNumber());
			statement.setInt(++i, seat.getRowNumber());
			
			statement.execute();

			statement.close();
			
			connection.close();

		} catch (SQLException e) {

			System.out.println("Connection Failed! Check output console");
			e.printStackTrace();
			return;

		}

		if (connection != null) {
			System.out.println("You made it, take control your database now!");
		} else {
			System.out.println("Failed to make connection!");
		}
	}


}
