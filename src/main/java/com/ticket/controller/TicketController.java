package com.ticket.controller;

import java.util.Date;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.WebApplicationContext;

import com.ticket.beans.SeatHold;
import com.ticket.beans.ServiceError;
import com.ticket.beans.TicketServiceRequest;
import com.ticket.beans.TicketServiceResponse;
import com.ticket.exception.TicketServiceException;
import com.ticket.service.TicketService;
import com.ticket.util.TicketServiceHelper;
import com.ticket.validator.TicketServiceValidator;

/**
 * Ticket Service Controller
 * 
 * @author Arun
 *
 */
@RestController
@RequestMapping("/ticket-booking-app/v1/venue/seats")
public class TicketController {
	
	private static final Logger logger = LogManager.getLogger(TicketController.class.getName());
	
	@Autowired 
	private TicketService ticketService;
	
	@Autowired
	private TicketServiceHelper serviceHelper;
	
	@Autowired
	private WebApplicationContext context;
	
	public TicketServiceValidator getValidator() {
        return (TicketServiceValidator) context.getBean("ticketServiceValidator");
    }
	
	/**
	 * Service for getting the total number of available seats.
	 * 
	 * @return ticketResponse
	 */
	@RequestMapping(method = RequestMethod.GET, produces = { MediaType.APPLICATION_JSON_VALUE })
	public TicketServiceResponse getNumberOfSeatsAvailable(){
		long startTime = new Date().getTime();
		Integer noSeatsAvl;
		try {
			noSeatsAvl = ticketService.numSeatsAvailable(null);
			TicketServiceResponse ticketResponse = new TicketServiceResponse();
			ticketResponse.setSeatsAvailable(noSeatsAvl);
			return ticketResponse;
		} catch (TicketServiceException tex) {
			logger.error("Error while trying to get available number of seats", tex);
			logger.info("Generating the error response");
			return serviceHelper.generateDefaultResponse(tex);
		} catch (Exception ex){
			logger.error("Error while trying to get available number of seats", ex);
			logger.info("Generating the error response");
			return serviceHelper.generateDefaultResponse(ex);
		} finally{
			logger.info("Response time for service Get Available Seats is "+(System.currentTimeMillis() - startTime)+"ms");
		}
	}
	
	/**
	 * Service for getting the total number of available seats for a level passed.
	 * 
	 * @param venueLevelId
	 * @return ticketResponse
	 */
	@RequestMapping(value="{levelId}", method = RequestMethod.GET, produces = { MediaType.APPLICATION_JSON_VALUE })
	public TicketServiceResponse getNumberOfSeatsAvailableForLevel(@PathVariable("levelId") Integer venueLevelId){
		long startTime = new Date().getTime();
		Integer noSeatsAvl;
		try {
			noSeatsAvl = ticketService.numSeatsAvailable(venueLevelId);
			TicketServiceResponse ticketResponse = new TicketServiceResponse();
			ticketResponse.setSeatsAvailable(noSeatsAvl);
			return ticketResponse;
		} catch (TicketServiceException tex) {
			return serviceHelper.generateDefaultResponse(tex);
		} catch (Exception ex){
			logger.error("Error while trying to get available number of seats for level", ex);
			return serviceHelper.generateDefaultResponse(ex);
		} finally{
			if(null != venueLevelId)
				logger.info("Response time for service Get Available Seats for a level "+venueLevelId+" is "+(System.currentTimeMillis() - startTime)+"ms");
		}
	}
	
	/**
	 * Service for Holding the seats
	 * 
	 * @param showSeatsInfo
	 * @param ticketRequest
	 * @return ticketResponse
	 */
	@RequestMapping(value="hold", method = RequestMethod.POST, produces = { MediaType.APPLICATION_JSON_VALUE }, consumes= {MediaType.APPLICATION_JSON_VALUE})
	public TicketServiceResponse holdSeats(@RequestParam (value="showSeats", required = false, defaultValue = "false") Boolean showSeatsInfo,
					@RequestBody TicketServiceRequest ticketRequest){
		long startTime = new Date().getTime();
		SeatHold seatHold;
		try {
			//Validating the service request
			List<ServiceError> errorList = getValidator().validateHoldSeatsRequest(ticketRequest);
			if(null != errorList && errorList.size() > 0){
				logger.info("Validation failed");
				return serviceHelper.generateErrorResponse(errorList);
			}
			else{
				seatHold = ticketService.findAndHoldSeats(ticketRequest.getNumSeats(), ticketRequest.getMinLevel(), 
						ticketRequest.getMaxLevel(), ticketRequest.getEmailId());
				TicketServiceResponse ticketResponse = new TicketServiceResponse();
				if(!showSeatsInfo){
					seatHold.setSeats(null);
				}
				ticketResponse.setSeatHold(seatHold);
				return ticketResponse;
			}
		} catch (TicketServiceException tex) {
			return serviceHelper.generateDefaultResponse(tex);
		} catch (Exception ex){
			logger.error("Error while trying to hold the seats", ex);
			return serviceHelper.generateDefaultResponse(ex);
		} finally {
			logger.info("Response time for service FindAndHoldSeats is "+(System.currentTimeMillis() - startTime)+"ms");
		}
	}
	
	/**
	 * Service for reserving the hold seats
	 * 
	 * @param ticketRequest
	 * @return ticketResponse
	 */
	@RequestMapping(value="reserve", method = RequestMethod.POST, produces = { MediaType.APPLICATION_JSON_VALUE }, consumes= {MediaType.APPLICATION_JSON_VALUE})
	public TicketServiceResponse reserveSeats(@RequestBody TicketServiceRequest ticketRequest){
		long startTime = new Date().getTime();
		String confirmationCd;
		TicketServiceResponse ticketResponse = new TicketServiceResponse();
		try {
			//Validating the service request
			List<ServiceError> errorList = getValidator().validateReserveSeatsRequest(ticketRequest);
			if(null != errorList && errorList.size() > 0){
				logger.info("Validation failed");
				return serviceHelper.generateErrorResponse(errorList);
			}else{
				confirmationCd = ticketService.reserveSeats(ticketRequest.getSeatHoldId(), ticketRequest.getEmailId());
				ticketResponse.setConfirmationCd(confirmationCd);
				return ticketResponse;
			}
		} catch (TicketServiceException tex) {
			return serviceHelper.generateDefaultResponse(tex);
		} catch (Exception ex){
			logger.error("Error while trying to reserve the seats", ex);
			return serviceHelper.generateDefaultResponse(ex);
		} finally{
			logger.info("Response time for service Resereve Seats is "+(System.currentTimeMillis() - startTime)+"ms");
		}
	}
}
