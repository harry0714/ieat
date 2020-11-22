package com.reservation.model;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public interface ReservationAppDAO_interface {
	public void insert(ReservationVO reservationVO); 
	public ReservationVO findByPrimaryKey(String reservation_no);

	public void update_qrcode_status(ReservationVO reservationVO);
	public Map<String,ReservationVO> getMemBooking(String mem_no);
	public void updateQrcodeStatus(String reser_no,int type);
	public byte[] getImage(String reservation_no);
	public Map<String,ReservationVO> getStoreBooking(String store_no);	
	public int checkSeat(Date reservation_date,String reservation_hour, String store_no);
}
