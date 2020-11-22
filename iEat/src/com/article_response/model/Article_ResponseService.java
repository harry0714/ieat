package com.article_response.model;
import java.util.List;
import java.util.Set;

import com.article.model.ArticleVO;
import com.article.model.Article_dao;
import com.article.model.Articledao_interface;
import com.article_response_report.model.Article_rs_reVO;

public class Article_ResponseService {

	private Article_ResponseDAO_interface dao;
	
	public Article_ResponseService() {
		
		dao = new Article_Response_DAO();
	}
	public Article_ResponseVO addarticle_response(String art_no,
			 String mem_no,  String art_rs_context,java.sql.Timestamp art_rs_date) { //�`�N�j�p�g

		Article_ResponseVO article_responseVO = new Article_ResponseVO();

		article_responseVO.setArt_no(art_no);
		article_responseVO.setMem_no(mem_no);
		article_responseVO.setArt_rs_context(art_rs_context);
		article_responseVO.setArt_rs_date(art_rs_date);
		
		dao.insert(article_responseVO);

		return article_responseVO;
	}

	public Article_ResponseVO updatarticle_response(String art_rs_no,String art_no,String mem_no, String art_rs_context,
			  java.sql.Timestamp art_rs_date) {

		Article_ResponseVO article_responseVO= new Article_ResponseVO();
		
		article_responseVO.setArt_rs_no(art_rs_no);
		article_responseVO.setArt_no(art_no);
		article_responseVO.setMem_no(mem_no);
		article_responseVO.setArt_rs_context(art_rs_context);
		article_responseVO.setArt_rs_date(art_rs_date);
		
		dao.update(article_responseVO);

		return article_responseVO;
	}

	public void deletearticle_response(String art_rs_no) {
		dao.delete(art_rs_no);
	}



	public List<Article_ResponseVO> getAll() {
		return dao.getAll();	
	}
	public Article_ResponseVO getOneArticle_Response(String art_rs_no) {
		// TODO Auto-generated method stub
		return dao.findByPrimaryKey(art_rs_no);
	}
	public Set<Article_rs_reVO> getArticle_Responses_ReportByArt_rs_no(String art_rs_no) {
		// TODO Auto-generated method stub
		return dao.getArticleResponseReportByArt_rs_no(art_rs_no);
//		return null;
	}
}
