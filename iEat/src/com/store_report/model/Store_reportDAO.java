package com.store_report.model;

import java.util.*;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

import java.sql.*; 

public class Store_reportDAO implements Store_reportDAO_interface{
	private static DataSource ds = null; 
	static {
		try{
			Context ctx = new InitialContext(); 
			ds = (DataSource) ctx.lookup("java:comp/env/jdbc/iEatDB"); 
		} catch (NamingException e){ 
			e.printStackTrace(); 
		}
	}
	
	
	private static final String INSERT_STMT = 
			"INSERT INTO store_report (store_report_no, store_no, mem_no, store_report_content, store_report_date, store_report_status) " 
			+ " VALUES ('SR'||LPAD(STORE_REPORT_SEQ.NEXTVAL,8,'0'), ?,?,?,sysdate,?)"; 
	private static final String GET_ALL_STMT = 
			"SELECT store_report_no, store_no, mem_no, store_report_content, to_char(store_report_date, 'yyyy-mm-dd') store_report_date, store_report_status "
			+ " FROM store_report ORDER BY store_report_no desc"; 
	private static final String GET_ONE_STMT = 
			"SELECT store_report_no, store_no, mem_no, store_report_content, to_char(store_report_date, 'yyyy-mm-dd') store_report_date, store_report_status " 
			+ " FROM store_report WHERE store_report_no = ?"; 
	private static final String DELETE = 
			"DELETE FROM store_report WHERE store_report_no = ?"; 
	private static final String UPDATE = 
			"UPDATE store_report set store_no=?, mem_no=?, store_report_content=?, store_report_date=?, store_report_status=? " 
			+ " WHERE store_report_no=? "; 
	private static final String GET_MORE_BY_STORE_REPORT_STATUS = 
			"SELECT store_report_no, store_no, mem_no, store_report_content, to_char(store_report_date, 'yyyy-mm-dd') store_report_date, store_report_status "
					+ " FROM store_report WHERE store_report_status=? ORDER BY store_report_no desc";
	@Override
	public void insert(Store_reportVO store_reportVO) {
		Connection con = null;
		PreparedStatement pstmt = null;
		
		try {
			con = ds.getConnection(); 
			pstmt = con.prepareStatement(INSERT_STMT);
			
			pstmt.setString(1, store_reportVO.getStore_no()); 
			pstmt.setString(2, store_reportVO.getMem_no());
			pstmt.setString(3, store_reportVO.getStore_report_content());
			pstmt.setString(4, store_reportVO.getStore_report_status());
			
			pstmt.executeUpdate(); 
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

	@Override
	public void update(Store_reportVO store_reportVO) {
		Connection con = null;
		PreparedStatement pstmt = null;
		
		try {
			con = ds.getConnection(); 
			pstmt = con.prepareStatement(UPDATE);
			
			pstmt.setString(1, store_reportVO.getStore_no()); 
			pstmt.setString(2, store_reportVO.getMem_no());
			pstmt.setString(3, store_reportVO.getStore_report_content());
			pstmt.setDate(4, store_reportVO.getStore_report_date());
			pstmt.setString(5, store_reportVO.getStore_report_status());
			pstmt.setString(6, store_reportVO.getStore_report_no());
			pstmt.executeUpdate();
			
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

	@Override
	public void delete(String store_report_no) {
		Connection con = null;
		PreparedStatement pstmt = null;
		
		try {
			con = ds.getConnection(); 
			pstmt = con.prepareStatement(DELETE);
			
			pstmt.setString(1, store_report_no); 
			pstmt.executeUpdate(); 
		}  catch (SQLException se) {
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
	public Store_reportVO findByPrimaryKey(String store_report_no) {
		Store_reportVO store_reportVO = null; 
		Connection con = null; 
		PreparedStatement pstmt = null;  
		ResultSet rs = null; 
		
		try {
			con = ds.getConnection(); 
			pstmt = con.prepareStatement(GET_ONE_STMT);
			
			pstmt.setString(1, store_report_no);
			rs = pstmt.executeQuery(); 
			
			while(rs.next()) {
				store_reportVO = new Store_reportVO(); 
				store_reportVO.setStore_report_no(rs.getString("store_report_no"));
				store_reportVO.setStore_no(rs.getString("store_no")); 
				store_reportVO.setMem_no(rs.getString("mem_no"));
				store_reportVO.setStore_report_content(rs.getString("store_report_content"));
				store_reportVO.setStore_report_date(rs.getDate("store_report_date"));
				store_reportVO.setStore_report_status(rs.getString("store_report_status"));				
			}
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
		return store_reportVO;
	}

	@Override
	public List<Store_reportVO> getAll() {
		List<Store_reportVO> list = new ArrayList<Store_reportVO>(); 
		Store_reportVO store_reportVO = null; 
		
		Connection con = null; 
		PreparedStatement pstmt = null; 
		ResultSet rs = null; 
		
		try {
			con = ds.getConnection(); 
			pstmt = con.prepareStatement(GET_ALL_STMT);
			rs = pstmt.executeQuery();
			while(rs.next()) {
				store_reportVO = new Store_reportVO(); 
				store_reportVO.setStore_report_no(rs.getString("store_report_no"));
				store_reportVO.setStore_no(rs.getString("store_no")); 
				store_reportVO.setMem_no(rs.getString("mem_no"));
				store_reportVO.setStore_report_content(rs.getString("store_report_content"));
				store_reportVO.setStore_report_date(rs.getDate("store_report_date"));
				store_reportVO.setStore_report_status(rs.getString("store_report_status"));
				list.add(store_reportVO); 
			}
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

	@Override
	public List<Store_reportVO> getMoreByStoreReportStatus(String store_report_status) {
		List<Store_reportVO> list = new ArrayList<Store_reportVO>(); 
		Store_reportVO store_reportVO = null; 
		
		Connection con = null; 
		PreparedStatement pstmt = null; 
		ResultSet rs = null; 
		
		try {
			
			con = ds.getConnection();
			pstmt = con.prepareStatement(GET_MORE_BY_STORE_REPORT_STATUS);
			pstmt.setString(1, store_report_status);
			rs = pstmt.executeQuery();
			
			while(rs.next()) {
				store_reportVO = new Store_reportVO(); 
				store_reportVO.setStore_report_no(rs.getString("store_report_no"));
				store_reportVO.setStore_no(rs.getString("store_no")); 
				store_reportVO.setMem_no(rs.getString("mem_no"));
				store_reportVO.setStore_report_content(rs.getString("store_report_content"));
				store_reportVO.setStore_report_date(rs.getDate("store_report_date"));
				store_reportVO.setStore_report_status(rs.getString("store_report_status"));
				list.add(store_reportVO); 
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
		return list;
	}	
}
