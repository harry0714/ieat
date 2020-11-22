package com.ord_meal.model;

import java.util.List;

public class Ord_mealService {
	private Ord_mealDAO_interface dao;
	public Ord_mealService(){
		dao = new Ord_mealDAO();
	}
	
//	public Ord_mealVO addOrd_meal(String ord_meal_no,String ord_no,String meal_no,Integer ord_meal_qty,Integer ord_meal_price){
//		Ord_mealVO ord_mealVO = new Ord_mealVO();
//		ord_mealVO.setMeal_no(meal_no);
//		ord_mealVO.setMeal_no(meal_no);
//		ord_mealVO.setOrd_meal_qty(ord_meal_qty);
//		ord_mealVO.setOrd_meal_price(ord_meal_price);
//		
//		return ord_mealVO;
//	}
	public Ord_mealVO addOrd_meal(String meal_no,Integer ord_meal_qty,Integer ord_meal_price){
		Ord_mealVO ord_mealVO = new Ord_mealVO();
		ord_mealVO.setMeal_no(meal_no);
		ord_mealVO.setOrd_meal_qty(ord_meal_qty);
		ord_mealVO.setOrd_meal_price(ord_meal_price);
		dao.insert(ord_mealVO);
		return ord_mealVO;
	}
	public Ord_mealVO updateOrd_meal(String ord_meal_no,String ord_no,String meal_no,Integer ord_meal_qty,Integer ord_meal_price){
		Ord_mealVO ord_mealVO = new Ord_mealVO();
		ord_mealVO.setOrd_meal_no(ord_meal_no);
		ord_mealVO.setMeal_no(meal_no);
		ord_mealVO.setMeal_no(meal_no);
		ord_mealVO.setOrd_meal_qty(ord_meal_qty);
		ord_mealVO.setOrd_meal_price(ord_meal_price);
		dao.update(ord_mealVO);
		return ord_mealVO;
	}
	public void deleteOrd_meal(String ord_meal_no){
		dao.delete(ord_meal_no);
	}
	public Ord_mealVO getOneMeal(String ord_meal_no){
		return dao.findByPrimaryKey(ord_meal_no);
	}
	public List<Ord_mealVO> getAll() {
		return dao.getAll();
	}
	public List<Ord_mealVO> getTopFiveMeal() {
		return dao.getTopFiveMeal();
	}
}
