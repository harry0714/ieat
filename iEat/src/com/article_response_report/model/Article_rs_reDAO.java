package com.article_response_report.model;

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





public class Article_rs_reDAO implements Article_rs_reDAO_interface  {
	
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
			"INSERT INTO Article_Response_Report (art_rs_re_no,mem_no,art_rs_no,art_rs_re_date,art_rs_re_con,art_rs_re_sta) VALUES ('ARRT'||TRIM(TO_CHAR(ART_RS_RE_NO_SEQ.NEXTVAL,'0000')) , ?, ?, ?, ?, ?)";
		private static final String GET_ALL_STMT = 
			"SELECT art_rs_re_no,mem_no,art_rs_no,art_rs_re_date,art_rs_re_con,art_rs_re_sta FROM Article_Response_Report order by art_rs_re_no desc";
		private static final String GET_ONE_STMT = 
			"SELECT art_rs_re_no,mem_no,art_rs_no,art_rs_re_date,art_rs_re_con,art_rs_re_sta FROM Article_Response_Report where art_rs_re_no = ?";
		private static final String DELETE = 
			"DELETE FROM Article_Response_Report where art_rs_re_no = ?";
		private static final String UPDATE = 
			"UPDATE Article_Response_Report set mem_no=?,art_rs_no=? , art_rs_re_date=? , art_rs_re_con=? ,art_rs_re_sta=? where art_rs_re_no = ?";
		private static final String GET_MORE_BY_ARTICLE_RESPONSE_REPORT_STATUS = 
				"SELECT * FROM Article_Response_Report where art_rs_re_sta=? order by art_rs_re_no desc";

		
	@Override
	public void insert(Article_rs_reVO article_rs_reVO) {

		Connection con = null;
		PreparedStatement pstmt = null;	
		try {

			con = ds.getConnection();
			pstmt = con.prepareStatement(INSERT_STMT);

			pstmt.setString(1, article_rs_reVO.getMem_no());
			pstmt.setString(2, article_rs_reVO.getArt_rs_no());
			pstmt.setTimestamp(3, article_rs_reVO.getArt_rs_re_date());
			pstmt.setString(4, article_rs_reVO.getArt_rs_re_con());
			pstmt.setString(5, article_rs_reVO.getArt_rs_re_sta());

			pstmt.executeUpdate();

			// Handle any driver errors
		}catch (SQLException e) {
			throw new RuntimeException("Couldn't load database driver. "
					+ e.getMessage());
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
	public void update(Article_rs_reVO article_rs_reVO) {
		Connection con = null;
		PreparedStatement pstmt = null;

		try {

			con = ds.getConnection();
			pstmt = con.prepareStatement(UPDATE);
		
			pstmt.setString(1, article_rs_reVO.getMem_no());
			pstmt.setString(2, article_rs_reVO.getArt_rs_no());
			pstmt.setTimestamp(3, article_rs_reVO.getArt_rs_re_date());
			pstmt.setString(4, article_rs_reVO.getArt_rs_re_con());
			pstmt.setString(5, article_rs_reVO.getArt_rs_re_sta());
			pstmt.setString(6, article_rs_reVO.getArt_rs_re_no());
		
		
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

	@Override
	public void delete(String art_rs_no) {
		Connection con = null;
		PreparedStatement pstmt = null;
		try {

			con = ds.getConnection();
			pstmt = con.prepareStatement(DELETE);

			pstmt.setString(1,art_rs_no);
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

	@Override
	public Article_rs_reVO  findByPrimaryKey(String art_rs_re_no) {
		// TODO Auto-generated method stub
		Article_rs_reVO article_rs_reVO = null;
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		
		try {
			con= ds.getConnection();
			pstmt = con.prepareStatement(GET_ONE_STMT);

			pstmt.setString(1, art_rs_re_no);

			rs = pstmt.executeQuery();
		
			while (rs.next()) {
				// ARTICLEVO �]�٬� Domain objects
				article_rs_reVO = new Article_rs_reVO();
				article_rs_reVO.setArt_rs_re_no(rs.getString("art_rs_re_no"));
				article_rs_reVO.setMem_no(rs.getString("mem_no"));
				article_rs_reVO.setArt_rs_no(rs.getString("art_rs_no"));
				
				article_rs_reVO.setArt_rs_re_date(rs.getTimestamp("art_rs_re_date"));
				article_rs_reVO.setArt_rs_re_con(rs.getString("art_rs_re_con"));
				article_rs_reVO.setArt_rs_re_sta(rs.getString("art_rs_re_sta"));	
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
		return  article_rs_reVO;
	}
	@Override
	public List<Article_rs_reVO> getAll() {
		List<Article_rs_reVO> list = new ArrayList<Article_rs_reVO>();
		Article_rs_reVO article_rs_reVO = null;

		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;

		try {
			
			con= ds.getConnection();
			pstmt = con.prepareStatement(GET_ALL_STMT);
			rs = pstmt.executeQuery();

			while (rs.next()) {
				// ARTICLEVO �]�٬� Domain objects
				article_rs_reVO = new Article_rs_reVO();
				article_rs_reVO.setArt_rs_re_no(rs.getString("art_rs_re_no"));
				article_rs_reVO.setMem_no(rs.getString("mem_no"));
				article_rs_reVO.setArt_rs_no(rs.getString("art_rs_no"));
				article_rs_reVO.setArt_rs_re_date(rs.getTimestamp("art_rs_re_date"));
				article_rs_reVO.setArt_rs_re_con(rs.getString("art_rs_re_con"));
				article_rs_reVO.setArt_rs_re_sta(rs.getString("art_rs_re_sta"));
				
				list.add(article_rs_reVO); // Store the row in the list
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
	public List<Article_rs_reVO> getMoreByArticleResponseReportStatus(String art_rs_re_sta) {
		List<Article_rs_reVO> list = new ArrayList<Article_rs_reVO>();
		Article_rs_reVO article_rs_reVO = null;
	
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
	
		try {
	
			
			con = ds.getConnection();
			pstmt = con.prepareStatement(GET_MORE_BY_ARTICLE_RESPONSE_REPORT_STATUS);
			pstmt.setString(1, art_rs_re_sta);
			rs = pstmt.executeQuery();
	
			while (rs.next()) {
				// empVO �]�٬� Domain objects
				
				article_rs_reVO = new Article_rs_reVO();
				
				article_rs_reVO.setArt_rs_re_no(rs.getString("art_rs_re_no"));
				article_rs_reVO.setMem_no(rs.getString("mem_no"));
				article_rs_reVO.setArt_rs_no(rs.getString("art_rs_no"));
				article_rs_reVO.setArt_rs_re_date(rs.getTimestamp("art_rs_re_date"));
				article_rs_reVO.setArt_rs_re_con(rs.getString("art_rs_re_con"));
				article_rs_reVO.setArt_rs_re_sta(rs.getString("art_rs_re_sta"));	
				
				list.add(article_rs_reVO); // Store the row in the list
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
}
