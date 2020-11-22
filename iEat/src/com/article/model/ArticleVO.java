package com.article.model;

import java.sql.Date;
import java.sql.Timestamp;
public class ArticleVO implements java.io.Serializable{
	
	private String art_no;
	private String art_name;
	private Timestamp art_date;
	private String art_context;
	private String mem_no;
	

	public Timestamp getArt_date() {
		return art_date;
	}
	public void setArt_date(Timestamp art_date) {
		this.art_date = art_date;
	}
	public String getArt_no() {
		return art_no;
	}
	public void setArt_no(String art_no) {
		this.art_no = art_no;
	}
	public String getArt_name() {
		return art_name;
	}
	public void setArt_name(String art_name) {
		this.art_name = art_name;
	}

	public String getArt_context() {
		return art_context;
	}
	public void setArt_context(String art_context) {
		this.art_context = art_context;
	}


	
	public String getMem_no() {
		return mem_no;
	}
	public void setMem_no(String mem_no) {
		this.mem_no = mem_no;
	}

}
	
	

