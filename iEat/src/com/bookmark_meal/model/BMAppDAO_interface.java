package com.bookmark_meal.model;

import java.util.List;


public interface BMAppDAO_interface {
	public void insert(Bookmark_MealVO bookmark_mealVO);
	public void delete(String mem_no,String meal_no);
	public List<String> findMealNoByMemNo(String mem_no);
}
