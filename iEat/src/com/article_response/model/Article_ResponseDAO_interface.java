package com.article_response.model;

import java.util.List;
import java.util.Set;

import com.article_response_report.model.Article_rs_reVO;


public interface Article_ResponseDAO_interface {
    public void insert(Article_ResponseVO article_responseVO);
    public void update(Article_ResponseVO article_responseVO);
    public void delete(String art_rs_no);
    public Article_ResponseVO findByPrimaryKey(String art_rs_no);
    public List<Article_ResponseVO> getAll();
	Set<Article_rs_reVO> getArticleResponseReportByArt_rs_no(String art_rs_no);
	
	
}
