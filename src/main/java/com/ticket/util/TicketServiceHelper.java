package com.ticket.util;

import java.util.List;

import org.springframework.stereotype.Component;

import com.ticket.beans.ServiceError;
import com.ticket.beans.TicketServiceResponse;
import com.ticket.exception.TicketServiceException;

@Component
public class TicketServiceHelper {

	/**
	 * 
	 * @param tex
	 * @return
	 */
	public TicketServiceResponse generateDefaultResponse(TicketServiceException tex) {
		TicketServiceResponse serviceResponse = new TicketServiceResponse();
		ServiceError error = new ServiceError();
		error.setErrorCode(tex.getErrorCode());
		error.setErrorMessage(tex.getMessage());
		serviceResponse.getServiceError().add(error);
		return serviceResponse;
	}
	
	/**
	 * 
	 * @param ex
	 * @return
	 */
	public TicketServiceResponse generateDefaultResponse(Exception ex) {
		TicketServiceResponse serviceResponse = new TicketServiceResponse();
		ServiceError error = new ServiceError();
		error.setErrorMessage(ex.getMessage());
		serviceResponse.getServiceError().add(error);
		return serviceResponse;
	}

	/**
	 * 
	 * @param errorList
	 * @return
	 */
	public TicketServiceResponse generateErrorResponse(List<ServiceError> errorList) {
		TicketServiceResponse serviceResponse = new TicketServiceResponse();
		serviceResponse.getServiceError().addAll(errorList);
		return serviceResponse;
	}

}
