package com.ticket.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.jmx.export.annotation.ManagedAttribute;
import org.springframework.jmx.export.annotation.ManagedResource;

/**
 * Config/Properties file for Ticket App
 * The data is JMX enabled , thus can be modified at run time using JCONSOLE
 * @author Arun
 *
 */
@Configuration
@ConfigurationProperties(prefix = "app.ticket")
@ManagedResource(objectName = "app.ticket.config:name=TicketServiceProperties", description = "Ticket Service Properties")
public class TicketServiceProperties {
	
	
	private Long holdExpiryTime;
	private String reserveAlDone;
	private String reserveErrorCd;
	private String emailError;
	private String emailErrorCd;
	private String holdExpired;
	private String holdExprCd;
	private String noFullReqSeatsAvl;
	private String noReqSeatsAvl;
	private String seatsAvlErrorCd;
	private String genError;
	private String genErrorCode;
	private String requestNull;
	private String requestNullErrorCd;
	private String emailIdNull;
	private String emailIdNullErrorCd;
	private String numSeatsNotValid;
	private String numSeatsNotValidErrorCode;
	private String minLevelNotValid;
	private String minLevelNotValidErrorCode;
	private String maxLevelNotValid;
	private String maxLevelNotValidErrorCode;
	private String minLevelHigherMaxLevel;
	private String minLevelHigherMaxLevelErrorCd;
	private String minMaxLevelLimit;
	private String minMaxLevelLimitErrorCd;
	private String seatHoldNotValid;
	private String seatHoldNotValidErrorCode;
	private String emailIdNotValid;
	private String emailIdNotValidErrorCd;
	
	
	@ManagedAttribute
	public Long getHoldExpiryTime() {
		return holdExpiryTime;
	}
	
	@ManagedAttribute
	public void setHoldExpiryTime(Long holdExpiryTime) {
		this.holdExpiryTime = holdExpiryTime;
	}
	
	@ManagedAttribute
	public String getReserveAlDone() {
		return reserveAlDone;
	}
	
	@ManagedAttribute
	public void setReserveAlDone(String reserveAlDone) {
		this.reserveAlDone = reserveAlDone;
	}
	
	@ManagedAttribute
	public String getReserveErrorCd() {
		return reserveErrorCd;
	}
	
	@ManagedAttribute
	public void setReserveErrorCd(String reserveErrorCd) {
		this.reserveErrorCd = reserveErrorCd;
	}
	
	@ManagedAttribute
	public String getEmailError() {
		return emailError;
	}
	
	@ManagedAttribute
	public void setEmailError(String emailError) {
		this.emailError = emailError;
	}
	
	@ManagedAttribute
	public String getEmailErrorCd() {
		return emailErrorCd;
	}
	
	@ManagedAttribute
	public void setEmailErrorCd(String emailErrorCd) {
		this.emailErrorCd = emailErrorCd;
	}
	
	@ManagedAttribute
	public String getHoldExpired() {
		return holdExpired;
	}
	
	@ManagedAttribute
	public void setHoldExpired(String holdExpired) {
		this.holdExpired = holdExpired;
	}
	
	@ManagedAttribute
	public String getHoldExprCd() {
		return holdExprCd;
	}
	
	@ManagedAttribute
	public void setHoldExprCd(String holdExprCd) {
		this.holdExprCd = holdExprCd;
	}

	@ManagedAttribute
	public String getSeatsAvlErrorCd() {
		return seatsAvlErrorCd;
	}

	@ManagedAttribute
	public void setSeatsAvlErrorCd(String seatsAvlErrorCd) {
		this.seatsAvlErrorCd = seatsAvlErrorCd;
	}

	@ManagedAttribute
	public String getGenError() {
		return genError;
	}

	@ManagedAttribute
	public void setGenError(String genError) {
		this.genError = genError;
	}

	@ManagedAttribute
	public String getGenErrorCode() {
		return genErrorCode;
	}

	@ManagedAttribute
	public void setGenErrorCode(String genErrorCode) {
		this.genErrorCode = genErrorCode;
	}

	@ManagedAttribute
	public String getNoFullReqSeatsAvl() {
		return noFullReqSeatsAvl;
	}

	@ManagedAttribute
	public void setNoFullReqSeatsAvl(String noFullReqSeatsAvl) {
		this.noFullReqSeatsAvl = noFullReqSeatsAvl;
	}

	@ManagedAttribute
	public String getNoReqSeatsAvl() {
		return noReqSeatsAvl;
	}

	@ManagedAttribute
	public void setNoReqSeatsAvl(String noReqSeatsAvl) {
		this.noReqSeatsAvl = noReqSeatsAvl;
	}

	@ManagedAttribute
	public String getRequestNull() {
		return requestNull;
	}

	@ManagedAttribute
	public void setRequestNull(String requestNull) {
		this.requestNull = requestNull;
	}

	@ManagedAttribute
	public String getRequestNullErrorCd() {
		return requestNullErrorCd;
	}

	@ManagedAttribute
	public void setRequestNullErrorCd(String requestNullErrorCd) {
		this.requestNullErrorCd = requestNullErrorCd;
	}

	@ManagedAttribute
	public String getNumSeatsNotValid() {
		return numSeatsNotValid;
	}

	@ManagedAttribute
	public void setNumSeatsNotValid(String numSeatsNotValid) {
		this.numSeatsNotValid = numSeatsNotValid;
	}

	@ManagedAttribute
	public String getNumSeatsNotValidErrorCode() {
		return numSeatsNotValidErrorCode;
	}

	@ManagedAttribute
	public void setNumSeatsNotValidErrorCode(String numSeatsNotValidErrorCode) {
		this.numSeatsNotValidErrorCode = numSeatsNotValidErrorCode;
	}

	@ManagedAttribute
	public String getEmailIdNull() {
		return emailIdNull;
	}

	@ManagedAttribute
	public void setEmailIdNull(String emailIdNull) {
		this.emailIdNull = emailIdNull;
	}

	@ManagedAttribute
	public String getEmailIdNullErrorCd() {
		return emailIdNullErrorCd;
	}

	@ManagedAttribute
	public void setEmailIdNullErrorCd(String emailIdNullErrorCd) {
		this.emailIdNullErrorCd = emailIdNullErrorCd;
	}
	
	@ManagedAttribute
	public String getMinLevelNotValid() {
		return minLevelNotValid;
	}

	@ManagedAttribute
	public void setMinLevelNotValid(String minLevelNotValid) {
		this.minLevelNotValid = minLevelNotValid;
	}

	@ManagedAttribute
	public String getMinLevelNotValidErrorCode() {
		return minLevelNotValidErrorCode;
	}

	@ManagedAttribute
	public void setMinLevelNotValidErrorCode(String minLevelNotValidErrorCode) {
		this.minLevelNotValidErrorCode = minLevelNotValidErrorCode;
	}

	@ManagedAttribute
	public String getMaxLevelNotValid() {
		return maxLevelNotValid;
	}

	@ManagedAttribute
	public void setMaxLevelNotValid(String maxLevelNotValid) {
		this.maxLevelNotValid = maxLevelNotValid;
	}

	@ManagedAttribute
	public String getMaxLevelNotValidErrorCode() {
		return maxLevelNotValidErrorCode;
	}

	@ManagedAttribute
	public void setMaxLevelNotValidErrorCode(String maxLevelNotValidErrorCode) {
		this.maxLevelNotValidErrorCode = maxLevelNotValidErrorCode;
	}
	
	@ManagedAttribute
	public String getMinLevelHigherMaxLevel() {
		return minLevelHigherMaxLevel;
	}

	@ManagedAttribute
	public void setMinLevelHigherMaxLevel(String minLevelHigherMaxLevel) {
		this.minLevelHigherMaxLevel = minLevelHigherMaxLevel;
	}

	@ManagedAttribute
	public String getMinLevelHigherMaxLevelErrorCd() {
		return minLevelHigherMaxLevelErrorCd;
	}

	@ManagedAttribute
	public void setMinLevelHigherMaxLevelErrorCd(String minLevelHigherMaxLevelErrorCd) {
		this.minLevelHigherMaxLevelErrorCd = minLevelHigherMaxLevelErrorCd;
	}

	@ManagedAttribute
	public String getMinMaxLevelLimit() {
		return minMaxLevelLimit;
	}

	@ManagedAttribute
	public void setMinMaxLevelLimit(String minMaxLevelLimit) {
		this.minMaxLevelLimit = minMaxLevelLimit;
	}

	@ManagedAttribute
	public String getMinMaxLevelLimitErrorCd() {
		return minMaxLevelLimitErrorCd;
	}

	@ManagedAttribute
	public void setMinMaxLevelLimitErrorCd(String minMaxLevelLimitErrorCd) {
		this.minMaxLevelLimitErrorCd = minMaxLevelLimitErrorCd;
	}

	@ManagedAttribute
	public String getSeatHoldNotValid() {
		return seatHoldNotValid;
	}

	@ManagedAttribute
	public void setSeatHoldNotValid(String seatHoldNotValid) {
		this.seatHoldNotValid = seatHoldNotValid;
	}

	@ManagedAttribute
	public String getSeatHoldNotValidErrorCode() {
		return seatHoldNotValidErrorCode;
	}

	@ManagedAttribute
	public void setSeatHoldNotValidErrorCode(String seatHoldNotValidErrorCode) {
		this.seatHoldNotValidErrorCode = seatHoldNotValidErrorCode;
	}

	@ManagedAttribute
	public String getEmailIdNotValid() {
		return emailIdNotValid;
	}

	@ManagedAttribute
	public void setEmailIdNotValid(String emailIdNotValid) {
		this.emailIdNotValid = emailIdNotValid;
	}

	@ManagedAttribute
	public String getEmailIdNotValidErrorCd() {
		return emailIdNotValidErrorCd;
	}

	@ManagedAttribute
	public void setEmailIdNotValidErrorCd(String emailIdNotValidErrorCd) {
		this.emailIdNotValidErrorCd = emailIdNotValidErrorCd;
	}
}
