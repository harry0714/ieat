package com.meal.model;

import java.util.List;
import java.util.Set;

public class MealService {

	private MealDAO_interface dao;
	public MealService(){
		dao = new MealDAO();
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
	public void deleteMeal(String meal_no){
		dao.delete(meal_no);
	}
	public MealVO getOneMeal(String meal_no){
		return dao.findByPrimaryKey(meal_no);
	}
	public List<MealVO> getAll() {
		return dao.getAll();
	}
	public Set<MealVO> getFindByMeal(String store_no) {
		return dao.getFindByMeal(store_no);
	}
	public List<MealVO> getRandomMeal() {
		return dao.getRandomMeal();
	}
	public List<MealVO> getOneStoreNoTop(String store_no,Integer meal_status ) {
		return dao.getOneStoreNoTop(store_no,meal_status);
	}

	public MealVO updateMealStatus(Integer meal_status,String meal_no){
		MealVO mealVO = new MealVO();
		mealVO.setMeal_no(meal_no);
		mealVO.setMeal_status(meal_status);
		dao.updateMealStatus(meal_status, meal_no);
		return mealVO;
	}
	public List<MealVO> getRandom(int quantity){
		return dao.getRandom(quantity);
	}
}
