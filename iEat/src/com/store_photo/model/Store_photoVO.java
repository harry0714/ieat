package com.store_photo.model;

public class Store_photoVO implements java.io.Serializable{ 
	private String photo_no; 
	private String store_no; 
	private String photo_name;
	private byte[] photo; 
	private String photo_des;
	public String getPhoto_no() {
		return photo_no;
	}
	public void setPhoto_no(String photo_no) {
		this.photo_no = photo_no;
	}
	public String getStore_no() {
		return store_no;
	}
	public void setStore_no(String store_no) {
		this.store_no = store_no;
	}
	public String getPhoto_name() {
		return photo_name;
	}
	public void setPhoto_name(String photo_name) {
		this.photo_name = photo_name;
	}
	public byte[] getPhoto() {
		return photo;
	}
	public void setPhoto(byte[] photo) {
		this.photo = photo;
	}
	public String getPhoto_des() {
		return photo_des;
	}
	public void setPhoto_des(String photo_des) {
		this.photo_des = photo_des;
	} 
}
