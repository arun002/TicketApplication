package com.ticket;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.math.BigDecimal;
import java.util.List;

import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.ticket.beans.SeatHold;
import com.ticket.beans.ServiceError;
import com.ticket.beans.TicketServiceRequest;
import com.ticket.beans.Venue;
import com.ticket.config.TicketServiceConfig;
import com.ticket.config.TicketServiceProperties;
import com.ticket.dao.SeatHoldDAO;
import com.ticket.dao.VenueDAO;
import com.ticket.exception.TicketServiceException;
import com.ticket.service.TicketService;
import com.ticket.validator.TicketServiceValidator;

@RunWith(SpringRunner.class)
@SpringBootTest
public class TicketApplicationTests {

	@Autowired
	private VenueDAO venueDAO;
	
	@Autowired
	private TicketService ticketService;
	
	@Autowired
	private TicketServiceValidator validator;
	
	@Autowired
	private TicketServiceProperties properties;
	
	@Test
	public void testFindAllVenues(){
		List<Venue> venueList = venueDAO.readAll();
		assertNotNull(venueList);
		assertTrue(!venueList.isEmpty());
	}
	
	@Test
	public void testNumberOfSeatsAvailableWithInvalidInput() throws TicketServiceException{
		int levelId = 0;
		assertEquals(0,ticketService.numSeatsAvailable(levelId));
		levelId = -1;
		assertEquals(0,ticketService.numSeatsAvailable(levelId));
		levelId = -1;
		assertEquals(0,ticketService.numSeatsAvailable(levelId));
	}
	
	@Test
	public void testFindAndHoldSeatsWithInvalidEmail(){
		TicketServiceRequest serviceRequest = new TicketServiceRequest();
		serviceRequest.setEmailId("aaaa@");
		serviceRequest.setNumSeats(10);
		serviceRequest.setMinLevel(1);
		serviceRequest.setMaxLevel(2);
		List<ServiceError> errorList = validator.validateHoldSeatsRequest(serviceRequest);
		assertTrue(!errorList.isEmpty());
		assertEquals(properties.getEmailIdNotValid(), errorList.get(0).getErrorMessage());
		assertEquals(properties.getEmailIdNotValidErrorCd(), errorList.get(0).getErrorCode());
	}
	
	@Test
	public void testFindAndHoldSeatsWithEmailEmpty(){
		TicketServiceRequest serviceRequest = new TicketServiceRequest();
		serviceRequest.setEmailId("  ");
		serviceRequest.setNumSeats(10);
		serviceRequest.setMinLevel(1);
		serviceRequest.setMaxLevel(2);
		List<ServiceError> errorList = validator.validateHoldSeatsRequest(serviceRequest);
		assertTrue(!errorList.isEmpty());
		assertEquals(properties.getEmailIdNull(), errorList.get(0).getErrorMessage());
		assertEquals(properties.getEmailIdNullErrorCd(), errorList.get(0).getErrorCode());
	}
	
	@Test
	public void testFindAndHoldSeatsWithNoEmail(){
		TicketServiceRequest serviceRequest = new TicketServiceRequest();
		serviceRequest.setNumSeats(10);
		serviceRequest.setMinLevel(1);
		serviceRequest.setMaxLevel(2);
		List<ServiceError> errorList = validator.validateHoldSeatsRequest(serviceRequest);
		assertTrue(!errorList.isEmpty());
		assertEquals(properties.getEmailIdNull(), errorList.get(0).getErrorMessage());
		assertEquals(properties.getEmailIdNullErrorCd(), errorList.get(0).getErrorCode());
	}
	
	@Test
	public void testFindAndHoldSeatsWithNoNumSeats(){
		TicketServiceRequest serviceRequest = new TicketServiceRequest();
		serviceRequest.setEmailId("arun.madhu@gmail.com");
		serviceRequest.setMinLevel(1);
		serviceRequest.setMaxLevel(2);
		List<ServiceError> errorList = validator.validateHoldSeatsRequest(serviceRequest);
		assertTrue(!errorList.isEmpty());
		assertEquals(properties.getNumSeatsReqd(), errorList.get(0).getErrorMessage());
		assertEquals(properties.getNumSeatsReqdErrorCode(), errorList.get(0).getErrorCode());
	}
	
	@Test
	public void testFindAndHoldSeatsWithNumSeatsZero(){
		TicketServiceRequest serviceRequest = new TicketServiceRequest();
		serviceRequest.setEmailId("arun.madhu@gmail.com");
		serviceRequest.setNumSeats(0);
		serviceRequest.setMinLevel(1);
		serviceRequest.setMaxLevel(2);
		List<ServiceError> errorList = validator.validateHoldSeatsRequest(serviceRequest);
		assertTrue(!errorList.isEmpty());
		assertEquals(properties.getNumSeatsNotValid(), errorList.get(0).getErrorMessage());
		assertEquals(properties.getNumSeatsNotValidErrorCode(), errorList.get(0).getErrorCode());
	}
	
	@Test
	public void testFindAndHoldSeatsWithNumSeatsNegative(){
		TicketServiceRequest serviceRequest = new TicketServiceRequest();
		serviceRequest.setEmailId("arun.madhu@gmail.com");
		serviceRequest.setNumSeats(-1);
		serviceRequest.setMinLevel(1);
		serviceRequest.setMaxLevel(2);
		List<ServiceError> errorList = validator.validateHoldSeatsRequest(serviceRequest);
		assertTrue(!errorList.isEmpty());
		assertEquals(properties.getNumSeatsNotValid(), errorList.get(0).getErrorMessage());
		assertEquals(properties.getNumSeatsNotValidErrorCode(), errorList.get(0).getErrorCode());
	}
	
	@Test
	public void testFindAndHoldSeatsWithNoEmailAndNoNumSeats(){
		TicketServiceRequest serviceRequest = new TicketServiceRequest();
		serviceRequest.setMinLevel(1);
		serviceRequest.setMaxLevel(2);
		List<ServiceError> errorList = validator.validateHoldSeatsRequest(serviceRequest);
		assertTrue(!errorList.isEmpty());
		assertEquals(properties.getEmailIdNull(), errorList.get(0).getErrorMessage());
		assertEquals(properties.getEmailIdNullErrorCd(), errorList.get(0).getErrorCode());
		assertEquals(properties.getNumSeatsReqd(), errorList.get(1).getErrorMessage());
		assertEquals(properties.getNumSeatsReqdErrorCode(), errorList.get(1).getErrorCode());
		
	}
	
	@Test
	public void testFindAndHoldSeatsWithMinLevelGreaterthanMaxLevel(){
		TicketServiceRequest serviceRequest = new TicketServiceRequest();
		serviceRequest.setEmailId("arun.madhu@gmail.com");
		serviceRequest.setNumSeats(10);
		serviceRequest.setMinLevel(3);
		serviceRequest.setMaxLevel(2);
		List<ServiceError> errorList = validator.validateHoldSeatsRequest(serviceRequest);
		assertTrue(!errorList.isEmpty());
		assertEquals(properties.getMinLevelHigherMaxLevel(), errorList.get(0).getErrorMessage());
		assertEquals(properties.getMinLevelHigherMaxLevelErrorCd(), errorList.get(0).getErrorCode());
	}
	
	@Test
	public void testFindAndHoldSeatsWithMinAndMaxLevelNotValid(){
		TicketServiceRequest serviceRequest = new TicketServiceRequest();
		serviceRequest.setEmailId("arun.madhu@gmail.com");
		serviceRequest.setNumSeats(5);
		serviceRequest.setMinLevel(5);
		serviceRequest.setMaxLevel(6);
		List<ServiceError> errorList = validator.validateHoldSeatsRequest(serviceRequest);
		assertTrue(!errorList.isEmpty());
		assertEquals(properties.getMinMaxLevelLimit(), errorList.get(0).getErrorMessage());
		assertEquals(properties.getMinMaxLevelLimitErrorCd(), errorList.get(0).getErrorCode());
	}
	
	@Test
	public void testFindAndHoldSeatsWithMoreThantheTotalNoOfAvailableSeats() throws TicketServiceException{
		TicketServiceRequest serviceRequest = new TicketServiceRequest();
		serviceRequest.setEmailId("arun.madhu@gmail.com");
		serviceRequest.setNumSeats(6300);
		serviceRequest.setMinLevel(1);
		serviceRequest.setMaxLevel(4);
		SeatHold seatHold = null;
		try{
			seatHold = ticketService.findAndHoldSeats(serviceRequest.getNumSeats(), serviceRequest.getMinLevel(), serviceRequest.getMaxLevel(), serviceRequest.getEmailId());
		}catch (TicketServiceException ex){
			assertNotNull(ex);
			assertEquals(properties.getNoFullReqSeatsAvl(),ex.getMessage());
			assertEquals(properties.getSeatsAvlErrorCd(), ex.getErrorCode());
		} finally {
			assertNull(seatHold);
		}
	}
	
	
	@Test
	public void testFindAndHoldSeats() throws TicketServiceException{
		TicketServiceRequest serviceRequest = new TicketServiceRequest();
		serviceRequest.setEmailId("arun.madhu@gmail.com");
		serviceRequest.setNumSeats(10);
		serviceRequest.setMinLevel(2);
		serviceRequest.setMaxLevel(3);
		List<ServiceError> errorList = validator.validateHoldSeatsRequest(serviceRequest);
		assertTrue(errorList.isEmpty());
		SeatHold seatHold = ticketService.findAndHoldSeats(serviceRequest.getNumSeats(), serviceRequest.getMinLevel(), serviceRequest.getMaxLevel(), serviceRequest.getEmailId());
		assertNotNull(seatHold);
		assertEquals(new Integer(10),seatHold.getNumSeats());
		assertTrue(!seatHold.getSeats().isEmpty());
		assertEquals(10, seatHold.getSeats().size());
	}
	
	@Test
	public void testFindAndHoldSeatsWithNoMinAndMaxLevel() throws TicketServiceException{
		TicketServiceRequest serviceRequest = new TicketServiceRequest();
		serviceRequest.setEmailId("arun.madhu@gmail.com");
		serviceRequest.setNumSeats(12);
		List<ServiceError> errorList = validator.validateHoldSeatsRequest(serviceRequest);
		assertTrue(errorList.isEmpty());
		SeatHold seatHold = ticketService.findAndHoldSeats(serviceRequest.getNumSeats(), serviceRequest.getMinLevel(), serviceRequest.getMaxLevel(), serviceRequest.getEmailId());
		assertNotNull(seatHold);
		assertEquals(new Integer(12),seatHold.getNumSeats());
		assertTrue(!seatHold.getSeats().isEmpty());
		assertEquals(12, seatHold.getSeats().size());
	}
	
	@Test
	public void testFindAndHoldSeatsWithOnlyMinLevel() throws TicketServiceException{
		TicketServiceRequest serviceRequest = new TicketServiceRequest();
		serviceRequest.setEmailId("arun.madhu@gmail.com");
		serviceRequest.setNumSeats(7);
		serviceRequest.setMinLevel(1);
		List<ServiceError> errorList = validator.validateHoldSeatsRequest(serviceRequest);
		assertTrue(errorList.isEmpty());
		SeatHold seatHold = ticketService.findAndHoldSeats(serviceRequest.getNumSeats(), serviceRequest.getMinLevel(), serviceRequest.getMaxLevel(), serviceRequest.getEmailId());
		assertNotNull(seatHold);
		assertEquals(new Integer(7),seatHold.getNumSeats());
		assertTrue(!seatHold.getSeats().isEmpty());
		assertEquals(7, seatHold.getSeats().size());
	}
	
	@Test
	public void testFindAndHoldSeatsWithOnlyMaxLevel() throws TicketServiceException{
		TicketServiceRequest serviceRequest = new TicketServiceRequest();
		serviceRequest.setEmailId("arun.madhu@gmail.com");
		serviceRequest.setNumSeats(4);
		serviceRequest.setMaxLevel(3);
		List<ServiceError> errorList = validator.validateHoldSeatsRequest(serviceRequest);
		assertTrue(errorList.isEmpty());
		SeatHold seatHold = ticketService.findAndHoldSeats(serviceRequest.getNumSeats(), serviceRequest.getMinLevel(), serviceRequest.getMaxLevel(), serviceRequest.getEmailId());
		assertNotNull(seatHold);
		assertEquals(new Integer(4),seatHold.getNumSeats());
		assertTrue(!seatHold.getSeats().isEmpty());
		assertEquals(4, seatHold.getSeats().size());
	}
	
	@Test
	public void testFindAndHoldSeatsForSeatPrice() throws TicketServiceException{
		TicketServiceRequest serviceRequest = new TicketServiceRequest();
		serviceRequest.setEmailId("arun.madhu@gmail.com");
		serviceRequest.setNumSeats(4);
		serviceRequest.setMinLevel(1);
		serviceRequest.setMaxLevel(1);
		List<ServiceError> errorList = validator.validateHoldSeatsRequest(serviceRequest);
		assertTrue(errorList.isEmpty());
		SeatHold seatHold = ticketService.findAndHoldSeats(serviceRequest.getNumSeats(), serviceRequest.getMinLevel(), serviceRequest.getMaxLevel(), serviceRequest.getEmailId());
		assertNotNull(seatHold);
		assertEquals(new Integer(4),seatHold.getNumSeats());
		assertEquals((TicketServiceConfig.levelSeatPrice.get(1)).multiply(new BigDecimal(serviceRequest.getNumSeats())),seatHold.getTotalSeatPrice());
		assertTrue(!seatHold.getSeats().isEmpty());
		assertEquals(4, seatHold.getSeats().size());
	}
	
	@Test
	public void testReservationHoldSeatsWithNoEmail() throws TicketServiceException{
		TicketServiceRequest serviceRequest = new TicketServiceRequest();
		SeatHold seatHold = ticketService.findAndHoldSeats(2, null, null, "test@gmail.com");
		serviceRequest.setSeatHoldId(seatHold.getId());
		List<ServiceError> errorList = validator.validateReserveSeatsRequest(serviceRequest);
		assertTrue(!errorList.isEmpty());
		assertEquals(properties.getEmailIdNull(), errorList.get(0).getErrorMessage());
		assertEquals(properties.getEmailIdNullErrorCd(), errorList.get(0).getErrorCode());
	}
	
	@Test
	public void testReservationHoldSeatsWithEmailInvalid() throws TicketServiceException{
		TicketServiceRequest serviceRequest = new TicketServiceRequest();
		SeatHold seatHold = ticketService.findAndHoldSeats(2, null, null, "test1@gmail.com");
		serviceRequest.setSeatHoldId(seatHold.getId());
		serviceRequest.setEmailId("test");
		List<ServiceError> errorList = validator.validateReserveSeatsRequest(serviceRequest);
		assertTrue(!errorList.isEmpty());
		assertEquals(properties.getEmailIdNotValid(), errorList.get(0).getErrorMessage());
		assertEquals(properties.getEmailIdNotValidErrorCd(), errorList.get(0).getErrorCode());
	}
	
	@Test
	public void testReservationHoldSeatsWithEmailEmpty() throws TicketServiceException{
		TicketServiceRequest serviceRequest = new TicketServiceRequest();
		SeatHold seatHold = ticketService.findAndHoldSeats(2, null, null, "test2@gmail.com");
		serviceRequest.setSeatHoldId(seatHold.getId());
		serviceRequest.setEmailId(" ");
		List<ServiceError> errorList = validator.validateReserveSeatsRequest(serviceRequest);
		assertTrue(!errorList.isEmpty());
		assertEquals(properties.getEmailIdNull(), errorList.get(0).getErrorMessage());
		assertEquals(properties.getEmailIdNullErrorCd(), errorList.get(0).getErrorCode());
	}
	
	@Test
	public void testReservationHoldSeatsWithEmailIdDifferent() throws TicketServiceException{
		SeatHold seatHold = ticketService.findAndHoldSeats(2, null, null, "test2@gmail.com");
		String confirmationCd = null;
		try{
			confirmationCd = ticketService.reserveSeats(seatHold.getId(),"test@gmail.com");
		} catch (TicketServiceException tex){
			assertNotNull(tex);
			assertEquals(properties.getEmailError(), tex.getMessage());
			assertEquals(properties.getEmailErrorCd(), tex.getErrorCode());
		} finally {
			assertNull(confirmationCd);
		}
	}
	
	@Test
	public void testReservationHoldSeatsWithNoHoldId() throws TicketServiceException{
		TicketServiceRequest serviceRequest = new TicketServiceRequest();
		serviceRequest.setEmailId("arun.madhu@gmail.com");
		List<ServiceError> errorList = validator.validateReserveSeatsRequest(serviceRequest);
		assertTrue(!errorList.isEmpty());
		assertEquals(properties.getSeatHoldNull(), errorList.get(0).getErrorMessage());
		assertEquals(properties.getSeatHoldNullErrorCode(), errorList.get(0).getErrorCode());
	}
	
	@Test
	public void testReservationHoldSeatsWithHoldIdZero(){
		TicketServiceRequest serviceRequest = new TicketServiceRequest();
		serviceRequest.setSeatHoldId(0l);
		serviceRequest.setEmailId("arun.madhu@gmail.com");
		List<ServiceError> errorList = validator.validateReserveSeatsRequest(serviceRequest);
		assertTrue(!errorList.isEmpty());
		assertEquals(properties.getSeatHoldNotValid(), errorList.get(0).getErrorMessage());
		assertEquals(properties.getSeatHoldNotValidErrorCode(), errorList.get(0).getErrorCode());
	}
	
	@Test
	public void testReservationHoldSeatsWithHoldIdNegative(){
		TicketServiceRequest serviceRequest = new TicketServiceRequest();
		serviceRequest.setSeatHoldId(-1l);
		serviceRequest.setEmailId("arun.madhu@gmail.com");
		List<ServiceError> errorList = validator.validateReserveSeatsRequest(serviceRequest);
		assertTrue(!errorList.isEmpty());
		assertEquals(properties.getSeatHoldNotValid(), errorList.get(0).getErrorMessage());
		assertEquals(properties.getSeatHoldNotValidErrorCode(), errorList.get(0).getErrorCode());
	}
	
	@Test
	public void testReservationHoldSeatsWithNoEmailAndHoldId(){
		TicketServiceRequest serviceRequest = new TicketServiceRequest();
		List<ServiceError> errorList = validator.validateReserveSeatsRequest(serviceRequest);
		assertTrue(!errorList.isEmpty());
		assertEquals(properties.getEmailIdNull(), errorList.get(0).getErrorMessage());
		assertEquals(properties.getEmailIdNullErrorCd(), errorList.get(0).getErrorCode());
		assertEquals(properties.getSeatHoldNull(), errorList.get(1).getErrorMessage());
		assertEquals(properties.getSeatHoldNullErrorCode(), errorList.get(1).getErrorCode());
	}
	
	@Test
	public void testReservationHoldSeatsWithHoldIdInvalid() throws TicketServiceException{
		SeatHold seatHold = ticketService.findAndHoldSeats(2, null, null, "test10@gmail.com");
		String confirmationCode = null;
		try{
			confirmationCode = ticketService.reserveSeats(70000,seatHold.getEmail());
		} catch (TicketServiceException tex){
			assertNotNull(tex);
			assertEquals(properties.getHoldExpired(), tex.getMessage());
			assertEquals(properties.getHoldExprCd(), tex.getErrorCode());
		} finally{
			assertNull(confirmationCode);
		}
	}
	
	@Ignore
	@Test
	public void testReservationHoldSeatsWithHoldIdExpired() throws TicketServiceException, InterruptedException{
		SeatHold seatHold = ticketService.findAndHoldSeats(2, null, null, "test10@gmail.com");
		String confirmationCode = null;
		try{
			Double time = properties.getHoldExpiryTime()*60*1000;
			Thread.sleep(time.longValue());
			confirmationCode = ticketService.reserveSeats(seatHold.getId(),seatHold.getEmail());
		} catch (TicketServiceException tex){
			assertNotNull(tex);
			assertEquals(properties.getHoldExpired(), tex.getMessage());
			assertEquals(properties.getHoldExprCd(), tex.getErrorCode());
		} finally {
			assertNull(confirmationCode);
		}
	}
	
	@Test
	public void testReservationHoldSeatsWithHoldIdAlreadyReserved() throws TicketServiceException{
		SeatHold seatHold = ticketService.findAndHoldSeats(2, null, null, "test11@gmail.com");
		ticketService.reserveSeats(seatHold.getId(),seatHold.getEmail());
		String confirmationCode = null;
		try{
			confirmationCode = ticketService.reserveSeats(seatHold.getId(),seatHold.getEmail());
		} catch(TicketServiceException tex){
			assertNotNull(tex);
			assertEquals(properties.getReserveAlDone()+" "+seatHold.getId(), tex.getMessage());
			assertEquals(properties.getReserveErrorCd(), tex.getErrorCode());
		} finally{
			assertNull(confirmationCode);
		}
	}
	
	@Test
	public void testReservationHoldSeats() throws TicketServiceException{
		SeatHold seatHold = ticketService.findAndHoldSeats(3, null, null, "test11@gmail.com");
		String confirmationCode = ticketService.reserveSeats(seatHold.getId(),seatHold.getEmail());
		assertNotNull(confirmationCode);
	}
	
	@Test
	public void testTotalAvailableSeatsAfterHold() throws TicketServiceException{
		int seatsAvl = ticketService.numSeatsAvailable(null);
		SeatHold seatHold = ticketService.findAndHoldSeats(2, null, null, "test11@gmail.com");
		assertNotNull(seatHold);
		assertEquals(seatsAvl -2 , ticketService.numSeatsAvailable(null));
	}
	
	@Test
	public void testAvailableSeatsForLevelAfterHold() throws TicketServiceException{
		int seatsAvl = ticketService.numSeatsAvailable(3);
		SeatHold seatHold = ticketService.findAndHoldSeats(1, 3, 3, "test11@gmail.com");
		assertNotNull(seatHold);
		assertEquals(seatsAvl -1 , ticketService.numSeatsAvailable(3));
	}
	
	@Test
	public void testTotalAvailableSeatsAfterHoldAndReservation() throws TicketServiceException {
		int seatsAvl = ticketService.numSeatsAvailable(null);
		SeatHold seatHold = ticketService.findAndHoldSeats(2, null, null, "test12@gmail.com");
		String confirmationCode = ticketService.reserveSeats(seatHold.getId(),seatHold.getEmail());
		assertNotNull(confirmationCode);
		assertEquals(seatsAvl -2 , ticketService.numSeatsAvailable(null));
	}
	
	@Test
	public void testAvailableSeatsForLevelAfterHoldAndReservation() throws TicketServiceException {
		int seatsAvl = ticketService.numSeatsAvailable(4);
		SeatHold seatHold = ticketService.findAndHoldSeats(1, 4, 4, "test12@gmail.com");
		String confirmationCode = ticketService.reserveSeats(seatHold.getId(),seatHold.getEmail());
		assertNotNull(confirmationCode);
		assertEquals(seatsAvl -1 , ticketService.numSeatsAvailable(4));
	}
	
	@Ignore
	@Test
	public void testTotalAvailableSeatsAfterHoldExpiry() throws TicketServiceException, InterruptedException {
		int seatsAvl = ticketService.numSeatsAvailable(null);
		ticketService.findAndHoldSeats(2, null, null, "test13@gmail.com");
		Double time = properties.getHoldExpiryTime()*60*1000;
		Thread.sleep(time.longValue()+1000);
		assertEquals(seatsAvl, ticketService.numSeatsAvailable(null));
	}
	
	@Ignore
	@Test
	public void testAvailableSeatsForLevelAfterHoldExpiry() throws TicketServiceException, InterruptedException {
		int seatsAvl = ticketService.numSeatsAvailable(2);
		ticketService.findAndHoldSeats(1, 2, 2, "test13@gmail.com");
		Double time = properties.getHoldExpiryTime()*60*1000;
		Thread.sleep(time.longValue()+1000);
		assertEquals(seatsAvl, ticketService.numSeatsAvailable(2));
	}
	
	
}
