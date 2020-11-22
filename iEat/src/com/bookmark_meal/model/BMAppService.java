package com.bookmark_meal.model;

import java.util.List;

public class BMAppService {
	BMAppDAO_interface dao = null;
	
	public BMAppService(){
		dao = new BMAppDAO();
	}
	
	public Bookmark_MealVO addBookmark_Meal(String mem_no,String meal_no){
		Bookmark_MealVO bookmark_mealVO = new Bookmark_MealVO();
		bookmark_mealVO.setMem_no(mem_no);
		bookmark_mealVO.setMeal_no(meal_no);
		dao.insert(bookmark_mealVO);
		return bookmark_mealVO;
		
	}
	
	public void deleteBookmark_Meal(String mem_no,String meal_no){
		dao.delete(mem_no, meal_no);
	}
	
	
	public List<String> getMealNoByMemNo(String mem_no){
		return dao.findMealNoByMemNo(mem_no);
	}
}
