package com.article_report.model;

import java.util.List;




public class Article_ReportService {

	private Article_ReportDAO_interface dao;
	
	public Article_ReportService() {
		
		dao = new article_report_dao();
	
	}

	public Article_reportVO addarticle_report(String art_no,
			 String mem_no , String art_re_context, java.sql.Timestamp art_re_date , String art_re_status) { //�`�N�j�p�g

		Article_reportVO article_reportVO = new Article_reportVO();

		article_reportVO.setArt_re_date(art_re_date);
		article_reportVO.setArt_no(art_no);
		article_reportVO.setArt_re_context(art_re_context);
		article_reportVO.setMem_no(mem_no);
		article_reportVO.setArt_re_status(art_re_status);
		
		
		dao.insert(article_reportVO);

		return article_reportVO;
	}

	public Article_reportVO updatarticle_report(String art_re_no,String art_no, java.sql.Timestamp art_re_date, String art_re_context,
			 String mem_no, String art_re_status) {

		Article_reportVO article_reportVO= new Article_reportVO();
		
		article_reportVO.setArt_re_no(art_re_no);
		article_reportVO.setArt_re_date(art_re_date);
		article_reportVO.setArt_no(art_no);
		article_reportVO.setArt_re_context(art_re_context);
		article_reportVO.setMem_no(mem_no);
		article_reportVO.setArt_re_status(art_re_status);
		
		dao.update(article_reportVO);

		return article_reportVO;
	}

	public void deletearticle(String art_re_no) {
		dao.delete(art_re_no);
	}

	public Article_reportVO getOnearticle(String art_re_no) {
		return dao.findByPrimaryKey(art_re_no);
	}

	public List<Article_reportVO> getAll() {
		return dao.getAll();
	}
	
	public List<Article_reportVO> getMoreByArticleReportStatus(String art_re_status){
		return dao.getMoreByArticleReposeStatus(art_re_status);
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
}
