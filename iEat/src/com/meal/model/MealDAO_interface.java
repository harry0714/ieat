package com.meal.model;

import java.util.*;

public interface MealDAO_interface {
	 public void insert(MealVO mealVo);
	 public void update(MealVO mealVo);
	 public void delete(String mealVo);
	 public MealVO findByPrimaryKey(String mealVo);
	 public List<MealVO> getAll(); 
//   public List<EmpVO> getAll(Map<String, String[]> map); 
	 public Set<MealVO> getFindByMeal(String store_no);
	 //首頁  隨機取得推薦餐點
	 public List<MealVO> getRandomMeal();
	public List<MealVO> getOneStoreNoTop(String store_no, Integer meal_status);
	public void updateMealStatus(Integer meal_status, String meal_no);
	
	public List<MealVO> getRandom(int quantity);//隨機取得餐點
}
