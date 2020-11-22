package com.article_response.model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

import com.article_response_report.model.Article_rs_reVO;

public class Article_Response_DAO implements Article_ResponseDAO_interface {

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
			"INSERT INTO Article_Response (art_rs_no,art_no, mem_no ,art_rs_context,art_rs_date) VALUES ('AS'||TRIM(TO_CHAR(ART_RS_NO_SEQ.NEXTVAL,'0000000')), ?, ?, ?, ?)";
		private static final String GET_ALL_STMT = 
			"SELECT art_rs_no,art_no,art_rs_context,art_rs_date,mem_no FROM Article_Response order by art_rs_no";
		private static final String GET_ONE_STMT = 
			"SELECT art_rs_no,art_no,art_rs_context,art_rs_date,mem_no FROM Article_Response where art_rs_no = ?";
		private static final String Get_Article_Response_Report_ByArt_rs_no_STMT =
			"Select art_rs_re_no,mem_no,art_rs_no,art_rs_re_date,art_rs_re_con,art_rs_re_sta FROM ARTICLE_RESPONSE_REPORT where art_rs_no=? order by art_rs_no";
		private static final String DELETE_ARTICLE_RESPONSE = 
			"DELETE FROM Article_Response where art_rs_no = ?";
		private static final String DELETE_ARTICLE_RESPONSE_REPORT =
			"DELETE FROM Article_Response_Report where art_rs_no = ?";
		private static final String UPDATE = 
			"UPDATE Article_response set art_no=?, mem_no=?, art_rs_context=? , art_rs_date=? where art_rs_no = ?";
		
	@Override
	public void insert(Article_ResponseVO article_responseVO) {

		Connection con = null;
		PreparedStatement pstmt = null;	
		try {

			con = ds.getConnection();
			pstmt = con.prepareStatement(INSERT_STMT);

			pstmt.setString(1, article_responseVO.getArt_no());
			pstmt.setString(2, article_responseVO.getMem_no());
			pstmt.setString(3, article_responseVO.getArt_rs_context());
			pstmt.setTimestamp(4, article_responseVO.getArt_rs_date());

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
	public void update(Article_ResponseVO article_responseVO) {
		Connection con = null;
		PreparedStatement pstmt = null;

		try {

			con = ds.getConnection();
			pstmt = con.prepareStatement(UPDATE);
			
			pstmt.setString(1, article_responseVO.getArt_no());
			pstmt.setString(2, article_responseVO.getMem_no());
			pstmt.setString(3, article_responseVO.getArt_rs_context());
			pstmt.setTimestamp(4, article_responseVO.getArt_rs_date());
			pstmt.setString(5, article_responseVO.getArt_rs_no());
		
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
//		try {
//
//			con = ds.getConnection();
//			pstmt = con.prepareStatement(DELETE);
//
//			pstmt.setString(1,art_rs_no);
//			pstmt.executeUpdate();
//	
//			// Handle any driver errors
//
//		} catch (SQLException se) {
//			throw new RuntimeException("A database error occured. "
//					+ se.getMessage());
//			// Clean up JDBC resources
//		} finally {
//			if (pstmt != null) {
//				try {
//					pstmt.close();
//				} catch (SQLException se) {
//					se.printStackTrace(System.err);
//				}
//			}
//			if (con != null) {
//				try {
//					con.close();
//				} catch (Exception e) {
//					e.printStackTrace(System.err);
//				}
//			}
//		}	
//	}
		
		int updateCount_ARTICLE_RESPONSE_REPORT = 0 ;
	try{
		
		con = ds.getConnection();
		// 1●設定於 pstm.executeUpdate()之前
		con.setAutoCommit(false);
		
		//先刪除食記回復
		pstmt = con.prepareStatement(DELETE_ARTICLE_RESPONSE_REPORT);
		pstmt.setString(1, art_rs_no);
		updateCount_ARTICLE_RESPONSE_REPORT = pstmt.executeUpdate();
		
		
		
		//刪除食記檢舉
		pstmt = con.prepareStatement(DELETE_ARTICLE_RESPONSE);
		pstmt.setString(1, art_rs_no);
		pstmt.executeUpdate();
		
		
		//2●設定於 pstm.executeUpdate()之後
		con.commit();
		con.setAutoCommit(true);
		System.out.println("刪除食記編號" + art_rs_no + "時,有食記回覆" + updateCount_ARTICLE_RESPONSE_REPORT
				+ "筆同時被刪除");

		
	}catch(SQLException se){
		throw new RuntimeException("A database error occured. "
				+ se.getMessage());
		// Clean up JDBC resources
		}finally {
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
	public Article_ResponseVO  findByPrimaryKey(String art_rs_no) {
		// TODO Auto-generated method stub
		Article_ResponseVO article_responseVO = null;
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		
		try {
			con= ds.getConnection();
			pstmt = con.prepareStatement(GET_ONE_STMT);

			pstmt.setString(1, art_rs_no);

			rs = pstmt.executeQuery();
		
			while (rs.next()) {
				// ARTICLEVO �]�٬� Domain objects
				article_responseVO = new Article_ResponseVO();
				article_responseVO.setArt_rs_no(rs.getString("art_rs_no"));
				article_responseVO.setArt_no(rs.getString("art_no"));
				article_responseVO.setArt_rs_context(rs.getString("art_rs_context"));
				article_responseVO.setMem_no(rs.getString("mem_no"));	
				article_responseVO.setArt_rs_date(rs.getTimestamp("art_rs_date"));
				
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
		return  article_responseVO;
	}
	@Override
	public List<Article_ResponseVO> getAll() {
		List<Article_ResponseVO> list = new ArrayList<Article_ResponseVO>();
		Article_ResponseVO article_responseVO = null;
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;

		try {
			
			con= ds.getConnection();
			pstmt = con.prepareStatement(GET_ALL_STMT);
			rs = pstmt.executeQuery();

			while (rs.next()) {
				// ARTICLEVO �]�٬� Domain objects
				article_responseVO = new Article_ResponseVO();
				
				article_responseVO.setArt_rs_no(rs.getString("art_rs_no"));
				article_responseVO.setArt_rs_context(rs.getString("art_rs_context"));
				article_responseVO.setArt_no(rs.getString("art_no"));
				article_responseVO.setMem_no(rs.getString("mem_no"));
				article_responseVO.setArt_rs_date(rs.getTimestamp("art_rs_date"));
				list.add(article_responseVO);
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
	public Set<Article_rs_reVO> getArticleResponseReportByArt_rs_no(String art_rs_no)  {
		Set<Article_rs_reVO> set = new LinkedHashSet<Article_rs_reVO>();
		Article_rs_reVO	  article_rs_reVO = null;
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			con = ds.getConnection();
			pstmt = con.prepareStatement(Get_Article_Response_Report_ByArt_rs_no_STMT);
			pstmt.setString(1, art_rs_no);
			rs = pstmt.executeQuery();
		
		while (rs.next()) {
			article_rs_reVO = new Article_rs_reVO();
			article_rs_reVO.setArt_rs_re_no(rs.getString("art_rs_re_no"));
			article_rs_reVO.setArt_rs_no(rs.getString("art_rs_no"));
			article_rs_reVO.setMem_no(rs.getString("mem_no"));
			article_rs_reVO.setArt_rs_re_date(rs.getTimestamp("art_rs_re_date"));
			article_rs_reVO.setArt_rs_re_con(rs.getString("art_rs_re_con"));
			article_rs_reVO.setArt_rs_re_sta(rs.getString("art_rs_re_sta"));
			set.add(article_rs_reVO); // Store the row in the vector
			
			}
			
		}catch (SQLException se) {
			throw new RuntimeException("A database error occured. "
					+ se.getMessage());
		
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
			return set;
		}
	
}
