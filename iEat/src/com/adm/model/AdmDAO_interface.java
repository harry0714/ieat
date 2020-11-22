package com.adm.model;

import java.util.*;

public interface AdmDAO_interface {
          public void insert(AdmVO admVO);
          public void update(AdmVO admVO);
          public void delete(String admVO);
          public AdmVO findByPrimaryKey(String admVO);
          public List<AdmVO> getAll();
          //萬用複合查詢(傳入參數型態Map)(回傳 List)
        //public List<AdmVO> getAll(Map<String, String[]> map);
		public AdmVO findByUser(String adm_user);
		public byte[] getAdmImage(String adm_no);
		
}
