package com.tools;

public class StatusToString {
	public static String getReportStatusString(String report_status){
		String status = null;
		switch (report_status.charAt(0)) {
		case '0':
			status = "未審核";
			break;
		case '1':
			status = "通過";
			break;
		case '2':
			status = "不通過";
			break;
		}
		
		return status;
	}
	
	public static String getOrderQRcodeStatusString(String ord_qrcode_status){
		String status =null;
		switch (ord_qrcode_status.charAt(0)) {
			case '0' :
				status = "已完成";
				break;
			case '1' :
				status = "已確認";
				break;
			case '2' :
				status = "逾期";
				break;
			case '3' :
				status = "已取消";
				break;
			case '4' :
				status = "待確認";
				break;
		}
		return status;
	}
	
	public static String getReservationQRcodeStatusString(String reservation_qrcode_status){
		String status = null;
		switch(reservation_qrcode_status.charAt(0)){
			case '0':
				status = "已完成";
				break;
			case '1':
				status = "已成立";
				break;
			case '2':
				status = "逾期";
				break;
			case '3':
				status = "已取消";
				break;
				
		}
		return status;
	}
	
	public static String getOrderPaidStatusString(int ord_paid){
		String status = null;
		switch (ord_paid) {
			case 0 :
				status = "已付款";
				break;
			case 1 :
				status = "未付款";
				break;
		}
		return status;
	}
}
