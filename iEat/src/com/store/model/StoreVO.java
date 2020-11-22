package com.store.model;

import java.io.Serializable;
import java.sql.Date;

public class StoreVO implements Serializable{
	private String store_no;
	private String store_id;
	private String store_psw;
	private String store_name;
	private String store_phone;
	private String store_owner;
	private String store_intro;
	private String store_ads;
	private String store_type_no;
	private String store_booking;
	private String store_open;
	private String store_book_amt;
	private String store_email;
	private String  store_status;
	private Date  store_validate;
	private Double store_star;
	private String store_latlng; 
	
	public String getStore_no() {
		return store_no;
	}
	public void setStore_no(String store_no) {
		this.store_no = store_no;
	}
	public String getStore_id() {
		return store_id;
	}
	public void setStore_id(String store_id) {
		this.store_id = store_id;
	}
	public String getStore_psw() {
		return store_psw;
	}
	public void setStore_psw(String store_psw) {
		this.store_psw = store_psw;
	}
	public String getStore_name() {
		return store_name;
	}
	public void setStore_name(String store_name) {
		this.store_name = store_name;
	}
	public String getStore_phone() {
		return store_phone;
	}
	public void setStore_phone(String store_phone) {
		this.store_phone = store_phone;
	}
	public String getStore_owner() {
		return store_owner;
	}
	public void setStore_owner(String store_owner) {
		this.store_owner = store_owner;
	}
	public String getStore_intro() {
		return store_intro;
	}
	public void setStore_intro(String store_intro) {
		this.store_intro = store_intro;
	}
	public String getStore_ads() {
		return store_ads;
	}
	public void setStore_ads(String store_ads) {
		this.store_ads = store_ads;
	}
	public String getStore_type_no() {
		return store_type_no;
	}
	public void setStore_type_no(String store_type_no) {
		this.store_type_no = store_type_no;
	}
	public String getStore_booking() {
		return store_booking;
	}
	public void setStore_booking(String store_booking) {
		this.store_booking = store_booking;
	}
	public String getStore_open() {
		return store_open;
	}
	public void setStore_open(String store_open) {
		this.store_open = store_open;
	}
	public String getStore_book_amt() {
		return store_book_amt;
	}
	public void setStore_book_amt(String store_book_amt) {
		this.store_book_amt = store_book_amt;
	}
	public String getStore_email() {
		return store_email;
	}
	public void setStore_email(String store_email) {
		this.store_email = store_email;
	}
	public String getStore_status() {
		return store_status;
	}
	public void setStore_status(String store_status) {
		this.store_status = store_status;
	}
	public Date getStore_validate() {
		return store_validate;
	}
	public void setStore_validate(Date store_validate) {
		this.store_validate = store_validate;
	}
	public Double getStore_star() {
		return store_star;
	}
	public void setStore_star(Double store_star) {
		this.store_star = store_star;
	}
	public String getStore_latlng() {
		return store_latlng;
	}
	public void setStore_latlng(String store_latlng) {
		this.store_latlng = store_latlng;
	}		
}
