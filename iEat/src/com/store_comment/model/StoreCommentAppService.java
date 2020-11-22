package com.store_comment.model;

import java.util.List;
import java.util.Map;

public class StoreCommentAppService {
	private StoreCommentAppDAO_interface dao;
	
	public StoreCommentAppService() {
		dao = new StoreCommentAppDAO();
	}
	
	public StoreCommentVO addStoreComment(String store_no, String mem_no, String comment_conect, 
			java.sql.Timestamp comment_time, Double comment_level) {
		
		StoreCommentVO storeCommentVO = new StoreCommentVO();
		
		storeCommentVO.setStore_no(store_no);
		storeCommentVO.setMem_no(mem_no);
		storeCommentVO.setComment_conect(comment_conect);
		storeCommentVO.setComment_time(comment_time);
		storeCommentVO.setComment_level(comment_level);
		dao.insert(storeCommentVO);
		
		return storeCommentVO;
	}

	public Map<String,StoreCommentVO> getStoreComment(String store_no){
		return dao.getStoreComment(store_no);
	}
}
