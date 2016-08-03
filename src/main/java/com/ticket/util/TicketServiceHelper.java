package com.ticket.util;

import org.springframework.stereotype.Component;

import com.ticket.beans.TicketServiceResponse;
import com.ticket.exception.TicketServiceException;

@Component
public class TicketServiceHelper {

	public TicketServiceResponse generateDefaultResponse(TicketServiceException tex) {
		TicketServiceResponse serviceResponse = new TicketServiceResponse();
		serviceResponse.setErrorCode(tex.getErrorCode());
		serviceResponse.setErrorMessage(tex.getMessage());
		return serviceResponse;
	}
	
	public TicketServiceResponse generateDefaultResponse(Exception ex) {
		TicketServiceResponse serviceResponse = new TicketServiceResponse();
		serviceResponse.setErrorMessage(ex.getMessage());
		return serviceResponse;
	}

}
