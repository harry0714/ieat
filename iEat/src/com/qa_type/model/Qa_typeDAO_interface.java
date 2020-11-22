package com.qa_type.model;

import java.util.*;

public interface Qa_typeDAO_interface {
          public void insert(Qa_typeVO qa_typeVO);
          public void update(Qa_typeVO qa_typeVO);
          public void delete(String qa_typeVO);
          public Qa_typeVO findByPrimaryKey(String qa_typeVO);
          public List<Qa_typeVO> getAll();
          //萬用複合查詢(傳入參數型態Map)(回傳 List)
        //public List<AdmVO> getAll(Map<String, String[]> map);
		
}
