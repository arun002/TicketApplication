package com.ticket.beans;

import java.math.BigDecimal;

public class Venue {
	
	private Integer levelId;
	private String levelName;
	private BigDecimal price;
	private Integer numbRows;
	private Integer seatsRow;
	
	public Integer getLevelId() {
		return levelId;
	}
	public void setLevelId(Integer levelId) {
		this.levelId = levelId;
	}
	public String getLevelName() {
		return levelName;
	}
	public void setLevelName(String levelName) {
		this.levelName = levelName;
	}
	public BigDecimal getPrice() {
		return price;
	}
	public void setPrice(BigDecimal price) {
		this.price = price;
	}
	public Integer getNumbRows() {
		return numbRows;
	}
	public void setNumbRows(Integer numbRows) {
		this.numbRows = numbRows;
	}
	public Integer getSeatsRow() {
		return seatsRow;
	}
	public void setSeatsRow(Integer seatsRow) {
		this.seatsRow = seatsRow;
	}
	
}
