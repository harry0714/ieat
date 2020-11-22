package com.advertisement.tools;

import java.sql.Date;

public enum AdStatusType {
	未開始,進行中,已結束;
	
	public static int getCode(Date startdate, Date enddate){
		
		Date currentdate= new Date(System.currentTimeMillis());
		int status;
		
		if(startdate.after(currentdate)){
			status = AdStatusType.未開始.ordinal();//廣告尚未開始
		}else if (enddate.before(currentdate)){
			status = AdStatusType.已結束.ordinal();//廣告已經結束
		}else{
			status = AdStatusType.進行中.ordinal();//廣告進行中
		}				
		return status;
	}

	public static String getStmt(int status){

		Date currentdate= new Date(System.currentTimeMillis());
		String where_stmt = null;
		
		switch(status){
			case 0:
				where_stmt = " WHERE to_char(ad_startdate, 'yyyy-mm-dd') > '"+ currentdate + "'" ;
				break;
			case 1:
				where_stmt = " WHERE to_char(ad_startdate, 'yyyy-mm-dd') <= '"+ currentdate+ "' and to_char(ad_enddate, 'yyyy-mm-dd') >= '" + currentdate +"'";
				break;
			case 2:
				where_stmt = " WHERE to_char(ad_enddate, 'yyyy-mm-dd') < '"+ currentdate + "'";				
				break;
			default:
				where_stmt = "";
				break;
		}
			
		return where_stmt;
	}
	public static String getCssClass(int status){

		String cssClass = null;
		
		switch(status){
			case 0:
				cssClass = "label-info";
				break;
			case 1:
				cssClass = "label-success";
				break;
			case 2:
				cssClass = "label-default";				
				break;
			default:
				cssClass = "";
				break;
		}
			
		return cssClass;
	}
}
