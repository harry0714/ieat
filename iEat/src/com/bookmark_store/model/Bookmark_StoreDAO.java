package com.bookmark_store.model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

public class Bookmark_StoreDAO implements Bookmark_StoreDAO_interface{
private static DataSource ds =null;
	
	static {
		try {
			Context ctx = new InitialContext();
			ds = (DataSource)ctx.lookup("java:comp/env/jdbc/iEatDB");
		} catch (NamingException e) {
			e.printStackTrace();
		}
	}

	private static final String INSERT_STMT = 
			"INSERT INTO bookmark_store VALUES (?, ?)";
		private static final String GET_ALL_STMT = 
			"SELECT * FROM bookmark_store order by mem_no";
		private static final String GET_ONE_STMT = 
			"SELECT * FROM bookmark_store where mem_no=? and store_no=? ";
		private static final String DELETE = 
			"DELETE FROM bookmark_store where mem_no = ? and store_no = ?";
		
		//此TABLE只有兩個欄位且都為PK 無法UPDATE
		private static final String UPDATE = 
			"UPDATE bookmark_store set MEM_NO=?, store_no = ? where mem_no = ? and store_no = ?";
		private static final String GET_ALL_STMT_BY_MEMNO = 
				"SELECT * FROM bookmark_store where mem_no=? order by store_no";
		private static final String GET_STORENO_STMT_BY_MEMNO = 
				"SELECT store_no FROM bookmark_store where mem_no=? order by store_no";
	

	@Override
	public void insert(Bookmark_StoreVO bookmark_storeVO) {
		Connection con = null;
		PreparedStatement pstmt = null;
		
		try {
			con = ds.getConnection();
			pstmt = con.prepareStatement(INSERT_STMT);
			pstmt.setString(1, bookmark_storeVO.getMem_no());
			pstmt.setString(2, bookmark_storeVO.getStore_no());
			
			pstmt.executeUpdate();
			
		} catch (SQLException e) {
			throw new RuntimeException("A database error occured. "
					+ e.getMessage());
		}finally{
			if(pstmt!=null){
				try {
					pstmt.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			
			if(con !=null){
				try {
					con.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}
		
	}

	@Override
	public void update(Bookmark_StoreVO bookmark_storeVO) {
		
	}

	@Override
	public void delete(String mem_no, String store_no) {
		Connection con = null;
		PreparedStatement pstmt = null;
		try {
			con = ds.getConnection();
			pstmt = con.prepareStatement(DELETE);
			pstmt.setString(1, mem_no);
			pstmt.setString(2, store_no);
			
			pstmt.executeUpdate();
		} catch (SQLException e) {
			throw new RuntimeException("A database error occured. "
					+ e.getMessage());
		}finally{
			if(pstmt!=null){
				try {
					pstmt.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			if(con!=null){
				try {
					con.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}
		
	}

	@Override
	public Bookmark_StoreVO findByPrimaryKey(String mem_no, String store_no)  {
		Connection con = null;
		PreparedStatement pstmt = null;
		Bookmark_StoreVO bookmark_storeVO = null;
		ResultSet rs = null;
		try {
			con = ds.getConnection();
			pstmt = con.prepareStatement(GET_ONE_STMT);
			pstmt.setString(1,mem_no);
			pstmt.setString(2,store_no);
			rs = pstmt.executeQuery();
			
			while(rs.next()){
				bookmark_storeVO = new Bookmark_StoreVO();
				bookmark_storeVO.setMem_no(rs.getString("mem_no"));
				bookmark_storeVO.setStore_no(rs.getString("store_no"));
			}
			
		} catch (SQLException e) {
			throw new RuntimeException("A database error occured. "
					+ e.getMessage());
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
		return bookmark_storeVO;
	}

	@Override
	public List<Bookmark_StoreVO> getAll()  {
		List<Bookmark_StoreVO> list = new ArrayList<>();
		Bookmark_StoreVO bookmark_storeVO = null;
		Connection con = null;
		PreparedStatement pstmt = null; 
		ResultSet rs = null;
		
		try {
			con = ds.getConnection();
			pstmt = con.prepareStatement(GET_ALL_STMT);
			rs = pstmt.executeQuery();
			
			while(rs.next()){
				bookmark_storeVO = new Bookmark_StoreVO();
				bookmark_storeVO.setMem_no(rs.getString("mem_no"));
				bookmark_storeVO.setStore_no(rs.getString("store_no"));
				list.add(bookmark_storeVO);
			}
		} catch (SQLException e) {
			throw new RuntimeException("A database error occured. "
					+ e.getMessage());
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
	public List<Bookmark_StoreVO> getMoreByMemNo(String mem_no) {
		List<Bookmark_StoreVO> list = new ArrayList<>();
		Bookmark_StoreVO bookmark_storeVO = null;
		Connection con = null;
		PreparedStatement pstmt = null; 
		ResultSet rs = null;
		
		try {
			con = ds.getConnection();
			pstmt = con.prepareStatement(GET_ALL_STMT_BY_MEMNO);
			pstmt.setString(1, mem_no);
			rs = pstmt.executeQuery();
			
			while(rs.next()){
				bookmark_storeVO = new Bookmark_StoreVO();
				bookmark_storeVO.setMem_no(rs.getString("mem_no"));
				bookmark_storeVO.setStore_no(rs.getString("store_no"));
				list.add(bookmark_storeVO);
			}
		} catch (SQLException e) {
			throw new RuntimeException("A database error occured. "
					+ e.getMessage());
		}finally {
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
	public List<String> findStoreNoByMemNo(String mem_no) {
		List<String> list = new ArrayList<>();
		String store_no = null;
		Connection con = null;
		PreparedStatement pstmt = null; 
		ResultSet rs = null;
		
		try {
			con = ds.getConnection();
			pstmt = con.prepareStatement(GET_STORENO_STMT_BY_MEMNO);
			pstmt.setString(1, mem_no);
			rs = pstmt.executeQuery();
			
			while(rs.next()){
				store_no = rs.getString("store_no");
				list.add(store_no);
			}
		} catch (SQLException e) {
			throw new RuntimeException("A database error occured. "
					+ e.getMessage());
		}finally {
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
