package com.store_comment.model;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;


public class StoreCommentDAO implements StoreCommentDAO_interface{

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
			"insert into STORE_COMMENT (comment_no,store_no,mem_no,comment_conect,comment_time,comment_level) values ('C'||TRIM(TO_CHAR(COMMENT_NO_SQ.NEXTVAL,'000000000')), ?, ?, ?, ?, ?)";
	private static final String GET_ALL_STMT = 
			"select comment_no,store_no,mem_no,comment_conect,comment_time,comment_level from store_comment order by comment_no";
	private static final String GET_ONE_STMT = 
			"select comment_no,store_no,mem_no,comment_conect,comment_time,comment_level from store_comment where comment_no = ?";
	private static final String GET_MEM_STMT = 
			"select comment_no,store_no,mem_no,comment_conect,comment_time,comment_level from store_comment where mem_no = ?";
	private static final String DELETE = 
			"delete from store_comment where comment_no = ?";
	private static final String UPDATE = 
			"update store_comment set store_no=?, mem_no=?, comment_conect=?, comment_time=?, comment_level=? where comment_no = ?";
	
	
	@Override
	public void insert(StoreCommentVO storeCommentVO) {
		Connection con = null;
		PreparedStatement pstmt = null;
		
		try {
			con = ds.getConnection();
			pstmt = con.prepareStatement(INSERT_STMT);
			
			pstmt.setString(1, storeCommentVO.getStore_no());
			pstmt.setString(2, storeCommentVO.getMem_no());
			pstmt.setString(3, storeCommentVO.getComment_conect());
			pstmt.setTimestamp(4, storeCommentVO.getComment_time());
			pstmt.setDouble(5, storeCommentVO.getComment_level());
			pstmt.executeUpdate();
		} catch (SQLException se) {
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
	public void update(StoreCommentVO storeCommentVO) {
		Connection con = null;
		PreparedStatement pstmt = null;
		
		try {

			con = ds.getConnection();
			pstmt = con.prepareStatement(UPDATE);
			pstmt.setString(1, storeCommentVO.getStore_no());
			pstmt.setString(2, storeCommentVO.getMem_no());
			pstmt.setString(3, storeCommentVO.getComment_conect());
			pstmt.setTimestamp(4, storeCommentVO.getComment_time());
			pstmt.setDouble(5, storeCommentVO.getComment_level());
			pstmt.setString(6, storeCommentVO.getComment_no());
			pstmt.executeUpdate();

			// Handle any driver errors
		} catch (SQLException se) {
			throw new RuntimeException("A database error occured. "
					+ se.getMessage());
			// Handle any SQL errors
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
	public void delete(String comment_no) {
		Connection con = null;
		PreparedStatement pstmt = null;
		
		try {

			con = ds.getConnection();
			pstmt = con.prepareStatement(DELETE);

			pstmt.setString(1, comment_no);

			pstmt.executeUpdate();

			// Handle any driver errors
		} catch (SQLException se) {
			throw new RuntimeException("A database error occured. "
					+ se.getMessage());
			// Handle any SQL errors
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
	public StoreCommentVO findByPrimaryKey(String comment_no) {
		
		StoreCommentVO storeCommentVO = null;
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		
		try {

			con = ds.getConnection();
			pstmt = con.prepareStatement(GET_ONE_STMT);

			pstmt.setString(1, comment_no);

			rs = pstmt.executeQuery();

			while (rs.next()) {
				// empVo 也稱為 Domain objects
				storeCommentVO = new StoreCommentVO();
				storeCommentVO.setComment_no(rs.getString("comment_no"));
				storeCommentVO.setStore_no(rs.getString("store_no"));
				storeCommentVO.setMem_no(rs.getString("mem_no"));
				storeCommentVO.setComment_conect(rs.getString("comment_conect"));
				storeCommentVO.setComment_time(rs.getTimestamp("comment_time"));
				storeCommentVO.setComment_level(rs.getDouble("comment_level"));
			}

			// Handle any driver errors
		} catch (SQLException se) {
			throw new RuntimeException("A database error occured. "
					+ se.getMessage());
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
		return storeCommentVO;
	}

	@Override
	public List<StoreCommentVO> getAll() {
		List<StoreCommentVO> list = new ArrayList<StoreCommentVO>();
		StoreCommentVO storeCommentVO = null;

		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		
		try {

			con = ds.getConnection();
			pstmt = con.prepareStatement(GET_ALL_STMT);
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
			throw new RuntimeException("A database error occured. "
					+ se.getMessage());
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

}
