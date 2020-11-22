package com.ord.model;

import java.util.*;
import java.util.Date;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.io.IOException;
import java.io.*;
import java.sql.*;
import java.text.SimpleDateFormat;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

import com.QRCode.java.QRCode;
import com.google.zxing.EncodeHintType;
import com.google.zxing.WriterException;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;
import com.ord_meal.model.Ord_mealDAO;
import com.ord_meal.model.Ord_mealVO;

public class OrdAppDAO implements OrdAppDAO_interface {
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
	private static final String INSERT_STMT = "INSERT INTO ORD(ORD_NO,MEM_NO,STORE_NO,ORD_DATE,ORD_PICKUP,ord_qrcode,ORD_QRCODE_STATUS,ORD_PAID,ORD_REPORT,ORD_REPORT_STATUS) VALUES ('O'||TRIM(TO_CHAR(ORD_NO_SQ.NEXTVAL,'000000000')),?,?,?,?,?,?,?,?,?)";

	private static final String GET_ONE_STMT = "SELECT ORD_NO,MEM_NO,STORE_NO,ORD_DATE,ORD_PICKUP,ord_qrcode,ORD_QRCODE_STATUS,ORD_PAID,ORD_REPORT,ORD_REPORT_STATUS FROM ORD where ORD_NO = ?";

	private static final String UPDATE = "UPDATE ORD set MEM_NO=?,STORE_NO=?,ORD_DATE=?,ORD_PICKUP=?,ORD_QRCODE_STATUS=?,ORD_PAID=?,ORD_REPORT=?,ORD_REPORT_STATUS=? WHERE ORD_NO=?";

	private static final String GET_MORE_STMT_BY_MEMBER = "SELECT * FROM ORD where MEM_NO = ? order by ord_no desc";
	private static final String GET_ORDMEALS_BY_ORDNO = "SELECT * FROM ord_meal where ord_no = ?";
	private static final String GETTOPFIVESTORE = "select rownum rank ,store_no from (select count(*) count, store_no from ord group by store_no order by count desc) where rownum <=5";
	private static final String UPDATE_STATUS = "UPDATE ORD set ORD_QRCODE_STATUS=? WHERE ORD_NO=?";
	private static final String GET_MEM_ORD = "SELECT ORD_NO,MEM_NO,ord.STORE_NO,s.store_name,to_char(ORD_DATE,'yyyy-MM-dd hh:mm:ss') ORD_DATE,to_char(ORD_PICKUP,'yyyy-MM-dd hh:mm:ss') ORD_PICKUP,ORD_QRCODE_STATUS,ORD_PAID,ORD_REPORT,ORD_REPORT_STATUS FROM ORD left join store s on ord.store_no = s.store_no where MEM_NO =? order by ord_no desc";
	private static final String GET_MEM_ORD_MEAL = "SELECT ord_meal_no,ord_no, om.meal_no,ord_meal_qty,ord_meal_price, meal_name FROM ord_meal om left join meal m on om.meal_no = m.meal_no where ord_no = ? ";
	private static final String UPDATE_QR = "UPDATE ORD set ORD_QRCODE_STATUS=? WHERE ORD_NO=?";
	private static final String GET_QRCODE_BY_ORD_NO = 
			"SELECT ORD_QRCODE FROM ORD WHERE ORD_NO=?";
	private static final String GET_STORE_ORD = "SELECT m.mem_name, mem_sex, ORD_NO,ord.MEM_NO,ord.STORE_NO,to_char(ORD_DATE,'yyyy-MM-dd hh:mm:ss') ORD_DATE,to_char(ORD_PICKUP,'yyyy-MM-dd hh:mm:ss') ORD_PICKUP,ORD_QRCODE_STATUS,ORD_PAID,ORD_REPORT,ORD_REPORT_STATUS FROM ORD left join member m on ord.mem_no = m.mem_no where STORE_NO =? order by ORD_PICKUP DESC";
	private static final String UPDATE_PAID = "UPDATE ORD set ORD_QRCODE_STATUS=0, ORD_PAID =0  WHERE ORD_NO=?";
	private static final String COUNT_UNCONFIRM = "SELECT count(*) from ORD WHERE ORD_QRCODE_STATUS=4 AND STORE_NO=?";
	
	public void insert(OrdVO ordVO) {
		// TODO Auto-generated method stub
		Connection con = null;
		PreparedStatement pstmt = null;

		try {

			con = ds.getConnection();
			pstmt = con.prepareStatement(INSERT_STMT);

			pstmt.setString(1, ordVO.getMem_no());
			pstmt.setString(2, ordVO.getStore_no());
			pstmt.setTimestamp(3, ordVO.getOrd_date());
			pstmt.setTimestamp(4, ordVO.getOrd_pickup());
			pstmt.setBytes(5, ordVO.getOrd_qrcode());
			pstmt.setString(6, ordVO.getOrd_qrcode_status());
			pstmt.setInt(7, ordVO.getOrd_paid());
			pstmt.setString(8, ordVO.getOrd_report());
			pstmt.setString(9, ordVO.getOrd_report_status());

			pstmt.executeUpdate();

			// Handle any driver errors
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



	@Override
	public void update(OrdVO ordVO) {
		// TODO Auto-generated method stub

		Connection con = null;
		PreparedStatement pstmt = null;

		try {

			con = ds.getConnection();
			pstmt = con.prepareStatement(UPDATE);

			pstmt.setString(1, ordVO.getMem_no());
			pstmt.setString(2, ordVO.getStore_no());
			pstmt.setTimestamp(3, ordVO.getOrd_date());
			pstmt.setTimestamp(4, ordVO.getOrd_pickup());
			pstmt.setString(5, ordVO.getOrd_qrcode_status());
			pstmt.setInt(6, ordVO.getOrd_paid());
			pstmt.setString(7, ordVO.getOrd_report());
			pstmt.setString(8, ordVO.getOrd_report_status());
			pstmt.setString(9, ordVO.getOrd_no());

			pstmt.executeUpdate();

			// Handle any driver errors
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

	@Override
	public OrdVO findByPrimaryKey(String ordVO1) {
		// TODO Auto-generated method stub
		OrdVO ordVO = null;
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;

		try {

			con = ds.getConnection();
			pstmt = con.prepareStatement(GET_ONE_STMT);

			pstmt.setString(1, ordVO1);

			rs = pstmt.executeQuery();

			while (rs.next()) {
				// empVo 也稱為 Domain objects
				ordVO = new OrdVO();
				ordVO.setOrd_no(rs.getString("ord_no"));
				ordVO.setMem_no(rs.getString("mem_no"));
				ordVO.setStore_no(rs.getString("store_no"));
				ordVO.setOrd_date(rs.getTimestamp("ord_date"));
				ordVO.setOrd_pickup(rs.getTimestamp("ord_pickup"));
				ordVO.setOrd_qrcode(null);
				ordVO.setOrd_qrcode_status(rs.getString("ord_qrcode_status"));
				ordVO.setOrd_paid(rs.getInt("ord_paid"));
				ordVO.setOrd_report(rs.getString("ord_report"));
				ordVO.setOrd_report_status(rs.getString("ord_report_status"));
			}

			// Handle any driver errors
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
		return ordVO;
	}


	@Override
	public List<OrdVO> getMoreByMember(String mem_no) {
		List<OrdVO> list = new ArrayList<OrdVO>();
		OrdVO ordVO = null;

		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;

		try {

			con = ds.getConnection();
			pstmt = con.prepareStatement(GET_MORE_STMT_BY_MEMBER);
			pstmt.setString(1, mem_no);
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

			// Handle any driver errors
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
		return list;
	}

	@Override
	public List<Ord_mealVO> getOrdMealsByOrdno(String ord_no) {
		List<Ord_mealVO> list = new ArrayList<>();
		Ord_mealVO ord_mealVO = null;

		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;

		try {

			con = ds.getConnection();
			pstmt = con.prepareStatement(GET_ORDMEALS_BY_ORDNO);
			pstmt.setString(1, ord_no);
			rs = pstmt.executeQuery();

			while (rs.next()) {
				// ordVO 也稱為 Domain objects
				ord_mealVO = new Ord_mealVO();
				ord_mealVO.setOrd_meal_no(rs.getString("ord_meal_no"));
				ord_mealVO.setOrd_no(rs.getString("ord_no"));
				ord_mealVO.setMeal_no(rs.getString("meal_no"));
				ord_mealVO.setOrd_meal_qty(rs.getInt("ord_meal_qty"));
				ord_mealVO.setOrd_meal_price(rs.getInt("ord_meal_price"));

				list.add(ord_mealVO); // Store the row in the list
			}

			// Handle any driver errors
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
		return list;
	}

	@Override
	public void insertOrd(Map<String, OrdVO> ordlist, Map<String, List<Ord_mealVO>> itemlist) {
		List<String> next_ord_nos = new ArrayList<String>();
		List<Timestamp> Timestamps = new ArrayList<Timestamp>();

		Connection con = null;
		PreparedStatement pstmt = null;
		try {
			con = ds.getConnection();
			con.setAutoCommit(false);

			Set<String> keys = ordlist.keySet();
			String cols[] = { "ord_no" };
			int t = 0;
			for (String key : keys) {
				t++;
				pstmt = con.prepareStatement(INSERT_STMT, cols);
				OrdVO ordVO = ordlist.get(key);
				pstmt.setString(1, ordVO.getMem_no());
				pstmt.setString(2, ordVO.getStore_no());
				pstmt.setTimestamp(3, ordVO.getOrd_date());
				pstmt.setTimestamp(4, ordVO.getOrd_pickup());
				pstmt.setBytes(5, ordVO.getOrd_qrcode());
				pstmt.setString(6, ordVO.getOrd_qrcode_status());
				pstmt.setInt(7, ordVO.getOrd_paid());
				pstmt.setString(8, ordVO.getOrd_report());
				pstmt.setString(9, ordVO.getOrd_report_status());
				pstmt.executeUpdate();

				String next_ord_no = null;
				ResultSet rs = pstmt.getGeneratedKeys();
				if (rs.next()) {
					next_ord_no = rs.getString(1);
					System.out.println("自增主鍵值= " + next_ord_no + "(剛新增成功的訂單編號)");
				} else {
					System.out.println("未取得自增主鍵值");
				}
				rs.close();
				next_ord_nos.add(next_ord_no);
				Timestamps.add(ordVO.getOrd_pickup());
				List<Ord_mealVO> ord_meallist = itemlist.get(key);

				Ord_mealDAO ord_mealDAO = new Ord_mealDAO();

				for (Ord_mealVO ord_mealVO : ord_meallist) {
					ord_mealVO.setOrd_no(next_ord_no);
					ord_mealDAO.insertMany(ord_mealVO, con);
				}

				///////// QRCODE//////////
				String charset = "UTF-8";
				Map<EncodeHintType, ErrorCorrectionLevel> hintMap = new HashMap<EncodeHintType, ErrorCorrectionLevel>();
				hintMap.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.L);

				///////// QRCODE//////////

				////// 新增QRCode圖片進資料庫///////
				try {
					String qrCodeData = "ord_no:" + next_ord_no;
					String ord_no = next_ord_no;
					pstmt = con.prepareStatement("update ord set ord_qrcode=? where ord_no='" + ord_no + "'");// 修改SQL指令
					pstmt.setBytes(1, QRCode.createQRCode(qrCodeData, charset, hintMap, 400, 400).toByteArray());
					int rowsUpdated = pstmt.executeUpdate();
					if (1 == rowsUpdated) {
						System.out.println(" row.");
					} else
						System.out.println("updated " + ord_no + " is fail"); // 錯誤訊息

				} catch (Exception e) {
					e.printStackTrace();
				}
				////// 新增QRCode圖片進資料庫///////

			}

			// 2●設定於 pstm.executeUpdate()之後
			con.commit();
			con.setAutoCommit(true);
			////// 執行緒///////
			if (next_ord_nos.size() != 0) {
				for (int i = 0; i < next_ord_nos.size(); i++) {
					Thread thread = new Thread(new Work(next_ord_nos.get(i), Timestamps.get(i)));
					thread.start();
				}
			}
			////// 執行緒///////
			// Handle any driver errors
		} catch (SQLException se) {
			if (con != null) {
				try {
					// 3●設定於當有exception發生時之catch區塊內
					System.err.print("Transaction is being ");
					se.printStackTrace();
					System.err.println("rolled back-由-order");
					con.rollback();
				} catch (SQLException excep) {
					throw new RuntimeException("rollback error occured. " + excep.getMessage());
				}
			}
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
	public List<OrdVO> getTopFiveStore() {
		// TODO Auto-generated method stub
		List<OrdVO> list = new ArrayList<OrdVO>();
		OrdVO ordVO = null;

		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;

		try {

			con = ds.getConnection();
			pstmt = con.prepareStatement(GETTOPFIVESTORE);
			rs = pstmt.executeQuery();

			while (rs.next()) {
				// ordVO 也稱為 Domain objects
				ordVO = new OrdVO();
				ordVO.setStore_no(rs.getString("store_no"));
				list.add(ordVO); // Store the row in the list
			}

			// Handle any driver errors
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
		return list;
	}

	@Override
	public void update_status(OrdVO ordVO) {
		// TODO Auto-generated method stub
		Connection con = null;
		PreparedStatement pstmt = null;

		try {

			con = ds.getConnection();
			pstmt = con.prepareStatement(UPDATE_STATUS);

			pstmt.setString(1, ordVO.getOrd_qrcode_status());
			pstmt.setString(2, ordVO.getOrd_no());
			pstmt.executeUpdate();

			// Handle any driver errors
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

	////// 執行緒///////
	public static class Work implements Runnable {
		private String ord_no;
		private Timestamp ord_pickup;
		static OrdAppService ordSvc = new OrdAppService();

		public Work(String ord_no, Timestamp ord_pickup) {
			this.ord_no = ord_no;
			this.ord_pickup = ord_pickup;
		}

		public void run() {
			System.out.println("訂單:" + ord_no + "開始判斷有無取餐" + ",取餐時間為:" + ord_pickup);
			try {
				Timestamp nowDate = new Timestamp(System.currentTimeMillis());
				long t = ord_pickup.getTime() - nowDate.getTime();
				Thread.sleep(t);
			} catch (InterruptedException ex) {
				ex.printStackTrace();
			}
			OrdVO ordVO = ordSvc.getOneOrd(ord_no);
			String status = ordVO.getOrd_qrcode_status();
			if(status.equals("1") || status.equals("4")){
				 System.out.println("訂單:"+ord_no +"尚未取餐,開始更改資料庫狀態值");
				 ordVO.setOrd_qrcode_status("2");
				 ordSvc.update_status(ordVO.getOrd_qrcode_status(), ord_no);
				 
			}
			System.out.println("訂單:"+ord_no +"結束執行緒");
		}
	}
	////// 執行緒///////

	@Override
	public Map<String,OrdVO> getMemOrd(String mem_no) {
		Map<String,OrdVO> map = new LinkedHashMap<String,OrdVO>();
		OrdVO ordVO = null;
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;

		try {

			con = ds.getConnection();
			pstmt = con.prepareStatement(GET_MEM_ORD);
			pstmt.setString(1, mem_no);
			rs = pstmt.executeQuery();

			while (rs.next()) {
				// ordVO 也稱為 Domain objects
				ordVO = new OrdVO();
				ordVO.setOrd_no(rs.getString("ord_no"));
				ordVO.setMem_no(rs.getString("mem_no"));
				ordVO.setStore_no(rs.getString("store_no"));
				ordVO.setOrd_date(rs.getTimestamp("ord_date"));
				ordVO.setOrd_pickup(rs.getTimestamp("ord_pickup"));
				ordVO.setOrd_qrcode_status(rs.getString("ord_qrcode_status"));
				ordVO.setOrd_paid(rs.getInt("ord_paid"));
				ordVO.setOrd_report(rs.getString("ord_report"));
				ordVO.setOrd_report_status(rs.getString("ord_report_status"));

				map.put(rs.getString("ord_no")+","+rs.getString("store_name"),ordVO); // Store the row in the list
			}

			// Handle any driver errors
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
		return map;
	}
	
	@Override
	public Map<String,Ord_mealVO> getMemOrdMeal(String ord_no) {
		Map<String,Ord_mealVO> map = new LinkedHashMap<String,Ord_mealVO>();
		Ord_mealVO ord_mealVO = null;

		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;

		try {

			con = ds.getConnection();
			pstmt = con.prepareStatement(GET_MEM_ORD_MEAL);
			pstmt.setString(1, ord_no);
			rs = pstmt.executeQuery();

			while (rs.next()) {
				// ordVO 也稱為 Domain objects
				ord_mealVO = new Ord_mealVO();
				ord_mealVO.setOrd_meal_no(rs.getString("ord_meal_no"));
				ord_mealVO.setOrd_no(rs.getString("ord_no"));
				ord_mealVO.setMeal_no(rs.getString("meal_no"));
				ord_mealVO.setOrd_meal_qty(rs.getInt("ord_meal_qty"));
				ord_mealVO.setOrd_meal_price(rs.getInt("ord_meal_price"));

				map.put(rs.getString("ord_no")+","+rs.getString("meal_name"),ord_mealVO); // Store the row in the list
			}

			// Handle any driver errors
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
		return map;
	}
	
	@Override
	public void updateQrcodeStatus(String ord_no,int type) {
		// TODO Auto-generated method stub

		Connection con = null;
		PreparedStatement pstmt = null;

		try {

			con = ds.getConnection();
			pstmt = con.prepareStatement(UPDATE_QR);

			pstmt.setInt(1,type);
			pstmt.setString(2,ord_no);

			pstmt.executeUpdate();

			// Handle any driver errors
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
	
	@Override
	public byte[] getImage(String ord_no) {
		// TODO Auto-generated method stub
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		byte[] image = null;
		
		try{
			con = ds.getConnection();
			pstmt = con.prepareStatement(GET_QRCODE_BY_ORD_NO);
			pstmt.setString(1, ord_no);
			rs = pstmt.executeQuery();
			
			while(rs.next()){
				image = rs.getBytes(1);
			}
			
		}catch (SQLException se) {
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
		return image;
	}
	
	@Override
	public Map<String,OrdVO> getStoreOrd(String store_no) {
		Map<String,OrdVO> map = new LinkedHashMap<String,OrdVO>();
		OrdVO ordVO = null;

		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;

		try {

			con = ds.getConnection();
			pstmt = con.prepareStatement(GET_STORE_ORD);
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
				ordVO.setOrd_qrcode_status(rs.getString("ord_qrcode_status"));
				ordVO.setOrd_paid(rs.getInt("ord_paid"));
				ordVO.setOrd_report(rs.getString("ord_report"));
				ordVO.setOrd_report_status(rs.getString("ord_report_status"));

				String mem_name = (rs.getString("mem_sex").equals("1"))?rs.getString("mem_name").charAt(0)+"先生":rs.getString("mem_name").charAt(0)+"小姐";
				map.put(rs.getString("ord_no")+","+mem_name,ordVO); // Store the row in the list
			}

			// Handle any driver errors
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
		return map;
	}
	
	@Override
	public void updatePaiedStatus(String ord_no) {
		// TODO Auto-generated method stub

		Connection con = null;
		PreparedStatement pstmt = null;

		try {

			con = ds.getConnection();
			pstmt = con.prepareStatement(UPDATE_PAID);

			pstmt.setString(1,ord_no);

			pstmt.executeUpdate();

			
			// Handle any driver errors
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
	
	@Override
	public int getUnconfirm(String store_no) {
		// TODO Auto-generated method stub

		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		int count = 0;
		
		try {

			con = ds.getConnection();
			pstmt = con.prepareStatement(COUNT_UNCONFIRM);

			pstmt.setString(1,store_no);

			rs = pstmt.executeQuery();

			while(rs.next()){
				count = rs.getInt(1);
			}
			
			// Handle any driver errors
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
		return count;
	}
}
