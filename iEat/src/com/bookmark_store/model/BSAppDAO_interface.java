package com.bookmark_store.model;

import java.util.List;


public interface BSAppDAO_interface {
	public void insert(Bookmark_StoreVO bookmark_storeVO);
	public void delete(String mem_no,String store_no);	
	public List<String> findStoreNoByMemNo(String mem_no);
}
