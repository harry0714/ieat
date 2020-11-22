package com.comment_report.model;

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

public class CommentReportDAO implements CommentReportDAO_interface{
	
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
			"insert into comment_report (comment_report_no,comment_no,mem_no,comment_report_context,comment_report_date,comment_report_status) values ('CR'||TRIM(TO_CHAR(COMMENT_REPORT_NO_SQ.NEXTVAL,'00000000')), ?, ?, ?, sysdate, ?)";
	private static final String GET_ALL_STMT = 
			"select comment_report_no,comment_no,mem_no,comment_report_context,comment_report_date,comment_report_status from comment_report order by comment_report_no desc";
	private static final String GET_ONE_STMT = 
			"select comment_report_no,comment_no,mem_no,comment_report_context,comment_report_date,comment_report_status from comment_report where comment_report_no = ?";
	private static final String DELETE = 
			"delete from comment_report where comment_report_no = ?";
	private static final String UPDATE = 
			"update comment_report set comment_no=?, mem_no=?, comment_report_context=?, comment_report_date=?, comment_report_status=? where comment_report_no = ?";
	private static final String GET_MORT_BY_COMMENT_REPORT_STATUS = 
			"select * from comment_report where comment_report_status=? order by comment_report_no desc";
	
	@Override
	public void insert(CommentReportVO commentreportVO) {
		
		Connection con = null;
		PreparedStatement pstmt = null;

		try {
			con = ds.getConnection();
			pstmt = con.prepareStatement(INSERT_STMT);
			
			pstmt.setString(1, commentreportVO.getComment_no());
			pstmt.setString(2, commentreportVO.getMem_no());
			pstmt.setString(3, commentreportVO.getComment_report_context());
			pstmt.setString(4, commentreportVO.getComment_report_status());
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
	public void update(CommentReportVO commentreportVO) {

		Connection con = null;
		PreparedStatement pstmt = null;

		try {

			con = ds.getConnection();
			pstmt = con.prepareStatement(UPDATE);

			pstmt.setString(1, commentreportVO.getComment_no());
			pstmt.setString(2, commentreportVO.getMem_no());
			pstmt.setString(3, commentreportVO.getComment_report_context());
			pstmt.setTimestamp(4, commentreportVO.getComment_report_date());
			pstmt.setString(5, commentreportVO.getComment_report_status());
			pstmt.setString(6, commentreportVO.getComment_report_no());

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
	public void delete(String comment_report_no) {
		
		Connection con = null;
		PreparedStatement pstmt = null;

		try {

			con = ds.getConnection();
			pstmt = con.prepareStatement(DELETE);

			pstmt.setString(1, comment_report_no);

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
	public CommentReportVO findByPrimaryKey(String comment_report_no) {
		
		CommentReportVO commentreportVO = null;
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;

		try {

			con = ds.getConnection();
			pstmt = con.prepareStatement(GET_ONE_STMT);

			pstmt.setString(1, comment_report_no);

			rs = pstmt.executeQuery();

			while (rs.next()) {
				// empVo 也稱為 Domain objects
				commentreportVO = new CommentReportVO();
				commentreportVO.setComment_report_no(rs.getString("comment_report_no"));
				commentreportVO.setComment_no(rs.getString("comment_no"));
				commentreportVO.setMem_no(rs.getString("mem_no"));
				commentreportVO.setComment_report_context(rs.getString("comment_report_context"));
				commentreportVO.setComment_report_date(rs.getTimestamp("comment_report_date"));
				commentreportVO.setComment_report_status(rs.getString("comment_report_status"));
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
		return commentreportVO;
	}
	@Override
	public List<CommentReportVO> getAll() {
		
		List<CommentReportVO> list = new ArrayList<CommentReportVO>();
		CommentReportVO commentreportVO = null;

		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;

		try {

			con = ds.getConnection();
			pstmt = con.prepareStatement(GET_ALL_STMT);
			rs = pstmt.executeQuery();

			while (rs.next()) {
				// empVO 也稱為 Domain objects
				commentreportVO = new CommentReportVO();
				commentreportVO.setComment_report_no(rs.getString("comment_report_no"));
				commentreportVO.setComment_no(rs.getString("comment_no"));
				commentreportVO.setMem_no(rs.getString("mem_no"));
				commentreportVO.setComment_report_context(rs.getString("comment_report_context"));
				commentreportVO.setComment_report_date(rs.getTimestamp("comment_report_date"));
				commentreportVO.setComment_report_status(rs.getString("comment_report_status"));
				list.add(commentreportVO); // Store the row in the list
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

	@Override
	public List<CommentReportVO> getMoreByCommentReportStatus(String comment_report_status) {
		List<CommentReportVO> list = new ArrayList<CommentReportVO>();
		CommentReportVO commentreportVO = null;

		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;

		try {

			con = ds.getConnection();
			pstmt = con.prepareStatement(GET_MORT_BY_COMMENT_REPORT_STATUS);
			pstmt.setString(1, comment_report_status);
			rs = pstmt.executeQuery();

			while (rs.next()) {
				// empVO 也稱為 Domain objects
				commentreportVO = new CommentReportVO();
				commentreportVO.setComment_report_no(rs.getString("comment_report_no"));
				commentreportVO.setComment_no(rs.getString("comment_no"));
				commentreportVO.setMem_no(rs.getString("mem_no"));
				commentreportVO.setComment_report_context(rs.getString("comment_report_context"));
				commentreportVO.setComment_report_date(rs.getTimestamp("comment_report_date"));
				commentreportVO.setComment_report_status(rs.getString("comment_report_status"));
				list.add(commentreportVO); // Store the row in the list
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
