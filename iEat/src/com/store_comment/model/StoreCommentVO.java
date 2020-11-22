package com.store_comment.model;

import java.sql.Date;
import java.sql.Timestamp;

public class StoreCommentVO implements java.io.Serializable {
	private String comment_no;
	private String store_no;
	private String mem_no;
	private String comment_conect;
	//private Date comment_time;
	private Timestamp comment_time;
	private Double comment_level;
	
	
	public String getComment_no() {
		return comment_no;
	}
	public void setComment_no(String comment_no) {
		this.comment_no = comment_no;
	}
	public String getStore_no() {
		return store_no;
	}
	public void setStore_no(String store_no) {
		this.store_no = store_no;
	}
	public String getMem_no() {
		return mem_no;
	}
	public void setMem_no(String mem_no) {
		this.mem_no = mem_no;
	}
	public String getComment_conect() {
		return comment_conect;
	}
	public void setComment_conect(String comment_conect) {
		this.comment_conect = comment_conect;
	}
	public Timestamp getComment_time() {
		return comment_time;
	}
	public void setComment_time(Timestamp comment_time) {
		this.comment_time = comment_time;
	}
	public Double getComment_level() {
		return comment_level;
	}
	public void setComment_level(Double comment_level) {
		this.comment_level = comment_level;
	}
	
	
}
