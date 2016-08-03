package com.ticket.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import com.ticket.beans.Venue;

@Repository
public class VenueDAO {
	
	@Autowired(required=true)
	private JdbcTemplate jdbcTemplate;
	
	private String lisOfAllFields = "LEVEL_ID,LEVEL_NAME,PRICE,NUM_ROWS,SEATS_ROW";
	
	public List<Venue> readAll(){
		String SQL = "SELECT " + lisOfAllFields + " FROM VENUE"; 
		List<Venue> venueList = jdbcTemplate.query(SQL, new RowMapper<Venue>() {
			@Override
			public Venue mapRow(ResultSet res, int arg1) throws SQLException {
				Venue venue = new Venue();
				venue.setLevelId(res.getInt("LEVEL_ID"));
				venue.setLevelName(res.getString("LEVEL_NAME"));
				venue.setPrice(res.getBigDecimal("PRICE"));
				venue.setNumbRows(res.getInt("NUM_ROWS"));
				venue.setSeatsRow(res.getInt("SEATS_ROW"));
				return venue;
			}
		});
		return venueList;
	}
	
	public Venue readByLevelId(Integer levelId){
		String SQL = "SELECT " + lisOfAllFields + " FROM VENUE WHERE LEVEL_ID = ?";
		List<Venue> venueList = jdbcTemplate.query(SQL, new Object[]{levelId}, new RowMapper<Venue>() {
			@Override
			public Venue mapRow(ResultSet res, int arg1) throws SQLException {
				Venue venue = new Venue();
				venue.setLevelId(res.getInt("LEVEL_ID"));
				venue.setLevelName(res.getString("LEVEL_NAME"));
				venue.setPrice(res.getBigDecimal("PRICE"));
				venue.setNumbRows(res.getInt("NUM_ROWS"));
				venue.setSeatsRow(res.getInt("SEATS_ROW"));
				return venue;
			}
			
		});
		if (venueList.isEmpty())
			return null;
		else {
			return venueList.get(0);
		}		
	}

}
