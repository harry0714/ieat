package com.store_report.model;

import java.sql.Date;

public class Store_reportVO implements java.io.Serializable{
	private String store_report_no; 
	private String store_no; 
	private String mem_no; 
	private String  store_report_content; 
	private Date store_report_date; 
	private String store_report_status;
	public String getStore_report_no() {
		return store_report_no;
	}
	public void setStore_report_no(String store_report_no) {
		this.store_report_no = store_report_no;
	}
	public String getStore_no() {
		return store_no;
	}
	public void setStore_no(String store_no) {
		this.store_no = store_no;
	}
	public String getMem_no() {
		return mem_no;
	}
	public void setMem_no(String mem_no) {
		this.mem_no = mem_no;
	}
	public String getStore_report_content() {
		return store_report_content;
	}
	public void setStore_report_content(String store_report_content) {
		this.store_report_content = store_report_content;
	}
	public Date getStore_report_date() {
		return store_report_date;
	}
	public void setStore_report_date(Date store_report_date) {
		this.store_report_date = store_report_date;
	}
	public String getStore_report_status() {
		return store_report_status;
	}
	public void setStore_report_status(String store_report_status) {
		this.store_report_status = store_report_status;
	}
}
