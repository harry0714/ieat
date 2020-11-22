package com.store_report.model;

import java.util.List;

public interface Store_reportDAO_interface {
	public void insert(Store_reportVO Store_reportVO); 
	public void update(Store_reportVO Store_reportVO); 
	public void delete(String store_report_no); 
	public Store_reportVO findByPrimaryKey(String store_report_no); 
	public List<Store_reportVO> getAll(); 
	public List<Store_reportVO> getMoreByStoreReportStatus(String store_report_status);
}
