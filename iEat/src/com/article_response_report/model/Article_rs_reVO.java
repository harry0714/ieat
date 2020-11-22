package com.article_response_report.model;
import java.sql.Date;
import java.sql.Timestamp;

public class Article_rs_reVO implements java.io.Serializable {
	
	private String mem_no;
	private String art_rs_re_con;
	private String art_rs_re_sta;
	private Timestamp art_rs_re_date;


	private String art_rs_re_no;
	private String art_rs_no;
	
	public String getArt_rs_no() 
	{
		return art_rs_no;
	}
	public void setArt_rs_no(String art_rs_no) {
		this.art_rs_no = art_rs_no;
	}

	public String getArt_rs_re_no() {
		return art_rs_re_no;
	}
	public void setArt_rs_re_no(String art_rs_re_no) {
		this.art_rs_re_no = art_rs_re_no;
	}

	public String getMem_no() {
		return mem_no;
	}
	public void setMem_no(String mem_no) {
		this.mem_no = mem_no;
	}

	public String getArt_rs_re_con() {
		return art_rs_re_con;
	}
	public void setArt_rs_re_con(String art_rs_re_con) {
		this.art_rs_re_con = art_rs_re_con;
	}
	public Timestamp getArt_rs_re_date() {
		return art_rs_re_date;
	}
	public void setArt_rs_re_date(Timestamp art_rs_re_date) {
		this.art_rs_re_date = art_rs_re_date;
	}
	public String getArt_rs_re_sta() {
		return art_rs_re_sta;
	}
	public void setArt_rs_re_sta(String art_rs_re_sta) {
		this.art_rs_re_sta = art_rs_re_sta;
	}

	
}
