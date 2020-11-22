package com.advertisement.model;

import java.util.List;
import java.util.Map;

import com.store.model.StoreVO;

public interface AdAppDAO_interface {
	public byte[] getAdImage(String ad_no);
	public Map<String,String> getCurrentAd();
}
