package com.discount_meal.model;

import java.io.Serializable;

public class Discount_mealVO implements Serializable{
	private String discount_no;
	private String meal_no;
	private Integer discount_meal_price;
	
	public String getDiscount_no() {
		return discount_no;
	}
	public void setDiscount_no(String discount_no) {
		this.discount_no = discount_no;
	}
	public String getMeal_no() {
		return meal_no;
	}
	public void setMeal_no(String meal_no) {
		this.meal_no = meal_no;
	}
	public Integer getDiscount_meal_price() {
		return discount_meal_price;
	}
	public void setDiscount_meal_price(Integer discount_meal_price) {
		this.discount_meal_price = discount_meal_price;
	}
}
