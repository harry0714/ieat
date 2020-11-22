package com.store.model;

import java.util.*;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

import com.discount.model.DiscountVO;
import com.member.model.MemberVO;
import com.ord.model.OrdVO;
import com.reservation.model.ReservationVO;
import com.store_comment.model.StoreCommentVO;
import com.store_photo.model.Store_photoVO;
import com.store_report.model.Store_reportVO;

import jdbc.util.CompositeQuery.jdbcUtil_CompositeQuery_Store;

import java.sql.*;
import java.sql.Date; 

public class StoreDAO implements StoreDAO_interface {
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
			"INSERT INTO store(store_no, store_id, store_psw, store_name, store_phone, store_owner, store_intro, store_ads, store_type_no, store_booking, store_open, store_book_amt, store_email, store_latlng) "
			+ "VALUES ('S'||LPAD(STORE_SEQ.NEXTVAL,9,'0'), ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?,?)"; 
	private static final String GET_ALL_STMT = 
			"SELECT store_no, store_id, store_psw, store_name, store_phone, store_owner, store_intro, store_ads, store_type_no, store_booking, store_open, store_book_amt, store_email, store_status, to_char(store_validate, 'yyyy-mm-dd') store_validate,  store_star, store_latlng "
			+ " FROM store order by store_no";
	private static final String GET_ONE_STMT =
			"SELECT store_no, store_id, store_psw, store_name, store_phone, store_owner, store_intro, store_ads, store_type_no, store_booking, store_open, store_book_amt, store_email, store_status, to_char(store_validate, 'yyyy-mm-dd') store_validate,  store_star , store_latlng"
			+ " FROM store WHERE store_no = ?"; 
	private static final String DELETE = 
			"DELETE FROM store WHERE store_no = ?"; 
	private static final String UPDATE = 
			"UPDATE store set store_psw=?, store_name=?, store_phone=?, store_owner=?, store_intro=?, store_ads=?, store_type_no=?, store_booking=?, store_open=?, store_book_amt=?, store_status=?, store_star=? , store_latlng=? where store_no = ?";
	// 自定義方法  從店家主鍵  查詢出該店家的全部照片
	private static final String GET_Store_photo_ByStore_no_STMT = 
			"SELECT store_no, photo_no, photo_name, photo, photo_des FROM store_photo WHERE store_no = ? order by photo_no "; 
	// 自定義方法  從店家主鍵  查詢出該店家的全部檢舉資訊 
	private static final String GET_Store_report_ByStore_no_STMT = 
			"SELECT store_report_no, store_no, mem_no, store_report_content, store_report_date, store_report_status FROM store_report WHERE store_no = ? ORDER BY store_report_no ";
	// 自定義方法 從店家主鍵  查詢出該店家全部的訂位資訊
	private static final String GET_RESERVATION_ByStore_no_STMT = 
			"SELECT reservation_no, mem_no, store_no, reservation_date, reservation_hour, reservation_guests, reservation_time, reservation_qrcode, reservation_qrcode_status, reservation_report, reservation_report_status FROM reservation WHERE store_no = ? ORDER BY reservation_no ";
	// 自定義方法  從店家查號  查詢店家資訊 登入驗證帳號密碼用 
	private static final String GET_ONE_STORE_BY_ID = 
			"SELECT * FROM store WHERE store_id = ? ";
	// 自定義方法 從店家Email 查詢店家資訊  忘記帳號密碼時使用 
	private static final String GET_ONE_STORE_BY_EMAIL = 
			"SELECT * FROM store WHERE store_email = ? ";
	// 取得審核狀態為1的店家
	private static final String GET_OPEN_STORE = 
			"SELECT s.store_no, store_id, store_psw, store_name, store_phone, store_owner, store_intro, store_ads, store_type_no, store_booking, store_open, store_book_amt, store_email, store_status, to_char(store_validate, 'yyyy-mm-dd') store_validate, NVL(avg_level, 0) store_star, store_latlng FROM store s left join (select store_no, avg(comment_level) avg_level from store_comment sc1 where  0 = (select count(*) from comment_report cr where cr.comment_no = sc1.comment_no and comment_report_status = 1) group by store_no) sc on s.store_no=sc.store_no WHERE store_status = 1 order by s.store_no";
	// 取得單張店家照片 顯示全部店家用 由其他方法所取代  所以不需要了
//	private static final String GET_FIRST_STORE_PHOTO = 
//			"SELECT photo_no from store_photo WHERE store_no = ? AND ROWNUM = 1 ";
	// 根據店家主鍵  取出店家所有的訂餐資訊
	private static final String GET_MORE_STMT_BY_STORE = 
			"SELECT * FROM ord WHERE store_no = ? "; 
	private static final String GET_MORE_BY_STORE_STATUS = "SELECT store_no, store_id, store_psw, store_name, store_phone, store_owner, store_intro, store_ads, store_type_no, store_booking, store_open, store_book_amt, store_email, store_status, to_char(store_validate, 'yyyy-mm-dd') store_validate,  store_star "
			+ "FROM store where store_status=? order by store_no"; 
	private static final String GET_Not_reviewed_STMT =
			"select store_no, store_id, store_psw, store_name, store_phone, store_owner, store_intro, store_ads, store_type_no, store_booking, store_open, store_book_amt, store_email, store_status, to_char(store_validate, 'yyyy-mm-dd') store_validate,  store_star FROM STORE  where store_status='2'";
	private static final String GET_ONE_updatePass_STMT =
			"UPDATE store set store_status=?, store_validate=? where store_no = ?";
	private static final String GET_reviewed_STMT =
			"select store_no, store_id, store_psw, store_name, store_phone, store_owner, store_intro, store_ads, store_type_no, store_booking, store_open, store_book_amt, store_email, store_status, to_char(store_validate, 'yyyy-mm-dd') store_validate,  store_star FROM STORE  where store_status<>'2'";
	//取得單筆店家JOIN含Store_star且有開店
	private static final String GET_ONE_HAVE_STORESTAR = 
			"SELECT s.store_no, store_id, store_psw, store_name, store_phone, store_owner, store_intro, store_ads, store_type_no, store_booking, store_open, store_book_amt, store_email, store_status, to_char(store_validate, 'yyyy-mm-dd') store_validate, NVL(avg_level, 0) store_star, store_latlng FROM store s left join (select store_no, avg(comment_level) avg_level from store_comment sc1 where  0 = (select count(*) from comment_report cr where cr.comment_no = sc1.comment_no and comment_report_status = 1) group by store_no) sc on s.store_no=sc.store_no WHERE store_status=1 and s.store_no=?";
		// 取得店家評論
	private static final String GET_BY_STORENO = 
			"select * from store_comment sc where 0 = (select count(*) from comment_report cr where cr.comment_no = sc.comment_no and comment_report_status = 1) and store_no=? order by comment_time desc";
	private static final String GET_DISCOUNT_BY_STORENO = 
			"select * from discount where store_no=? and sysdate between DISCOUNT_STARTDATE and DISCOUNT_ENDDATE";
	
	
	@Override
	public void insert(StoreVO storeVO) {
		Connection con = null; 
		PreparedStatement pstmt = null; 
		
		try{
			con = ds.getConnection();
			pstmt = con.prepareStatement(INSERT_STMT); 
			
			pstmt.setString(1, storeVO.getStore_id()); 
			pstmt.setString(2, storeVO.getStore_psw());
			pstmt.setString(3, storeVO.getStore_name());
			pstmt.setString(4, storeVO.getStore_phone());
			pstmt.setString(5, storeVO.getStore_owner());
			pstmt.setString(6, storeVO.getStore_intro());
			pstmt.setString(7, storeVO.getStore_ads());
			pstmt.setString(8, storeVO.getStore_type_no());
			pstmt.setString(9, storeVO.getStore_booking()); 
			pstmt.setString(10, storeVO.getStore_open()); 
			pstmt.setString(11, storeVO.getStore_book_amt()); 
			pstmt.setString(12, storeVO.getStore_email());
			pstmt.setString(13, storeVO.getStore_latlng());			
			pstmt.executeUpdate(); 
			
		}  catch (SQLException se) {
			throw new RuntimeException("A database error occured. "
					+ se.getMessage());
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
	@Override
	public void update(StoreVO storeVO) {
		
		Connection con = null; 
		PreparedStatement pstmt = null; 
		
		try{
			con = ds.getConnection();
			pstmt = con.prepareStatement(UPDATE);
			
			pstmt.setString(1, storeVO.getStore_psw());
			pstmt.setString(2, storeVO.getStore_name());
			pstmt.setString(3, storeVO.getStore_phone());
			pstmt.setString(4, storeVO.getStore_owner());
			pstmt.setString(5, storeVO.getStore_intro());
			pstmt.setString(6, storeVO.getStore_ads());
			pstmt.setString(7, storeVO.getStore_type_no());
			pstmt.setString(8, storeVO.getStore_booking());
			pstmt.setString(9, storeVO.getStore_open());
			pstmt.setString(10, storeVO.getStore_book_amt());
			pstmt.setString(11, storeVO.getStore_status());
			pstmt.setDouble(12, storeVO.getStore_star());
			pstmt.setString(13, storeVO.getStore_latlng());
			pstmt.setString(14, storeVO.getStore_no());
			
			pstmt.executeUpdate();
		}catch (SQLException se) {
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

	@Override
	public void delete(String store_no) {
		
		Connection con = null; 
		PreparedStatement pstmt = null; 
		
		try{
			con = ds.getConnection();
			pstmt = con.prepareStatement(DELETE);
			
			pstmt.setString(1, store_no);
			
			pstmt.executeUpdate(); 
		}catch (SQLException se) {
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

	@Override
	public StoreVO findByPrimaryKey(String store_no) {
		StoreVO storeVO = null;
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		
		try {

			con = ds.getConnection();
			pstmt = con.prepareStatement(GET_ONE_STMT);

			pstmt.setString(1, store_no);

			rs = pstmt.executeQuery();

			while (rs.next()) {
				// empVo 也稱為 Domain objects
				storeVO = new StoreVO();
				storeVO.setStore_no(rs.getString("store_no"));
				storeVO.setStore_id(rs.getString("store_id"));
				storeVO.setStore_psw(rs.getString("store_psw"));
				storeVO.setStore_name(rs.getString("store_name"));
				storeVO.setStore_phone(rs.getString("store_phone"));
				storeVO.setStore_owner(rs.getString("store_owner"));
				storeVO.setStore_intro(rs.getString("store_intro"));
				storeVO.setStore_ads(rs.getString("store_ads"));
				storeVO.setStore_type_no(rs.getString("store_type_no"));
				storeVO.setStore_booking(rs.getString("store_booking"));
				storeVO.setStore_open(rs.getString("store_open"));
				storeVO.setStore_book_amt(rs.getString("store_book_amt"));
				storeVO.setStore_email(rs.getString("store_email"));
				storeVO.setStore_status(rs.getString("store_status"));
				storeVO.setStore_validate(rs.getDate("store_validate"));
				storeVO.setStore_star(rs.getDouble("store_star"));
				storeVO.setStore_latlng(rs.getString("store_latlng"));			
			} 
			
		}catch (SQLException se) {
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
		return storeVO;
	}

	@Override
	public List<StoreVO> getAll() {
		List<StoreVO> list = new ArrayList<StoreVO>(); 
		StoreVO storeVO = null; 
		
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		
		try {
			con = ds.getConnection();
			pstmt = con.prepareStatement(GET_ALL_STMT);
			rs = pstmt.executeQuery();
			
			while (rs.next()) {
				storeVO = new StoreVO();
				storeVO.setStore_no(rs.getString("store_no"));
				storeVO.setStore_id(rs.getString("store_id"));
				storeVO.setStore_psw(rs.getString("store_psw"));
				storeVO.setStore_name(rs.getString("store_name"));
				storeVO.setStore_phone(rs.getString("store_phone"));
				storeVO.setStore_owner(rs.getString("store_owner"));
				storeVO.setStore_intro(rs.getString("store_intro"));
				storeVO.setStore_ads(rs.getString("store_ads"));
				storeVO.setStore_type_no(rs.getString("store_type_no"));
				storeVO.setStore_booking(rs.getString("store_booking"));
				storeVO.setStore_open(rs.getString("store_open"));
				storeVO.setStore_book_amt(rs.getString("store_book_amt"));
				storeVO.setStore_email(rs.getString("store_email"));
				storeVO.setStore_status(rs.getString("store_status"));
				storeVO.setStore_validate(rs.getDate("store_validate"));
				storeVO.setStore_star(rs.getDouble("store_star"));
				storeVO.setStore_latlng(rs.getString("store_latlng"));		
				list.add(storeVO); 
			}
			
		} 	catch (SQLException se) {
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
	@Override
	public Set<Store_photoVO> getStore_photosByStore_no(String store_no) {
		Set<Store_photoVO> set = new LinkedHashSet<Store_photoVO>(); 
		Store_photoVO store_photoVO = null; 
		
		Connection con = null; 
		PreparedStatement pstmt = null; 
		ResultSet rs = null; 
		
		try{
			con = ds.getConnection();
			pstmt = con.prepareStatement(GET_Store_photo_ByStore_no_STMT); 
			pstmt.setString(1, store_no);
			rs = pstmt.executeQuery();
			
			while(rs.next()) {
				store_photoVO = new Store_photoVO();  
				store_photoVO.setPhoto_no(rs.getString("photo_no")); 
				store_photoVO.setPhoto_name(rs.getString("photo_name")); 
				store_photoVO.setPhoto(rs.getBytes("photo"));
				store_photoVO.setPhoto_des(rs.getString("photo_des")); 
				store_photoVO.setStore_no(rs.getString("store_no"));
				set.add(store_photoVO); 
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
		return set;
	}
	@Override
	public Set<Store_reportVO> getStore_reportByStore_no(String store_no) {
		Set<Store_reportVO> set = new LinkedHashSet<Store_reportVO>(); 
		Store_reportVO store_reportVO = null; 
		
		Connection con = null; 
		PreparedStatement pstmt = null; 
		ResultSet rs = null; 
		
		try{
			con = ds.getConnection();
			pstmt = con.prepareStatement(GET_Store_report_ByStore_no_STMT); 
			pstmt.setString(1, store_no);
			rs = pstmt.executeQuery();
			
			while(rs.next()) {
				store_reportVO = new Store_reportVO();  
				store_reportVO.setStore_report_no(rs.getString("store_report_no")); 
				store_reportVO.setStore_no(rs.getString("store_no")); 
				store_reportVO.setMem_no(rs.getString("mem_no"));
				store_reportVO.setStore_report_content(rs.getString("store_report_content")); 
				store_reportVO.setStore_report_date(rs.getDate("store_report_date"));
				store_reportVO.setStore_report_status(rs.getString("store_report_status")); 
				set.add(store_reportVO); 
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
		return set;
	}
	@Override
	public Set<ReservationVO> getReservationByStore_no(String store_no) {
		Set<ReservationVO> set = new LinkedHashSet<ReservationVO>();
		ReservationVO reservationVO = null;  
		
		Connection con = null; 
		PreparedStatement pstmt = null; 
		ResultSet rs = null; 
		
		try {
			con = ds.getConnection(); 
			pstmt = con.prepareStatement(GET_RESERVATION_ByStore_no_STMT);
			pstmt.setString(1, store_no);
			rs = pstmt.executeQuery(); 
			
			while(rs.next()) {
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
				set.add(reservationVO); 
				
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
		return set; 
		}
	// 萬用複合查詢的方法
	@Override
	public List<StoreVO> getAll(Map<String, String[]> map) {
		List<StoreVO> list = new ArrayList<StoreVO>(); 
		StoreVO storeVO = null; 
		
		Connection con = null; 
		PreparedStatement pstmt = null; 
		ResultSet rs = null; 
		
		try {
			con = ds.getConnection(); 
			String finalSQL = "SELECT s.store_no, store_id, store_psw, store_name, store_phone, store_owner, store_intro, store_ads, store_type_no, store_booking, store_open, store_book_amt, store_email, store_status, to_char(store_validate, 'yyyy-mm-dd') store_validate, NVL(avg_level, 0) store_star, store_latlng FROM store s left join (select store_no, avg(comment_level) avg_level from store_comment group by store_no) sc on s.store_no=sc.store_no " 
			+ jdbcUtil_CompositeQuery_Store.getWhereCondition(map)
			+ " order by store_no "; 
			
			pstmt = con.prepareStatement(finalSQL); 
			System.out.println("●●finalSQL(by DAO) = "+finalSQL);
			rs = pstmt.executeQuery();			
			
			while(rs.next()) {
				storeVO = new StoreVO();
				storeVO.setStore_no(rs.getString("store_no"));
				storeVO.setStore_id(rs.getString("store_id"));
				storeVO.setStore_psw(rs.getString("store_psw"));
				storeVO.setStore_name(rs.getString("store_name"));
				storeVO.setStore_phone(rs.getString("store_phone"));
				storeVO.setStore_owner(rs.getString("store_owner"));
				storeVO.setStore_intro(rs.getString("store_intro"));
				storeVO.setStore_ads(rs.getString("store_ads"));
				storeVO.setStore_type_no(rs.getString("store_type_no"));
				storeVO.setStore_booking(rs.getString("store_booking"));
				storeVO.setStore_open(rs.getString("store_open"));
				storeVO.setStore_book_amt(rs.getString("store_book_amt"));
				storeVO.setStore_email(rs.getString("store_email"));
				storeVO.setStore_status(rs.getString("store_status"));
				storeVO.setStore_validate(rs.getDate("store_validate"));
				storeVO.setStore_star(rs.getDouble("store_star"));		
				list.add(storeVO); 
			}
			
		}catch (SQLException se) {
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
		return list;
	}
	//自定義方法  根據id 查詢店家帳號密碼
	@Override
	public StoreVO getOneStoreIdAndPsw(String store_id) {
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		
		StoreVO storeVO = null;
		
		try {
			con = ds.getConnection();
			pstmt = con.prepareStatement(GET_ONE_STORE_BY_ID);
			pstmt.setString(1, store_id);
			
			rs = pstmt.executeQuery(); 
			while(rs.next()) {
				storeVO = new StoreVO();
				storeVO.setStore_no(rs.getString("store_no"));
				storeVO.setStore_id(rs.getString("store_id"));
				storeVO.setStore_psw(rs.getString("store_psw"));
				storeVO.setStore_name(rs.getString("store_name"));
				storeVO.setStore_phone(rs.getString("store_phone"));
				storeVO.setStore_owner(rs.getString("store_owner"));
				storeVO.setStore_intro(rs.getString("store_intro"));
				storeVO.setStore_ads(rs.getString("store_ads"));
				storeVO.setStore_type_no(rs.getString("store_type_no"));
				storeVO.setStore_booking(rs.getString("store_booking"));
				storeVO.setStore_open(rs.getString("store_open"));
				storeVO.setStore_book_amt(rs.getString("store_book_amt"));
				storeVO.setStore_email(rs.getString("store_email"));
				storeVO.setStore_status(rs.getString("store_status"));
				storeVO.setStore_validate(rs.getDate("store_validate"));
				storeVO.setStore_star(rs.getDouble("store_star"));		
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
	return storeVO;
	}
	@Override
	public StoreVO getOneStoreByEmail(String store_email) {
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		
		StoreVO storeVO = null;
		try {
			con = ds.getConnection();
			pstmt = con.prepareStatement(GET_ONE_STORE_BY_EMAIL);
			pstmt.setString(1, store_email);
			
			rs = pstmt.executeQuery(); 
			while(rs.next()) {
				storeVO = new StoreVO();
				storeVO.setStore_no(rs.getString("store_no"));
				storeVO.setStore_id(rs.getString("store_id"));
				storeVO.setStore_psw(rs.getString("store_psw"));
				storeVO.setStore_name(rs.getString("store_name"));
				storeVO.setStore_phone(rs.getString("store_phone"));
				storeVO.setStore_owner(rs.getString("store_owner"));
				storeVO.setStore_intro(rs.getString("store_intro"));
				storeVO.setStore_ads(rs.getString("store_ads"));
				storeVO.setStore_type_no(rs.getString("store_type_no"));
				storeVO.setStore_booking(rs.getString("store_booking"));
				storeVO.setStore_open(rs.getString("store_open"));
				storeVO.setStore_book_amt(rs.getString("store_book_amt"));
				storeVO.setStore_email(rs.getString("store_email"));
				storeVO.setStore_status(rs.getString("store_status"));
				storeVO.setStore_validate(rs.getDate("store_validate"));
				storeVO.setStore_star(rs.getDouble("store_star"));		
				
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
		return storeVO;
	}
	
	@Override
	public List<StoreVO> getOpenStore() {
		List<StoreVO> list = new ArrayList<StoreVO>(); 
		StoreVO storeVO = null; 
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		
		try {
			con = ds.getConnection();
			pstmt = con.prepareStatement(GET_OPEN_STORE);
			rs = pstmt.executeQuery();
			
			while (rs.next()) {
				storeVO = new StoreVO();
				storeVO.setStore_no(rs.getString("store_no"));
				storeVO.setStore_id(rs.getString("store_id"));
				storeVO.setStore_psw(rs.getString("store_psw"));
				storeVO.setStore_name(rs.getString("store_name"));
				storeVO.setStore_phone(rs.getString("store_phone"));
				storeVO.setStore_owner(rs.getString("store_owner"));
				storeVO.setStore_intro(rs.getString("store_intro"));
				storeVO.setStore_ads(rs.getString("store_ads"));
				storeVO.setStore_type_no(rs.getString("store_type_no"));
				storeVO.setStore_booking(rs.getString("store_booking"));
				storeVO.setStore_open(rs.getString("store_open"));
				storeVO.setStore_book_amt(rs.getString("store_book_amt"));
				storeVO.setStore_email(rs.getString("store_email"));
				storeVO.setStore_status(rs.getString("store_status"));
				storeVO.setStore_validate(rs.getDate("store_validate"));
				storeVO.setStore_star(rs.getDouble("store_star"));
				storeVO.setStore_latlng(rs.getString("store_latlng"));	
				list.add(storeVO); 
			}
			
		} 	catch (SQLException se) {
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
	
	@Override
	public List<OrdVO> getMoreByStore(String store_no) {
		List<OrdVO> list = new ArrayList<OrdVO>();
		OrdVO ordVO = null;

		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		
		try {

			con =ds.getConnection();
			pstmt = con.prepareStatement(GET_MORE_STMT_BY_STORE);
			pstmt.setString(1, store_no);
			rs = pstmt.executeQuery();
			
			while (rs.next()) {
				// ordVO 也稱為 Domain objects
				ordVO = new OrdVO();
				ordVO.setOrd_no(rs.getString("ord_no"));
				ordVO.setMem_no(rs.getString("mem_no"));
				ordVO.setStore_no(rs.getString("store_no"));
				ordVO.setOrd_date(rs.getTimestamp("ord_date"));
				ordVO.setOrd_pickup(rs.getTimestamp("ord_pickup"));
				ordVO.setOrd_qrcode(rs.getBytes("ord_qrcode"));
				ordVO.setOrd_qrcode_status(rs.getString("ord_qrcode_status"));
				ordVO.setOrd_paid(rs.getInt("ord_paid"));
				ordVO.setOrd_report(rs.getString("ord_report"));
				ordVO.setOrd_report_status(rs.getString("ord_report_status"));

				list.add(ordVO); // Store the row in the list
			}
	}	catch (SQLException se) {
		throw new RuntimeException("A database error occured. " + se.getMessage());
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
	@Override
	public List<StoreVO> getMoreByStoreStatus(String store_status) {
		List<StoreVO> list = new ArrayList<StoreVO>(); 
		StoreVO storeVO = null; 
		
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		
		try {
			con = ds.getConnection();
			pstmt = con.prepareStatement(GET_MORE_BY_STORE_STATUS);
			pstmt.setString(1, store_status);
			rs = pstmt.executeQuery();
			
			while (rs.next()) {
				storeVO = new StoreVO();
				storeVO.setStore_no(rs.getString("store_no"));
				storeVO.setStore_id(rs.getString("store_id"));
				storeVO.setStore_psw(rs.getString("store_psw"));
				storeVO.setStore_name(rs.getString("store_name"));
				storeVO.setStore_phone(rs.getString("store_phone"));
				storeVO.setStore_owner(rs.getString("store_owner"));
				storeVO.setStore_intro(rs.getString("store_intro"));
				storeVO.setStore_ads(rs.getString("store_ads"));
				storeVO.setStore_type_no(rs.getString("store_type_no"));
				storeVO.setStore_booking(rs.getString("store_booking"));
				storeVO.setStore_open(rs.getString("store_open"));
				storeVO.setStore_book_amt(rs.getString("store_book_amt"));
				storeVO.setStore_email(rs.getString("store_email"));
				storeVO.setStore_status(rs.getString("store_status"));
				storeVO.setStore_validate(rs.getDate("store_validate"));
				storeVO.setStore_star(rs.getDouble("store_star"));		
				list.add(storeVO); 
			}
			
		} 	catch (SQLException se) {
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
	@Override
	public List<StoreVO> findByNot_reviewed() {
		List<StoreVO> notreviewed = new ArrayList<StoreVO>(); 
		StoreVO storeVO = null;
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			con = ds.getConnection();
			pstmt = con.prepareStatement(GET_Not_reviewed_STMT);
			rs = pstmt.executeQuery();
			while (rs.next()) {
				// empVo �]�٬� Domain objects
				storeVO = new StoreVO();
				storeVO.setStore_no(rs.getString("store_no"));
				storeVO.setStore_id(rs.getString("store_id"));
				storeVO.setStore_name(rs.getString("store_name"));
				storeVO.setStore_phone(rs.getString("store_phone"));
				storeVO.setStore_owner(rs.getString("store_owner"));
				storeVO.setStore_intro(rs.getString("store_intro"));
				storeVO.setStore_ads(rs.getString("store_ads"));
				storeVO.setStore_type_no(rs.getString("store_type_no"));
				storeVO.setStore_booking(rs.getString("store_booking"));
				storeVO.setStore_open(rs.getString("store_open"));
				storeVO.setStore_book_amt(rs.getString("store_book_amt"));
				storeVO.setStore_email(rs.getString("store_email"));
				storeVO.setStore_status(rs.getString("store_status"));
				storeVO.setStore_validate(rs.getDate("store_validate"));
				notreviewed.add(storeVO);		
			} 
			
		}catch (SQLException se) {
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
		return notreviewed;
	}
	/****** 更新資料  店家審核通過  來自後端 **************/ 
	@Override
	public void updatePass(String store_status,Date  store_validate, String store_no) {
		
		Connection con = null; 
		PreparedStatement pstmt = null; 
		
		try{
			con = ds.getConnection();
			pstmt = con.prepareStatement(GET_ONE_updatePass_STMT);
						
			pstmt.setString(1, store_status);
			pstmt.setDate(2, store_validate);
			pstmt.setString(3, store_no);
			System.out.println(pstmt.executeUpdate());
		}catch (SQLException se) {
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
	@Override
	public List<StoreVO> findBy_reviewed() {
		List<StoreVO> reviewed = new ArrayList<StoreVO>(); 
		StoreVO storeVO = null;
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		
		try {

			con = ds.getConnection();
			pstmt = con.prepareStatement(GET_reviewed_STMT);


			rs = pstmt.executeQuery();

			while (rs.next()) {
				// empVo �]�٬� Domain objects
				storeVO = new StoreVO();
				storeVO.setStore_no(rs.getString("store_no"));
				storeVO.setStore_id(rs.getString("store_id"));
				storeVO.setStore_name(rs.getString("store_name"));
				storeVO.setStore_phone(rs.getString("store_phone"));
				storeVO.setStore_owner(rs.getString("store_owner"));
				storeVO.setStore_intro(rs.getString("store_intro"));
				storeVO.setStore_ads(rs.getString("store_ads"));
				storeVO.setStore_type_no(rs.getString("store_type_no"));
				storeVO.setStore_booking(rs.getString("store_booking"));
				storeVO.setStore_open(rs.getString("store_open"));
				storeVO.setStore_book_amt(rs.getString("store_book_amt"));
				storeVO.setStore_email(rs.getString("store_email"));
				storeVO.setStore_status(rs.getString("store_status"));
				storeVO.setStore_validate(rs.getDate("store_validate"));
				reviewed.add(storeVO);		
			} 
			
		}catch (SQLException se) {
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
		return reviewed;
	}
	@Override
	public StoreVO getOneStoreAndStoreStar(String store_no) {
		StoreVO storeVO = null;
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;

		try {
			con = ds.getConnection();
			pstmt = con.prepareStatement(GET_ONE_HAVE_STORESTAR);

			pstmt.setString(1, store_no);

			rs = pstmt.executeQuery();

			while (rs.next()) {
				// empVo 也稱為 Domain objects
				storeVO = new StoreVO();
				storeVO.setStore_no(rs.getString("store_no"));
				storeVO.setStore_id(rs.getString("store_id"));
				storeVO.setStore_psw(rs.getString("store_psw"));
				storeVO.setStore_name(rs.getString("store_name"));
				storeVO.setStore_phone(rs.getString("store_phone"));
				storeVO.setStore_owner(rs.getString("store_owner"));
				storeVO.setStore_intro(rs.getString("store_intro"));
				storeVO.setStore_ads(rs.getString("store_ads"));
				storeVO.setStore_type_no(rs.getString("store_type_no"));
				storeVO.setStore_booking(rs.getString("store_booking"));
				storeVO.setStore_open(rs.getString("store_open"));
				storeVO.setStore_book_amt(rs.getString("store_book_amt"));
				storeVO.setStore_email(rs.getString("store_email"));
				storeVO.setStore_status(rs.getString("store_status"));
				storeVO.setStore_validate(rs.getDate("store_validate"));
				storeVO.setStore_star(rs.getDouble("store_star"));
				storeVO.setStore_latlng(rs.getString("store_latlng"));
			}

		} catch (SQLException se) {
			throw new RuntimeException("A database error occured. " + se.getMessage());
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
		return storeVO;
	}
	@Override
	public List<StoreCommentVO> getByStroeNo(String store_no) {
		List<StoreCommentVO> list = new ArrayList<StoreCommentVO>();
		StoreCommentVO storeCommentVO = null;

		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;

		try {

			con = ds.getConnection();
			pstmt = con.prepareStatement(GET_BY_STORENO);
			pstmt.setString(1, store_no);
			rs = pstmt.executeQuery();

			while (rs.next()) {
				// empVO 也稱為 Domain objects
				storeCommentVO = new StoreCommentVO();
				storeCommentVO.setComment_no(rs.getString("comment_no"));
				storeCommentVO.setStore_no(rs.getString("store_no"));
				storeCommentVO.setMem_no(rs.getString("mem_no"));
				storeCommentVO.setComment_conect(rs.getString("comment_conect"));
				storeCommentVO.setComment_time(rs.getTimestamp("comment_time"));
				storeCommentVO.setComment_level(rs.getDouble("comment_level"));
				list.add(storeCommentVO); // Store the row in the list
			}

			// Handle any driver errors
		} catch (SQLException se) {
			throw new RuntimeException("A database error occured. " + se.getMessage());
			// Handle any SQL errors
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
	
	@Override
	public List<DiscountVO> getDiscountByStoreNO(String store_no) {
		List<DiscountVO> list = new ArrayList<DiscountVO>();
		DiscountVO discountVO = null;

		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;

		try {

			con = ds.getConnection();
			pstmt = con.prepareStatement(GET_DISCOUNT_BY_STORENO);
			pstmt.setString(1, store_no);
			rs = pstmt.executeQuery();

			while (rs.next()) {
				// discountVO 也稱為 Domain objects
				discountVO = new DiscountVO();
				discountVO.setDiscount_no(rs.getString("discount_no"));
				discountVO.setDiscount_title(rs.getString("discount_title"));
				discountVO.setDiscount_startdate(rs.getDate("discount_startdate"));
				discountVO.setDiscount_enddate(rs.getDate("discount_enddate"));
				discountVO.setStore_no(rs.getString("store_no"));

				list.add(discountVO); // Store the row in the list
			}

			// Handle any SQL errors
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
}