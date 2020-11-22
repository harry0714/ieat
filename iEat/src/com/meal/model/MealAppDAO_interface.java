package com.meal.model;

import java.util.*;

public interface MealAppDAO_interface {
	 public void insert(MealVO mealVo);
	 public void update(MealVO mealVo);

	 public MealVO findByPrimaryKey(String mealVo);
	 public List<MealVO> getAllStoreMeal(String store_no);
	 public Set<MealVO> getFindByMeal(String store_no);
	 public byte[] getMealImg(String meal_no); 
	 public MealVO getOneAvailableMeal(String meal_no);
	 public Map<String,MealVO> getTopMeals();
	 public void updateMealStatus(String meal_no, int status);
}
