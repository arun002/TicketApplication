package com.ticket.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import com.ticket.beans.Reservation;
import com.ticket.util.TicketApplicationUtil;

@Repository
public class ReservationDAO {
	
	@Autowired(required=true)
	private JdbcTemplate jdbcTemplate;
	
	private String listOfFields = "RESERVE_ID,CONFIRM_CODE,EMAIL,CONFIRM_DATE,NUM_SEATS";
	
	private String fieldsForInsert = "CONFIRM_CODE,EMAIL,CONFIRM_DATE,NUM_SEATS";
	
	public List<Reservation> readByLevel(Integer levelId){
		String SQL = "SELECT "+listOfFields+" FROM RESERVATION WHERE LEVEL_ID = ?";
		List<Reservation> reservationList = jdbcTemplate.query(SQL, new Object[]{levelId}, new RowMapper<Reservation>() {
			@Override
			public Reservation mapRow(ResultSet res, int arg1) throws SQLException {
				Reservation reservation = new Reservation();
				reservation.setId(res.getInt("RESERVE_ID"));
				reservation.setConfirmationCode(res.getString("CONFIRM_CODE"));
				reservation.setEmail(res.getString("EMAIL"));
				reservation.setConfirmationDate(res.getTimestamp("CONFIRM_DATE"));
				reservation.setNumSeats(res.getInt("NUM_SEATS"));
				return reservation;
			}
			
		});
		return reservationList;
	}
	
	public Reservation createReservation(Reservation reservation){
		String SQL = "INSERT INTO RESERVATION ( "+fieldsForInsert+" )"+ "VALUES (?,?,?,?)";
		KeyHolder holder = new GeneratedKeyHolder();
		jdbcTemplate.update(new PreparedStatementCreator() {
			
			@Override
			public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {
				PreparedStatement statement = connection.prepareStatement(SQL,new String[]{"reserve_id"});
				int i=0;
				statement.setString(++i, reservation.getConfirmationCode());
				statement.setString(++i, reservation.getEmail());
				statement.setTimestamp(++i, TicketApplicationUtil.covertToSQLTimestamp(reservation.getConfirmationDate()) );
				statement.setInt(++i, reservation.getNumSeats());
				return statement;
			}
		}, holder);
		reservation.setId(holder.getKey().longValue());
		return reservation;
		
	}

}
