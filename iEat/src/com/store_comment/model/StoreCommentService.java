package com.store_comment.model;

import java.util.List;

public class StoreCommentService {
	private StoreCommentDAO_interface dao;
	
	public StoreCommentService() {
		dao = new StoreCommentDAO();
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
	
	public StoreCommentVO updateStoreComment(String comment_no, String store_no, String mem_no, String comment_conect, 
			java.sql.Timestamp comment_time, Double comment_level ) {
		
		StoreCommentVO storecommentVO = new StoreCommentVO();
		
		storecommentVO.setComment_no(comment_no);
		storecommentVO.setStore_no(store_no);
		storecommentVO.setMem_no(mem_no);
		storecommentVO.setComment_conect(comment_conect);
		storecommentVO.setComment_time(comment_time);
		storecommentVO.setComment_level(comment_level);
		dao.update(storecommentVO);
		
		return storecommentVO;
	}
	
	public void deleteStoreComment(String comment_no) {
		dao.delete(comment_no);
	}
	
	public StoreCommentVO getOneStoreComment(String comment_no) {
		return dao.findByPrimaryKey(comment_no);
	} 
	
	public List<StoreCommentVO> getAll() {
		return dao.getAll();
	}
	
}
