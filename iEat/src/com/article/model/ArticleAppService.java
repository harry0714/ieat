package com.article.model;
import java.sql.Timestamp;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.article.model.ArticleVO;

public class ArticleAppService {

	private ArticleAppDAO_interface dao;
	
	public ArticleAppService() {
		dao = new ArticleAppDAO();
	}
	
	public byte[] getArticelImageByArt_no(String art_no){
		return dao.getImage(art_no);
	}
	
	public Map<String,ArticleVO> getAllWithName(){
		return dao.getAllWithName();
	}
}

