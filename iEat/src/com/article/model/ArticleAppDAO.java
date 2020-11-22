package com.article.model;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Base64;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;
import javax.xml.bind.DatatypeConverter;

//import com.article_report.model.Article_ReportVO;
//import com.article_response.model.Article_ResponseVO;

//import jbdcUtil.CompositeQuery_Article_rs;

public class ArticleAppDAO implements ArticleAppDAO_interface {
	private String result;
	private byte[] imagebytes;
	private static DataSource ds = null;
	static {
		try {
			Context ctx = new InitialContext();
			ds = (DataSource) ctx.lookup("java:comp/env/jdbc/iEatDB");
		} catch (NamingException e) {
			e.printStackTrace();
		}
	}



	private static final String GET_IMAGE_BY_ART_NO_STMT = "SELECT ART_CONTEXT FROM ARTICLE WHERE ART_NO=?";
	private static final String GET_ALLWITHNAME = "SELECT art_no,art_name, art_date,art_context,article.mem_no, mem_name FROM ARTICLE left join member on article.mem_no = member.mem_no order by art_no desc";

	@Override
	public Map<String,ArticleVO> getAllWithName() {
		Map<String,ArticleVO> list = new LinkedHashMap<String,ArticleVO>();
		ArticleVO articleVO = null;

		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		
		StringBuilder sb = null;
		StringBuilder result = null;
		String finalString = "";

		try {
			con = ds.getConnection();
			pstmt = con.prepareStatement(GET_ALLWITHNAME);
			rs = pstmt.executeQuery();

			while (rs.next()) {
				String art_mem_no = (rs.getString("art_no")+","+rs.getString("mem_name"));
				articleVO = new ArticleVO();
				articleVO.setArt_no(rs.getString("art_no"));
				articleVO.setArt_date(rs.getTimestamp("art_date"));
				articleVO.setArt_name(rs.getString("art_name"));
				articleVO.setMem_no(rs.getString("mem_no"));
				
				
				sb =new StringBuilder(rs.getString("art_context"));
				result = new StringBuilder();
				
				int count = 50;
				
				while(sb.indexOf("<p>")!=-1&&result.length()<count){
					if(sb.substring(sb.indexOf("<p>"), sb.indexOf("</p>")+4).indexOf("<img")>=0){
						sb.delete(sb.indexOf("<p>"), sb.indexOf("</p>")+4);
					}else{
						
						int index = sb.substring(sb.indexOf("<p>"), sb.indexOf("</p>")).indexOf("<br />");
						while(index!=-1){
							sb.replace(sb.indexOf("<br />"), sb.indexOf("<br />") + 6, " ");
							index = sb.substring(sb.indexOf("<p>"), sb.indexOf("</p>")).indexOf("<br />");
						}
						result.append(sb.substring(sb.indexOf("<p>")+3, sb.indexOf("</p>")));
						sb.delete(sb.indexOf("<p>"), sb.indexOf("</p>")+4);
					}
				}
				
				if(result.length()>=count){
					finalString = result.substring(0,count).toString()+"...";
				}else{
					finalString = result.toString()+"...";
				}

				articleVO.setArt_context(finalString);
				
				list.put(art_mem_no,articleVO); // Store the row in the list
			}

			// Handle any driver errors
		} catch (SQLException se) {
			throw new RuntimeException("A database error occured. " + se.getMessage());
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
	public byte[] getImage(String art_no) {
		// TODO Auto-generated method stub
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String img = null;

		try {
			con = ds.getConnection();
			pstmt = con.prepareStatement(GET_IMAGE_BY_ART_NO_STMT);
			pstmt.setString(1, art_no);
			rs = pstmt.executeQuery();

			while (rs.next()) {
				img = rs.getString(1);
			}

			if (img.contains("<img")) {
				result = img.substring(img.indexOf("src=\"data:image/jpeg;base64,") + 28);
				if(result.contains("\"")){
					result =result.substring(0, result.indexOf("\""));
				}
				System.out.println("clob圖片的String字串" + result);
				// final Base64.Decoder decoder = Base64.getDecoder();
				imagebytes = DatatypeConverter.parseBase64Binary(result);

			}

			System.out.println("imagebytes =" + imagebytes);

		} catch (SQLException se) {
			throw new RuntimeException("A database error occured. " + se.getMessage());
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
		return imagebytes;
	}
}