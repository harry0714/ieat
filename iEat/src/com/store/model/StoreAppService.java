package com.store.model;

import java.sql.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.store_report.model.Store_reportVO;
import com.googleapis.maps.schema.GeoLocation;
import com.member.model.MemberVO;
import com.reservation.model.ReservationVO;
import com.store_photo.model.Store_photoVO;

public class StoreAppService {

	private StoreAppDAO_interface dao;

	public StoreAppService() {
		dao = new StoreAppDAO();
	}


	public StoreVO updateStore(String store_no, String store_id, String store_psw, String store_name,
			String store_phone, String store_owner, String store_intro, String store_ads, String store_type_no,
			String store_booking, String store_open, String store_book_amt, String store_email, String store_status,
			Double store_star, String store_latlng) {

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


	public StoreVO getOneStore(String store_no) {
		return dao.findByPrimaryKey(store_no);
	}


	public StoreVO getOneStoreIdAndPsw(String store_id, String store_psw) {
		return dao.getOneStoreIdAndPsw(store_id,store_psw);
	}

	public StoreVO getOneStoreByEmail(String store_email) {
		return dao.getOneStoreByEmail(store_email);
	}

	public List<StoreVO> getOpenStore() {
		return dao.getOpenStore();
	}

	// 取得一張照片
	public byte[] getStoreImg(String store_no) {
		return dao.getStoreImg(store_no);
	}

	// 取得一個OPEN店家
	public StoreVO getOneAvailableStore(String store_no) {
		return dao.getOneAvailableStore(store_no);
	}

	public List<StoreVO> getAllKeyword(String keyword) {
		return dao.getAllKeyword(keyword);
	}

	public void updateStoreKey(String store_no,String store_key){
		dao.updateStoreKey(store_no,store_key);
	}
	
	public String getStoreKey(String store_no){
		return dao.getStoreKey(store_no);
	}

	public List<StoreVO> getTopStores(){
		return dao.getTopStores();
	}
	
	public void updatePwd(String store_no,String new_pwd){
		dao.updatePwd(store_no,new_pwd);
	}
}
