package com.ord_meal.model;

import java.sql.Connection;
import java.util.List;

public interface Ord_mealDAO_interface {
	 public void insert(Ord_mealVO ord_mealVO);
	 public void update(Ord_mealVO ord_mealVO);
	 public void delete(String ord_mealVO);
	 public Ord_mealVO findByPrimaryKey(String ord_mealVO);
	 public List<Ord_mealVO> getAll();
//  public List<EmpVO> getAll(Map<String, String[]> map); 
	 
	 public void insertMany(Ord_mealVO ord_mealVO,Connection con);
	 public List<Ord_mealVO> getTopFiveMeal();
}
