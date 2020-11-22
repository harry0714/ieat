package com.article_response_report.model;

import java.util.List;

import com.article_response.model.Article_ResponseDAO_interface;
import com.article_response.model.Article_ResponseVO;
import com.article_response.model.Article_Response_DAO;

public class Article_rs_reService {

	private Article_rs_reDAO_interface dao;
	
	public Article_rs_reService() {
		
		dao = new Article_rs_reDAO();
	}
	public Article_rs_reVO addarticle_rs_re(String mem_no ,String art_rs_no, java.sql.Timestamp art_rs_date, String art_rs_re_con,
		String art_rs_re_sta ) { //�`�N�j�p�g

		Article_rs_reVO article_rs_reVO = new Article_rs_reVO();

		article_rs_reVO.setMem_no(mem_no);
		article_rs_reVO.setArt_rs_no(art_rs_no);
		article_rs_reVO.setArt_rs_re_date(art_rs_date);
		article_rs_reVO.setArt_rs_re_con(art_rs_re_con);
		article_rs_reVO.setArt_rs_re_sta(art_rs_re_sta);	
		
		dao.insert(article_rs_reVO);

		return article_rs_reVO;
	}

	public Article_rs_reVO updatart_rs_re(String mem_no,String art_rs_no , java.sql.Timestamp art_rs_date, String art_rs_re_con,
			String art_rs_re_sta ,String art_rs_re_no) {

		Article_rs_reVO article_rs_reVO= new Article_rs_reVO();
		
		article_rs_reVO.setArt_rs_re_no(art_rs_re_no);
		
		article_rs_reVO.setArt_rs_no(art_rs_no);
		article_rs_reVO.setMem_no(mem_no);
		article_rs_reVO.setArt_rs_re_date(art_rs_date);
		article_rs_reVO.setArt_rs_re_con(art_rs_re_con);
		article_rs_reVO.setArt_rs_re_sta(art_rs_re_sta);
		
		dao.update(article_rs_reVO);

		return article_rs_reVO;
	}

	public void delete_article_rs_re(String  art_rs_re_no) {
		dao.delete( art_rs_re_no);
	}



	public List<Article_rs_reVO> getAll() {
		return dao.getAll();
	}
	public Article_rs_reVO getOneArticle_RS_RE(String art_rs_re_no) {
		// TODO Auto-generated method stub
		return dao.findByPrimaryKey(art_rs_re_no);
	}
	public List<Article_rs_reVO> getMoreByArticelResponseReportStatus(String art_rs_re_sta) {
		return dao.getMoreByArticleResponseReportStatus(art_rs_re_sta);
	}
}
