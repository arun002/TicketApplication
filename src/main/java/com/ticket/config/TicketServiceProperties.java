package com.ticket.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.jmx.export.annotation.ManagedAttribute;
import org.springframework.jmx.export.annotation.ManagedResource;

/**
 * Config/Properties file for Ticket App
 * The data is JMX enabled , thus can be modified at run time.
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

}
