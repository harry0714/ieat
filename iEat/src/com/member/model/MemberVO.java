package com.member.model;

import java.sql.Date;

public class MemberVO implements java.io.Serializable{


	private String mem_no;
	private String mem_name;
	private String mem_acct;
	private String mem_pwd;
	private String mem_sex;
	private Date mem_bd;
	private String mem_email;
	private String mem_phone;
	private String mem_zip;
	private String mem_addr;
	private byte[] mem_photo;
	private Date mem_validate;
	
	public String getMem_no() {
		return mem_no;
	}
	public void setMem_no(String mem_no) {
		this.mem_no = mem_no;
	}
	public String getMem_name() {
		return mem_name;
	}
	public void setMem_name(String mem_name) {
		this.mem_name = mem_name;
	}
	public String getMem_acct() {
		return mem_acct;
	}
	public void setMem_acct(String mem_acct) {
		this.mem_acct = mem_acct;
	}
	public String getMem_pwd() {
		return mem_pwd;
	}
	public void setMem_pwd(String mem_pwd) {
		this.mem_pwd = mem_pwd;
	}
	public String getMem_sex() {
		return mem_sex;
	}
	public void setMem_sex(String mem_sex) {
		this.mem_sex = mem_sex;
	}
	public Date getMem_bd() {
		return mem_bd;
	}
	public void setMem_bd(Date mem_bd) {
		this.mem_bd = mem_bd;
	}
	public String getMem_email() {
		return mem_email;
	}
	public void setMem_email(String mem_email) {
		this.mem_email = mem_email;
	}
	public String getMem_phone() {
		return mem_phone;
	}
	public void setMem_phone(String mem_phone) {
		this.mem_phone = mem_phone;
	}
	public String getMem_zip() {
		return mem_zip;
	}
	public void setMem_zip(String mem_zip) {
		this.mem_zip = mem_zip;
	}
	public String getMem_addr() {
		return mem_addr;
	}
	public void setMem_addr(String mem_addr) {
		this.mem_addr = mem_addr;
	}
	public byte[] getMem_photo() {
		return mem_photo;
	}
	public void setMem_photo(byte[] mem_photo) {
		this.mem_photo = mem_photo;
	}
	public Date getMem_validate() {
		return mem_validate;
	}
	public void setMem_validate(Date mem_validate) {
		this.mem_validate = mem_validate;
	}
	
	
}
