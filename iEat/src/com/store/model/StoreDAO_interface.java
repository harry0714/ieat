package com.store.model;

import java.sql.Date;
import java.util.*;

import com.discount.model.DiscountVO;
import com.ord.model.OrdVO;
import com.reservation.model.ReservationVO;
import com.store_comment.model.StoreCommentVO;
import com.store_photo.model.Store_photoVO;
import com.store_report.model.Store_reportVO;

public interface StoreDAO_interface {
	public void insert(StoreVO StoreVO); 
	public void update(StoreVO StoreVO); 
	public void delete(String store_no);
	public StoreVO findByPrimaryKey(String store_no); 
	public List<StoreVO> getAll();
	//查詢出店家對應的店家照片(一對多) (回傳set)
	public Set<Store_photoVO> getStore_photosByStore_no(String store_no);
	public Set<Store_reportVO> getStore_reportByStore_no(String store_no);
	public Set<ReservationVO> getReservationByStore_no(String store_no);
	// 萬用複合查詢(傳入參數型態為Map) (回傳list) 
	public List<StoreVO> getAll(Map<String, String[]> map);
	// 登入使用 查詢帳號密碼是否正確
	public StoreVO getOneStoreIdAndPsw(String store_id);
	// 忘記帳號密碼使用  用email查詢 storeVO
	public StoreVO getOneStoreByEmail(String store_email);
	//取得店家狀態 
	public List<StoreVO> getOpenStore();
	// 取得單張照片的編號  此方法不再需要了  被 store_photo 內的getfindFirstStore 所取代 
	public List<DiscountVO> getDiscountByStoreNO(String store_no);
//	public String getFirstPhoto(String store_no);	
	public List<OrdVO> getMoreByStore(String store_no);
	public List<StoreVO> getMoreByStoreStatus(String store_status);
	public List<StoreVO> findByNot_reviewed();
	public void updatePass(String store_status, Date store_validate, String store_no);
	public List<StoreVO> findBy_reviewed();
	public StoreVO getOneStoreAndStoreStar(String store_no);
	public List<StoreCommentVO> getByStroeNo(String store_no); 
}
