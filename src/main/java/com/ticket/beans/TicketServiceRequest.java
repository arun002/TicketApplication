package com.ticket.beans;

import org.springframework.stereotype.Component;

@Component
public class TicketServiceRequest {
	
	private String emailId;
	
	private Integer minLevel;
	
	private Integer maxLevel;
	
	private Integer numSeats;
	
	private Integer seatHoldId;

	public String getEmailId() {
		return emailId;
	}

	public void setEmailId(String emailId) {
		this.emailId = emailId;
	}

	public Integer getMinLevel() {
		return minLevel;
	}

	public void setMinLevel(Integer minLevel) {
		this.minLevel = minLevel;
	}

	public Integer getMaxLevel() {
		return maxLevel;
	}

	public void setMaxLevel(Integer maxLevel) {
		this.maxLevel = maxLevel;
	}

	public Integer getNumSeats() {
		return numSeats;
	}

	public void setNumSeats(Integer numSeats) {
		this.numSeats = numSeats;
	}

	public Integer getSeatHoldId() {
		return seatHoldId;
	}

	public void setSeatHoldId(Integer seatHoldId) {
		this.seatHoldId = seatHoldId;
	}
}
