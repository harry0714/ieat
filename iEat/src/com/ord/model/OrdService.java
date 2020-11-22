package com.ord.model;

import java.util.List;
import java.util.Map;

import com.ord_meal.model.Ord_mealVO;

import java.sql.Timestamp;

public class OrdService {
	private OrdDAO_interface dao;

	public OrdService() {
		dao = new OrdDAO();
	}

	public OrdVO addOrd(String mem_no, String store_no, Timestamp ord_date, Timestamp ord_pickup,
			String ord_qrcode_status, Integer ord_paid, String ord_report, String ord_report_status) {

		OrdVO ordVO = new OrdVO();

		ordVO.setMem_no(mem_no);
		ordVO.setStore_no(store_no);
		ordVO.setOrd_date(ord_date);
		ordVO.setOrd_pickup(ord_pickup);
		ordVO.setOrd_qrcode_status(ord_qrcode_status);
		ordVO.setOrd_paid(ord_paid);
		ordVO.setOrd_report(ord_report);
		ordVO.setOrd_report_status(ord_report_status);
		return ordVO;
	}

	public OrdVO updateOrd(String mem_no, String store_no, Timestamp ord_date, Timestamp ord_pickup,
			String ord_qrcode_status, Integer ord_paid, String ord_report, String ord_report_status, String ord_no) {
		OrdVO ordVO = new OrdVO();
		ordVO.setMem_no(mem_no);
		ordVO.setStore_no(store_no);
		ordVO.setOrd_date(ord_date);
		ordVO.setOrd_pickup(ord_pickup);
		ordVO.setOrd_qrcode_status(ord_qrcode_status);
		ordVO.setOrd_paid(ord_paid);
		ordVO.setOrd_report(ord_report);
		ordVO.setOrd_report_status(ord_report_status);
		ordVO.setOrd_no(ord_no);
		dao.update(ordVO);
		return ordVO;
	}
	

	public void deleteOrd(String ord_no) {
		dao.delete(ord_no);
	}

	public OrdVO getOneMeal(String ord_no) {
		return dao.findByPrimaryKey(ord_no);
	}

	public List<OrdVO> getAll() {
		return dao.getAll();
	}
	
	public List<OrdVO> getMoreByMember(String mem_no){
		return dao.getMoreByMember(mem_no);
	}
	
	public List<Ord_mealVO> getOrdMealsByOrdno(String ord_no){
		return dao.getOrdMealsByOrdno(ord_no);
	}
	public void insertOrd(Map<String,OrdVO> ordlist, Map<String,List<Ord_mealVO>> itemlist){
		dao.insertOrd(ordlist,itemlist);
	}
	public List<OrdVO> getTopFiveStore() {
		return dao.getTopFiveStore();
	}
	public OrdVO update_status(String ord_qrcode_status, String ord_no){
		OrdVO ordVO = new OrdVO();
		ordVO.setOrd_qrcode_status(ord_qrcode_status);
		ordVO.setOrd_no(ord_no);
		dao.update_status(ordVO);
		return ordVO;
	}
	public List<OrdVO> getgetMoreByOrderReportStatus(String ord_report_status){
		return dao.getMoreByOrdReportStatus(ord_report_status);
	}
	public List<OrdVO> getgetMoreByOrderReportStatus(){
		return dao.getMoreByOrdReportStatus();
	}
	public List<OrdVO> getAll(Map<String,String[]> map){
		return dao.getAll(map);
	}
	//取得店家訂餐資訊
	public List<OrdVO> getMoreByStore(String store_no){
		return dao.getMoreByStore(store_no);
	}
}

