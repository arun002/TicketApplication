package com.ticket.validator;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import com.ticket.beans.ServiceError;
import com.ticket.beans.TicketServiceRequest;
import com.ticket.config.TicketServiceConfig;
import com.ticket.config.TicketServiceProperties;

/**
 * Service Validator class
 * 
 * @author Arun
 *
 */
@Component
@Scope("prototype")
public class TicketServiceValidator {
	
	Logger logger = LogManager.getLogger(TicketServiceValidator.class.getName());
	
	@Autowired
	private TicketServiceProperties properties;
	
	private List<ServiceError> errorList = new ArrayList<>();
	
	private static String regex = "^(.+)@(.+)$";
	
	private static Pattern pattern = Pattern.compile(regex);

	
	/**
	 * 
	 * @param ticketRequest
	 * @return
	 */
	public List<ServiceError> validateHoldSeatsRequest(TicketServiceRequest ticketRequest) {
		if(null != ticketRequest){
			validateManadatoryParams(ticketRequest.getEmailId(), ticketRequest.getNumSeats());
			validateOptionalParams(ticketRequest.getMinLevel(), ticketRequest.getMaxLevel());
		}
		else{
			validateRequestNull(ticketRequest);
		}
		return errorList;
	}
	
	/**
	 * 
	 * @param ticketRequest
	 */
	public List<ServiceError> validateReserveSeatsRequest(TicketServiceRequest ticketRequest) {
		if(null != ticketRequest){
			validateEmailId(ticketRequest.getEmailId());
			if(null == ticketRequest.getSeatHoldId()){
				logger.error("SeatHoldId is null");
				ServiceError error = new ServiceError();
				error.setErrorMessage(properties.getSeatHoldNull());
				error.setErrorCode(properties.getSeatHoldNullErrorCode());
				errorList.add(error);
			}
			else if(ticketRequest.getSeatHoldId() <= 0){
				logger.error("SeatHoldId is zero or negative");
				ServiceError error = new ServiceError();
				error.setErrorMessage(properties.getSeatHoldNotValid());
				error.setErrorCode(properties.getSeatHoldNotValidErrorCode());
				errorList.add(error);
			}
		}else{
			validateRequestNull(ticketRequest);
		}
		return errorList;
	}
	
	/**
	 * 
	 * @param emailId
	 * @param numSeats
	 */
	private void validateManadatoryParams(String emailId, Integer numSeats) {
		validateEmailId(emailId);
		if(null == numSeats){
			logger.error("numSeats is null");
			ServiceError error = new ServiceError();
			error.setErrorMessage(properties.getNumSeatsReqd());
			error.setErrorCode(properties.getNumSeatsReqdErrorCode());
			errorList.add(error);
		}
		else if (numSeats <= 0){
			logger.error("numSeats is zero or negative");
			ServiceError error = new ServiceError();
			error.setErrorMessage(properties.getNumSeatsNotValid());
			error.setErrorCode(properties.getNumSeatsNotValidErrorCode());
			errorList.add(error);
		}
	}
	
	private void validateOptionalParams(Integer minLevel, Integer maxLevel) {
		if(null != minLevel && minLevel <= 0){
			logger.error("minLevel is zero or negative");
			ServiceError error = new ServiceError();
			error.setErrorMessage(properties.getMinLevelNotValid());
			error.setErrorCode(properties.getMinLevelNotValidErrorCode());
			errorList.add(error);
		}
		if(null != maxLevel && maxLevel <= 0){
			logger.error("maxLevel is zero or negative");
			ServiceError error = new ServiceError();
			error.setErrorMessage(properties.getMaxLevelNotValid());
			error.setErrorCode(properties.getMaxLevelNotValidErrorCode());
			errorList.add(error);
		}
		if(null != minLevel && null != maxLevel &&  minLevel > maxLevel){
			logger.error("minLevel is higher than maxLevel");
			ServiceError error = new ServiceError();
			error.setErrorMessage(properties.getMinLevelHigherMaxLevel());
			error.setErrorCode(properties.getMinLevelHigherMaxLevelErrorCd());
			errorList.add(error);
		}
		validateLevelswithinLimit(minLevel,maxLevel);
	}


	private void validateLevelswithinLimit(Integer minLevel, Integer maxLevel) {
		Integer maximumLevel = TicketServiceConfig.maxLevel;
		if(null != minLevel && null != maxLevel && (minLevel > maximumLevel || maxLevel > maximumLevel)){
			logger.error("Levelids are out side the limit");
			ServiceError error = new ServiceError();
			error.setErrorMessage(properties.getMinMaxLevelLimit());
			error.setErrorCode(properties.getMinMaxLevelLimitErrorCd());
			errorList.add(error);
		}
	}
	
	private void validateEmailId(String emailId) {
		if(!StringUtils.hasText(emailId)){
			logger.error("emailId is null or empty");
			ServiceError error = new ServiceError();
			error.setErrorMessage(properties.getEmailIdNull());
			error.setErrorCode(properties.getEmailIdNullErrorCd());
			errorList.add(error);
		}else{
			Matcher matcher = pattern.matcher(emailId);
			if(!matcher.matches()){
				logger.error("emailId is null or empty");
				ServiceError error = new ServiceError();
				error.setErrorMessage(properties.getEmailIdNotValid());
				error.setErrorCode(properties.getEmailIdNotValidErrorCd());
				errorList.add(error);
			}
		}
	}
	
	private void validateRequestNull(TicketServiceRequest ticketRequest) {
		logger.error("Request is null");
		ServiceError error = new ServiceError();
		error.setErrorCode(properties.getRequestNullErrorCd());
		error.setErrorMessage(properties.getRequestNull());
		errorList.add(error);
	}

}
