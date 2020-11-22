package com.store_type.model;

import java.util.List;

public class Store_typeService {

	private Store_typeDAO_interface dao;

	public Store_typeService() {
		dao = new Store_typeDAO();
	}

	public Store_typeVO addStore_type(String store_type_no, String store_type_name) {

		Store_typeVO store_typeVO = new Store_typeVO();

		store_typeVO.setStore_type_no(store_type_no);
		store_typeVO.setStore_type_name(store_type_name);
		
		dao.insert(store_typeVO);

		return store_typeVO;
	}

	public Store_typeVO updateStore_type(String store_type_no, String store_type_name) {

		Store_typeVO store_typeVO = new Store_typeVO();

		store_typeVO.setStore_type_no(store_type_no);
		store_typeVO.setStore_type_name(store_type_name);
		dao.update(store_typeVO);

		return store_typeVO;
	}

	public void deleteStore_type(String store_type_no) {
		dao.delete(store_type_no);
	}

	public Store_typeVO getOneStore_type(String store_type_no) {
		return dao.findByPrimaryKey(store_type_no);
	}

	public List<Store_typeVO> getAll() {
		return dao.getAll();
	}
}
