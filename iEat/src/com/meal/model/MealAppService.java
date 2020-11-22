package com.meal.model;

import java.util.List;
import java.util.Map;
import java.util.Set;

public class MealAppService {

	private MealAppDAO_interface dao;
	public MealAppService(){
		dao = new MealAppDAO();
	}
	
	public MealVO addMeal(String store_no,byte[] meal_photo,String meal_name,String meal_descr,Integer meal_price,Integer meal_status,
			Integer meal_discount){
		
		MealVO mealVO = new MealVO();
		mealVO.setStore_no(store_no);
		mealVO.setMeal_photo(meal_photo);
		mealVO.setMeal_name(meal_name);
		mealVO.setMeal_descr(meal_descr);
		mealVO.setMeal_price(meal_price);
		mealVO.setMeal_status(meal_status);
		mealVO.setMeal_discount(meal_discount);
		dao.insert(mealVO);
		return mealVO;
	}
	
	public MealVO updateMeal(String meal_no,String store_no,byte[] meal_photo,String meal_name,String meal_descr,Integer meal_price,Integer meal_status,
			Integer meal_discount){
		MealVO mealVO = new MealVO();
		mealVO.setMeal_no(meal_no);
		mealVO.setStore_no(store_no);
		mealVO.setMeal_photo(meal_photo);
		mealVO.setMeal_name(meal_name);
		mealVO.setMeal_descr(meal_descr);
		mealVO.setMeal_price(meal_price);
		mealVO.setMeal_status(meal_status);
		mealVO.setMeal_discount(meal_discount);
		dao.update(mealVO);
		return mealVO;
	}

	public MealVO getOneMeal(String meal_no){
		return dao.findByPrimaryKey(meal_no);
	}

	public List<MealVO> getStoreMeal (String store_no){
		return dao.getAllStoreMeal(store_no);
	}
	public Set<MealVO> getFindByMeal(String store_no) {
		return dao.getFindByMeal(store_no);
	}
	 public byte[] getMealImg(String meal_no){
		 return dao.getMealImg(meal_no);
	 }
	 public MealVO getOneAvailableMeal(String meal_no){
		 return dao.getOneAvailableMeal(meal_no);
	 }
	 public Map<String,MealVO> getTopMeals(){
		 return dao.getTopMeals();
	 }
	 public void updateMealStatus(String meal_no, int status){
		 dao.updateMealStatus(meal_no,status);
	 }
}
