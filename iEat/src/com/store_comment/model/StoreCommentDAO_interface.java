package com.store_comment.model;

import java.util.List;


public interface StoreCommentDAO_interface {
	public void insert(StoreCommentVO storecommentVO);
	public void update(StoreCommentVO storecommentVO);
	public void delete(String comment_no);
	public StoreCommentVO findByPrimaryKey(String comment_no);
	public List<StoreCommentVO> getAll();
}
