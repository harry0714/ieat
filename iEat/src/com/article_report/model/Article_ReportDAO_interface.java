package com.article_report.model;
import java.util.List;



public interface Article_ReportDAO_interface {

    public void insert(Article_reportVO article_reportVO);
    public void update(Article_reportVO article_reportVO);
    public void delete(String art_re_no);
    public Article_reportVO findByPrimaryKey(String art_re_no);
    public List<Article_reportVO> getAll();
    public List<Article_reportVO> getMoreByArticleReposeStatus(String art_re_status);
	
	
	
}
