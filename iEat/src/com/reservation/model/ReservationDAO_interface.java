package com.reservation.model;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public interface ReservationDAO_interface {
	public void insert(ReservationVO reservationVO); 
	public void update(ReservationVO reservationVO); 
	public void delete(String reservation_no); 
	public ReservationVO findByPrimaryKey(String reservation_no);
	public List<ReservationVO> getAll();
	public List<ReservationVO> getAll(Map<String,String[]> map);
	public ArrayList<Integer> getGuests(String reservation_hour, Date reservation_date, String store_no);
	public List<ReservationVO> getMoreByReservationReportStatus(String reservation_report_status);
	public List<ReservationVO> getMoreByReservationReportStatus();
	public List<ReservationVO> getReservationsByMember(String mem_no);
	public List<ReservationVO> getReservationsByStore(String store_no);
	public void update_qrcode_status(ReservationVO reservationVO);
	
}
