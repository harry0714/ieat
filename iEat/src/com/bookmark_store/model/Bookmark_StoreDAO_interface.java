package com.bookmark_store.model;

import java.util.List;


public interface Bookmark_StoreDAO_interface {
	public void insert(Bookmark_StoreVO bookmark_storeVO);
	public void update(Bookmark_StoreVO bookmark_storeVO);
	public void delete(String mem_no,String store_no);
	public Bookmark_StoreVO findByPrimaryKey(String mem_no,String store_no);
	public List<Bookmark_StoreVO> getAll();
	public List<Bookmark_StoreVO> getMoreByMemNo(String mem_no);
	public List<String> findStoreNoByMemNo(String mem_no);
}
