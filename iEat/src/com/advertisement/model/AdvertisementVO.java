package com.advertisement.model;

import java.io.Serializable;
import java.sql.Date;

public class AdvertisementVO implements Serializable{
	private String ad_no;
	private byte[] ad_image;
	private String ad_imagetitle;
	private Date ad_startdate;
	private Date ad_enddate;
	private String store_no;
	
	public String getAd_no() {
		return ad_no;
	}
	public void setAd_no(String ad_no) {
		this.ad_no = ad_no;
	}
	public byte[] getAd_image() {
		return ad_image;
	}
	public void setAd_image(byte[] ad_image) {
		this.ad_image = ad_image;
	}
	public String getAd_imagetitle() {
		return ad_imagetitle;
	}
	public void setAd_imagetitle(String ad_imagetitle) {
		this.ad_imagetitle = ad_imagetitle;
	}
	public Date getAd_startdate() {
		return ad_startdate;
	}
	public void setAd_startdate(Date ad_startdate) {
		this.ad_startdate = ad_startdate;
	}
	public Date getAd_enddate() {
		return ad_enddate;
	}
	public void setAd_enddate(Date ad_enddate) {
		this.ad_enddate = ad_enddate;
	}
	public String getStore_no() {
		return store_no;
	}
	public void setStore_no(String store_no) {
		this.store_no = store_no;
	}

}
