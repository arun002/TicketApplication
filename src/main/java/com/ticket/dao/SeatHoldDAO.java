package com.ticket.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import com.ticket.beans.SeatHold;
import com.ticket.util.TicketApplicationUtil;

@Repository
public class SeatHoldDAO {
	
	@Autowired(required = true)
	private JdbcTemplate jdbcTemplate; 
	
	private String listOfFileds = "HOLD_ID,EMAIL,HOLD_DATE,NUM_SEATS,RESERVATION_ID,PRICE";
	private String filedsForInsert = "EMAIL,HOLD_DATE,NUM_SEATS";
	
	public SeatHold createHold(SeatHold seatHold){
		String SQL = "INSERT INTO SEAT_HOLD (" +filedsForInsert+ ") VALUES (?,?,?)";
		KeyHolder holder = new GeneratedKeyHolder();
		jdbcTemplate.update(new PreparedStatementCreator() {
			@Override
			public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {
				PreparedStatement ps = connection.prepareStatement(SQL,new String[]{"hold_id"});
				int i = 0;
				ps.setString(++i, seatHold.getEmail());
				ps.setTimestamp(++i, TicketApplicationUtil.covertToSQLTimestamp(seatHold.getHoldDate()));
				ps.setInt(++i, seatHold.getNumSeats());
				return ps;
			}
		},holder);
		seatHold.setId(holder.getKey().longValue());
		return seatHold;
	}

	
	public SeatHold readByHoldId(long holdId){
		String SQL = "SELECT "+listOfFileds +" FROM SEAT_HOLD WHERE HOLD_ID = ?";
		List<SeatHold> seats = jdbcTemplate.query(SQL, new Object[]{holdId},new RowMapper<SeatHold>() {

			@Override
			public SeatHold mapRow(ResultSet res, int arg1) throws SQLException {
				SeatHold seatHold = new SeatHold();
				seatHold.setId(res.getLong("HOLD_ID"));
				seatHold.setEmail(res.getString("EMAIL"));
				seatHold.setHoldDate(res.getTimestamp("HOLD_DATE"));
				seatHold.setNumSeats(res.getInt("NUM_SEATS"));
				seatHold.setReservationId(res.getLong("RESERVATION_ID"));
				seatHold.setTotalSeatPrice(res.getBigDecimal("PRICE"));
				return seatHold;
			}
		});
		if(seats.isEmpty()){
			return null;
		}
		else{
			return seats.get(0);
		}		
	}
	
	public List<SeatHold> readAllExpiredHolds(long expiryTtime){
		String SQL = "SELECT "+listOfFileds+" FROM SEAT_HOLD WHERE (SELECT  (DATE_PART('day', current_timestamp::timestamp - HOLD_DATE::timestamp) * 24 + " +
               "DATE_PART('hour', current_timestamp::timestamp - HOLD_DATE::timestamp)) * 60 + "+
               "DATE_PART('minute', current_timestamp::timestamp - HOLD_DATE::timestamp)) > ? AND RESERVATION_ID IS NULL";
		List<SeatHold> seatHoldList = jdbcTemplate.query(SQL, new Object[]{expiryTtime},new RowMapper<SeatHold>() {

			@Override
			public SeatHold mapRow(ResultSet res, int arg1) throws SQLException {
				SeatHold seatHold = new SeatHold();
				seatHold.setId(res.getLong("HOLD_ID"));
				seatHold.setEmail(res.getString("EMAIL"));
				seatHold.setHoldDate(res.getTimestamp("HOLD_DATE"));
				seatHold.setNumSeats(res.getInt("NUM_SEATS"));
				seatHold.setReservationId(res.getLong("RESERVATION_ID"));
				seatHold.setTotalSeatPrice(res.getBigDecimal("PRICE"));
				return seatHold;
			}
		});
		return seatHoldList;	
	}
	
	public int updateSeatHoldWithReservation(SeatHold seatHold){
		String SQL = "UPDATE SEAT_HOLD SET RESERVATION_ID = ? WHERE HOLD_ID=?";
		int rowsUpdated = jdbcTemplate.update(SQL, seatHold.getReservationId(), seatHold.getId()) ;
		return rowsUpdated;
		
	}
	
	public int updateSeatHoldPrice(SeatHold seatHold){
		String SQL = "UPDATE SEAT_HOLD SET PRICE = ? WHERE HOLD_ID=?";
		int rowsUpdated = jdbcTemplate.update(SQL, seatHold.getTotalSeatPrice(), seatHold.getId()) ;
		return rowsUpdated;
		
	}


	public int deleteExpiredHoldIds(List<Long> holdIds) {
		String SQL = "DELETE FROM SEAT_HOLD WHERE HOLD_ID in (:holdIds)";
		NamedParameterJdbcTemplate namedTemplate = new NamedParameterJdbcTemplate(jdbcTemplate);
		Map namedParameters =  Collections.singletonMap("holdIds", holdIds);
	    int rows = namedTemplate .update(SQL,namedParameters);
	    return rows;
	}

}
