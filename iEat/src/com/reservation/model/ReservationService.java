package com.reservation.model;

import java.sql.Date;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ReservationService {
	
	private ReservationDAO_interface dao;
	
	public ReservationService() {
		dao = new ReservationDAO(); 
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
	public ReservationVO updateReservation(String reservation_no, String mem_no, String store_no, Date  reservation_date, 
			String reservation_hour, Integer reservation_guests, Timestamp reservation_time, byte[] reservation_qrcode, String reservation_qrcode_status, 
			String reservation_report, String reservation_report_status) {
		
		
		ReservationVO reservationVO = new ReservationVO(); 
		reservationVO.setReservation_no(reservation_no);
		reservationVO.setMem_no(mem_no);
		reservationVO.setStore_no( store_no);
		reservationVO.setReservation_date(reservation_date);
		reservationVO.setReservation_hour(reservation_hour);
		reservationVO.setReservation_guests(reservation_guests);
		reservationVO.setReservation_time(reservation_time);
		reservationVO.setReservation_qrcode(reservation_qrcode); 
		reservationVO.setReservation_qrcode_status(reservation_qrcode_status); 
		dao.update(reservationVO);
		
		return reservationVO;		
	}
	//預留給Struts2用的
	public void updateReservation(ReservationVO reservationVO) { 
		dao.update(reservationVO);
	}
	public void deleteReservation(String reservation_no) {
		dao.delete(reservation_no);
	}
	public ReservationVO getOneReservation(String reservation_no) {
		return dao.findByPrimaryKey(reservation_no); 
	}
	public List<ReservationVO> getAll() {
		return dao.getAll(); 
	}
	public List<ReservationVO> getAll(Map<String,String[]> map){
		return dao.getAll(map);
	}
	// 取出總訂位人數
	public ArrayList<Integer> getGuests(String reservation_hour, Date reservation_date, String store_no) {
		return dao.getGuests(reservation_hour, reservation_date, store_no);
	}
	// 取出訂位檢舉狀態
	public List<ReservationVO> getMoreByReservationReportStatus(String reservation_report_status){
		return dao.getMoreByReservationReportStatus(reservation_report_status);
	}
	public List<ReservationVO> getMoreByReservationReportStatus(){
		return dao.getMoreByReservationReportStatus();
	}
	// 根據會員主鍵  取得會員訂位的資料
	public List<ReservationVO> getReservationsByMember(String mem_no) {
		return dao.getReservationsByMember(mem_no); 
	}
	//根據店家主鍵  取出店家的每筆訂位訂單
	public List<ReservationVO> getReservationsByStore(String store_no) {
		return dao.getReservationsByStore(store_no);
	}
	public ReservationVO update_qrcode_status(String reservation_qrcode_status, String reservation_no) {
		ReservationVO reservationVO = new ReservationVO(); 
		reservationVO.setReservation_qrcode_status(reservation_qrcode_status);
		reservationVO.setReservation_no(reservation_no);
		dao.update_qrcode_status(reservationVO); 
		return reservationVO; 
	}
}
