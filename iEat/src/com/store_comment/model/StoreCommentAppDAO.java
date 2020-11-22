package com.store_comment.model;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;


public class StoreCommentAppDAO implements StoreCommentAppDAO_interface{

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

	
	private static final String GET_STORE_COMMENT = 
			"select comment_no,store_no,store_comment.mem_no,comment_conect,to_char(comment_time, 'yyyy-mm-dd HH:mm:ss') comment_time,comment_level, mem_name from store_comment left join member on store_comment.mem_no = member.mem_no where store_no =? order by comment_no DESC";
	
	
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
	public Map<String,StoreCommentVO> getStoreComment(String store_no) {
		Map<String,StoreCommentVO> list = new LinkedHashMap<String,StoreCommentVO>();
		StoreCommentVO storeCommentVO = null;

		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		
		try {

			con = ds.getConnection();
			pstmt = con.prepareStatement(GET_STORE_COMMENT);
			pstmt.setString(1, store_no);
			rs = pstmt.executeQuery();

			while (rs.next()) {
				String comment_str = rs.getString("comment_no")+","+rs.getString("mem_name");
				storeCommentVO = new StoreCommentVO();
				storeCommentVO.setComment_no(rs.getString("comment_no"));
				storeCommentVO.setStore_no(rs.getString("store_no"));
				storeCommentVO.setMem_no(rs.getString("mem_no"));
				storeCommentVO.setComment_conect(rs.getString("comment_conect"));
				storeCommentVO.setComment_time(rs.getTimestamp("comment_time"));
				storeCommentVO.setComment_level(rs.getDouble("comment_level"));
		
				list.put(comment_str,storeCommentVO); // Store the row in the list
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
