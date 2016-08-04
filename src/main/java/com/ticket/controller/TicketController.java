package com.ticket.controller;

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

import com.ticket.beans.SeatHold;
import com.ticket.beans.TicketServiceRequest;
import com.ticket.beans.TicketServiceResponse;
import com.ticket.exception.TicketServiceException;
import com.ticket.service.TicketService;
import com.ticket.util.TicketServiceHelper;

@RestController
@RequestMapping("/ticket-booking-app/v1/venue/seats")
public class TicketController {
	
	private static final Logger logger = LogManager.getLogger(TicketController.class.getName());
	
	@Autowired 
	private TicketService ticketService;
	@Autowired
	private TicketServiceHelper serviceHelper;
	
	@RequestMapping(method = RequestMethod.GET, produces = { MediaType.APPLICATION_JSON_VALUE })
	public TicketServiceResponse getNumberOfSeatsAvailable(){
		logger.debug("Entry getNumberOfSeatsAvailable");
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
			logger.debug("Exit getNumberOfSeatsAvailable");
		}
	}
	
	@RequestMapping(value="{levelId}", method = RequestMethod.GET, produces = { MediaType.APPLICATION_JSON_VALUE })
	public TicketServiceResponse getNumberOfSeatsAvailable(@PathVariable("levelId") Integer venueLevelId){
		Integer noSeatsAvl;
		try {
			noSeatsAvl = ticketService.numSeatsAvailable(venueLevelId);
			TicketServiceResponse ticketResponse = new TicketServiceResponse();
			ticketResponse.setSeatsAvailable(noSeatsAvl);
			return ticketResponse;
		} catch (TicketServiceException tex) {
			return serviceHelper.generateDefaultResponse(tex);
		} catch (Exception ex){
			return serviceHelper.generateDefaultResponse(ex);
		}
	}
	
	@RequestMapping(value="hold", method = RequestMethod.POST, produces = { MediaType.APPLICATION_JSON_VALUE }, consumes= {MediaType.APPLICATION_JSON_VALUE})
	public TicketServiceResponse holdSeats(@RequestParam (value="showSeats", required = false, defaultValue = "false") Boolean showSeatsInfo,@RequestBody TicketServiceRequest ticketRequest){
		SeatHold seatHold;
		try {
			seatHold = ticketService.findAndHoldSeats(ticketRequest.getNumSeats(), ticketRequest.getMinLevel(), 
					ticketRequest.getMaxLevel(), ticketRequest.getEmailId());
			TicketServiceResponse ticketResponse = new TicketServiceResponse();
			if(!showSeatsInfo){
				seatHold.setSeats(null);
			}
			ticketResponse.setSeatHold(seatHold);
			return ticketResponse;
		} catch (TicketServiceException tex) {
			return serviceHelper.generateDefaultResponse(tex);
		} catch (Exception ex){
			return serviceHelper.generateDefaultResponse(ex);
		}
	}
	
	@RequestMapping(value="reserve", method = RequestMethod.POST, produces = { MediaType.APPLICATION_JSON_VALUE }, consumes= {MediaType.APPLICATION_JSON_VALUE})
	public TicketServiceResponse reserveSeats(@RequestBody TicketServiceRequest ticketRequest){
		String confirmationCd;
		TicketServiceResponse ticketResponse = new TicketServiceResponse();
		try {
			confirmationCd = ticketService.reserveSeats(ticketRequest.getSeatHoldId(), ticketRequest.getEmailId());
			ticketResponse.setConfirmationCd(confirmationCd);
		} catch (TicketServiceException tex) {
			return serviceHelper.generateDefaultResponse(tex);
		} catch (Exception ex){
			return serviceHelper.generateDefaultResponse(ex);
		}
		return ticketResponse;
	}
}
