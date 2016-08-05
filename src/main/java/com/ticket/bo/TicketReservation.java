package com.ticket.bo;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.UUID;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.ticket.beans.Reservation;
import com.ticket.beans.Seat;
import com.ticket.beans.SeatHold;
import com.ticket.beans.Venue;
import com.ticket.config.TicketServiceConfig;
import com.ticket.config.TicketServiceProperties;
import com.ticket.dao.ReservationDAO;
import com.ticket.dao.SeatDAO;
import com.ticket.dao.SeatHoldDAO;
import com.ticket.exception.TicketServiceException;
import com.ticket.util.TicketApplicationUtil;

@Component
public class TicketReservation {
	
	private static Logger logger = LogManager.getLogger(TicketReservation.class.getName());
	
	@Autowired
	private ReservationDAO reservDAO;
	
	@Autowired
	private SeatHoldDAO seatHoldDAO;
	
	@Autowired
	private SeatDAO seatDAO;
	
	@Autowired
	private TicketServiceProperties serviceProperties;
	

	public int getNumberOfSeatsAvl(Integer levelId, boolean isExpired) throws TicketServiceException {
		try{
			List<Venue> venueList = new ArrayList<>();
			int numOfSeatsAvl = 0;
			int numOfReservedSeats = 0;
			if(null != levelId){
				try{
					Venue venue = TicketServiceConfig.venueList.get(levelId-1);
					if(null != venue) {
						venueList.add(venue);
					}
				} catch (Exception ex){
					logger.error("Level Id is not valid "+levelId);
					return 0;
				}
			}
			else{
				venueList = TicketServiceConfig.venueList;
			}
			if(!isExpired){
				logger.debug("Started Removing the expired seat holds");
				removeExpiredSeatHolds();
				logger.debug("Done Removing the expired seat holds");
			}
			if(null != venueList && venueList.size() > 0){
				for(Venue venue : venueList){
					numOfSeatsAvl = numOfSeatsAvl + venue.getNumbRows() * venue.getSeatsRow();
				}
				List<Seat> seats ;
				if(null != levelId){
					seats = seatDAO.readSeatsNotAvailablebyLevel(levelId);
				}
				else{
					seats = seatDAO.readSeatsNotAvailable();
				}
				if(null != seats && !seats.isEmpty()){
					numOfReservedSeats = seats.size();
				}
			}
			return numOfSeatsAvl - numOfReservedSeats;
		} catch(Exception ex){
			logger.error("Exception occurred while fetching the number of seats available", ex);
			throw new TicketServiceException(serviceProperties.getGenError(), serviceProperties.getGenErrorCode());
		}
	}
	
	/**
	 * Find and Hold the seats
	 * 
	 * @param numSeatsReq
	 * @param minLevel
	 * @param maxLevel
	 * @param customerEmail
	 * @return seatHold
	 * 
	 * @throws TicketServiceException
	 */
	public SeatHold findAndHoldSeats(int numSeatsReq, Integer minLevel,Integer maxLevel, String customerEmail) throws TicketServiceException{
		try{
			logger.debug("Started Removing the expired seat holds");
			removeExpiredSeatHolds();
			logger.debug("Done removing the expired seat holds");
			Map<Integer, Venue> venueMap = populateVenueDetails();
			int tmpNoSeatsReq = numSeatsReq;
			if(null == minLevel && null != maxLevel) {
				minLevel = TicketServiceConfig.minLevel;
			}
			else if(null == maxLevel && null != minLevel) {
				maxLevel = TicketServiceConfig.maxLevel;
			}
			else if(null == minLevel && null == maxLevel){
				minLevel = TicketServiceConfig.minLevel;
				maxLevel = TicketServiceConfig.maxLevel;
			}
			logger.info("Minlevel - "+minLevel);
			logger.info("Maxlevel - "+maxLevel);
			if(null != minLevel && null != maxLevel){
				//Stored requested seats and its level based on the availability
				//key - level id , value - seats requested/available
				Map<Integer,Integer> seatsAvlMap = new HashMap<>(); 
				int numSeatsAvl = 0;
				for(int index=minLevel; index <= maxLevel; index++){
					numSeatsAvl = getNumberOfSeatsAvl(index, true);
					if(tmpNoSeatsReq <= numSeatsAvl){
						seatsAvlMap.put(index, tmpNoSeatsReq);
						tmpNoSeatsReq = 0;
						break;
					}
					else{
						if(numSeatsAvl > 0) {
							seatsAvlMap.put(index, numSeatsAvl);
							tmpNoSeatsReq -= numSeatsAvl;
						}
					}
				}
				// requested seats are not available for the requested levels
				if(tmpNoSeatsReq > 0){ 
					if(numSeatsAvl > 0){ 
						logger.error("Requested seats are not fully available");
						throw new TicketServiceException(serviceProperties.getNoFullReqSeatsAvl(),serviceProperties.getSeatsAvlErrorCd());
					}
					else{
						logger.error("Requested seats are not available");
						throw new TicketServiceException(serviceProperties.getNoReqSeatsAvl(),serviceProperties.getSeatsAvlErrorCd());
					}
				}
				// Requested seats are available
				else{
					//Hold the seat
					logger.debug("Create and populate seat hold object");
					SeatHold seatHold = new SeatHold();
					seatHold.setEmail(customerEmail);
					seatHold.setHoldDate(new Date());
					seatHold.setNumSeats(numSeatsReq);
					seatHold = seatHoldDAO.createHold(seatHold);
					logger.debug("Hold has been created");
					BigDecimal seatPrice;
					BigDecimal seatPriceLevel = new BigDecimal("0.00");
					//Create the seats requested
					Set<Map.Entry<Integer,Integer>> entrySet = seatsAvlMap.entrySet();
					for(Entry<Integer,Integer> entry : entrySet){
						List<Seat> occupiedSeats = seatDAO.readSeatsNotAvailablebyLevel(entry.getKey());
						seatPrice = TicketServiceConfig.levelSeatPrice.get(entry.getKey()); //Seat price for the level
						seatPriceLevel = seatPriceLevel.add(seatPrice.multiply(new BigDecimal(entry.getValue()))); // Total price of requested seats for the level
						Map<Integer,List<Integer>> occupiedSeatsMap = new HashMap<>();//Map of occupied seats for a level: key - row number , value - list of occupied seat nums
						Venue venue = venueMap.get(entry.getKey());
						int maxSeatsInRow = venue.getSeatsRow(); //Max seats in a row for the level
						// Check if there are occupied seats
						if(null != occupiedSeats && occupiedSeats.size() > 0){
							logger.debug("There are occupied seats for the level "+entry.getKey());
							List<Integer> occupSeatNumList;
							for(Seat seat : occupiedSeats){
								if(null != occupiedSeatsMap.get(seat.getRowNumber())){
									occupSeatNumList = occupiedSeatsMap.get(seat.getRowNumber());
									occupSeatNumList.add(seat.getSeatNumber());
								}
								else{
									occupSeatNumList = new ArrayList<>();
									occupSeatNumList.add(seat.getSeatNumber());
									occupiedSeatsMap.put(seat.getRowNumber(),occupSeatNumList);
								}
							}
							int rowNum = 1;
							int seatNum = 1;
							//Iterate through seats
							for(int count=1; count<=entry.getValue();){
								if(seatNum > maxSeatsInRow){
									seatNum = 1;
									rowNum++;
								}
								List <Integer> occupiedSeatNums = occupiedSeatsMap.get(rowNum);
								if(null != occupiedSeatNums && occupiedSeatNums.size() > 0){
									if(!occupiedSeatNums.contains(seatNum)){
										logger.info("It's an available seat, hold it!! Seat Number : "+seatNum+" Row Number : "+rowNum+" Level : "+entry.getKey());
										Seat venueSeat = new Seat();
										venueSeat.setSeatHoldId(seatHold.getId());
										venueSeat.setLevelId(entry.getKey());
										venueSeat.setRowNumber(rowNum);
										venueSeat.setSeatNumber(seatNum);
										venueSeat.setPrice(seatPrice);
										seatHold.getSeats().add(venueSeat);
										//Update occupiedSeatsMap
										occupSeatNumList = occupiedSeatsMap.get(rowNum);
										if(null != occupSeatNumList){
											occupSeatNumList.add(seatNum);
										}
										else{
											occupSeatNumList = new ArrayList<>();
											occupSeatNumList.add(seatNum);
											occupiedSeatsMap.put(rowNum, occupSeatNumList);
										}
										count++;
									}
								}else{ //Seats are not occupied for this row
									Seat venueSeat = new Seat();
									venueSeat.setSeatHoldId(seatHold.getId());
									venueSeat.setLevelId(entry.getKey());
									venueSeat.setRowNumber(rowNum);
									venueSeat.setSeatNumber(seatNum);
									venueSeat.setPrice(seatPrice);
									seatHold.getSeats().add(venueSeat);
									count++;
								}
								seatNum++;
							}
						}
						else{
							logger.debug("No occupied seats for the level "+entry.getKey());
							int seatCount = 1;
							int rowNum = 1;
							for(int count=1; count<=entry.getValue(); count++){
								Seat venueSeat = new Seat();
								venueSeat.setSeatHoldId(seatHold.getId());
								venueSeat.setLevelId(entry.getKey());
								if(seatCount > maxSeatsInRow){
									seatCount = 1;
									rowNum++;
								}
								venueSeat.setRowNumber(rowNum);
								venueSeat.setSeatNumber(seatCount);
								venueSeat.setPrice(seatPrice);
								seatHold.getSeats().add(venueSeat);
								seatCount++;
						 }
					}
				}
				//Batch insert to SEATS table
				if(null != seatHold){
					seatHold.setTotalSeatPrice(seatPriceLevel);
					logger.debug("Total Seat Price for the hold is $"+seatPriceLevel);
					//Update the seat hold price
					logger.debug("Updating seat_hold table with the hold price - started");
					seatHoldDAO.updateSeatHoldPrice(seatHold);
					logger.debug("Updating seat_hold table with the hold price - finished");
					List<Seat> seats = seatHold.getSeats();
					if(null != seats && seats.size() > 0){
						logger.debug("Batch inserts to the SEATS table started");
						seatDAO.batchCreateSeats(seats);
						logger.debug("Batch inserts to the SEATS table finished");
					}
				}
				return seatHold;
			}
		  }
		}catch(TicketServiceException tex){
			throw tex;
		}catch(Exception ex){
			logger.error("Error occurred while holding the seats", ex);
			throw new TicketServiceException(serviceProperties.getGenError(), serviceProperties.getGenErrorCode());
		}
		return null;
	}
	
	/**
	 * Reserve seats with hold id and customer email and returns confirmation code
	 * 
	 * @param seatHoldId
	 * @param cutomerEmail
	 * @return confirmationCode
	 * 
	 * @throws TicketServiceException
	 */
	public String reserveSeats(long seatHoldId, String cutomerEmail) throws TicketServiceException{ 
		try{
			logger.debug("Started Removing the expired seat holds");
			removeExpiredSeatHolds();
			logger.debug("Done Removing the expired seat holds");
			//Read the seat hold for seat hold id
			SeatHold seatHold = seatHoldDAO.readByHoldId(seatHoldId);
			if(null != seatHold){
				//Check the hold id is already reserved or not
				if(seatHold.getReservationId() != 0 ){
					logger.error("Hold id is already reserved");
					throw new TicketServiceException(serviceProperties.getReserveAlDone()+" "+seatHold.getId(),serviceProperties.getReserveErrorCd());
				}
				else if(null != seatHold.getEmail() && !seatHold.getEmail().equalsIgnoreCase(cutomerEmail)){
					logger.error("Email id is not matching");
					throw new TicketServiceException(serviceProperties.getEmailError(),serviceProperties.getEmailErrorCd());
				}
				//Check the hold id expired or not
				if(checkHoldExpired(seatHold)){
					logger.error("Hold id is already expired");
					throw new TicketServiceException(serviceProperties.getHoldExpired(),serviceProperties.getHoldExprCd());
				}
				else{
					//Create a reservation
					logger.debug("Creating the reservation");
					Reservation reservation = new Reservation();
					reservation.setConfirmationCode(UUID.randomUUID().toString());
					reservation.setConfirmationDate(new Date());
					reservation.setEmail(cutomerEmail);
					reservation.setNumSeats(seatHold.getNumSeats());
					reservation.setPrice(seatHold.getTotalSeatPrice());
					reservDAO.createReservation(reservation);
					logger.debug("Reservation has been created");
					//Update seat hold with reservation id
					logger.debug("Updating the SEAT_hold table with the reservation id");
					seatHold.setReservationId(reservation.getId());
					seatHoldDAO.updateSeatHoldWithReservation(seatHold); 
					logger.debug("Done updating the SEAT_hold table with the reservation id");
					return reservation.getConfirmationCode();
				}
			}
			else{
				logger.error("Hold id passed is null");
				throw new TicketServiceException(serviceProperties.getHoldExpired(),serviceProperties.getHoldExprCd());
			}
		}catch (TicketServiceException tex){
			throw tex;
		}catch (Exception ex){
			logger.error("Error occurred while reserving the seats", ex);
			throw new TicketServiceException(serviceProperties.getGenError(), serviceProperties.getGenErrorCode());
		}
	}
	
	/**
	 * Populates the venue details : key - level id , value - Venue object
	 * 
	 * @return venueMap
	 */
	private Map<Integer,Venue> populateVenueDetails(){
		List<Venue> venueList = TicketServiceConfig.venueList;
		Map<Integer,Venue> venueMap = new HashMap<>();
		for(Venue venue : venueList){
			venueMap.put(venue.getLevelId(), venue);
		}
		return venueMap;
	}
	
	/**
	 * Checks whether the seat hold is expired or not
	 * 
	 * @param seatHold
	 * @return boolean
	 */
	private boolean checkHoldExpired(SeatHold seatHold){
		if(null != seatHold){
			if(null != seatHold.getHoldDate()){
				long timeDiff = TicketApplicationUtil.getTimeDiffInMinutes(seatHold.getHoldDate());
				logger.info("Hold Expiry time is "+serviceProperties.getHoldExpiryTime()+"minutes");
				if(serviceProperties.getHoldExpiryTime() > timeDiff){
					logger.info("Hold id - "+ seatHold.getId() +"is NOT expired");
					return false;
				}
			}
		}
		logger.info("Hold id - "+ seatHold.getId() +"is expired");
		return true;
	}
	
	/**
	 * Remove the expired holds from SEATS and SEAT_HOLD tables
	 * 
	 */
	private void removeExpiredSeatHolds() {
		//Get all the expired hold ids from Seat Hold 
		List<SeatHold> seatHoldList = seatHoldDAO.readAllExpiredHolds(serviceProperties.getHoldExpiryTime());
		List<Long> holdIds = new ArrayList<>();
		if(null != seatHoldList && seatHoldList.size() > 0){
			for(SeatHold seatHold : seatHoldList){
				holdIds.add(seatHold.getId());
			}
			if(null != holdIds && holdIds.size() >0){
				// Batch Delete all the expired hold ids from seats
				int seatRows = seatDAO.deleteSeatsForHoldIds(holdIds);
				logger.info("Number of seats deleted "+seatRows);
				//Batch Delete all the expired hold ids from seat hold
				int seatHoldRows = seatHoldDAO.deleteExpiredHoldIds(holdIds);
				logger.info("Number of seat holds deleted "+seatHoldRows);
			}
		}
	}
}
