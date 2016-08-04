package com.ticket.beans;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

@Component
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class TicketServiceResponse {
	
	private Integer seatsAvailable;

	public Integer getSeatsAvailable() {
		return seatsAvailable;
	}

	public void setSeatsAvailable(Integer seatsAvailable) {
		this.seatsAvailable = seatsAvailable;
	}
	
	private SeatHold seatHold;

	public SeatHold getSeatHold() {
		return seatHold;
	}

	public void setSeatHold(SeatHold seatHold) {
		this.seatHold = seatHold;
	}
	
	private String confirmationCd;

	public String getConfirmationCd() {
		return confirmationCd;
	}

	public void setConfirmationCd(String confirmationCd) {
		this.confirmationCd = confirmationCd;
	}
	
	@JsonProperty("Error")
	private List<ServiceError> serviceError = new ArrayList<>();
	
	public List<ServiceError> getServiceError() {
		return serviceError;
	}

	public void setServiceError(List<ServiceError> serviceError) {
		this.serviceError = serviceError;
	}

	private String errorCode;
	
	private String errorMessage;

	public String getErrorCode() {
		return errorCode;
	}

	public void setErrorCode(String errorCode) {
		this.errorCode = errorCode;
	}

	public String getErrorMessage() {
		return errorMessage;
	}

	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}
	

}
