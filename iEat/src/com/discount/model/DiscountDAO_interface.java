package com.discount.model;

import java.util.List;

import com.discount_meal.model.Discount_mealVO;

public interface DiscountDAO_interface {
	public void insert(DiscountVO discountVO);
	public void update(DiscountVO discountVO);
	public void delete(String discount_no);
	public DiscountVO findByPrimaryKey(String discount_no);
	public List<DiscountVO> getAll();
	public List<DiscountVO> getAllByStore(String store_no);
	
	public int getCountMeal(String discount_no);
	public void updateDiscountWithMeal(DiscountVO discountVO,List<Discount_mealVO> meal_list);
	public void insertDiscountWithMeal(DiscountVO discountVO,List<Discount_mealVO> meal_list);
}
