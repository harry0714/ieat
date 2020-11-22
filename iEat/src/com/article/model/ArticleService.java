package com.article.model;

import java.util.List;
import java.util.Map;
import java.util.Set;

import com.article.model.ArticleVO;
import com.article_report.model.Article_reportVO;
import com.article_response.model.Article_ResponseVO;

public class ArticleService {

	private Articledao_interface dao;
	
	public ArticleService() {
		dao = new Article_dao();
	}
	
	public ArticleVO addarticle(String art_name, java.sql.Timestamp art_date, String art_context
			, String mem_no) { 

		ArticleVO articleVO = new ArticleVO();

		articleVO.setArt_date(art_date);
		articleVO.setArt_name(art_name);
		articleVO.setArt_context(art_context);
		articleVO.setMem_no(mem_no);
		
		dao.insert(articleVO);

		return articleVO;
	}

	public ArticleVO updatarticle(String art_no, String art_name, java.sql.Timestamp art_date, String art_context,
		 String mem_no) {

		ArticleVO articleVO = new ArticleVO();
		
		articleVO.setArt_no(art_no);
		articleVO.setArt_name(art_name);
		articleVO.setArt_date(art_date);
		
		articleVO.setArt_context(art_context);
		articleVO.setMem_no(mem_no);
		
		dao.update(articleVO);

		return articleVO;
	}

	public void deleteArticle(String art_no) {
		dao.delete(art_no);
	}

	public ArticleVO getOneArticle(String art_no) {
		return dao.findByPrimaryKey(art_no);
	}
	

	public List<ArticleVO> getAll() {
		return dao.getAll();
	}
	public List<ArticleVO> getAll(Map<String, String[]> map) {
		return dao.getAll(map);
	}
	
	public Set<Article_ResponseVO> getArticle_ResponsesByArt_no(String art_no) {
		return dao.getArticleResponsesByArt_NO(art_no);
	}

	public Set<Article_reportVO> getArticle_ReportsByArt_no(String art_no) {
		// TODO Auto-generated method stub
		return dao.getArticleReportsByArt_NO(art_no);
	}
	public String getArticelImageByArt_no(String art_no){
		return dao.getImage(art_no);
	}
	public List<ArticleVO> getRandom(){
		return dao.getRandom();
	}
	public	String getContext(String art_no,int count){
		return dao.getContext(art_no,count);
	}
}

