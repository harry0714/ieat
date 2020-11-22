package com.discount_meal.model;

import java.util.List;

public class Discount_mealService {
	private Discount_mealDAO_interface dao;
	
	public Discount_mealService(){
		dao = new Discount_mealDAO();
	}
	
	public Discount_mealVO addDiscount_meal(String discount_no, String meal_no, Integer discount_meal_price){
		Discount_mealVO discount_mealVO = new Discount_mealVO();
		
		discount_mealVO.setDiscount_no(discount_no);
		discount_mealVO.setMeal_no(meal_no);
		discount_mealVO.setDiscount_meal_price(discount_meal_price);
		
		return discount_mealVO;
	}
	
	public Discount_mealVO updateDiscount_meal(String discount_no, String meal_no, Integer discount_meal_price){
		Discount_mealVO discount_mealVO = new Discount_mealVO();
		
		discount_mealVO.setDiscount_no(discount_no);
		discount_mealVO.setMeal_no(meal_no);
		discount_mealVO.setDiscount_meal_price(discount_meal_price);;
		dao.update(discount_mealVO);
		
		return discount_mealVO;
	}
	
	public void deleteDiscount_meal(String discount_no, String meal_no){
		dao.delete(discount_no, meal_no);
	}
	
	public Discount_mealVO getOneDiscount(String discount_no, String meal_no){
		return dao.findByPrimaryKey(discount_no, meal_no);
	}
	
	public List<Discount_mealVO> getAll(){			
		return dao.getAll();
	}

	public List<Discount_mealVO> getDiscountMeals(String discount_no){			
		return dao.getDiscountMeals(discount_no);
	}	
	
}
