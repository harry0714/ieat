package com.advertisement.model;

import java.util.List;
import java.util.Map;

import com.advertisement.tools.AdStatusType;
import com.store.model.StoreVO;

public class AdAppService {
	private AdAppDAO_interface dao;
	
	public AdAppService(){
		dao = new AdAppDAO();
	}
	
	public byte[] getAdImage(String ad_no){
		return dao.getAdImage(ad_no);
	}
	
	public Map<String,String> getCurrentAd(){
		return dao.getCurrentAd();
	}
}
