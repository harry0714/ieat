package com.discount_meal.model;

import java.sql.Connection;
import java.util.List;

public interface Discount_mealDAO_interface {
	public void insert(Discount_mealVO discount_mealVO);
	public void update(Discount_mealVO discount_mealVO);
	public void delete(String discount_no, String meal_no);
	public Discount_mealVO findByPrimaryKey(String discount_no, String meal_no);
	public List<Discount_mealVO> getAll();
	
	public List<Discount_mealVO> getDiscountMeals(String discount_no);
	public void insertMany(Discount_mealVO discount_mealVO, Connection con);
}
