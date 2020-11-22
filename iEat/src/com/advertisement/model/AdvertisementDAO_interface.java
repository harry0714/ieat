package com.advertisement.model;

import java.util.List;

public interface AdvertisementDAO_interface {
	public void insert(AdvertisementVO advertisementVO);
	public void update(AdvertisementVO advertisementVO);
	public void delete(String ad_no);
	public AdvertisementVO findByPrimaryKey(String ad_no);
	public List<AdvertisementVO> getAll();
	public List<AdvertisementVO> getSome(String stmt);
	public byte[] getAdImage(String ad_no);
	public List<AdvertisementVO> getRandom(int quantity); //隨機取得幾筆   結束日期大於今天   且 要有圖片
}
