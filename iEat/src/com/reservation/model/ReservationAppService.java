package com.reservation.model;

import java.sql.Date;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ReservationAppService {
	
	private ReservationAppDAO_interface dao;
	
	public ReservationAppService() {
		dao = new ReservationAppDAO(); 
	}
	public ReservationVO addReservation(String mem_no, String store_no, Date  reservation_date, 
			String reservation_hour, Integer reservation_guests, Timestamp reservation_time) {
		
		ReservationVO reservationVO = new ReservationVO(); 
		reservationVO.setMem_no(mem_no);
		reservationVO.setStore_no( store_no);
		reservationVO.setReservation_date(reservation_date);
		reservationVO.setReservation_hour(reservation_hour);
		reservationVO.setReservation_guests(reservation_guests);
		reservationVO.setReservation_time(reservation_time);
		dao.insert(reservationVO);
		
		return reservationVO;		
	}
	
	//預留給Struts2 用的 
	public void addReservation(ReservationVO reservationVO) {
		dao.insert(reservationVO); 
	}


	public ReservationVO getOneReservation(String reservation_no) {
		return dao.findByPrimaryKey(reservation_no); 
	}


	public ReservationVO update_qrcode_status(String reservation_qrcode_status, String reservation_no) {
		ReservationVO reservationVO = new ReservationVO(); 
		reservationVO.setReservation_qrcode_status(reservation_qrcode_status);
		reservationVO.setReservation_no(reservation_no);
		dao.update_qrcode_status(reservationVO); 
		return reservationVO; 
	}
	public Map<String,ReservationVO> getMemBooking(String mem_no){
		return dao.getMemBooking(mem_no);
	}
	
	public void updateQrcodeStatus(String reser_no,int type){
		dao.updateQrcodeStatus(reser_no,type);
	}
	
	public byte[] getQrcode(String reservation_no){
		return dao.getImage(reservation_no);
	}
	public Map<String,ReservationVO> getStoreBooking(String store_no){
		return dao.getStoreBooking(store_no);
	}
	
	public int checkSeat(Date reservation_date,String reservation_hour, String store_no){
		return dao.checkSeat(reservation_date,reservation_hour,store_no);
	}
}
