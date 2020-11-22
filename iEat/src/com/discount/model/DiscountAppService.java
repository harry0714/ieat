package com.discount.model;

import java.util.List;
import java.util.Map;

import com.discount_meal.model.Discount_mealVO;

public class DiscountAppService {
	private DiscountAppDAO_interface dao;
	
	public DiscountAppService(){
		dao = new DiscountAppDAO();
	}
	
	public Map<String,DiscountVO> getCurrentDiscount(){
		return dao.getCurrentDiscount();
	}
	
}
