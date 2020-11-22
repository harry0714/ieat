package com.member.model;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

import com.article.model.ArticleVO;
import com.ord.model.OrdVO;

import jdbc.util.CompositeQuery.CompositeQuery_Member;

public class MemberDAO implements MemberDAO_interface{
private static DataSource ds = null;
	
	static{
		try {
			Context ctx = new InitialContext();
			ds = (DataSource)ctx.lookup("java:comp/env/jdbc/iEatDB");
		} catch (NamingException e) {
			e.printStackTrace();
		}
	}
	private static final String INSERT_STMT = 
			"INSERT INTO member (MEM_NO ,MEM_NAME ,MEM_ACCT ,MEM_PWD ,MEM_SEX ,MEM_BD ,MEM_EMAIL ,MEM_PHONE ,MEM_ZIP ,MEM_ADDR ,MEM_PHOTO ,MEM_VALIDATE, MEM_KEY) VALUES ('M'||LPAD(MEMBER_SEQ.NEXTVAL,9,'0'), ?, ?, ?, ? , ? , ? , ? , ? , ? , ? , sysdate,null)";
		private static final String GET_ALL_STMT = 
			"SELECT MEM_NO ,MEM_NAME ,MEM_ACCT ,MEM_PWD ,MEM_SEX ,MEM_BD ,MEM_EMAIL ,MEM_PHONE ,MEM_ZIP ,MEM_ADDR ,MEM_PHOTO ,MEM_VALIDATE FROM member order by mem_no";
		private static final String GET_ONE_STMT = 
			"SELECT MEM_NO ,MEM_NAME ,MEM_ACCT ,MEM_PWD ,MEM_SEX ,MEM_BD ,MEM_EMAIL ,MEM_PHONE ,MEM_ZIP ,MEM_ADDR ,MEM_PHOTO ,MEM_VALIDATE FROM member where mem_no = ?";
		private static final String DELETE = 
			"DELETE FROM member where mem_no = ?";
		private static final String UPDATE = 
			"UPDATE member set  MEM_NAME=?, MEM_ACCT=?, MEM_PWD=?, MEM_SEX=?, MEM_BD=?,MEM_EMAIL=?,MEM_PHONE=?,MEM_ZIP=?,MEM_ADDR=?,MEM_PHOTO=?,MEM_VALIDATE=? where mem_no = ?";
		private static final String GET_ONE_BY_ACCT_PWD = 
				"SELECT * FROM MEMBER WHERE MEM_ACCT=? AND MEM_PWD=?";
		private static final String UPDATE_FORM_EMBER = 
				"UPDATE member set  MEM_NAME=?, MEM_PWD=?, MEM_SEX=?,MEM_EMAIL=?,MEM_PHONE=?,MEM_ZIP=?,MEM_ADDR=?,MEM_PHOTO=? where mem_no = ?";
		private static final String GET_ARTICLE_BY_MEMNO = 
				"SELECT * FROM ARTICLE WHERE MEM_NO=? ORDER BY art_date desc";
		private static final String GET_MORE_STMT_BY_MEMBER =
				"SELECT * FROM ORD where MEM_NO = ?";
		//後端  會員停權
		private static final String Cancel_memCount_STMT = 
				"UPDATE member set MEM_VALIDATE=? where mem_no = ?";
	@Override
	public void insert(MemberVO memberVO) {
		Connection con =null;
		PreparedStatement pstmt = null;
		try {
			
			con = ds.getConnection();			
			pstmt = con.prepareStatement(INSERT_STMT);
			pstmt.setString(1, memberVO.getMem_name());
			pstmt.setString(2, memberVO.getMem_acct());
			pstmt.setString(3, memberVO.getMem_pwd());
			pstmt.setString(4, memberVO.getMem_sex());
			pstmt.setDate(5, memberVO.getMem_bd());
			pstmt.setString(6, memberVO.getMem_email());
			pstmt.setString(7, memberVO.getMem_phone());
			pstmt.setString(8, memberVO.getMem_zip());
			pstmt.setString(9, memberVO.getMem_addr());
			pstmt.setBytes(10, memberVO.getMem_photo());
			
			
			pstmt.executeUpdate();
			
			// Handle any SQL errors
		} catch (SQLException e) {
			throw new RuntimeException("A database error occured. "
					+ e.getMessage());
			
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
	public void update(MemberVO memberVO) {
		Connection con =null;
		PreparedStatement pstmt = null;
		try {
			
			con = ds.getConnection();
			
			pstmt = con.prepareStatement(UPDATE);
			pstmt.setString(1, memberVO.getMem_name());
			pstmt.setString(2, memberVO.getMem_acct());
			pstmt.setString(3, memberVO.getMem_pwd());
			pstmt.setString(4, memberVO.getMem_sex());
			pstmt.setDate(5, memberVO.getMem_bd());
			pstmt.setString(6, memberVO.getMem_email());
			pstmt.setString(7, memberVO.getMem_phone());
			pstmt.setString(8, memberVO.getMem_zip());
			pstmt.setString(9, memberVO.getMem_addr());
			pstmt.setBytes(10, memberVO.getMem_photo());
			pstmt.setDate(11, memberVO.getMem_validate());
			pstmt.setString(12, memberVO.getMem_no());
			
			pstmt.executeUpdate();
		} catch (SQLException e) {
			throw new RuntimeException("A database error occured. "
					+ e.getMessage());
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
	public void delete(String mem_no) {
		Connection con = null;
		PreparedStatement pstmt = null;
		try {
			
			con = ds.getConnection();
			
			pstmt = con.prepareStatement(DELETE);
			
			pstmt.setString(1, mem_no);
			
			pstmt.executeUpdate();
		} catch (SQLException e) {
			throw new RuntimeException("A database error occured. "
					+ e.getMessage());
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
	public MemberVO findByPrimaryKey(String mem_no) {
		Connection con = null;
		PreparedStatement pstmt = null;
		MemberVO memberVO = null;
		ResultSet rs = null;
		try {
			
			con = ds.getConnection();
			
			pstmt = con.prepareStatement(GET_ONE_STMT);
			
			pstmt.setString(1,mem_no);
			
			rs = pstmt.executeQuery();
			
			while(rs.next()){
				memberVO = new MemberVO();
				memberVO.setMem_no(rs.getString("mem_no"));
				memberVO.setMem_name(rs.getString("mem_name"));
				memberVO.setMem_acct(rs.getString("mem_acct"));
				memberVO.setMem_pwd(rs.getString("mem_pwd"));
				memberVO.setMem_sex(rs.getString("mem_sex"));
				memberVO.setMem_bd(rs.getDate("mem_bd"));
				memberVO.setMem_email(rs.getString("mem_email"));
				memberVO.setMem_phone(rs.getString("mem_phone"));
				memberVO.setMem_zip(rs.getString("mem_zip"));
				memberVO.setMem_addr(rs.getString("mem_addr"));
				memberVO.setMem_photo(rs.getBytes("mem_photo"));
				memberVO.setMem_validate(rs.getDate("mem_validate"));
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
		return memberVO;
	}

	@Override
	public List<MemberVO> getAll() {
		List<MemberVO> list = new ArrayList<>();
		Connection con = null;
		PreparedStatement pstmt = null;
		MemberVO memberVO = null;
		ResultSet rs = null;
		try {
			
			con = ds.getConnection();
			
			pstmt = con.prepareStatement(GET_ALL_STMT);
			
			rs = pstmt.executeQuery();
			
			while(rs.next()){
				memberVO = new MemberVO();
				memberVO.setMem_no(rs.getString("mem_no"));
				memberVO.setMem_name(rs.getString("mem_name"));
				memberVO.setMem_acct(rs.getString("mem_acct"));
				memberVO.setMem_pwd(rs.getString("mem_pwd"));
				memberVO.setMem_sex(rs.getString("mem_sex"));
				memberVO.setMem_bd(rs.getDate("mem_bd"));
				memberVO.setMem_email(rs.getString("mem_email"));
				memberVO.setMem_phone(rs.getString("mem_phone"));
				memberVO.setMem_zip(rs.getString("mem_zip"));
				memberVO.setMem_addr(rs.getString("mem_addr"));
				memberVO.setMem_photo(rs.getBytes("mem_photo"));
				memberVO.setMem_validate(rs.getDate("mem_validate"));
				list.add(memberVO);
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
	public MemberVO fingByAcctPwd(String mem_acct, String mem_pwd) {
		Connection con = null;
		PreparedStatement pstmt = null;
		MemberVO memberVO = null;
		ResultSet rs = null;
		try {
			
			con = ds.getConnection();
			pstmt = con.prepareStatement(GET_ONE_BY_ACCT_PWD);
			pstmt.setString(1, mem_acct);
			pstmt.setString(2, mem_pwd);
			
			rs = pstmt.executeQuery();
			while(rs.next()){
				memberVO = new MemberVO();
				memberVO.setMem_no(rs.getString("mem_no"));
				memberVO.setMem_name(rs.getString("mem_name"));
				memberVO.setMem_acct(rs.getString("mem_acct"));
				memberVO.setMem_pwd(rs.getString("mem_pwd"));
				memberVO.setMem_sex(rs.getString("mem_sex"));
				memberVO.setMem_bd(rs.getDate("mem_bd"));
				memberVO.setMem_email(rs.getString("mem_email"));
				memberVO.setMem_phone(rs.getString("mem_phone"));
				memberVO.setMem_zip(rs.getString("mem_zip"));
				memberVO.setMem_addr(rs.getString("mem_addr"));
				memberVO.setMem_photo(rs.getBytes("mem_photo"));
				memberVO.setMem_validate(rs.getDate("mem_validate"));
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
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
		return memberVO;
	}

	@Override
	public List<MemberVO> getAll(Map<String, String[]> map) {
		Connection con = null;
		PreparedStatement pstmt = null;
		List<MemberVO> list = new ArrayList<>();
		MemberVO memberVO = null;
		ResultSet rs = null;
		try {
			con = ds.getConnection();
			String finalSQL = "select * from member "
			          + CompositeQuery_Member.get_WhereCondition(map)
			          + "order by mem_no";
			pstmt = con.prepareStatement(finalSQL);
			rs = pstmt.executeQuery();
			while(rs.next()){
				memberVO = new MemberVO();
				memberVO.setMem_no(rs.getString("mem_no"));
				memberVO.setMem_name(rs.getString("mem_name"));
				memberVO.setMem_acct(rs.getString("mem_acct"));
				memberVO.setMem_pwd(rs.getString("mem_pwd"));
				memberVO.setMem_sex(rs.getString("mem_sex"));
				memberVO.setMem_bd(rs.getDate("mem_bd"));
				memberVO.setMem_email(rs.getString("mem_email"));
				memberVO.setMem_phone(rs.getString("mem_phone"));
				memberVO.setMem_zip(rs.getString("mem_zip"));
				memberVO.setMem_addr(rs.getString("mem_addr"));
				memberVO.setMem_photo(rs.getBytes("mem_photo"));
				memberVO.setMem_validate(rs.getDate("mem_validate"));
				list.add(memberVO);
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
	public void updateForMember(MemberVO memberVO) {
		Connection con = null;
		PreparedStatement pstmt = null;
		try {
			con = ds.getConnection();
			
			pstmt = con.prepareStatement(UPDATE_FORM_EMBER);
			pstmt.setString(1, memberVO.getMem_name());
			pstmt.setString(2, memberVO.getMem_pwd());
			pstmt.setString(3, memberVO.getMem_sex());
			pstmt.setString(4, memberVO.getMem_email());
			pstmt.setString(5, memberVO.getMem_phone());
			pstmt.setString(6, memberVO.getMem_zip());
			pstmt.setString(7, memberVO.getMem_addr());
			pstmt.setBytes(8, memberVO.getMem_photo());
			pstmt.setString(9, memberVO.getMem_no());
			
			pstmt.executeUpdate();
		} catch (SQLException e) {
			throw new RuntimeException("A database error occured. "
					+ e.getMessage());
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
	public List<ArticleVO> getArtByMember(String mem_no) {
		Connection con = null;
		PreparedStatement pstmt = null;
		List<ArticleVO> list = new ArrayList<>();
		ArticleVO artVO = null;
		ResultSet rs = null;
		try {
			con = ds.getConnection();
			pstmt = con.prepareStatement(GET_ARTICLE_BY_MEMNO);
			pstmt.setString(1,mem_no);
			rs = pstmt.executeQuery();
			while(rs.next()){
				artVO = new ArticleVO();
				artVO.setArt_no(rs.getString("art_no"));
				artVO.setArt_date(rs.getTimestamp("art_date"));
				artVO.setArt_context(rs.getString("art_context"));
				artVO.setMem_no(rs.getString("mem_no"));
				artVO.setArt_name(rs.getString("art_name"));
				list.add(artVO);
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
	public List<OrdVO> getMoreByMember(String mem_no) {
		List<OrdVO> list = new ArrayList<OrdVO>();
		OrdVO ordVO = null;

		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;

		try {

			con =ds.getConnection();
			pstmt = con.prepareStatement(GET_MORE_STMT_BY_MEMBER);
			pstmt.setString(1, mem_no);
			rs = pstmt.executeQuery();

			while (rs.next()) {
				// ordVO 也稱為 Domain objects
				ordVO = new OrdVO();
				ordVO.setOrd_no(rs.getString("ord_no"));
				ordVO.setMem_no(rs.getString("mem_no"));
				ordVO.setStore_no(rs.getString("store_no"));
				ordVO.setOrd_date(rs.getTimestamp("ord_date"));
				ordVO.setOrd_pickup(rs.getTimestamp("ord_pickup"));
				ordVO.setOrd_qrcode(rs.getBytes("ord_qrcode"));
				ordVO.setOrd_qrcode_status(rs.getString("ord_qrcode_status"));
				ordVO.setOrd_paid(rs.getInt("ord_paid"));
				ordVO.setOrd_report(rs.getString("ord_report"));
				ordVO.setOrd_report_status(rs.getString("ord_report_status"));

				list.add(ordVO); // Store the row in the list
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
	public void updateDate(Date mem_validate, String mem_no) {
		Connection con = null; 
		PreparedStatement pstmt = null; 
		
		try{
			con = ds.getConnection();
			pstmt = con.prepareStatement(Cancel_memCount_STMT);
						
			pstmt.setDate(1, mem_validate);
			
			pstmt.setString(2, mem_no);
			System.out.println(pstmt.executeUpdate());
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
}
