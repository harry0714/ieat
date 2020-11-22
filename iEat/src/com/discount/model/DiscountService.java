package com.discount.model;

import java.util.List;

import com.discount_meal.model.Discount_mealVO;

public class DiscountService {
	private DiscountDAO_interface dao;
	
	public DiscountService(){
		dao = new DiscountDAO();
	}
	
	public DiscountVO addDiscount(String discount_title, java.sql.Date discount_startdate,
			java.sql.Date discount_enddate, String store_no) {

		DiscountVO discountVO = new DiscountVO();

		discountVO.setDiscount_title(discount_title);
		discountVO.setDiscount_startdate(discount_startdate);
		discountVO.setDiscount_enddate(discount_enddate);
		discountVO.setStore_no(store_no);
		dao.insert(discountVO);

		return discountVO;
	}
	
	public DiscountVO updateDiscount(String discount_no, String discount_title, java.sql.Date discount_startdate,
			java.sql.Date discount_enddate, String store_no) {

		DiscountVO discountVO = new DiscountVO();

		discountVO.setDiscount_no(discount_no);
		discountVO.setDiscount_title(discount_title);
		discountVO.setDiscount_startdate(discount_startdate);
		discountVO.setDiscount_enddate(discount_enddate);
		discountVO.setStore_no(store_no);
		dao.update(discountVO);

		return discountVO;
	}
	
	public void deleteDiscount(String discount_no) {
		
		
		dao.delete(discount_no);
	}

	public DiscountVO getOneDiscount(String discount_no) {
		return dao.findByPrimaryKey(discount_no);
	}

	public List<DiscountVO> getAll() {
		return dao.getAll();
	}

	public List<DiscountVO> getStoreDiscount(String store_no) {
		return dao.getAllByStore(store_no);
	}
	
	public int getCountMeal(String discount_no){			
		return dao.getCountMeal(discount_no);
	}
	
	public void updateDiscountWithMeal(DiscountVO discountVO,List<Discount_mealVO> meal_list){
		dao.updateDiscountWithMeal(discountVO,meal_list);
	}
	
	public void insertDiscountWithMeal(DiscountVO discountVO,List<Discount_mealVO> meal_list){
		dao.insertDiscountWithMeal(discountVO,meal_list);
	}	
	
}
