package com.reservation_report.controller;

import java.io.IOException;
import java.sql.Date;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.email.model.MailService;
import com.member.model.MemberService;
import com.member.model.MemberVO;
import com.reservation.model.ReservationService;
import com.reservation.model.ReservationVO;
import com.store.model.StoreService;
import com.store.model.StoreVO;


public class ReservationReportServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
    
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doPost(request, response);
	}


	protected void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
		String action = req.getParameter("action");
		
		//進入後端訂位檢舉頁面
		if("enterreservationreport".equals(action)){
			List<String> errorMessage = new ArrayList<>();
			req.setAttribute("errorMessage", errorMessage);
			try{
				
				String status_selected = (String)req.getParameter("status_selected");
				ReservationService reservationSvc = new ReservationService();
				List<ReservationVO> reservationList = null;
				if(status_selected.isEmpty()){
					reservationList = reservationSvc.getMoreByReservationReportStatus();
				}else{
					reservationList = reservationSvc.getMoreByReservationReportStatus(status_selected);
				}
				req.setAttribute("reservationList", reservationList);
				RequestDispatcher failureView = req.getRequestDispatcher("/back-end/report/reservation-report/reservationreport.jsp");
				failureView.forward(req, res);
				
			}catch(Exception e){
				errorMessage.add(e.getMessage());
				RequestDispatcher failureView = req.getRequestDispatcher("/back-end/report/reservation-report/reservationreport.jsp");
				failureView.forward(req, res);
			}
		}
		
		//訂位檢舉審核通過
		if("checkpass".equals(action)){
			List<String> errorMessage = new ArrayList<>();
			req.setAttribute("errorMessage", errorMessage);
			try{
				String reservation_no = (String)req.getParameter("reservation_no");
				System.out.println(reservation_no);
				ReservationService reservationSvc = new ReservationService();
				ReservationVO reservationVO = reservationSvc.getOneReservation(reservation_no);
				if(!reservationVO.getReservation_report_status().equals("0")){
					errorMessage.add("此店家檢舉已被審核");
					RequestDispatcher failureView = req.getRequestDispatcher("/back-end/report/reservation-report/reservationreport.jsp");
					failureView.forward(req, res);
					return;
				}
				//變更訂位檢舉狀態為1
				reservationVO.setReservation_report_status("1");
				reservationSvc.updateReservation(reservationVO.getReservation_no(), reservationVO.getMem_no(), reservationVO.getStore_no(), reservationVO.getReservation_date(), reservationVO.getReservation_hour(), reservationVO.getReservation_guests(), reservationVO.getReservation_time(), reservationVO.getReservation_qrcode(), reservationVO.getReservation_qrcode_status(), reservationVO.getReservation_report(), reservationVO.getReservation_report_status());
				
				//寄信給檢舉店家和被檢舉的會員
				MailService mailSvc = new MailService();
				StoreService storeSvc = new StoreService();
				StoreVO storeVO = storeSvc.getOneStore(reservationVO.getStore_no());
				MemberService memberSvc = new MemberService();
				MemberVO memberVO = memberSvc.getOneMember(reservationVO.getMem_no());
				
				//寄給店家
				String storeMessage = storeVO.getStore_name()+" 你好!\n你對這筆訂位紀錄  編號:'"+reservationVO.getReservation_no()+"' 的檢舉，已審核通過";
				mailSvc.sendMail(storeVO.getStore_email(),"iEat 檢舉通知", storeMessage);
				
				
				//查詢該會員訂位紀錄被檢舉通過次數
				Map<String,String[]> map = new TreeMap<String,String[]>();
				map.put("mem_no", new String[]{reservationVO.getMem_no()});
				map.put("reservation_report_status", new String[]{"1"});
				List<ReservationVO> res_repList = reservationSvc.getAll(map);
				System.out.println(reservationVO.getMem_no()+" 被檢舉次數:"+res_repList.size());
				
				//會員訂位檢舉超過3次會被停權3天
				String memberMessage =null;
				if(res_repList.size()<=3)
					memberMessage = memberVO.getMem_name()+" 你好!\n你的訂位紀錄 編號:'"+reservationVO.getReservation_no()+"' 被檢舉\n檢舉內容:'"+reservationVO.getReservation_report()+"\n你的訂位紀錄已被檢舉"+res_repList.size()+"次，3次之後將被停權";
				else{
					memberMessage = memberVO.getMem_name()+" 你好!\n你的訂位紀錄 編號:'"+reservationVO.getReservation_no()+"' 被檢舉\n檢舉內容:'"+reservationVO.getReservation_report()+"\n由於你的訂位已被檢舉超過3次，你將會有3天無法登入";
					memberVO.setMem_validate(new Date(new java.util.Date().getTime()+(long)(3*24*60*60*1000)));
					memberSvc.updateMember(memberVO.getMem_no(), memberVO.getMem_name(), memberVO.getMem_acct(), memberVO.getMem_pwd(), memberVO.getMem_sex(), memberVO.getMem_bd(), memberVO.getMem_email(), memberVO.getMem_phone(), memberVO.getMem_zip(), memberVO.getMem_addr(), memberVO.getMem_photo(),memberVO.getMem_validate() );
				}
				//寄信給會員
				mailSvc.sendMail(memberVO.getMem_email(), "iEat 檢舉通知", memberMessage);
				
				//
				String status_selected = (String)req.getParameter("status_selected");
				List<ReservationVO> reservationList = null;
				if(status_selected.isEmpty()){
					reservationList = reservationSvc.getMoreByReservationReportStatus();
				}else{
					reservationList = reservationSvc.getMoreByReservationReportStatus(status_selected);
				}
				req.setAttribute("reservationList", reservationList);
				RequestDispatcher failureView = req.getRequestDispatcher("/back-end/report/reservation-report/reservationreport.jsp");
				failureView.forward(req, res);
			}catch(Exception e){
				System.out.println();
				errorMessage.add("有錯誤:" + e.getMessage());
				RequestDispatcher failureView = req.getRequestDispatcher("/back-end/report/reservation-report/reservationreport.jsp");
				failureView.forward(req, res);
				
			}
		}
		//訂位檢舉審核不通過
		if("checkfail".equals(action)){
			List<String> errorMessage = new ArrayList<>();
			req.setAttribute("errorMessage", errorMessage);
			try{
				String reservation_no = (String)req.getParameter("reservation_no");

				ReservationService reservationSvc = new ReservationService();
				ReservationVO reservationVO = reservationSvc.getOneReservation(reservation_no);
				if(!reservationVO.getReservation_report_status().equals("0")){
					errorMessage.add("此店家檢舉已被審核");
					RequestDispatcher failureView = req.getRequestDispatcher("/back-end/report/reservation-report/reservationreport.jsp");
					failureView.forward(req, res);
					return;
				}
				//變更訂位檢舉狀態為2
				reservationVO.setReservation_report_status("2");
				reservationSvc.updateReservation(reservationVO.getReservation_no(), reservationVO.getMem_no(), reservationVO.getStore_no(), reservationVO.getReservation_date(), reservationVO.getReservation_hour(), reservationVO.getReservation_guests(), reservationVO.getReservation_time(), reservationVO.getReservation_qrcode(), reservationVO.getReservation_qrcode_status(), reservationVO.getReservation_report(), reservationVO.getReservation_report_status());
				
				//
				String status_selected = (String)req.getParameter("status_selected");
				List<ReservationVO> reservationList = null;
				if(status_selected.isEmpty()){
					reservationList = reservationSvc.getMoreByReservationReportStatus();
				}else{
					reservationList = reservationSvc.getMoreByReservationReportStatus(status_selected);
				}
				
				req.setAttribute("reservationList", reservationList);
				RequestDispatcher failureView = req.getRequestDispatcher("/back-end/report/reservation-report/reservationreport.jsp");
				failureView.forward(req, res);
			}catch(Exception e){
				System.out.println();
				errorMessage.add("有錯誤:" + e.getMessage());
				RequestDispatcher failureView = req.getRequestDispatcher("/back-end/report/reservation-report/reservationreport.jsp");
				failureView.forward(req, res);
				
			}
		}
	}

}
