package com.member.model;

import java.sql.Connection;
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

import jdbc.util.CompositeQuery.CompositeQuery_Member;

public class MemberAppDAO implements MemberAppDAO_interface {
	private static DataSource ds = null;

	static {
		try {
			Context ctx = new InitialContext();
			ds = (DataSource) ctx.lookup("java:comp/env/jdbc/iEatDB");
		} catch (NamingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	private static final String INSERT_STMT = "INSERT INTO member (MEM_NO ,MEM_NAME ,MEM_ACCT ,MEM_PWD ,MEM_SEX ,MEM_BD ,MEM_EMAIL ,MEM_PHONE ,MEM_ZIP ,MEM_ADDR ,MEM_PHOTO ,MEM_VALIDATE) VALUES ('M'||LPAD(MEMBER_SEQ.NEXTVAL,9,'0'), ?, ?, ?, ? , ? , ? , ? , ? , ? , ? , sysdate)";
	private static final String GET_ALL_STMT = "SELECT MEM_NO ,MEM_NAME ,MEM_ACCT ,MEM_PWD ,MEM_SEX ,MEM_BD ,MEM_EMAIL ,MEM_PHONE ,MEM_ZIP ,MEM_ADDR ,MEM_PHOTO ,MEM_VALIDATE FROM member order by mem_no";
	private static final String DELETE = "DELETE FROM member where mem_no = ?";
	private static final String UPDATE = "UPDATE member set  MEM_NAME=?, MEM_ACCT=?, MEM_PWD=?, MEM_SEX=?, MEM_BD=?,MEM_EMAIL=?,MEM_PHONE=?,MEM_ZIP=?,MEM_ADDR=?,MEM_PHOTO=?,MEM_VALIDATE=? where mem_no = ?";

	private static final String GET_ONE_STMT = "SELECT MEM_NO ,MEM_NAME ,MEM_ACCT ,MEM_PWD ,MEM_SEX ,MEM_BD ,MEM_EMAIL ,MEM_PHONE ,MEM_ZIP ,MEM_ADDR,MEM_VALIDATE FROM member where mem_no = ?";

	private static final String GET_ONE_BY_ACCT_PWD = "SELECT MEM_NO FROM MEMBER WHERE MEM_ACCT=? AND MEM_PWD=?";
	private static final String UPDATE_FORM_EMBER = "UPDATE member set  MEM_NAME=?, MEM_PWD=?, MEM_SEX=?,MEM_EMAIL=?,MEM_PHONE=?,MEM_ZIP=?,MEM_ADDR=?,MEM_PHOTO=? where mem_no = ?";
	private static final String GET_MEM_IMG = "SELECT MEM_PHOTO FROM member where mem_no = ?";
	private static final String FIND_MEM_BY_ID = "SELECT MEM_NO ,MEM_NAME ,MEM_ACCT ,MEM_PWD ,MEM_SEX ,MEM_BD ,MEM_EMAIL ,MEM_PHONE ,MEM_ZIP ,MEM_ADDR,MEM_VALIDATE FROM member where mem_no = ?";

	private static final String FIND_MEM_BY_ACCT_STMT = "SELECT COUNT(*) FROM MEMBER WHERE MEM_ACCT=?";
	private static final String UPLOAD_KEY_STMT = "UPDATE member set  MEM_KEY=? WHERE MEM_NO = ?" ;
	private static final String GET_MEM_KEY = "SELECT MEM_KEY FROM MEMBER WHERE MEM_NO = ?";
	private static final String UPDATE_PWD = "UPDATE member set  MEM_PWD=? WHERE MEM_NO = ?";
	
	@Override
	public String insert(MemberVO memberVO) {
		Connection con = null;
		PreparedStatement pstmt = null;
		String mem_no = null;

		try {

			con = ds.getConnection();
			String[] cols = { "MEM_NO" };
			pstmt = con.prepareStatement(INSERT_STMT, cols);

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

			ResultSet rs = pstmt.getGeneratedKeys();
			if (rs.next()) {
				mem_no = rs.getString(1);
			} else {
				System.out.println("未取得自增主鍵值");
			}

			// Handle any SQL errors
		} catch (SQLException e) {
			throw new RuntimeException("A database error occured. " + e.getMessage());

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
		return mem_no;
	}

	@Override
	public void update(MemberVO memberVO) {
		Connection con = null;
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
			throw new RuntimeException("A database error occured. " + e.getMessage());
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
	public MemberVO findByPrimaryKey(String mem_no) {
		Connection con = null;
		PreparedStatement pstmt = null;
		MemberVO memberVO = null;
		ResultSet rs = null;
		try {

			con = ds.getConnection();

			pstmt = con.prepareStatement(GET_ONE_STMT);

			pstmt.setString(1, mem_no);

			rs = pstmt.executeQuery();

			while (rs.next()) {
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
				memberVO.setMem_validate(rs.getDate("mem_validate"));
			}
		} catch (SQLException e) {
			throw new RuntimeException("A database error occured. " + e.getMessage());
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
	public String fingByAcctPwd(String mem_acct, String mem_pwd) {
		Connection con = null;
		PreparedStatement pstmt = null;
		String mem_no = null;
		ResultSet rs = null;
		try {

			con = ds.getConnection();
			pstmt = con.prepareStatement(GET_ONE_BY_ACCT_PWD);
			pstmt.setString(1, mem_acct);
			pstmt.setString(2, mem_pwd);

			rs = pstmt.executeQuery();
			while (rs.next()) {
				mem_no = rs.getString("mem_no");
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
		return mem_no;
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
			throw new RuntimeException("A database error occured. " + e.getMessage());
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
	public byte[] getMemImage(String mem_no) {
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		byte[] mem_photo = null;

		try {

			con = ds.getConnection();

			pstmt = con.prepareStatement(GET_MEM_IMG);

			pstmt.setString(1, mem_no);
			rs = pstmt.executeQuery();

			while (rs.next()) {
				mem_photo = rs.getBytes("mem_photo");
			}
		} catch (SQLException e) {
			throw new RuntimeException("A database error occured. " + e.getMessage());
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
		return mem_photo;
	}

	@Override
	public MemberVO findMemberByNo(String mem_no) {
		// TODO Auto-generated method stub
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		MemberVO memberVO = null;

		try {
			con = ds.getConnection();
			pstmt = con.prepareStatement(FIND_MEM_BY_ID);
			pstmt.setString(1, mem_no);
			rs = pstmt.executeQuery();

			while (rs.next()) {
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
	public int findMemberByAcct(String mem_acct) {
		// TODO Auto-generated method stub
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		int count = 0;

		try {
			con = ds.getConnection();
			pstmt = con.prepareStatement(FIND_MEM_BY_ACCT_STMT);
			pstmt.setString(1, mem_acct);
			rs = pstmt.executeQuery();

			while (rs.next()) {
				count = rs.getInt(1);
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

		return count;
	}
	
	@Override
	public void updateMemberKey(String mem_no,String mem_key) {
		Connection con = null;
		PreparedStatement pstmt = null;
		try {

			con = ds.getConnection();

			pstmt = con.prepareStatement(UPLOAD_KEY_STMT);
			
			pstmt.setString(1, mem_key);
			pstmt.setString(2, mem_no);

			pstmt.executeUpdate();
			
			System.out.println(mem_key);
		} catch (SQLException e) {
			throw new RuntimeException("A database error occured. " + e.getMessage());
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
	public String getMemberKey(String mem_no) {
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String mem_key = null;
		try {

			con = ds.getConnection();
			pstmt = con.prepareStatement(GET_MEM_KEY);
			pstmt.setString(1, mem_no);


			rs = pstmt.executeQuery();
			while (rs.next()) {
				mem_key = rs.getString("mem_key");
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
		return mem_key;
	}
	
	@Override
	public void updatePwd(String mem_no,String new_pwd) {
		Connection con = null;
		PreparedStatement pstmt = null;
		try {
			con = ds.getConnection();

			pstmt = con.prepareStatement(UPDATE_PWD);
			
			pstmt.setString(1, new_pwd);
			pstmt.setString(2, mem_no);

			pstmt.executeUpdate();

		} catch (SQLException e) {
			throw new RuntimeException("A database error occured. " + e.getMessage());
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
