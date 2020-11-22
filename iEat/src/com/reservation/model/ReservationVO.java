package com.reservation.model;

import java.sql.Date;
import java.sql.Timestamp;

public class ReservationVO implements java.io.Serializable{
	private String reservation_no;
	private String mem_no; 
	private String store_no; 
	private Date  reservation_date; 
	private String reservation_hour; 
	private Integer reservation_guests; 
	private Timestamp reservation_time; 
	private byte[] reservation_qrcode;
	private String reservation_qrcode_status;
	private String reservation_report; 
	private String reservation_report_status;
	
	public String getReservation_no() {
		return reservation_no;
	}
	public void setReservation_no(String reservation_no) {
		this.reservation_no = reservation_no;
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
	public Date getReservation_date() {
		return reservation_date;
	}
	public void setReservation_date(Date reservation_date) {
		this.reservation_date = reservation_date;
	}
	public String getReservation_hour() {
		return reservation_hour;
	}
	public void setReservation_hour(String reservation_hour) {
		this.reservation_hour = reservation_hour;
	}
	public Integer getReservation_guests() {
		return reservation_guests;
	}
	public void setReservation_guests(Integer reservation_guests) {
		this.reservation_guests = reservation_guests;
	}

	public Timestamp getReservation_time() {
		return reservation_time;
	}
	public void setReservation_time(Timestamp reservation_time) {
		this.reservation_time = reservation_time;
	}
	public byte[] getReservation_qrcode() {
		return reservation_qrcode;
	}
	public void setReservation_qrcode(byte[] reservation_qrcode) {
		this.reservation_qrcode = reservation_qrcode;
	}
	public String getReservation_qrcode_status() {
		return reservation_qrcode_status;
	}
	public void setReservation_qrcode_status(String reservation_qrcode_status) {
		this.reservation_qrcode_status = reservation_qrcode_status;
	}
	public String getReservation_report() {
		return reservation_report;
	}
	public void setReservation_report(String reservation_report) {
		this.reservation_report = reservation_report;
	}
	public String getReservation_report_status() {
		return reservation_report_status;
	}
	public void setReservation_report_status(String reservation_report_status) {
		this.reservation_report_status = reservation_report_status;
	} 
}
