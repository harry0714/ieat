package com.adm.model;

import java.sql.Date;

public class AdmVO implements java.io.Serializable {
	private String adm_no;
	private String adm_user;
	private String adm_psd;
	private String adm_name;
	private String adm_sex;
	private Date adm_bd;
	private String adm_email;
	private String adm_phone;
	private String adm_level;
	private String adm_addr;
	private byte[] adm_photo;
	public String getAdm_no() {
		return adm_no;
	}
	public void setAdm_no(String adm_no) {
		this.adm_no = adm_no;
	}
	public String getAdm_user() {
		return adm_user;
	}
	public void setAdm_user(String adm_user) {
		this.adm_user = adm_user;
	}
	public String getAdm_psd() {
		return adm_psd;
	}
	public void setAdm_psd(String adm_psd) {
		this.adm_psd = adm_psd;
	}
	public String getAdm_name() {
		return adm_name;
	}
	public void setAdm_name(String adm_name) {
		this.adm_name = adm_name;
	}
	public String getAdm_sex() {
		return adm_sex;
	}
	public void setAdm_sex(String adm_sex) {
		this.adm_sex = adm_sex;
	}
	public Date getAdm_bd() {
		return adm_bd;
	}
	public void setAdm_bd(Date adm_bd) {
		this.adm_bd = adm_bd;
	}
	public String getAdm_email() {
		return adm_email;
	}
	public void setAdm_email(String adm_email) {
		this.adm_email = adm_email;
	}
	
	
	public String getAdm_phone() {
		return adm_phone;
	}
	public void setAdm_phone(String adm_phone) {
		this.adm_phone = adm_phone;
	}
	public String getAdm_level() {
		return adm_level;
	}
	public void setAdm_level(String adm_level) {
		this.adm_level = adm_level;
	}
	public String getAdm_addr() {
		return adm_addr;
	}
	public void setAdm_addr(String adm_addr) {
		this.adm_addr = adm_addr;
	}
	public byte[] getAdm_photo() {
		return adm_photo;
	}
	public void setAdm_photo(byte[] adm_photo) {
		this.adm_photo = adm_photo;
	}
	
}
