package com.store_report.model;

import java.util.List; 

public class Store_reportService {
	
	private Store_reportDAO_interface dao; 
	
	public Store_reportService() {
		dao = new Store_reportDAO(); 
	}
	
	public Store_reportVO addStore_report(String store_no, String mem_no, 
																					 String store_report_content, String store_report_status) {
		Store_reportVO store_reportVO = new Store_reportVO(); 

		store_reportVO.setStore_no(store_no);
		store_reportVO.setMem_no(mem_no); 
		store_reportVO.setStore_report_content(store_report_content);
		store_reportVO.setStore_report_status(store_report_status); 
		dao.insert(store_reportVO);
		return store_reportVO; 
	}
	
	// 預留給Struts2用的
	public void addStore_report(Store_reportVO store_reportVO) {
		dao.insert(store_reportVO);
	}
	
	public  Store_reportVO udpateStore_report(String store_report_no, String store_no, String mem_no, 
			 String store_report_content, java.sql.Date store_report_date, String store_report_status) {
		
		Store_reportVO store_reportVO = new Store_reportVO(); 
		store_reportVO.setStore_report_no(store_report_no); 
		store_reportVO.setStore_no(store_no);
		store_reportVO.setMem_no(mem_no); 
		store_reportVO.setStore_report_content(store_report_content);
		store_reportVO.setStore_report_date(store_report_date); 
		store_reportVO.setStore_report_status(store_report_status);
		dao.update(store_reportVO);
		return store_reportVO; 
	}
	// 預留給Struts2 用的 
	public void updateStore_report(Store_reportVO store_reportVO) {
		dao.update(store_reportVO);
	}
	public void deleteStore_report(String store_report_no) {
		dao.delete(store_report_no);
	}
	public Store_reportVO getOneStore_report(String store_report_no) {
		return dao.findByPrimaryKey(store_report_no); 	
	}
	public List<Store_reportVO> getAll() {
		return dao.getAll(); 
	}
	public List<Store_reportVO> getMoreByStoreReportStatus(String store_report_status){
		return dao.getMoreByStoreReportStatus(store_report_status);
	}
}
