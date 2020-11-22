package com.comment_report.model;

import java.sql.Date;
import java.sql.Timestamp;

public class CommentReportVO implements java.io.Serializable {
	private String comment_report_no;
	private String comment_no;
	private String mem_no;
	private String comment_report_context;
	private Timestamp comment_report_date;
	private String comment_report_status;
	
	public String getComment_report_no() {
		return comment_report_no;
	}
	public void setComment_report_no(String comment_report_no) {
		this.comment_report_no = comment_report_no;
	}
	public String getComment_no() {
		return comment_no;
	}
	public void setComment_no(String comment_no) {
		this.comment_no = comment_no;
	}
	public String getMem_no() {
		return mem_no;
	}
	public void setMem_no(String mem_no) {
		this.mem_no = mem_no;
	}
	public String getComment_report_context() {
		return comment_report_context;
	}
	public void setComment_report_context(String comment_report_context) {
		this.comment_report_context = comment_report_context;
	}
	public Timestamp getComment_report_date() {
		return comment_report_date;
	}
	public void setComment_report_date(Timestamp comment_report_date) {
		this.comment_report_date = comment_report_date;
	}
	public String getComment_report_status() {
		return comment_report_status;
	}
	public void setComment_report_status(String comment_report_status) {
		this.comment_report_status = comment_report_status;
	}
	
	
}
