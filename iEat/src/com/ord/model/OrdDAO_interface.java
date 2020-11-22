package com.ord.model;


import java.util.List;
import java.util.Map;

import com.ord_meal.model.Ord_mealVO;

public interface OrdDAO_interface {
	public void insert(OrdVO ordVO);
	public void update(OrdVO ordVO);
	public void delete(String ordVO);
	public OrdVO findByPrimaryKey(String ordVO);
	public List<OrdVO> getAll();
	public List<OrdVO> getMoreByMember(String mem_no);
	//萬用複合查詢(傳入參數型態Map)(回傳 List)
//  public List<EmpVO> getAll(Map<String, String[]> map); 
	
	public List<Ord_mealVO> getOrdMealsByOrdno(String ord_no);
	public void insertOrd(Map<String,OrdVO> ordlist, Map<String,List<Ord_mealVO>> itemlist);
	public List<OrdVO> getTopFiveStore();
	public void update_status(OrdVO ordVO);
	
	public List<OrdVO> getMoreByOrdReportStatus(String ord_report_status);
	public List<OrdVO> getMoreByOrdReportStatus();
	public List<OrdVO> getAll(Map<String,String[]> map);
	public List<OrdVO> getMoreByStore(String store_no);
}
