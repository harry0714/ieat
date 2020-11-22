package com.meal.model;

import java.sql.Blob;

public class MealVO implements java.io.Serializable{
	private String meal_no;
	private String store_no;
	private byte[] meal_photo;
	private String meal_name;
	private String meal_descr;
	private Integer meal_price;
	private Integer meal_status;
	private Integer meal_discount;
		
	public String getMeal_no() {
		return meal_no;
	}
	public void setMeal_no(String meal_no) {
		this.meal_no = meal_no;
	}
	public String getStore_no() {
		return store_no;
	}
	public void setStore_no(String store_no) {
		this.store_no = store_no;
	}
	
	public String getMeal_name() {
		return meal_name;
	}
	public void setMeal_name(String meal_name) {
		this.meal_name = meal_name;
	}
	public String getMeal_descr() {
		return meal_descr;
	}
	public void setMeal_descr(String meal_descr) {
		this.meal_descr = meal_descr;
	}
	public Integer getMeal_price() {
		return meal_price;
	}
	public void setMeal_price(Integer meal_price) {
		this.meal_price = meal_price;
	}
	public Integer getMeal_status() {
		return meal_status;
	}
	public void setMeal_status(Integer meal_status) {
		this.meal_status = meal_status;
	}
	public Integer getMeal_discount() {
		return meal_discount;
	}
	public void setMeal_discount(Integer meal_discount) {
		this.meal_discount = meal_discount;
	}
	public byte[] getMeal_photo() {
		return meal_photo;
	}
	public void setMeal_photo(byte[] meal_photo) {
		this.meal_photo = meal_photo;
	}
}
