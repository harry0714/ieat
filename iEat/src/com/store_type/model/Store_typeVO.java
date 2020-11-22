package com.store_type.model;

import java.io.Serializable;

public class Store_typeVO implements Serializable{
	private String store_type_no;
	private String store_type_name;
	public String getStore_type_no() {
		return store_type_no;
	}
	public void setStore_type_no(String store_type_no) {
		this.store_type_no = store_type_no;
	}
	public String getStore_type_name() {
		return store_type_name;
	}
	public void setStore_type_name(String store_type_name) {
		this.store_type_name = store_type_name;
	}
}
