package com.qa.model;

import java.io.Serializable;

public class QaVO implements Serializable{
	private String qa_no;
	private String qa_type_no;
	private String q_context;
	private String a_context;
	public String getQa_no() {
		return qa_no;
	}
	public void setQa_no(String qa_no) {
		this.qa_no = qa_no;
	}
	public String getQa_type_no() {
		return qa_type_no;
	}
	public void setQa_type_no(String qa_type_no) {
		this.qa_type_no = qa_type_no;
	}
	public String getQ_context() {
		return q_context;
	}
	public void setQ_context(String q_context) {
		this.q_context = q_context;
	}
	public String getA_context() {
		return a_context;
	}
	public void setA_context(String a_context) {
		this.a_context = a_context;
	}
	
	
}
