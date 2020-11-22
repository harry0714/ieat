package com.ord.model;

import java.sql.Date;
import java.sql.Timestamp;

public class OrdVO implements java.io.Serializable{
	private String ord_no;
	private String mem_no;
	private String store_no;
	private Timestamp ord_date;
	private	Timestamp  ord_pickup;
	private byte[] ord_qrcode;
	private	String	ord_qrcode_status;
	private Integer	ord_paid;
	private String	ord_report;
	private String	ord_report_status;
	
	public String getOrd_no() {
		return ord_no;
	}
	public void setOrd_no(String ord_no) {
		this.ord_no = ord_no;
	}
	public String getMem_no() {
		return mem_no;
	}
	public void setMem_no(String mem_no) {
		this.mem_no = mem_no;
	}
	public String getStore_no() {
		return store_no;
	}
	public void setStore_no(String store_no) {
		this.store_no = store_no;
	}
	public Timestamp getOrd_date() {
		return ord_date;
	}
	public void setOrd_date(Timestamp ord_date) {
		this.ord_date = ord_date;
	}
	public Timestamp getOrd_pickup() {
		return ord_pickup;
	}
	public void setOrd_pickup(Timestamp ord_pickup) {
		this.ord_pickup = ord_pickup;
	}

	public String getOrd_qrcode_status() {
		return ord_qrcode_status;
	}
	public void setOrd_qrcode_status(String ord_qrcode_status) {
		this.ord_qrcode_status = ord_qrcode_status;
	}
	public Integer getOrd_paid() {
		return ord_paid;
	}
	public void setOrd_paid(Integer ord_paid) {
		this.ord_paid = ord_paid;
	}
	public String getOrd_report() {
		return ord_report;
	}
	public void setOrd_report(String ord_report) {
		this.ord_report = ord_report;
	}
	public String getOrd_report_status() {
		return ord_report_status;
	}
	public void setOrd_report_status(String ord_report_status) {
		this.ord_report_status = ord_report_status;
	}
	public byte[] getOrd_qrcode() {
		return ord_qrcode;
	}
	public void setOrd_qrcode(byte[] ord_qrcode) {
		this.ord_qrcode = ord_qrcode;
	}
}
