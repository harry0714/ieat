package com.bookmark_store.model;

import java.util.List;

public class BSAppService {
	private BSAppDAO_interface dao = null;
	
	public BSAppService(){
		dao = new BSAppDAO();
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

	public List<String> getStoreNoByMemNo(String mem_no){
		return dao.findStoreNoByMemNo(mem_no);
	}
	
}
