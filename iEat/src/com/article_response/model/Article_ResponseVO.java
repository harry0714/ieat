package com.article_response.model;

import java.sql.Timestamp;

public class Article_ResponseVO implements  java.io.Serializable {
	
	private String art_rs_no;
	private String art_no;
	private String art_rs_context;
	private String mem_no;
	private Timestamp art_rs_date;
	
	
	
	public String getMem_no() {
		return mem_no;
	}
	public void setMem_no(String mem_no) {
		this.mem_no = mem_no;
	}
	public String getArt_rs_no() {
		return art_rs_no;
	}
	public void setArt_rs_no(String art_rs_no) {
		this.art_rs_no = art_rs_no;
	}
	public String getArt_no() {
		return art_no;
	}
	public void setArt_no(String art_no) {
		this.art_no = art_no;
	}
	public String getArt_rs_context() {
		return art_rs_context;
	}
	public void setArt_rs_context(String art_rs_context) {
		this.art_rs_context = art_rs_context;
	}

	public Timestamp getArt_rs_date() {
		return art_rs_date;
	}
	public void setArt_rs_date(Timestamp art_rs_date) {
		this.art_rs_date = art_rs_date;
	}
	
}
