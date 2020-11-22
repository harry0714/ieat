package com.ord.model;

import java.util.List;
import java.util.Map;

import com.ord_meal.model.Ord_mealVO;

import java.sql.Timestamp;

public class OrdAppService {
	private OrdAppDAO_interface dao;

	public OrdAppService() {
		dao = new OrdAppDAO();
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
	

	public OrdVO getOneOrd(String ord_no) {
		return dao.findByPrimaryKey(ord_no);
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
	public Map<String,OrdVO> getMemOrd(String mem_no){
		return dao.getMemOrd(mem_no);
	}
	public Map<String,Ord_mealVO> getMemOrdMeal(String ord_no){
		return dao.getMemOrdMeal(ord_no);
	}
	public void updateQrcodeStatus(String ord_no,int type){
		dao.updateQrcodeStatus(ord_no,type);
	}
	public byte[] getQrcode(String ord_no){
		return dao.getImage(ord_no);
	}
	public Map<String,OrdVO> getStoreOrd(String store_no){
		return dao.getStoreOrd(store_no);
	}
	public void updatePaiedStatus(String ord_no){
		dao.updatePaiedStatus(ord_no);
	}
	public int getUnconfirm(String store_no){
		return dao.getUnconfirm(store_no);
	}
}

