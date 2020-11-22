package com.store_photo.model;

import java.util.List;

public class Store_photoService {
	private Store_photoDAO_interface dao; 
	
	public Store_photoService() {
		dao = new Store_photoDAO(); 
	}
	
	public Store_photoVO addStore_photo(String store_no, String photo_name, byte[] photo, String photo_des) { 
		Store_photoVO store_photoVO = new Store_photoVO(); 
		store_photoVO.setStore_no(store_no);
		store_photoVO.setPhoto_name(photo_name); 
		store_photoVO.setPhoto(photo);
		store_photoVO.setPhoto_des(photo_des); 
		dao.insert(store_photoVO); 
		return store_photoVO; 
	}
	//預留給Struts2用的 
	public void addStore_photo(Store_photoVO store_photoVO) {
		dao.insert(store_photoVO); 
	}
	public Store_photoVO updateStore_photo(String photo_no, String store_no, String photo_name, byte[] photo, String photo_des) {
		Store_photoVO store_photoVO = new Store_photoVO();
		store_photoVO.setPhoto_no(photo_no); 
		store_photoVO.setStore_no(store_no);
		store_photoVO.setPhoto_name(photo_name); 
		store_photoVO.setPhoto(photo);
		store_photoVO.setPhoto_des(photo_des); 
		dao.update(store_photoVO); 
		return store_photoVO; 
	}
	//預留給Struts2用的 
	public void updateStore_photo(Store_photoVO store_photoVO) {
		dao.update(store_photoVO); 
	}
	public void deleteStore_photo(String photo_no) {
		dao.delete(photo_no); 
	}
	public Store_photoVO getOneStore_photo(String photo_no) {
		return dao.findByPrimaryKey(photo_no); 
	}
	public List<Store_photoVO> getAll() {
		return dao.getAll(); 
	}

	public Store_photoVO getfindFirstStore(String store_no) {
		return dao.findFirstStore(store_no); 
	}
	public List<Store_photoVO> getOneStorePhoto(String store_no) {
		return dao.getOneStorePhoto(store_no); 
	}
	
	// 後端使用到的方法
	public int countStorePhoto(String store_no) {
		return dao.countStorePhoto(store_no); 
	}
	public Store_photoVO getOneStorePhoto(String store_no,Integer rownum) {
		return dao.getOneStorePhoto2(store_no,rownum); 
	}
}

