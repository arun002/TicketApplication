package com.ticket.config;

import java.util.List;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.ticket.beans.Venue;
import com.ticket.dao.VenueDAO;

@Component
public class TicketServiceConfig {
	
	public static List<Venue> venueList;
	
	public static Integer minLevel = 1;
	
	public static Integer maxLevel = 4;
	
	@Autowired
	private VenueDAO venueDAO;
	
	@PostConstruct
	public void populateVenueDetails(){
		venueList = venueDAO.readAll();
		if(null != venueList && venueList.size() > 0){
			minLevel = venueList.get(0).getLevelId();
			maxLevel = venueList.get(venueList.size() - 1).getLevelId();
		}
	}

}
