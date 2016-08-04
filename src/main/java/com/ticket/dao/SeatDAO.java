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

import com.ticket.beans.Seat;

@Repository
public class SeatDAO {

	@Autowired
	private JdbcTemplate jdbcTemplate;
	
	@Autowired
	private NamedParameterJdbcTemplate namedParamJdbcTemplate;
	
	private String listOfFields = "SEAT_ID,HOLD_ID,LEVEL_ID,ROW_NUM,SEAT_NUM";
	private String filedsForInsert = "HOLD_ID,LEVEL_ID,ROW_NUM,SEAT_NUM";
	
	public Seat createSeat(Seat seat){
		String SQL = "INSERT INTO SEATS ( "+filedsForInsert+" ) VALUES (?,?,?,?)";
		KeyHolder holder = new GeneratedKeyHolder();
		final String GENERATED_COLUMNS[] = { "seat_id" };
		jdbcTemplate.update(new PreparedStatementCreator() {
			@Override
			public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {
				PreparedStatement statement = connection.prepareStatement(SQL, GENERATED_COLUMNS);
				int i = 0;
				statement.setLong(++i, seat.getSeatHoldId());
				statement.setLong(++i, seat.getLevelId());
				statement.setInt(++i, seat.getRowNumber());
				statement.setInt(++i, seat.getSeatNumber());
				return statement;
			}
		}, holder);
		seat.setId(holder.getKey().longValue());
		return seat;
	}
	
	public List<Seat> readSeatsNotAvailablebyLevel(int levelId){
		String SQL = "SELECT "+ listOfFields+" FROM SEATS WHERE HOLD_ID IS NOT NULL AND LEVEL_ID = ? ORDER BY ROW_NUM, SEAT_NUM ";
		List<Seat> seats = jdbcTemplate.query(SQL, new Object[]{levelId}, new RowMapper<Seat>() {

			@Override
			public Seat mapRow(ResultSet res, int arg1) throws SQLException {
				Seat seat = new Seat();
				seat.setId(res.getLong("SEAT_ID"));
				seat.setLevelId(res.getInt("LEVEL_ID"));
				seat.setRowNumber(res.getInt("ROW_NUM"));
				seat.setSeatNumber(res.getInt("SEAT_NUM"));
				return seat;
			}
		});
		return seats;
	}
	
	public List<Seat> readSeatsNotAvailable(){
		String SQL = "SELECT "+ listOfFields+" FROM SEATS WHERE HOLD_ID IS NOT NULL";
		List<Seat> seats = jdbcTemplate.query(SQL, new RowMapper<Seat>() {

			@Override
			public Seat mapRow(ResultSet res, int arg1) throws SQLException {
				Seat seat = new Seat();
				seat.setId(res.getLong("SEAT_ID"));
				seat.setLevelId(res.getInt("LEVEL_ID"));
				seat.setRowNumber(res.getInt("ROW_NUM"));
				seat.setSeatNumber(res.getInt("SEAT_NUM"));
				return seat;
			}
		});
		return seats;
	}
	
	public Seat findMaxSeatNumberByLevel(int levelId){
		String SQL = "SELECT MAX(ROW_NUM) AS MAX_ROW , MAX(SEAT_NUM) AS MAX_SEAT FROM SEATS WHERE HOLD_ID IS NOT NULL "
				+ "AND LEVEL_ID = ? GROUP BY ROW_NUM,SEAT_NUM";
		List<Seat> seats = jdbcTemplate.query(SQL, new Object[]{levelId}, new RowMapper<Seat>() {

			@Override
			public Seat mapRow(ResultSet res, int arg1) throws SQLException {
				Seat seat = new Seat();
				seat.setRowNumber(res.getInt("MAX_ROW"));
				seat.setSeatNumber(res.getInt("MAX_SEAT"));
				return seat;
			}
		});
		if(seats.isEmpty()){
			return null;
		}
		else{
			return seats.get(0);
		}
	}

	public int deleteSeatsForHoldIds(List<Long> seatHoldIds){
		String SQL = "DELETE FROM SEATS WHERE HOLD_ID in (:holdIds)";
		Map namedParameters =  Collections.singletonMap("holdIds", seatHoldIds);
	    int rows = namedParamJdbcTemplate .update(SQL,namedParameters);
	    return rows;
		
	}
}
