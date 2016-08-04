package com.ticket.bo;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.UUID;

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
			if(!isExpired){
				removeExpiredSeatHolds();
			}
			List<Venue> venueList = new ArrayList<>();
			int numOfSeatsAvl = 0;
			int numOfReservedSeats = 0;
			if(null != levelId){
				Venue venue = TicketServiceConfig.venueList.get(levelId-1);
				//Venue venue = venueDAO.readByLevelId(levelId);
				if(null != venue) {
					venueList.add(venue);
				}
			}
			else{
				venueList = TicketServiceConfig.venueList;
				//venueList = venueDAO.readAll();
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
			ex.printStackTrace();
			throw new TicketServiceException(serviceProperties.getGenError(), serviceProperties.getGenErrorCode());
		}
	}
	
	public SeatHold findAndHoldSeats(int numSeatsReq, Integer minLevel,Integer maxLevel, String customerEmail) throws TicketServiceException{
		try{
			removeExpiredSeatHolds();
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
					if(numSeatsAvl > 0){ // only these seats are available for the level
						throw new TicketServiceException(serviceProperties.getNoFullReqSeatsAvl(),serviceProperties.getSeatsAvlErrorCd());
					}
					else{
						throw new TicketServiceException(serviceProperties.getNoReqSeatsAvl(),serviceProperties.getSeatsAvlErrorCd());
					}
				}
				// Requested seats are available
				else{
					//Hold the seat
					SeatHold seatHold = new SeatHold();
					seatHold.setEmail(customerEmail);
					seatHold.setHoldDate(new Date());
					seatHold.setNumSeats(numSeatsReq);
					seatHold = seatHoldDAO.createHold(seatHold);
					
					//Create the seats requested
					Set<Map.Entry<Integer,Integer>> entrySet = seatsAvlMap.entrySet();
					
					for(Entry<Integer,Integer> entry : entrySet){
						List<Seat> occupiedSeats = seatDAO.readSeatsNotAvailablebyLevel(entry.getKey());
						Map<Integer,List<Integer>> occupiedSeatsMap = new HashMap<>();//Map of occupied seats for a level: key - row number , value - list of occupied seat nums
						Venue venue = venueMap.get(entry.getKey());
						int maxSeatsInRow = venue.getSeatsRow(); //Max seats in a row for the level
						// Check if there are occupied seats
						if(null != occupiedSeats && occupiedSeats.size() > 0){
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
								//Iterate through occupied seats
								List <Integer> occupiedSeatNums = occupiedSeatsMap.get(rowNum);
								if(null != occupiedSeatNums && occupiedSeatNums.size() > 0){
									if(!occupiedSeatNums.contains(seatNum)){
										Seat venueSeat = new Seat();
										venueSeat.setSeatHoldId(seatHold.getId());
										venueSeat.setLevelId(entry.getKey());
										venueSeat.setRowNumber(rowNum);
										venueSeat.setSeatNumber(seatNum);
										seatDAO.createSeat(venueSeat);
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
								}else{
									Seat venueSeat = new Seat();
									venueSeat.setSeatHoldId(seatHold.getId());
									venueSeat.setLevelId(entry.getKey());
									venueSeat.setRowNumber(rowNum);
									venueSeat.setSeatNumber(seatNum);
									seatDAO.createSeat(venueSeat);
									seatHold.getSeats().add(venueSeat);
									count++;
								}
								seatNum++;
							}
						}
						else{
							int seatCount = 1;
							int rowNum = 1;
							for(int count=1; count<=entry.getValue(); count++){
								Seat venueSeat = new Seat();
								venueSeat.setSeatHoldId(seatHold.getId());
								venueSeat.setLevelId(entry.getKey());
								venueSeat.setReservationId(0);
								if(seatCount > maxSeatsInRow){
									seatCount = 1;
									rowNum++;
								}
								venueSeat.setRowNumber(rowNum);
								venueSeat.setSeatNumber(seatCount);
								seatDAO.createSeat(venueSeat);
								seatHold.getSeats().add(venueSeat);
								seatCount++;
						 }
					}
				}
				return seatHold;
			}
		  }
		}catch(TicketServiceException tex){
			throw tex;
		}catch(Exception ex){
			throw new TicketServiceException(serviceProperties.getGenError(), serviceProperties.getGenErrorCode());
		}
		return null;
	}
	
	public String reserveSeats(long seatHoldId, String cutomerEmail) throws TicketServiceException{ 
		try{
			removeExpiredSeatHolds();
			//Read the seat hold for seat hold id
			SeatHold seatHold = seatHoldDAO.readByHoldId(seatHoldId);
			if(null != seatHold){
				//Check the hold id is already reserved or not
				if(seatHold.getReservationId() != 0 ){
					throw new TicketServiceException(serviceProperties.getReserveAlDone()+" "+seatHold.getId(),serviceProperties.getReserveErrorCd());
				}
				else if(null != seatHold.getEmail() && !seatHold.getEmail().equalsIgnoreCase(cutomerEmail)){
					throw new TicketServiceException(serviceProperties.getEmailError(),serviceProperties.getEmailErrorCd());
				}
				//Check the hold id expired or not
				if(checkHoldExpired(seatHold)){
					throw new TicketServiceException(serviceProperties.getHoldExpired(),serviceProperties.getHoldExprCd());
				}
				else{
					//Create a reservation
					Reservation reservation = new Reservation();
					reservation.setConfirmationCode(UUID.randomUUID().toString());
					reservation.setConfirmationDate(new Date());
					reservation.setEmail(cutomerEmail);
					reservation.setNumSeats(seatHold.getNumSeats());
					reservDAO.createReservation(reservation);
					//Update seat hold with reservation id
					seatHold.setReservationId(reservation.getId());
					seatHoldDAO.updateSeatHoldWithReservation(seatHold); 
					return reservation.getConfirmationCode();
				}
			}
			else{
				throw new TicketServiceException(serviceProperties.getHoldExpired(),serviceProperties.getHoldExprCd());
			}
		}catch (TicketServiceException tex){
			throw tex;
		}catch (Exception ex){
			ex.printStackTrace();
			throw new TicketServiceException(serviceProperties.getGenError(), serviceProperties.getGenErrorCode());
		}
	}
	
	private Map<Integer,Venue> populateVenueDetails(){
		//List<Venue> venueList = venueDAO.readAll();
		List<Venue> venueList = TicketServiceConfig.venueList;
		Map<Integer,Venue> venueMap = new HashMap<>();
		for(Venue venue : venueList){
			venueMap.put(venue.getLevelId(), venue);
		}
		return venueMap;
	}
	
	private boolean checkHoldExpired(SeatHold seatHold){
		if(null != seatHold){
			if(null != seatHold.getHoldDate()){
				long timeDiff = TicketApplicationUtil.getTimeDiffInMinutes(seatHold.getHoldDate());
				if(serviceProperties.getHoldExpiryTime() > timeDiff){
					return false;
				}
			}
		}
		return true;
	}
	
	private void removeExpiredSeatHolds() {
		//Get all the expired hold ids from Seat Hold 
		List<SeatHold> seatHoldList = seatHoldDAO.readAllExpiredHolds(serviceProperties.getHoldExpiryTime());
		List<Long> holdIds = new ArrayList<>();
		for(SeatHold seatHold : seatHoldList){
			holdIds.add(seatHold.getId());
		}
		if(null != holdIds && holdIds.size() >0){
			// Batch Delete all the expired hold ids from seats
			int seatRows = seatDAO.deleteSeatsForHoldIds(holdIds);
			//Batch Delete all the expired hold ids from seat hold
			int seatHoldRows = seatHoldDAO.deleteExpiredHoldIds(holdIds);
		}
	}
}
