package com.ticket.beans;

import java.util.Date;

public class Reservation {
	
	private long id;
	
	private String confirmationCode;
	
	private String email;
	
	private Date confirmationDate;
	
	private int numSeats;
	
	private Integer holdId;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getConfirmationCode() {
		return confirmationCode;
	}

	public void setConfirmationCode(String confirmationCode) {
		this.confirmationCode = confirmationCode;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public Date getConfirmationDate() {
		return confirmationDate;
	}

	public void setConfirmationDate(Date confirmationDate) {
		this.confirmationDate = confirmationDate;
	}

	public Integer getNumSeats() {
		return numSeats;
	}

	public void setNumSeats(Integer numSeats) {
		this.numSeats = numSeats;
	}

	public Integer getHoldId() {
		return holdId;
	}

	public void setHoldId(Integer holdId) {
		this.holdId = holdId;
	}

}
