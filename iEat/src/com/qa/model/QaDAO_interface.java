package com.qa.model;

import java.util.*;

public interface QaDAO_interface {
          public void insert(QaVO qaVO);
          public void update(QaVO qaVO);
          public void delete(String qaVO);
          public QaVO findByPrimaryKey(String qaVO);
          public List<QaVO> getAll();
          //萬用複合查詢(傳入參數型態Map)(回傳 List)
        //public List<AdmVO> getAll(Map<String, String[]> map);
		
}
