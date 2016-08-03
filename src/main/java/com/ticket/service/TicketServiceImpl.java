package com.ticket.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ticket.beans.SeatHold;
import com.ticket.bo.TicketReservation;
import com.ticket.exception.TicketServiceException;

@Service
public class TicketServiceImpl implements TicketService {

	@Autowired
	private TicketReservation ticketReservation;
	
	@Override
	public int numSeatsAvailable(Integer levelId) throws TicketServiceException {
		return ticketReservation.getNumberOfSeatsAvl(levelId,false);
	}

	@Override
	public SeatHold findAndHoldSeats(int numSeats, Integer minLevel, Integer maxLevel, String customerEmail) throws TicketServiceException {
		return ticketReservation.findAndHoldSeats(numSeats, minLevel, maxLevel, customerEmail);
	}

	@Override
	public String reserveSeats(long seatHoldId, String cutomerEmail) throws TicketServiceException{
		return ticketReservation.reserveSeats(seatHoldId, cutomerEmail);
	}
}