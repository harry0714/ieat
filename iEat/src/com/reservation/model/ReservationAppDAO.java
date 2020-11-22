package com.reservation.model;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

import com.QRCode.java.QRCode;
import com.google.zxing.EncodeHintType;
import com.google.zxing.WriterException;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;

public class ReservationAppDAO implements ReservationAppDAO_interface{
	// 一個應用程式中,針對一個資料庫 ,共用一個DataSource即可
	private static DataSource ds = null;
	static {
		try {
			Context ctx = new InitialContext();
			ds = (DataSource) ctx.lookup("java:comp/env/jdbc/iEatDB");
		} catch (NamingException e) {
			e.printStackTrace();
		}
	}
	
	private static final String INSERT_STMT = 
			"INSERT INTO reservation (reservation_no,mem_no,store_no, reservation_date, reservation_hour,reservation_guests, reservation_time, reservation_qrcode) "
			+ "VALUES ('R'||LPAD(RESERVATION_SEQ.NEXTVAL,9,'0'), ?, ?, ?, ?, ?, ?,?)";
	
	private static final String GET_ONE_STMT = 
			"SELECT reservation_no,mem_no,store_no,to_char(reservation_date,'yyyy-mm-dd') reservation_date, reservation_hour,reservation_guests,to_char(reservation_time,'yyyy-MM-dd hh:mm:ss') reservation_time,  reservation_qrcode, reservation_qrcode_status, reservation_report, reservation_report_status  "
			+ "FROM reservation where reservation_no = ?";
	
	
	private static final String GET_MORE_BY_RESERVATION_REPORT_STATUS = 
			"SELECT reservation_no,mem_no,store_no,to_char(reservation_date,'yyyy-mm-dd') reservation_date,reservation_hour,reservation_guests,to_char(reservation_time,'yyyy-MM-dd hh:mm:ss') reservation_time,  reservation_qrcode, reservation_qrcode_status, reservation_report, reservation_report_status "
			+ "FROM reservation where reservation_report_status=? order by reservation_no desc";
	
	// 從訂位訂單的編號  去修改qrcode的狀態
	private static final String UPDATE_STATUS = 
	"UPDATE reservation SET reservation_qrcode_status=? WHERE reservation_no=? ";
	
	private static final String GET_MEM_RESER = 
			"SELECT store_name, reservation_no,mem_no,store.store_no,to_char(reservation_date,'yyyy-mm-dd') reservation_date,reservation_hour,reservation_guests,to_char(reservation_time,'yyyy-MM-dd hh:mm:ss') reservation_time, reservation_qrcode_status, reservation_report, reservation_report_status "
			+ "FROM reservation left join store on reservation.store_no = store.store_no where mem_no=? order by reservation_time desc";
	
	private static final String UPDATE_QR = "UPDATE RESERVATION set reservation_qrcode_status=? WHERE RESERVATION_NO=?";
	
	private static final String GET_QRCODE_BY_RESERVATION_NO =
			"SELECT RESERVATION_QRCODE FROM RESERVATION WHERE RESERVATION_NO=?";

	private static final String GET_STORE_RESER = 
			"SELECT mem_name, mem_sex, reservation_no,member.mem_no,store_no,to_char(reservation_date,'yyyy-mm-dd') reservation_date,reservation_hour,reservation_guests,to_char(reservation_time,'yyyy-MM-dd hh:mm:ss') reservation_time, reservation_qrcode_status, reservation_report, reservation_report_status "
			+ "FROM reservation left join member on reservation.mem_no = member.mem_no where store_no=? order by reservation_time desc";
	
	private static final String CHECK_SEAT = "SELECT SUM(reservation_guests) FROM  RESERVATION WHERE to_char(reservation_date,'yyyy-mm-dd') = ? and reservation_hour = ? and store_no = ? and reservation_qrcode_status <> 3";
	
	@Override
	public void insert(ReservationVO reservationVO) {
		Connection con = null;
		PreparedStatement pstmt = null;
		
		try {

			con = ds.getConnection();
			con.setAutoCommit(false);
			
			String cols[] = { "reservation_no", "reservation_hour", "reservation_date" };
			pstmt = con.prepareStatement(INSERT_STMT, cols);
			pstmt.setString(1, reservationVO.getMem_no());
			pstmt.setString(2, reservationVO.getStore_no());
			pstmt.setDate(3, reservationVO.getReservation_date());
			pstmt.setString(4, reservationVO.getReservation_hour());
			pstmt.setInt(5, reservationVO.getReservation_guests());
			pstmt.setTimestamp(6, reservationVO.getReservation_time());
			pstmt.setBytes(7, reservationVO.getReservation_qrcode());
			pstmt.executeUpdate();
			
			/*********************取得主鍵的值  當作QRCode的資料新增****************/
			String next_reservation_no = null;
			String reservation_hour = null;
			Date reservation_date = null; 			
			ResultSet rs = pstmt.getGeneratedKeys();			
			if (rs.next()) {
				next_reservation_no = rs.getString(1);
				reservation_hour = rs.getString(2); 
				reservation_date = rs.getDate(3); 				
				System.out.println("自增主鍵值= " + next_reservation_no + "(剛新增成功的訂位編號)");
				System.out.println("訂位日期="+reservation_date+"******訂位時段"+reservation_hour);
			} else {
				System.out.println("未取得自增主鍵值");
			}
			rs.close(); 
			
			/*****************QRCode新增********************/
			String charset = "UTF-8";
			Map<EncodeHintType, ErrorCorrectionLevel> hintMap = new HashMap<EncodeHintType, ErrorCorrectionLevel>();
			hintMap.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.L);

			/****************將QRcode圖片  傳進資料庫**********************/
			try {
				String qrCodeData = "reservation_no:"+next_reservation_no;
					String reservation_no = next_reservation_no;
					pstmt = con.prepareStatement("UPDATE reservation SET reservation_qrcode=? where reservation_no='"+reservation_no+"'");// 修改SQL指令
					pstmt.setBytes(1, QRCode.createQRCode(qrCodeData, charset, hintMap, 400, 400).toByteArray());
					int rowsUpdated = pstmt.executeUpdate();
					if (1 == rowsUpdated){
						System.out.println(" row.");
					}
					else {
						System.out.println("updated "+reservation_no+" is fail"); 
					}// 錯誤訊息
			} catch (Exception e) {
				e.printStackTrace();
			}
			
			// 2●設定於 pstm.executeUpdate()之後
			con.commit();
			con.setAutoCommit(true);
			
			/**********開新執行緒  以確認是否有在訂位時間的30分鐘內去  若沒有 將QRCode狀態改為已失效****/
			if(next_reservation_no != null) {
				System.out.println("xxxxxxxxxxxxxxxxxxxxxxxxxxx");
				Thread thread = new Thread(new Work(next_reservation_no, reservation_hour, reservation_date));
				thread.start();
			}
			
			// Handle any driver errors
		}  catch (SQLException se) {
			throw new RuntimeException("A database error occured. "
					+ se.getMessage());
			// Clean up JDBC resources
		}  
		finally {
			if (pstmt != null) {
				try {
					pstmt.close();
				} catch (SQLException se) {
					se.printStackTrace(System.err);
				}
			}
			if (con != null) {
				try {
					con.close();
				} catch (Exception e) {
					e.printStackTrace(System.err);
				}
			}
		}
	}


	@Override
	public ReservationVO findByPrimaryKey(String reservation_no) {
		ReservationVO reservationVO = null;
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;

		try {

			con = ds.getConnection();
			pstmt = con.prepareStatement(GET_ONE_STMT);

			pstmt.setString(1, reservation_no);

			rs = pstmt.executeQuery();

			while (rs.next()) {
				// empVo 也稱為 Domain objects
				reservationVO = new ReservationVO();
				reservationVO.setReservation_no(rs.getString("reservation_no"));
				reservationVO.setMem_no(rs.getString("mem_no"));
				reservationVO.setStore_no(rs.getString("store_no"));
				reservationVO.setReservation_date(rs.getDate("reservation_date"));
				reservationVO.setReservation_hour(rs.getString("reservation_hour"));
				reservationVO.setReservation_guests(rs.getInt("reservation_guests"));
				reservationVO.setReservation_time(rs.getTimestamp("reservation_time"));
				reservationVO.setReservation_qrcode(rs.getBytes("reservation_qrcode"));
				reservationVO.setReservation_qrcode_status(rs.getString("reservation_qrcode_status"));
				reservationVO.setReservation_report(rs.getString("reservation_report"));
				reservationVO.setReservation_report_status(rs.getString("reservation_report_status"));				
			}

			// Handle any driver errors
		} catch (SQLException se) {
			throw new RuntimeException("A database error occured. "
					+ se.getMessage());
			// Clean up JDBC resources
		} finally {
			if (rs != null) {
				try {
					rs.close();
				} catch (SQLException se) {
					se.printStackTrace(System.err);
				}
			}
			if (pstmt != null) {
				try {
					pstmt.close();
				} catch (SQLException se) {
					se.printStackTrace(System.err);
				} 
			}
			if (con != null) {
				try {
					con.close();
				} catch (Exception e) {
					e.printStackTrace(System.err);
				}
			}
		}
		return reservationVO;
	}



	public List<ReservationVO> getMoreByReservationReportStatus(String reservation_report_status) {
		List<ReservationVO> list = new ArrayList<ReservationVO>();
		ReservationVO reservationVO = null;

		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;

		try {

			con = ds.getConnection();
			pstmt = con.prepareStatement(GET_MORE_BY_RESERVATION_REPORT_STATUS);
			pstmt.setString(1, reservation_report_status);
			rs = pstmt.executeQuery();

			while (rs.next()) {
				// empVO 也稱為 Domain objects
				reservationVO = new ReservationVO();
				reservationVO.setReservation_no(rs.getString("reservation_no"));
				reservationVO.setMem_no(rs.getString("mem_no"));
				reservationVO.setStore_no(rs.getString("store_no"));
				reservationVO.setReservation_date(rs.getDate("reservation_date"));
				reservationVO.setReservation_hour(rs.getString("Reservation_hour"));
				reservationVO.setReservation_guests(rs.getInt("reservation_guests"));
				reservationVO.setReservation_time(rs.getTimestamp("reservation_time"));
				reservationVO.setReservation_qrcode(rs.getBytes("reservation_qrcode"));
				reservationVO.setReservation_qrcode_status(rs.getString("reservation_qrcode_status"));
				reservationVO.setReservation_report(rs.getString("reservation_report"));
				reservationVO.setReservation_report_status(rs.getString("reservation_report_status"));
				list.add(reservationVO); // Store the row in the list
			}

			// Handle any driver errors
		} catch (SQLException se) {
			throw new RuntimeException("A database error occured. "
					+ se.getMessage());
			// Clean up JDBC resources
		} finally {
			if (rs != null) {
				try {
					rs.close();
				} catch (SQLException se) {
					se.printStackTrace(System.err);
				}
			}
			if (pstmt != null) {
				try {
					pstmt.close();
				} catch (SQLException se) {
					se.printStackTrace(System.err);
				}
			}
			if (con != null) {
				try {
					con.close();
				} catch (Exception e) {
					e.printStackTrace(System.err);
				}
			}
		}
		return list;
	}


	// 修改訂位的QRCode狀態
	@Override
	public void update_qrcode_status(ReservationVO reservationVO) {
		Connection con = null;
		PreparedStatement pstmt = null;
		
		try {
			con = ds.getConnection();
			pstmt = con.prepareStatement(UPDATE_STATUS);

			pstmt.setString(1, reservationVO.getReservation_qrcode_status());
			pstmt.setString(2, reservationVO.getReservation_no());
			pstmt.executeUpdate();
		} catch (SQLException se) {
			throw new RuntimeException("A database error occured. " + se.getMessage());
			// Clean up JDBC resources
		} finally {
			if (pstmt != null) {
				try {
					pstmt.close();
				} catch (SQLException se) {
					se.printStackTrace(System.err);
				}
			}
			if (con != null) {
				try {
					con.close();
				} catch (Exception e) {
					e.printStackTrace(System.err);
				}
			}
		}		
	}

	/**************** 建立一個執行緒 *************************/ 
	public static class Work implements Runnable {
		private String reservation_no; 
		private String reservation_hour; 
		private Date reservation_date; 
		
		static ReservationAppService reservationSvc = new ReservationAppService();
		
		public Work (String reservation_no, String reservation_hour, Date reservation_date) {
			this.reservation_no = reservation_no; 
			this.reservation_hour = reservation_hour; 
			this.reservation_date = reservation_date; 
		}
		@Override
		public void run() {
			System.out.println("訂位:"+reservation_no +"開始判斷有無到場"+",到場日期為:"+reservation_date+"預約時段為："+reservation_hour);
			try {
				Integer hour = new Integer(reservation_hour); 
				Long needTime = reservation_date.getTime() + hour*60*60*1000L + 30*60*1000L; //訂位的30分後 若狀態沒改變 則 讓QRCode自動失效				
				Timestamp sysdate = new Timestamp(System.currentTimeMillis()); //取得當前的系統日期
				long sleepTime = needTime - sysdate.getTime(); //取得要讓Thread休息多久
				System.out.println("sleepTime=="+sleepTime);
				Thread.sleep(sleepTime);
			} catch(Exception e) {
				e.printStackTrace();
			}
			ReservationVO reservationVO = reservationSvc.getOneReservation(reservation_no); 
			String reservation_qrcode_status = reservationVO.getReservation_qrcode_status(); 
			if(reservation_qrcode_status.equals("1")) { //若QRCode不是店家已確認 已取消 或以逾期的狀態 
				System.out.println("訂位訂單 編號:"+reservation_no+"未到,開始更改資料庫狀態值");
				reservationVO.setReservation_qrcode_status("2"); //QRCode狀態 改為已逾期
				reservationSvc.update_qrcode_status(reservationVO.getReservation_qrcode_status(), reservation_no); 
			}
			System.out.println("訂位訂單 編號:"+reservation_no+"QRCode狀態 已改為逾期");
			System.out.println("結束執行緒"); 
			
		}
	}
	
	public Map<String,ReservationVO> getMemBooking(String mem_no){
		Map<String,ReservationVO> map = new LinkedHashMap<String,ReservationVO>();
		
		ReservationVO reservationVO = null;

		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;

		try {
			con = ds.getConnection();
			pstmt = con.prepareStatement(GET_MEM_RESER);
			pstmt.setString(1,mem_no);
			rs = pstmt.executeQuery();

			while (rs.next()) {
				// empVO 也稱為 Domain objects
				reservationVO = new ReservationVO();
				reservationVO.setReservation_no(rs.getString("reservation_no"));
				reservationVO.setMem_no(rs.getString("mem_no"));
				reservationVO.setStore_no(rs.getString("store_no"));
				reservationVO.setReservation_date(rs.getDate("reservation_date"));
				reservationVO.setReservation_hour(rs.getString("Reservation_hour"));
				reservationVO.setReservation_guests(rs.getInt("reservation_guests"));
				reservationVO.setReservation_time(rs.getTimestamp("reservation_time"));
				reservationVO.setReservation_qrcode(null);
				reservationVO.setReservation_qrcode_status(rs.getString("reservation_qrcode_status"));
				reservationVO.setReservation_report(rs.getString("reservation_report"));
				reservationVO.setReservation_report_status(rs.getString("reservation_report_status"));
				map.put(rs.getString("reservation_no")+","+rs.getString("store_name"),reservationVO); 
			}

			// Handle any driver errors
		} catch (SQLException se) {
			throw new RuntimeException("A database error occured. "
					+ se.getMessage());
			// Clean up JDBC resources
		} finally {
			if (rs != null) {
				try {
					rs.close();
				} catch (SQLException se) {
					se.printStackTrace(System.err);
				}
			}
			if (pstmt != null) {
				try {
					pstmt.close();
				} catch (SQLException se) {
					se.printStackTrace(System.err);
				}
			}
			if (con != null) {
				try {
					con.close();
				} catch (Exception e) {
					e.printStackTrace(System.err);
				}
			}
		}
		
		return map;
	}
	
	@Override
	public byte[] getImage(String reservation_no) {
		// TODO Auto-generated method stub
		
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		byte[] qrcode = null;
		
		try{
			
			con = ds.getConnection();
			pstmt = con.prepareStatement(GET_QRCODE_BY_RESERVATION_NO);
			pstmt.setString(1, reservation_no);
			rs = pstmt.executeQuery();
			
			while(rs.next()){
				qrcode = rs.getBytes(1);
			}
			
			
		}catch(SQLException se){
			throw new RuntimeException("A database error occured. "
					+ se.getMessage());
		}finally{
			if (rs != null) {
				try {
					rs.close();
				} catch (SQLException se) {
					se.printStackTrace(System.err);
				}
			}
			if (pstmt != null) {
				try {
					pstmt.close();
				} catch (SQLException se) {
					se.printStackTrace(System.err);
				}
			}
			if (con != null) {
				try {
					con.close();
				} catch (Exception e) {
					e.printStackTrace(System.err);
				}
			}
		}
		return qrcode;
	}
	
	@Override
	public void updateQrcodeStatus(String reser_no,int type) {
		Connection con = null;
		PreparedStatement pstmt = null;

		try {
			
			con = ds.getConnection();			
			pstmt = con.prepareStatement(UPDATE_QR);

			pstmt.setInt(1, type);
			pstmt.setString(2, reser_no);		
			pstmt.executeUpdate();

			// Handle any driver errors
		} catch (SQLException se) {
			throw new RuntimeException("A database error occured. "
					+ se.getMessage());
			// Clean up JDBC resources
		} finally {
			if (pstmt != null) {
				try {
					pstmt.close();
				} catch (SQLException se) {
					se.printStackTrace(System.err);
				}
			}
			if (con != null) {
				try {
					con.close();
				} catch (Exception e) {
					e.printStackTrace(System.err);
				}
			}
		}
	}
	
	public Map<String,ReservationVO> getStoreBooking(String store_no){
		Map<String,ReservationVO> map = new LinkedHashMap<String,ReservationVO>();
		
		ReservationVO reservationVO = null;

		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;

		try {
			con = ds.getConnection();
			pstmt = con.prepareStatement(GET_STORE_RESER);
			pstmt.setString(1,store_no);
			rs = pstmt.executeQuery();

			while (rs.next()) {
				// empVO 也稱為 Domain objects
				reservationVO = new ReservationVO();
				reservationVO.setReservation_no(rs.getString("reservation_no"));
				reservationVO.setMem_no(rs.getString("mem_no"));
				reservationVO.setStore_no(rs.getString("store_no"));
				reservationVO.setReservation_date(rs.getDate("reservation_date"));
				reservationVO.setReservation_hour(rs.getString("Reservation_hour"));
				reservationVO.setReservation_guests(rs.getInt("reservation_guests"));
				reservationVO.setReservation_time(rs.getTimestamp("reservation_time"));
				reservationVO.setReservation_qrcode(null);
				reservationVO.setReservation_qrcode_status(rs.getString("reservation_qrcode_status"));
				reservationVO.setReservation_report(rs.getString("reservation_report"));
				reservationVO.setReservation_report_status(rs.getString("reservation_report_status"));
				
				String mem_name = (rs.getString("mem_sex").equals("1"))?rs.getString("mem_name").charAt(0)+"先生":rs.getString("mem_name").charAt(0)+"小姐";
				map.put(rs.getString("reservation_no")+","+mem_name,reservationVO); 
			}

			// Handle any driver errors
		} catch (SQLException se) {
			throw new RuntimeException("A database error occured. "
					+ se.getMessage());
			// Clean up JDBC resources
		} finally {
			if (rs != null) {
				try {
					rs.close();
				} catch (SQLException se) {
					se.printStackTrace(System.err);
				}
			}
			if (pstmt != null) {
				try {
					pstmt.close();
				} catch (SQLException se) {
					se.printStackTrace(System.err);
				}
			}
			if (con != null) {
				try {
					con.close();
				} catch (Exception e) {
					e.printStackTrace(System.err);
				}
			}
		}
		
		return map;
	}
	
	@Override
	public int checkSeat(Date reservation_date,String reservation_hour,String store_no) {
		Connection con = null; 
		PreparedStatement pstmt = null; 
		ResultSet rs = null;  

		SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd");
		
		int count = 0;

		try{
			con = ds.getConnection();
			pstmt = con.prepareStatement(CHECK_SEAT); 
			pstmt.setString(1, sf.format(reservation_date));
			pstmt.setString(2, reservation_hour);
			pstmt.setString(3, store_no);
			rs = pstmt.executeQuery();
			
			while(rs.next()) {				
				count  = rs.getInt(1); 
			}
			
	} catch (SQLException se) {
		throw new RuntimeException("A database error occured. "
				+ se.getMessage());
	} finally {
		if (rs != null) {
			try {
				rs.close();
			} catch (SQLException se) {
				se.printStackTrace(System.err);
			}
		}
		if (pstmt != null) {
			try {
				pstmt.close();
			} catch (SQLException se) {
				se.printStackTrace(System.err);
			}
		}
		if (con != null) {
			try {
				con.close();
			} catch (Exception e) {
				e.printStackTrace(System.err);
			}
		}
	}
		System.out.println(sf.format(reservation_date));
		System.out.println(count);
		return count;
	}
}	
