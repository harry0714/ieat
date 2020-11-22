package com.store_comment.model;

import java.util.List;
import java.util.Map;


public interface StoreCommentAppDAO_interface {
	public void insert(StoreCommentVO storecommentVO);

	public Map<String,StoreCommentVO> getStoreComment(String store_no);
}
