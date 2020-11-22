package com.bookmark_meal.model;

import java.util.List;


public interface Bookmark_MealDAO_interface {
	public void insert(Bookmark_MealVO bookmark_mealVO);
	public void update(Bookmark_MealVO bookmark_mealVO);
	public void delete(String mem_no,String meal_no);
	public Bookmark_MealVO findByPrimaryKey(String mem_no,String meal_no);
	public List<Bookmark_MealVO> getAll();
	public List<Bookmark_MealVO> findByMemNo(String mem_no);
	public List<String> findMealNoByMemNo(String mem_no);
}
