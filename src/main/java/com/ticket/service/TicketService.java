package com.ticket.service;

import com.ticket.beans.SeatHold;
import com.ticket.exception.TicketServiceException;

public interface TicketService {
	
	int numSeatsAvailable(Integer levelId) throws TicketServiceException;
	
	SeatHold findAndHoldSeats(int numSeats, Integer minLevel,Integer maxLevel, String customerEmail) throws TicketServiceException;
	
	public String reserveSeats(long seatHoldId, String cutomerEmail) throws TicketServiceException;

}
