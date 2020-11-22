package com.store_type.model;

import java.util.List;


public interface Store_typeDAO_interface {
	 public void insert(Store_typeVO store_typeVO);
     public void update(Store_typeVO store_typeVO);
     public void delete(String store_typeVO);
     public Store_typeVO findByPrimaryKey(String store_typeVO);
     public List<Store_typeVO> getAll();
     //萬用複合查詢(傳入參數型態Map)(回傳 List)
   //public List<Store_typeVo> getAll(Map<String, String[]> map);

}
