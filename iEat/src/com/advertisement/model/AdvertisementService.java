package com.advertisement.model;

import java.util.List;

import com.advertisement.tools.AdStatusType;

public class AdvertisementService {
	private AdvertisementDAO_interface dao;
	
	public AdvertisementService(){
		dao = new AdvertisementDAO();
	}
	
	public AdvertisementVO addAdvertisement(byte[] ad_image, String ad_imagetitle, java.sql.Date ad_startdate, java.sql.Date ad_enddate, String store_no){
		AdvertisementVO advertisementVO = new AdvertisementVO();

		advertisementVO.setAd_image(ad_image);
		advertisementVO.setAd_imagetitle(ad_imagetitle);
		advertisementVO.setAd_startdate(ad_startdate);
		advertisementVO.setAd_enddate(ad_enddate);
		advertisementVO.setStore_no(store_no);		
		dao.insert(advertisementVO);
		
		return advertisementVO;
	}
	
	public AdvertisementVO updateAdvertisement(String ad_no, byte[] ad_image, String ad_imagetitle, java.sql.Date ad_startdate, java.sql.Date ad_enddate, String store_no){
		AdvertisementVO advertisementVO = new AdvertisementVO();

		advertisementVO.setAd_no(ad_no);
		advertisementVO.setAd_image(ad_image);
		advertisementVO.setAd_imagetitle(ad_imagetitle);
		advertisementVO.setAd_startdate(ad_startdate);
		advertisementVO.setAd_enddate(ad_enddate);
		advertisementVO.setStore_no(store_no);
		dao.update(advertisementVO);
		
		return advertisementVO;
	}
	
	public void deleteAdvertisement(String ad_no){
		dao.delete(ad_no);
	}
	
	public AdvertisementVO getOneAdvertisement(String ad_no){
		return dao.findByPrimaryKey(ad_no);
	}
	
	public List<AdvertisementVO> getAll(){
		return dao.getAll();
	}
	
	public List<AdvertisementVO> getSome(int status){
		return dao.getSome(AdStatusType.getStmt(status));
	}
	
	public byte[] getAdImage(String ad_no){
		return dao.getAdImage(ad_no);
	}
	public List<AdvertisementVO> getRandom(int quantity){
		return dao.getRandom(quantity);
	}
}
