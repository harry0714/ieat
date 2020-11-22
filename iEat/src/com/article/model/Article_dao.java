package com.article.model;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

import com.article_report.model.Article_reportVO;
import com.article_response.model.Article_ResponseVO;

import jdbc.util.CompositeQuery.CompositeQuery_Article_rs;



public class Article_dao implements Articledao_interface {
	
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
			"INSERT INTO ARTICLE (art_no,art_name,art_date,art_context,mem_no) VALUES ('A'||TRIM(TO_CHAR(ART_NO_SEQ.NEXTVAL,'000000000')) , ?, ?, ?, ?)";
			private static final String GET_ALL_STMT = 
			"SELECT art_no,art_name, art_date,art_context,mem_no FROM ARTICLE order by art_no desc";
			private static final String GET_ONE_STMT = 
			"SELECT * FROM ARTICLE where art_no=? ";
			
			private static final String GET_Article_Responses_ByArt_no_STMT = 
			"SELECT art_rs_no,art_no,mem_no,art_rs_context,art_rs_date FROM ARTICLE_RESPONSE where art_no =?  order by art_no desc";
			
			private static final String GET_Article_Reports_ByArt_NO_STMT = 
			"SELECT art_re_no,art_no,mem_no,art_re_context,art_re_date,art_re_status FROM ARTICLE_REPORT where art_no =?  order by art_no";
			
			private static final String
			DELETE_ARTICLE_RESPONSE = "DELETE FROM Article_Response where art_rs_no = ?";
			private static final String
			DELETE_ARTICLE_REPORT = "DELETE FROM Article_Report where art_re_no = ?";
			private static final String DELETE_ARTICLE = 
					"DELETE FROM ARTICLE where art_no = ?";
			
			private static final String UPDATE = 
					"UPDATE ARTICLE set art_name=?,  art_date=? , art_context=?, mem_no=? where art_no = ?";
			private static final String GET_IMAGE_BY_ART_NO_STMT = 
					"SELECT ART_CONTEXT FROM ARTICLE WHERE ART_NO=?";			
			private static final String  GET_RANDOM =
					"select * from (select * from article order by dbms_random.value)where rownum <=4";
			private static final String GET_CONTEXT_BY_ART_NO_STMT = 
					"SELECT ART_CONTEXT FROM ARTICLE WHERE ART_NO=?";
		
	@Override
	public void insert(ArticleVO articleVO) {
		Connection con = null;
		PreparedStatement pstmt = null;
		
		try {

			con = ds.getConnection();
			pstmt = con.prepareStatement(INSERT_STMT);

			pstmt.setString(1, articleVO.getArt_name());
			pstmt.setTimestamp(2, articleVO.getArt_date());
			pstmt.setString(3, articleVO.getArt_context());
			
			
			
			pstmt.setString(4, articleVO.getMem_no());
			

			pstmt.executeUpdate();

			// Handle any SQL errors
		}catch (SQLException se) {
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
	public void update(ArticleVO articleVO) {
		Connection con = null;
		PreparedStatement pstmt = null;

		try {

			con = ds.getConnection();
			pstmt = con.prepareStatement(UPDATE);

			pstmt.setString(1, articleVO.getArt_name());
			pstmt.setTimestamp(2, articleVO.getArt_date());
			pstmt.setString(3, articleVO.getArt_context());
			pstmt.setString(4, articleVO.getMem_no());
			pstmt.setString(5, articleVO.getArt_no());
			

			pstmt.executeUpdate();

			// Handle any driver errors
		} catch (SQLException se) {
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
		}
	}

	@Override
	public void delete(String art_no) {
		
		Connection con = null;
		PreparedStatement pstmt = null;
		
//		try {
//
//
//			con = ds.getConnection();
//			pstmt = con.prepareStatement(DELETE_ARTICLE);
//			pstmt.setString(1,art_no);
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
//		
		
		
		
		
		
		
		
		int updateCount_ARTICLE_RESPONSE = 0;
		int updateCount_ARTICLE_REPORT = 0;

		
		try {
			
			con = ds.getConnection();
			// 1●設定於 pstm.executeUpdate()之前
			con.setAutoCommit(false);
			
			//先刪除食記回復
			pstmt = con.prepareStatement(DELETE_ARTICLE_RESPONSE);
			pstmt.setString(1, art_no);
			updateCount_ARTICLE_RESPONSE = pstmt.executeUpdate();
			
			//刪除食記檢舉
			pstmt = con.prepareStatement(DELETE_ARTICLE_REPORT);
			pstmt.setString(1, art_no);
			updateCount_ARTICLE_REPORT = pstmt.executeUpdate();
			
			//再刪除食記
			pstmt = con.prepareStatement(DELETE_ARTICLE);
			pstmt.setString(1, art_no);
			pstmt.executeUpdate();
			
			
			//2●設定於 pstm.executeUpdate()之後
			con.commit();
			con.setAutoCommit(true);
			System.out.println("刪除食記編號" + art_no + "時,有食記回覆" + updateCount_ARTICLE_RESPONSE
					+ "筆同時被刪除");
			System.out.println("刪除食記編號" + art_no + "時,有食記檢舉"+ updateCount_ARTICLE_REPORT
					+"筆同時刪除");
			
		}catch (SQLException se) {
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
	public ArticleVO findByPrimaryKey(String art_no) {
		ArticleVO articleVO = null;
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {

			con = ds.getConnection();
			pstmt = con.prepareStatement(GET_ONE_STMT);
			pstmt.setString(1, art_no);

			rs = pstmt.executeQuery();
			while (rs.next()) {
				
				articleVO = new ArticleVO();
				articleVO.setArt_no(rs.getString("art_no"));
				articleVO.setArt_date(rs.getTimestamp("art_date"));
				articleVO.setArt_name(rs.getString("art_name"));
				articleVO.setArt_context(rs.getString("art_context"));
				articleVO.setMem_no(rs.getString("mem_no"));
				
			}

			// Handle any driver errors
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
			return articleVO;
		
	}

	@Override
	public List<ArticleVO> getAll() {
		List<ArticleVO> list = new ArrayList<ArticleVO>();
		ArticleVO articleVO = null;

		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		
		try {

			con = ds.getConnection();
			pstmt = con.prepareStatement(GET_ALL_STMT);
			rs = pstmt.executeQuery();

			while (rs.next()) {
				// empVO �]�٬� Domain objects
				articleVO = new ArticleVO();
				articleVO.setArt_no(rs.getString("art_no"));
				articleVO.setArt_date(rs.getTimestamp("art_date"));
				articleVO.setArt_name(rs.getString("art_name"));
				articleVO.setArt_context(rs.getString("art_context"));
				articleVO.setMem_no(rs.getString("mem_no"));
				list.add(articleVO); // Store the row in the list
			}

			// Handle any driver errors
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
	
	
	@Override
	public Set<Article_ResponseVO> getArticleResponsesByArt_NO(String art_no) {
		Set<Article_ResponseVO> set = new LinkedHashSet<Article_ResponseVO>();
		Article_ResponseVO article_responseVO = null;
	
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
	
		try {
	
			con = ds.getConnection();
			pstmt = con.prepareStatement(GET_Article_Responses_ByArt_no_STMT);
			pstmt.setString(1, art_no);
			rs = pstmt.executeQuery();
	
			while (rs.next()) {
				article_responseVO = new Article_ResponseVO();
				article_responseVO.setArt_rs_no(rs.getString("art_rs_no"));
				article_responseVO.setArt_no(rs.getString("art_no"));
				article_responseVO.setMem_no(rs.getString("mem_no"));
				article_responseVO.setArt_rs_context(rs.getString("art_rs_context"));
				article_responseVO.setArt_rs_date(rs.getTimestamp("art_rs_date"));
				set.add(article_responseVO); // Store the row in the vector
			}
	
			// Handle any SQL errors
		} catch (SQLException se) {
			throw new RuntimeException("A database error occured. "
					+ se.getMessage());
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
		return set;
	}

	
	
	
	@Override
	public Set<Article_reportVO> getArticleReportsByArt_NO(String art_no) {
		Set<Article_reportVO> set = new LinkedHashSet<Article_reportVO>();
		
		Article_reportVO article_reportVO = null;
	
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
	
		try {
	
			con = ds.getConnection();
			pstmt = con.prepareStatement(GET_Article_Reports_ByArt_NO_STMT);
			pstmt.setString(1, art_no);
			rs = pstmt.executeQuery();
	
			while (rs.next()) {
				article_reportVO = new Article_reportVO();
				article_reportVO.setArt_re_no(rs.getString("art_re_no"));
				article_reportVO.setArt_no(rs.getString("art_no"));
				article_reportVO.setMem_no(rs.getString("mem_no"));
				article_reportVO.setArt_re_context(rs.getString("art_re_context"));
				article_reportVO.setArt_re_date(rs.getTimestamp("art_re_date"));
				article_reportVO.setArt_re_status(rs.getString("art_re_status"));
				
				set.add(article_reportVO); // Store the row in the vector
			}
	
			// Handle any SQL errors
		} catch (SQLException se) {
			throw new RuntimeException("A database error occured. "
					+ se.getMessage());
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
		return set;
	


	}

	@Override
	public List<ArticleVO> getAll(Map<String, String[]> map) {
		// TODO Auto-generated method stub
		List<ArticleVO> list = new ArrayList<ArticleVO>();
		ArticleVO articleVO = null;
	
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
	
		try {
			
			con = ds.getConnection();
			String finalSQL = "select * from Article "
		          + CompositeQuery_Article_rs.get_WhereCondition(map)
		          + "order by art_no";
			pstmt = con.prepareStatement(finalSQL);
			System.out.println("����finalSQL(by DAO) = "+finalSQL);
			rs = pstmt.executeQuery();
			while (rs.next()) {
				articleVO = new ArticleVO();
				articleVO.setArt_no(rs.getString("art_no"));
				articleVO.setArt_name(rs.getString("art_name"));
				articleVO.setArt_date(rs.getTimestamp("art_date"));
				articleVO.setArt_context(rs.getString("art_context"));
				//articleVO.setArt_image(rs.getBytes(null));
				articleVO.setMem_no(rs.getString("mem_no"));
				list.add(articleVO); // Store the row in the List
			}
	
			// Handle any SQL errors
		} catch (SQLException se) {
			throw new RuntimeException("A database error occured. "
					+ se.getMessage());
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
	public String getImage(String art_no) {
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String img = null;
		String result;
		
		try{
			con = ds.getConnection();
			pstmt = con.prepareStatement(GET_IMAGE_BY_ART_NO_STMT);
			pstmt.setString(1, art_no);
			rs = pstmt.executeQuery();
			
			while(rs.next()){
				img = rs.getString(1);
			}
			
			if(img.contains("<img")){			
				result = img.substring(img.indexOf("<img src=")+10);
				if(result.contains("\"")){
					result =result.substring(0, result.indexOf("\""));
				}
			}else{
				result = null; 
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
		return result;
	}

	@Override
	public List<ArticleVO> getRandom() {
		List<ArticleVO> list = new ArrayList<ArticleVO>();
		ArticleVO articleVO = null;
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		
		try {
		
			con = ds.getConnection();
			pstmt = con.prepareStatement(GET_RANDOM);
			rs = pstmt.executeQuery();
			
			while (rs.next()) {
				
				articleVO = new ArticleVO();
				articleVO.setArt_no(rs.getString("art_no"));
				articleVO.setArt_date(rs.getTimestamp("art_date"));
				articleVO.setArt_name(rs.getString("art_name"));
				articleVO.setArt_context(rs.getString("art_context"));
				articleVO.setMem_no(rs.getString("mem_no"));
				list.add(articleVO); // Store the row in the list
			}

			// Handle any driver errors
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
	@Override
	public String getContext(String art_no,int count) {
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		StringBuilder sb = null;
		StringBuilder result = new StringBuilder();


		try{
			con = ds.getConnection();
			pstmt = con.prepareStatement(GET_CONTEXT_BY_ART_NO_STMT);
			pstmt.setString(1, art_no);
			rs = pstmt.executeQuery();
			
			while(rs.next()){
				sb =new StringBuilder(rs.getString("art_context"));
			}
			
			while(sb.indexOf("<p>")!=-1&&result.length()<count){
				if(sb.substring(sb.indexOf("<p>"), sb.indexOf("</p>")+4).indexOf("<img")>=0){
					sb.delete(sb.indexOf("<p>"), sb.indexOf("</p>")+4);
				}else{
					result.append(sb.substring(sb.indexOf("<p>")+3, sb.indexOf("</p>")));
					sb.delete(sb.indexOf("<p>"), sb.indexOf("</p>")+4);
				}

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
		return result.toString();
	}
}