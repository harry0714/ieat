package com.article.model;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.article_report.model.Article_reportVO;
import com.article_response.model.Article_ResponseVO;

public interface Articledao_interface {
          public void insert(ArticleVO articleVO);
          public void update(ArticleVO articleVO);
          public void delete(String art_no);
          public ArticleVO findByPrimaryKey(String art_no);
          public Set<Article_ResponseVO> getArticleResponsesByArt_NO(String art_no);
          //�U�νƦX�d��(�ǤJ�Ѽƫ��AMap)(�^�� List)
//        public List<EmpVO> getAll(Map<String, String[]> map); 
          public List<ArticleVO> getAll();
          public List<ArticleVO> getAll(Map<String, String[]> map);
          public  Set<Article_reportVO> getArticleReportsByArt_NO(String art_no);
		public String getImage(String art_no);
		public List<ArticleVO> getRandom();
		public	String getContext(String art_no,int count);
}

	
	
	

