package com.store.model;

import java.util.*;

import com.reservation.model.ReservationVO;
import com.store_photo.model.Store_photoVO;
import com.store_report.model.Store_reportVO;

public interface StoreAppDAO_interface {

	public void update(StoreVO StoreVO); 

	public StoreVO findByPrimaryKey(String store_no); 

	// 登入使用 查詢帳號密碼是否正確
	public StoreVO getOneStoreIdAndPsw(String store_id,String store_psw);
	// 忘記帳號密碼使用  用email查詢 storeVO
	public StoreVO getOneStoreByEmail(String store_email);
	//取得店家狀態 
	public List<StoreVO> getOpenStore(); 
	
	//取得一張店家照片
	public byte[] getStoreImg(String store_no);
	//取得一家open店
	public StoreVO getOneAvailableStore(String store_no);
	
	//餐廳關鍵字搜尋
	public List<StoreVO> getAllKeyword(String keyword);
	
	public void updateStoreKey(String store_no,String store_key);
	public String getStoreKey(String store_no);
	public List<StoreVO> getTopStores();
	
	public void updatePwd(String store_no,String new_pwd);
}
