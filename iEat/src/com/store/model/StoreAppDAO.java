package com.store.model;

import java.util.*;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

import com.reservation.model.ReservationVO;
import com.store_photo.model.Store_photoVO;
import com.store_report.model.Store_reportVO;

import jdbc.util.CompositeQuery.jdbcUtil_CompositeQuery_Store;

import java.sql.*; 

public class StoreAppDAO implements StoreAppDAO_interface {
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
			"SELECT store_no, store_id, store_psw, store_name, store_phone, store_owner, store_intro, store_ads, store_type_no, store_booking, store_open, store_book_amt, store_email, store_status, to_char(store_validate, 'yyyy-mm-dd') store_validate,  store_star , store_latlng "
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
			"SELECT * FROM store WHERE store_id = ? and store_psw = ?";
	// 自定義方法 從店家Email 查詢店家資訊  忘記帳號密碼時使用 
	private static final String GET_ONE_STORE_BY_EMAIL = 
			"SELECT * FROM store WHERE store_email = ? ";
	
	// 取得審核狀態為1的店家
	private static final String GET_OPEN_STORE = 
			"SELECT s.store_no, store_id, store_psw, store_name, store_phone, store_owner, store_intro, store_ads, store_type_no, store_booking, store_open, store_book_amt, store_email, store_status, to_char(store_validate, 'yyyy-mm-dd') store_validate, NVL(avg_level, 0) store_star, store_latlng FROM store s left join (select store_no, avg(comment_level) avg_level from store_comment group by store_no) sc on s.store_no=sc.store_no WHERE store_status = 1 order by s.store_no";
	
	//取得一張照片
	private static final String GET_STORE_IMG =
			"SELECT photo FROM store_photo WHERE ROWNUM <=1 AND store_no = ?"; 
	//取得一個OPEN店家
	private static final String GET_ONE_AVAILABLE_STORE =
			"SELECT s.store_no, store_id, store_psw, store_name, store_phone, store_owner, store_intro, store_ads, store_type_no, store_booking, store_open, store_book_amt, store_email, store_status, to_char(store_validate, 'yyyy-mm-dd') store_validate, NVL(avg_level, 0) store_star, store_latlng FROM store s left join (select store_no, avg(comment_level) avg_level from store_comment group by store_no) sc on s.store_no=sc.store_no WHERE store_status = 1 and s.store_no = ?";
	
	private static final String UPLOAD_KEY_STMT = "UPDATE store set  STORE_KEY=? WHERE STORE_NO = ?" ;
	private static final String GET_STORE_KEY = "SELECT STORE_KEY FROM STORE WHERE STORE_NO = ?";
	private static final String GET_TOP_STORE = "select * from (select ord.store_no,bbb.store_name, bbb.store_ads,store_star, count(*) times from ord left join (SELECT s.store_no, store_name, store_ads, "
			+ "store_status, NVL(avg_level, 0) store_star FROM store s left join (select store_no, avg(comment_level) avg_level from store_comment group by store_no) sc on s.store_no=sc.store_no) bbb on ord.store_no = bbb.store_no where bbb.store_status = 1 group by bbb.store_name, ord.store_no,bbb.store_ads,store_star order by times DESC) where rownum<=10";
	
	private static final String UPDATE_PWD = "UPDATE store set  store_psw=? WHERE store_no = ?";
	

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


	//自定義方法  根據id 查詢店家帳號密碼
	@Override
	public StoreVO getOneStoreIdAndPsw(String store_id,String store_psw) {
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		
		StoreVO storeVO = null;
		
		try {
			con = ds.getConnection();
			pstmt = con.prepareStatement(GET_ONE_STORE_BY_ID);
			pstmt.setString(1, store_id);
			pstmt.setString(2, store_psw);
			
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
	public byte[] getStoreImg(String store_no) {
		
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		byte[] photo = null;
		
		try {

			con = ds.getConnection();
			pstmt = con.prepareStatement(GET_STORE_IMG);

			pstmt.setString(1, store_no);

			rs = pstmt.executeQuery();

			while (rs.next()) {
				photo = rs.getBytes("photo");
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
		return photo;
	}	
	
	
	@Override
	public StoreVO getOneAvailableStore(String store_no) {
		StoreVO storeVO = null;
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		
		try {

			con = ds.getConnection();
			pstmt = con.prepareStatement(GET_ONE_AVAILABLE_STORE);

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
	public List<StoreVO> getAllKeyword(String keyword) {
		List<StoreVO> list = new ArrayList<StoreVO>(); 
		StoreVO storeVO = null; 
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		
		try {
			con = ds.getConnection();
			pstmt = con.prepareStatement("SELECT store_no, store_id, store_psw, store_name, store_phone, store_owner, store_intro, store_ads, store_type_no, store_booking, store_open, store_book_amt, store_email, store_status, to_char(store_validate, 'yyyy-mm-dd') store_validate,  store_star, store_latlng  "
					+ " FROM store WHERE store_status = 1 and store_name like '%"+keyword+"%'order by store_no");
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
	public void updateStoreKey(String store_no,String store_key) {
		Connection con = null;
		PreparedStatement pstmt = null;
		try {

			con = ds.getConnection();

			pstmt = con.prepareStatement(UPLOAD_KEY_STMT);
			
			pstmt.setString(1, store_key);
			pstmt.setString(2, store_no);

			pstmt.executeUpdate();
			
			System.out.println(store_key);
		} catch (SQLException e) {
			throw new RuntimeException("A database error occured. " + e.getMessage());
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
	public String getStoreKey(String store_no) {
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String store_key = null;
		try {

			con = ds.getConnection();
			pstmt = con.prepareStatement(GET_STORE_KEY);
			pstmt.setString(1, store_no);


			rs = pstmt.executeQuery();
			while (rs.next()) {
				store_key = rs.getString("store_key");
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
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
		return store_key;
	}
	
	@Override
	public List<StoreVO> getTopStores() {
		List<StoreVO> list = new ArrayList<StoreVO>(); 
		StoreVO storeVO = null; 
		
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		
		try {
			con = ds.getConnection();
			pstmt = con.prepareStatement(GET_TOP_STORE);
			rs = pstmt.executeQuery();
			
			while (rs.next()) {
				storeVO = new StoreVO();
				storeVO.setStore_no(rs.getString("store_no"));
				storeVO.setStore_name(rs.getString("store_name"));
				storeVO.setStore_ads(rs.getString("store_ads"));
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
	public void updatePwd(String store_no,String new_pwd) {
		Connection con = null;
		PreparedStatement pstmt = null;
		try {
			con = ds.getConnection();

			pstmt = con.prepareStatement(UPDATE_PWD);
			
			pstmt.setString(1, new_pwd);
			pstmt.setString(2, store_no);

			pstmt.executeUpdate();

		} catch (SQLException e) {
			throw new RuntimeException("A database error occured. " + e.getMessage());
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
}