package com.article_response_report.model;

import java.util.List;


public interface Article_rs_reDAO_interface {

    public void insert(Article_rs_reVO article_rs_reVO);
    public void update(Article_rs_reVO article_rs_reVO);
    public void delete(String art_rs_re_no);
    public Article_rs_reVO findByPrimaryKey(String art_rs_re_no);
    public List<Article_rs_reVO> getAll();
    public List<Article_rs_reVO> getMoreByArticleResponseReportStatus(String art_rs_re_sta);
	
}
