package com.store.model;

import java.sql.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.store_report.model.Store_reportVO;
import com.discount.model.DiscountVO;
import com.ord.model.OrdVO;
import com.reservation.model.ReservationVO;
import com.store_comment.model.StoreCommentVO;
import com.store_photo.model.Store_photoVO;

public class StoreService {
	
	private StoreDAO_interface dao;
	
	public StoreService() {
		dao = new StoreDAO(); 
	}
	
	public StoreVO addStore(String store_id, String store_psw,
			String store_name, String store_phone, String store_owner, String store_intro, String store_ads,  
			String store_type_no, String store_booking, String store_open, String store_book_amt, String store_email, String store_latlng) {

		StoreVO storeVO = new StoreVO();

		storeVO.setStore_id(store_id);
		storeVO.setStore_psw(store_psw);
		storeVO.setStore_name(store_name);
		storeVO.setStore_phone(store_phone);
		storeVO.setStore_owner(store_owner);
		storeVO.setStore_intro(store_intro);
		storeVO.setStore_ads(store_ads);
		storeVO.setStore_type_no(store_type_no);
		storeVO.setStore_booking(store_booking);
		storeVO.setStore_open(store_open);
		storeVO.setStore_book_amt(store_book_amt);
		storeVO.setStore_email(store_email);
		storeVO.setStore_latlng(store_latlng);
		dao.insert(storeVO);

		return storeVO;
	}
	// 預留給Struts2 用的 
	public void addStore(StoreVO storeVO) {
		dao.insert(storeVO);
	}
	
	public StoreVO updateStore(String store_no, String store_id, String store_psw,
			String store_name, String store_phone, String store_owner, String store_intro, String store_ads, 
			String store_type_no, String store_booking, String store_open, String store_book_amt, String store_email, String store_status, Double store_star, String store_latlng) {

		StoreVO storeVO = new StoreVO();

		storeVO.setStore_no(store_no);
		storeVO.setStore_id(store_id);
		storeVO.setStore_psw(store_psw);
		storeVO.setStore_name(store_name);
		storeVO.setStore_phone(store_phone);
		storeVO.setStore_owner(store_owner);
		storeVO.setStore_intro(store_intro);
		storeVO.setStore_ads(store_ads);
		storeVO.setStore_type_no(store_type_no);
		storeVO.setStore_booking(store_booking);
		storeVO.setStore_open(store_open);
		storeVO.setStore_book_amt(store_book_amt);
		storeVO.setStore_email(store_email);
		storeVO.setStore_status(store_status);
		storeVO.setStore_star(store_star);
		storeVO.setStore_latlng(store_latlng);
		dao.update(storeVO);

		return storeVO;
	}
	// 預留給Struts2 用的
	public void updateStore(StoreVO storeVO) {
		dao.update(storeVO);
	}
	
	public void deleteStoreVO(String store_no) {
		dao.delete(store_no); 
	}
	public StoreVO getOneStore(String store_no) {
		return dao.findByPrimaryKey(store_no);
	}
	public List<StoreVO> getAll() {
		return dao.getAll();
	}
	// 查詢店家照片資料 
	public Set<Store_photoVO> getStore_photosByStore_no(String store_no) {
		return dao.getStore_photosByStore_no(store_no); 
	}
	// 查詢店家檢舉資料
	public Set<Store_reportVO> getStore_reportByStore_no(String store_no) {
		return dao.getStore_reportByStore_no(store_no);
	}
	// 查詢店家訂位紀錄
	public Set<ReservationVO> getReservationByStore_no(String store_no) {
		return dao.getReservationByStore_no(store_no);
	}

	public List<StoreVO> getAll(Map<String, String[]> map) {
		return dao.getAll(map);
	}

	public StoreVO getOneStoreIdAndPsw(String store_id) {
		return dao.getOneStoreIdAndPsw(store_id);
	}

	public StoreVO getOneStoreByEmail(String store_email) {
		return dao.getOneStoreByEmail(store_email);
	}
	public List<StoreVO> getOpenStore() {
		return dao.getOpenStore();
	}	
	// 不需要了  已經被其他的方法所取代  store_photoSvc.getfindFirstStore
//	public String getFirstPhoto(String store_no) {
//		return dao.getFirstPhoto(store_no); 
//	}
	// 取得店家的評分
	public StoreVO getOneStoreAndStoreStar(String store_no) {
		return dao.getOneStoreAndStoreStar(store_no);
	}
	public List<DiscountVO> getDiscountByStoreNo(String store_no){
		return dao.getDiscountByStoreNO(store_no);
	}
/******************* 這邊定義後端會用到的方法 **********************/ 
	public List<OrdVO> getMoreByStore(String store_no) {
		return dao.getMoreByStore(store_no);
	}

	public List<StoreVO> getMoreByStoreStatus(String store_status){
		return dao.getMoreByStoreStatus(store_status);
	}
	public List<StoreVO> findByNot_reviewed() {
				return dao.findByNot_reviewed();
		}
	public StoreVO updateStoreStatus(String store_status,Date  store_validate, String store_no) {
		dao.updatePass(store_status, store_validate,store_no);
		return dao.findByPrimaryKey(store_no);
	}
	public List<StoreVO> findBy_reviewed() {
		return dao.findBy_reviewed();
	}

	public List<StoreCommentVO> getByStoreNo(String store_no) {
		return dao.getByStroeNo(store_no);
	}
}
