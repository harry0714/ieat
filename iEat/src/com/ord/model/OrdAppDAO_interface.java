package com.ord.model;


import java.util.List;
import java.util.Map;

import com.ord_meal.model.Ord_mealVO;

public interface OrdAppDAO_interface {
	public void insert(OrdVO ordVO);
	public void update(OrdVO ordVO);

	public OrdVO findByPrimaryKey(String ordVO);

	public List<OrdVO> getMoreByMember(String mem_no);

	public List<Ord_mealVO> getOrdMealsByOrdno(String ord_no);
	public void insertOrd(Map<String,OrdVO> ordlist, Map<String,List<Ord_mealVO>> itemlist);
	public List<OrdVO> getTopFiveStore();
	public void update_status(OrdVO ordVO);
	public Map<String,OrdVO> getMemOrd(String mem_no);
	public Map<String,Ord_mealVO> getMemOrdMeal(String ord_no);
	public void updateQrcodeStatus(String ord_no,int type);
	public byte[] getImage(String ord_no);
	public Map<String,OrdVO> getStoreOrd(String store_no);
	public void updatePaiedStatus(String ord_no);
	
	public int getUnconfirm(String store_no);
}
