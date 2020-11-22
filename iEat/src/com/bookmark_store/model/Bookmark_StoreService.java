package com.bookmark_store.model;

import java.util.List;

public class Bookmark_StoreService {
	private Bookmark_StoreDAO_interface dao = null;
	
	public Bookmark_StoreService(){
		dao = new Bookmark_StoreDAO();
	}
	
	public Bookmark_StoreVO addBookmark_Store(String mem_no,String store_no){
		Bookmark_StoreVO bookmark_storeVO = new Bookmark_StoreVO();
		bookmark_storeVO.setMem_no(mem_no);
		bookmark_storeVO.setStore_no(store_no);
		dao.insert(bookmark_storeVO);
		return bookmark_storeVO;
	}
	public void deleteBookmark_Store(String mem_no,String store_no){
		dao.delete(mem_no, store_no);
	}
	public Bookmark_StoreVO getOneBookmark_Store(String mem_no,String store_no){
		return dao.findByPrimaryKey(mem_no, store_no);
	}
	public List<Bookmark_StoreVO> getAll(){
		return dao.getAll();
	}
	public List<Bookmark_StoreVO> getMoreByMemNo(String mem_no){
		return dao.getMoreByMemNo(mem_no);
	}
	public List<String> getStoreNoByMemNo(String mem_no){
		return dao.findStoreNoByMemNo(mem_no);
	}
	
}
