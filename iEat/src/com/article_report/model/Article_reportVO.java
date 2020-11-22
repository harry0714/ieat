package com.article_report.model;
import java.sql.Date;
import java.sql.Timestamp;

public class Article_reportVO implements java.io.Serializable {
	
	
	private String art_re_no;
	private String art_no;
	private String mem_no;
	private String art_re_context;
	private Timestamp art_re_date;
	private String art_re_status;
	
	public String getArt_re_no() {
		return art_re_no;
	}
	public void setArt_re_no(String art_re_no) {
		this.art_re_no = art_re_no;
	}
	public String getArt_no() {
		return art_no;
	}
	public void setArt_no(String art_no) {
		this.art_no = art_no;
	}
	public String getMem_no() {
		return mem_no;
	}
	public void setMem_no(String mem_no) {
		this.mem_no = mem_no;
	}
	public String getArt_re_context() {
		return art_re_context;
	}
	public void setArt_re_context(String art_re_context) {
		this.art_re_context = art_re_context;
	}
	
	public Timestamp getArt_re_date() {
		return art_re_date;
	}
	public void setArt_re_date(Timestamp art_re_date) {
		this.art_re_date = art_re_date;
	}

	
	public String getArt_re_status() {
		return art_re_status;
	}
	public void setArt_re_status(String art_re_status) {
		this.art_re_status = art_re_status;
	}
}
