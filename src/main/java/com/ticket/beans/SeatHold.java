package com.ticket.beans;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import org.springframework.stereotype.Component;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.ticket.util.JsonDateSerializer;

@Component
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class SeatHold {
	
	private Long id;
	
	private String email;
	
	@JsonSerialize(using=JsonDateSerializer.class)
	private Date holdDate;
	
	private Integer numSeats;
	
	@JsonIgnore
	private Long reservationId;
	
	@JsonIgnore
	private BigDecimal totalSeatPrice = new BigDecimal("0.00");
	
	@JsonProperty("totalSeatPrice")
	private String strTotalPrice;
	
	private List<Seat> seats = new ArrayList<>();

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public Date getHoldDate() {
		return holdDate;
	}

	public void setHoldDate(Date holdDate) {
		this.holdDate = holdDate;
	}

	public Integer getNumSeats() {
		return numSeats;
	}

	public void setNumSeats(Integer numSeats) {
		this.numSeats = numSeats;
	}

	public List<Seat> getSeats() {
		return seats;
	}

	public void setSeats(List<Seat> seats) {
		this.seats = seats;
	}

	public Long getReservationId() {
		return reservationId;
	}

	public void setReservationId(Long reservationId) {
		this.reservationId = reservationId;
	}

	public BigDecimal getTotalSeatPrice() {
		return totalSeatPrice;
	}

	public void setTotalSeatPrice(BigDecimal totalSeatPrice) {
		this.totalSeatPrice = totalSeatPrice;
	}

	public String getStrTotalPrice() {
		if(null != totalSeatPrice){
			strTotalPrice = NumberFormat.getCurrencyInstance(Locale.US).format(totalSeatPrice.doubleValue());
		}
		return strTotalPrice;
	}

	public void setStrTotalPrice(String strTotalPrice) {
		this.strTotalPrice = strTotalPrice;
	}

}
