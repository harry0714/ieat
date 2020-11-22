package com.article_report.model;

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



public class article_report_dao implements Article_ReportDAO_interface {

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
			"INSERT INTO Article_Report (art_re_no,art_no,mem_no,art_re_context,art_re_date,art_re_status) VALUES ('AR'||TRIM(TO_CHAR(ARTICLE_RE_SEQ.NEXTVAL,'0000000')) , ?, ?, ?, ?, ?)";
		private static final String GET_ALL_STMT = 
			"SELECT art_re_no,art_no,mem_no,art_re_context, art_re_date,art_re_status FROM Article_Report order by art_re_no desc";
		private static final String GET_ONE_STMT = 
			"SELECT art_re_no,art_no,mem_no,art_re_context, art_re_date,art_re_status FROM Article_Report where art_re_no = ?";
		private static final String DELETE = 
			"DELETE FROM Article_Report where art_re_no = ?";
		private static final String UPDATE = 
			"UPDATE Article_Report set art_no=?, mem_no=?, art_re_context=? ,art_re_date=? , art_re_status=? where art_re_no = ?";
		private static final String GET_MORT_BY_ARTICLE_REPORT_STATUS = 
				"SELECT * FROM Article_Report where art_re_status=? order by art_re_no DESC";
	@Override
	public void insert(Article_reportVO article_reportVO) {

		Connection con = null;
		PreparedStatement pstmt = null;	
		try {

			con = ds.getConnection();
			pstmt = con.prepareStatement(INSERT_STMT);

			pstmt.setString(1, article_reportVO.getArt_no());
			pstmt.setString(2, article_reportVO.getMem_no());
			pstmt.setString(3, article_reportVO.getArt_re_context());
			pstmt.setTimestamp(4, article_reportVO.getArt_re_date());
			pstmt.setString(5, article_reportVO.getArt_re_status());

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
	public void update(Article_reportVO article_reportVO) {
		Connection con = null;
		PreparedStatement pstmt = null;

		try {

			con = ds.getConnection();
			pstmt = con.prepareStatement(UPDATE);
		
			pstmt.setString(1, article_reportVO.getArt_no());
			pstmt.setString(2, article_reportVO.getMem_no());
			pstmt.setString(3, article_reportVO.getArt_re_context());
			pstmt.setTimestamp(4, article_reportVO.getArt_re_date());
			pstmt.setString(5, article_reportVO.getArt_re_status());
			pstmt.setString(6, article_reportVO.getArt_re_no());;
		
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
	public void delete(String art_re_no) {
		Connection con = null;
		PreparedStatement pstmt = null;
		try {

			con = ds.getConnection();
			pstmt = con.prepareStatement(DELETE);

			pstmt.setString(1,art_re_no);
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
	public Article_reportVO  findByPrimaryKey(String art_re_no) {
		// TODO Auto-generated method stub
		Article_reportVO article_reportVO = null;
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		
		try {
			con= ds.getConnection();
			pstmt = con.prepareStatement(GET_ONE_STMT);

			pstmt.setString(1, art_re_no);

			rs = pstmt.executeQuery();
		
			while (rs.next()) {
				// ARTICLEVO 也稱為 Domain objects
				article_reportVO = new Article_reportVO();
				article_reportVO.setArt_re_no(rs.getString("art_re_no"));
				article_reportVO.setArt_no(rs.getString("art_no"));
				article_reportVO.setMem_no(rs.getString("mem_no"));
				article_reportVO.setArt_re_context(rs.getString("art_re_context"));
				article_reportVO.setArt_re_date(rs.getTimestamp("art_re_date"));
				article_reportVO.setArt_re_status(rs.getString("art_re_status"));
				
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
		return  article_reportVO;
	}
	@Override
	public List<Article_reportVO> getAll() {
		List<Article_reportVO> list = new ArrayList<Article_reportVO>();
		Article_reportVO article_reportVO = null;

		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;

		try {
			
			con= ds.getConnection();
			pstmt = con.prepareStatement(GET_ALL_STMT);
			rs = pstmt.executeQuery();

			while (rs.next()) {
				// ARTICLEVO 也稱為 Domain objects
				article_reportVO = new Article_reportVO();
				article_reportVO.setArt_re_no(rs.getString("art_re_no"));
				article_reportVO.setArt_no(rs.getString("art_no"));
				article_reportVO.setMem_no(rs.getString("mem_no"));
				article_reportVO.setArt_re_context(rs.getString("art_re_context"));
				article_reportVO.setArt_re_date(rs.getTimestamp("art_re_date"));
				article_reportVO.setArt_re_status(rs.getString("art_re_status"));
				list.add(article_reportVO); // Store the row in the list
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
	public List<Article_reportVO> getMoreByArticleReposeStatus(String art_re_status) {
		List<Article_reportVO> list = new ArrayList<Article_reportVO>();
		Article_reportVO article_reportVO = null;

		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;

		try {

			con = ds.getConnection();
			pstmt = con.prepareStatement(GET_MORT_BY_ARTICLE_REPORT_STATUS);
			pstmt.setString(1, art_re_status);
			rs = pstmt.executeQuery();

			while (rs.next()) {
				// empVO 嚙稽嚙誶穿蕭 Domain objects
				article_reportVO = new Article_reportVO();
				
				article_reportVO.setArt_re_no(rs.getString("art_re_no"));
				article_reportVO.setArt_no(rs.getString("art_no"));
				article_reportVO.setMem_no(rs.getString("mem_no"));
				article_reportVO.setArt_re_context(rs.getString("art_re_context"));
				article_reportVO.setArt_re_date(rs.getTimestamp("art_re_date"));
				article_reportVO.setArt_re_status(rs.getString("art_re_status"));
				
				list.add(article_reportVO); // Store the row in the list
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
