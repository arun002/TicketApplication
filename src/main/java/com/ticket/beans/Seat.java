package com.ticket.beans;

import org.springframework.stereotype.Component;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;

@Component
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class Seat {
	
	@JsonIgnore
	private long Id;
	
	@JsonIgnore
	private long seatHoldId;
	
	private int levelId;
	
	@JsonIgnore
	private String levelName;
	
	private int rowNumber;
	
	private int seatNumber;
	
	@JsonIgnore
	private long reservationId;

	@JsonIgnore
	public long getId() {
		return Id;
	}

	public void setId(long id) {
		Id = id;
	}

	public long getSeatHoldId() {
		return seatHoldId;
	}

	public void setSeatHoldId(long seatHoldId) {
		this.seatHoldId = seatHoldId;
	}

	public int getLevelId() {
		return levelId;
	}

	public void setLevelId(int levelId) {
		this.levelId = levelId;
	}

	public String getLevelName() {
		return levelName;
	}

	public void setLevelName(String levelName) {
		this.levelName = levelName;
	}

	public int getRowNumber() {
		return rowNumber;
	}

	public void setRowNumber(int rowNumber) {
		this.rowNumber = rowNumber;
	}

	public int getSeatNumber() {
		return seatNumber;
	}

	public void setSeatNumber(int seatNumber) {
		this.seatNumber = seatNumber;
	}

	public long getReservationId() {
		return reservationId;
	}

	public void setReservationId(long reservationId) {
		this.reservationId = reservationId;
	}

}
