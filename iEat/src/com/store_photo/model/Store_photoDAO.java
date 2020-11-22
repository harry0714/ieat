package com.store_photo.model;

import java.util.*;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

import java.sql.*;
import java.io.*; 

public class Store_photoDAO implements Store_photoDAO_interface{
	private static DataSource ds = null;
	static {
		try {
			Context ctx = new InitialContext();
			ds = (DataSource) ctx.lookup("java:comp/env/jdbc/iEatDB");
		} catch (NamingException e) {
			e.printStackTrace();
		}
	}
	
	private static final String INSERT_STMT = "INSERT INTO store_photo (photo_no, store_no, photo_name, photo, photo_des) "
			+ " VALUES ('P'||LPAD(PHOTO_SEQ.NEXTVAL,9,'0'), ?,?,?,?)"; 
	
	private static final String GET_ALL_STMT = "SELECT photo_no, store_no, photo_name, photo, photo_des FROM store_photo "
			+ " ORDER BY photo_no"; 
	
	private static final String GET_ONE_STMT = "SELECT photo_no, store_no, photo_name, photo, photo_des "
			+" FROM store_photo WHERE photo_no = ? "; 
	private static final String DELETE = "DELETE FROM store_photo where photo_no = ?";
	private static final String UPDATE = 
			"UPDATE store_photo set store_no=?, photo_name=?, photo=?, photo_des=? where photo_no=? ";
	//取得店家的第一張照片
	private static final String FIRST_STORE = 
			"select photo_no, photo from store_photo where store_no = ? and rownum = 1";
	private static final String GET_ONE_STORE_PHOTO_STMT = "SELECT photo_no, store_no, photo_name, photo, photo_des "
			+" FROM store_photo WHERE store_no = ? "; 
	private static final String GET_COUNT_STORE_PHOTO_STMT = 
			"SELECT count (*) from store_photo where store_no=?";
	private static final String GET_ONE_STORE_PHOTO_STMT2 = 
			"SELECT photo_no, store_no, photo_name, photo, photo_des from(select photo_no, store_no, photo_name, photo, photo_des, rownum as rn FROM store_photo where store_no=?) where rn=?";
	
	@Override
	public void insert(Store_photoVO store_photoVO) {
		Connection con = null; 
		PreparedStatement pstmt = null; 
		
		try {
			con = ds.getConnection();
			pstmt = con.prepareStatement(INSERT_STMT);
			
			pstmt.setString(1, store_photoVO.getStore_no());
			pstmt.setString(2, store_photoVO.getPhoto_name());
			pstmt.setBytes(3, store_photoVO.getPhoto()); 
			pstmt.setString(4, store_photoVO.getPhoto_des());
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
	public void update(Store_photoVO store_photoVO) {
		Connection con = null;
		PreparedStatement pstmt = null;
		
		try {
			con = ds.getConnection();
			pstmt = con.prepareStatement(UPDATE);
			
			pstmt.setString(1, store_photoVO.getStore_no()); 
			pstmt.setString(2, store_photoVO.getPhoto_name()); 
			pstmt.setBytes(3, store_photoVO.getPhoto()); 
			pstmt.setString(4, store_photoVO.getPhoto_des()); 
			pstmt.setString(5, store_photoVO.getPhoto_no()); 
			
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
	public void delete(String photo_no) {
		Connection con = null; 
		PreparedStatement pstmt = null; 
		
		try {
			con = ds.getConnection();
			pstmt = con.prepareStatement(DELETE);
			
			pstmt.setString(1, photo_no); 
			
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
	public Store_photoVO findByPrimaryKey(String photo_no) {
		Store_photoVO store_photoVO = null; 
		Connection con = null; 
		PreparedStatement pstmt = null; 
		ResultSet rs = null; 
		
		try{
			con = ds.getConnection();
			pstmt = con.prepareStatement(GET_ONE_STMT);
			
			pstmt.setString(1, photo_no);
			
			rs = pstmt.executeQuery(); 
			
			while(rs.next()) {
				store_photoVO = new Store_photoVO(); 
				store_photoVO.setPhoto_no(rs.getString("photo_no"));
				store_photoVO.setStore_no(rs.getString("store_no"));
				store_photoVO.setPhoto_name(rs.getString("photo_name"));
				store_photoVO.setPhoto(rs.getBytes("photo"));
				store_photoVO.setPhoto_des(rs.getString("photo_des")); 				
			}
		}  catch (SQLException se) {
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
		return store_photoVO;
	}

	@Override
	public List<Store_photoVO> getAll() {
		List<Store_photoVO> list = new ArrayList<Store_photoVO>(); 
		Store_photoVO store_photoVO = null; 
		
		Connection con = null; 
		PreparedStatement pstmt = null; 
		ResultSet rs = null;
		
		try {
			
			con = ds.getConnection();
			pstmt = con.prepareStatement(GET_ALL_STMT); 
			rs = pstmt.executeQuery(); 
			
			while(rs.next()) {
				store_photoVO = new Store_photoVO(); 
				store_photoVO.setPhoto_no(rs.getString("photo_no"));
				store_photoVO.setStore_no(rs.getString("store_no"));
				store_photoVO.setPhoto_name(rs.getString("photo_name"));
				store_photoVO.setPhoto(rs.getBytes("photo"));
				store_photoVO.setPhoto_des(rs.getString("photo_des"));
				list.add(store_photoVO); 
			}
		}  catch (SQLException se) {
			throw new RuntimeException("A database error occured. "
					+ se.getMessage());
			// Clean up JDBC resources
		} finally {
			if(rs != null) {
				try{
					rs.close();
				} catch(SQLException se) {
					se.printStackTrace(System.err);
				}
			}
			if(pstmt != null) {
				try{
					pstmt.close();
				} catch (SQLException se) {
					se.printStackTrace(System.err); 
				}
			}
			if(con != null) {
				try{
					con.close();
				} catch (SQLException se) {
					se.printStackTrace(System.err); 
				}
			}
		}
		return list;
	}

	@Override
	public Store_photoVO findFirstStore(String store_no) {
		// TODO Auto-generated method stub
		Store_photoVO store_photoVO = null; 
		Connection con = null; 
		PreparedStatement pstmt = null; 
		ResultSet rs = null; 
		
		try{
			con = ds.getConnection();
			pstmt = con.prepareStatement(FIRST_STORE);
			
			pstmt.setString(1, store_no);
			
			rs = pstmt.executeQuery(); 
			
			while(rs.next()) {
				store_photoVO = new Store_photoVO(); 
				store_photoVO.setPhoto_no(rs.getString("photo_no"));
				store_photoVO.setPhoto(rs.getBytes("photo"));
			}
		}  catch (SQLException se) {
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
		return store_photoVO;
	}
	@Override
	public List<Store_photoVO> getOneStorePhoto(String store_no) {
		List<Store_photoVO> list = new ArrayList<Store_photoVO>(); 
		Store_photoVO store_photoVO = null; 
		
		Connection con = null; 
		PreparedStatement pstmt = null; 
		ResultSet rs = null;
		
		try {
			
			con = ds.getConnection();
			pstmt = con.prepareStatement(GET_ONE_STORE_PHOTO_STMT); 
			pstmt.setString(1,store_no);
			rs = pstmt.executeQuery(); 
			
			while(rs.next()) {
				store_photoVO = new Store_photoVO(); 
				store_photoVO.setPhoto_no(rs.getString("photo_no"));
				store_photoVO.setStore_no(rs.getString("store_no"));
				store_photoVO.setPhoto_name(rs.getString("photo_name"));
				store_photoVO.setPhoto(rs.getBytes("photo"));
				store_photoVO.setPhoto_des(rs.getString("photo_des"));
				list.add(store_photoVO); 
			}
		}  catch (SQLException se) {
			throw new RuntimeException("A database error occured. "
					+ se.getMessage());
			// Clean up JDBC resources
		} finally {
			if(rs != null) {
				try{
					rs.close();
				} catch(SQLException se) {
					se.printStackTrace(System.err);
				}
			}
			if(pstmt != null) {
				try{
					pstmt.close();
				} catch (SQLException se) {
					se.printStackTrace(System.err); 
				}
			}
			if(con != null) {
				try{
					con.close();
				} catch (SQLException se) {
					se.printStackTrace(System.err); 
				}
			}
		}
		return list;
	}

	@Override
	public int countStorePhoto(String store_no) {
		Store_photoVO store_photoVO = null; 
		
		Connection con = null; 
		PreparedStatement pstmt = null; 
		ResultSet rs = null;
		int count=0;
		try {
			
			con = ds.getConnection();
			pstmt = con.prepareStatement(GET_COUNT_STORE_PHOTO_STMT); 
			pstmt.setString(1,store_no);
			rs = pstmt.executeQuery(); 
			
			while(rs.next()){
				count = rs.getInt(1);
			}
			
		}  catch (SQLException se) {
			throw new RuntimeException("A database error occured. "
					+ se.getMessage());
			// Clean up JDBC resources
		} finally {
			if(rs != null) {
				try{
					rs.close();
				} catch(SQLException se) {
					se.printStackTrace(System.err);
				}
			}
			if(pstmt != null) {
				try{
					pstmt.close();
				} catch (SQLException se) {
					se.printStackTrace(System.err); 
				}
			}
			if(con != null) {
				try{
					con.close();
				} catch (SQLException se) {
					se.printStackTrace(System.err); 
				}
			}
		}
		return count;
	}

	@Override
	public Store_photoVO getOneStorePhoto2(String store_no, Integer rownum) {
		Store_photoVO store_photoVO = null; 
		
		Connection con = null; 
		PreparedStatement pstmt = null; 
		ResultSet rs = null;
		
		try {
			
			con = ds.getConnection();
			pstmt = con.prepareStatement(GET_ONE_STORE_PHOTO_STMT2); 
			pstmt.setString(1,store_no);
			pstmt.setInt(2,rownum);
			rs = pstmt.executeQuery(); 
			
			while(rs.next()) {
				store_photoVO = new Store_photoVO(); 
				store_photoVO.setPhoto_no(rs.getString("photo_no"));
				store_photoVO.setStore_no(rs.getString("store_no"));
				store_photoVO.setPhoto_name(rs.getString("photo_name"));
				store_photoVO.setPhoto(rs.getBytes("photo"));
				store_photoVO.setPhoto_des(rs.getString("photo_des"));
			}
			
		}  catch (SQLException se) {
			throw new RuntimeException("A database error occured. "
					+ se.getMessage());
			// Clean up JDBC resources
		} finally {
			if(rs != null) {
				try{
					rs.close();
				} catch(SQLException se) {
					se.printStackTrace(System.err);
				}
			}
			if(pstmt != null) {
				try{
					pstmt.close();
				} catch (SQLException se) {
					se.printStackTrace(System.err); 
				}
			}
			if(con != null) {
				try{
					con.close();
				} catch (SQLException se) {
					se.printStackTrace(System.err); 
				}
			}
		}
		return store_photoVO;
	}	
}
