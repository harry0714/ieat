package com.store_photo.model;

import java.util.List;

public interface Store_photoDAO_interface {
	public void insert(Store_photoVO store_photoVO); 
	public void update(Store_photoVO store_photoVO); 
	public void delete(String photo_no); 
	public Store_photoVO findByPrimaryKey(String photo_no); 
	public List<Store_photoVO> getAll();
	public Store_photoVO findFirstStore(String store_no);
	public List<Store_photoVO> getOneStorePhoto(String store_no);
	//後端用的方法
	public int countStorePhoto(String store_no);
	public Store_photoVO getOneStorePhoto2(String store_no, Integer rownum); 
}
