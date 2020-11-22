package com.discount.model;

import java.sql.Date;

public class DiscountVO implements java.io.Serializable {
	private String discount_no;
	private String discount_title;
	private Date discount_startdate;
	private Date discount_enddate;
	private String store_no;
	
	public String getDiscount_no() {
		return discount_no;
	}
	public void setDiscount_no(String discount_no) {
		this.discount_no = discount_no;
	}
	public String getDiscount_title() {
		return discount_title;
	}
	public void setDiscount_title(String discount_title) {
		this.discount_title = discount_title;
	}
	public Date getDiscount_startdate() {
		return discount_startdate;
	}
	public void setDiscount_startdate(Date discount_startdate) {
		this.discount_startdate = discount_startdate;
	}
	public Date getDiscount_enddate() {
		return discount_enddate;
	}
	public void setDiscount_enddate(Date discount_enddate) {
		this.discount_enddate = discount_enddate;
	}
	public String getStore_no() {
		return store_no;
	}
	public void setStore_no(String store_no) {
		this.store_no = store_no;
	}

}
