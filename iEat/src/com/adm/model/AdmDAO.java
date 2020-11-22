package com.adm.model;

import java.util.*;
import java.sql.*;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

public class AdmDAO implements AdmDAO_interface {

	// 一個應用程式中,針對一個資料庫 ,共用一個DataSource即可
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
			"INSERT INTO administrator (adm_no,adm_name,adm_user,adm_psd,adm_sex,adm_bd,adm_email,adm_phone,adm_level,adm_addr,adm_photo) VALUES ('A'||LPAD(ADMINISTRATOR_SEQ.NEXTVAL,9,'0'), ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
		private static final String GET_ALL_STMT = 
			"SELECT adm_no,adm_name,adm_user,adm_psd,adm_sex,adm_bd,adm_email,adm_phone,adm_level,adm_addr,adm_photo FROM administrator order by adm_no";
		private static final String GET_ONE_STMT = 
			"SELECT adm_no,adm_name,adm_user,adm_psd,adm_sex,adm_bd,adm_email,adm_phone,adm_level,adm_addr,adm_photo FROM administrator where adm_no = ?";
		private static final String DELETE = 
			"DELETE FROM administrator where adm_no = ?";
		private static final String UPDATE = 
			"UPDATE administrator set adm_name=?, adm_user=?, adm_psd=?, adm_sex=?, adm_bd=?, adm_email=?, adm_phone=? , adm_level=?, adm_addr=?, adm_photo=? where adm_no = ?";
		private static final String GET_User_STMT = 
				"SELECT adm_user,adm_psd,adm_name,adm_level FROM administrator where adm_user = ?";
		private static final String GET_ADM_IMAGE = 
				"SELECT adm_photo FROM administrator where adm_no=?";

	@Override
	public void insert(AdmVO admVO) {

		Connection con = null;
		PreparedStatement pstmt = null;

		try {

			con = ds.getConnection();
			pstmt = con.prepareStatement(INSERT_STMT);
			pstmt.setString(1, admVO.getAdm_name());
			pstmt.setString(2, admVO.getAdm_user());
			pstmt.setString(3, admVO.getAdm_psd());
			pstmt.setString(4, admVO.getAdm_sex());
			pstmt.setDate(5, admVO.getAdm_bd());
			pstmt.setString(6, admVO.getAdm_email());
			pstmt.setString(7, admVO.getAdm_phone());
			pstmt.setString(8, admVO.getAdm_level());
			pstmt.setString(9, admVO.getAdm_addr());
			pstmt.setBytes(10, admVO.getAdm_photo());
			
			
			pstmt.executeUpdate();

			// Handle any SQL errors
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
	public void update(AdmVO admVO) {

		Connection con = null;
		PreparedStatement pstmt = null;

		try {

			con = ds.getConnection();
			pstmt = con.prepareStatement(UPDATE);

			
			pstmt.setString(1, admVO.getAdm_name());
			pstmt.setString(2, admVO.getAdm_user());
			pstmt.setString(3, admVO.getAdm_psd());
			pstmt.setString(4, admVO.getAdm_sex());
			pstmt.setDate(5, admVO.getAdm_bd());
			pstmt.setString(6, admVO.getAdm_email());
			pstmt.setString(7, admVO.getAdm_phone());
			pstmt.setString(8, admVO.getAdm_level());
			pstmt.setString(9, admVO.getAdm_addr());
			pstmt.setBytes(10, admVO.getAdm_photo());
			pstmt.setString(11, admVO.getAdm_no());
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
	public void delete(String admVO) {

		Connection con = null;
		PreparedStatement pstmt = null;

		try {

			con = ds.getConnection();
			pstmt = con.prepareStatement(DELETE);

			pstmt.setString(1, admVO);

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
	public AdmVO findByPrimaryKey(String adm_no) {

		AdmVO admVO = null;
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;

		try {

			con = ds.getConnection();
			pstmt = con.prepareStatement(GET_ONE_STMT);

			pstmt.setString(1, adm_no);

			rs = pstmt.executeQuery();

			while (rs.next()) {
				// empVo 也稱為 Domain objects
				admVO = new AdmVO();
				admVO.setAdm_no(rs.getString("adm_no"));
				admVO.setAdm_name(rs.getString("adm_name"));
				admVO.setAdm_psd(rs.getString("adm_psd"));
				admVO.setAdm_user(rs.getString("adm_user"));
				admVO.setAdm_sex(rs.getString("adm_sex"));
				admVO.setAdm_bd(rs.getDate("adm_bd"));
				admVO.setAdm_email(rs.getString("adm_email"));
				admVO.setAdm_phone(rs.getString("adm_phone"));
				admVO.setAdm_level(rs.getString("adm_level"));
				admVO.setAdm_addr(rs.getString("adm_addr"));
				admVO.setAdm_photo(rs.getBytes("adm_photo"));
				
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
		return admVO;
	}

	@Override
	public List<AdmVO> getAll() {
		List<AdmVO> list = new ArrayList<AdmVO>();
		AdmVO admVO = null;

		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;

		try {

			con = ds.getConnection();
			pstmt = con.prepareStatement(GET_ALL_STMT);
			rs = pstmt.executeQuery();

			while (rs.next()) {
				// empVO 也稱為 Domain objects
				admVO = new AdmVO();
				admVO.setAdm_no(rs.getString("adm_no"));
				admVO.setAdm_name(rs.getString("adm_name"));
				admVO.setAdm_psd(rs.getString("adm_psd"));
				admVO.setAdm_user(rs.getString("adm_user"));
				admVO.setAdm_sex(rs.getString("adm_sex"));
				admVO.setAdm_bd(rs.getDate("adm_bd"));
				admVO.setAdm_email(rs.getString("adm_email"));
				admVO.setAdm_phone(rs.getString("adm_phone"));
				admVO.setAdm_level(rs.getString("adm_level"));
				admVO.setAdm_addr(rs.getString("adm_addr"));
				admVO.setAdm_photo(rs.getBytes("adm_photo"));
				list.add(admVO); // Store the row in the list
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
	public AdmVO findByUser(String adm_user) {
		AdmVO admVO = null;
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;

		try {

			con = ds.getConnection();
			pstmt = con.prepareStatement(GET_User_STMT);

			pstmt.setString(1, adm_user);
			
			rs = pstmt.executeQuery();

			while (rs.next()) {
				// empVo �]�٬� Domain objects
				admVO = new AdmVO();
				admVO.setAdm_name(rs.getString("adm_name"));
				admVO.setAdm_psd(rs.getString("adm_psd"));
				admVO.setAdm_user(rs.getString("adm_user"));
				admVO.setAdm_level(rs.getString("adm_level"));
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

		return admVO;
	}
	@Override
	public byte[] getAdmImage(String adm_no) {
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		byte[] adm_photo = null;

		try {

			con = ds.getConnection();
			pstmt = con.prepareStatement(GET_ADM_IMAGE);
			
			pstmt.setString(1, adm_no);
			
			rs = pstmt.executeQuery();
		
		while(rs.next()){		
			adm_photo = rs.getBytes("adm_photo");
		}
			
		} catch (SQLException e) {
			throw new RuntimeException("A database error occured. "
					+ e.getMessage());
		}finally{
			if(rs!=null){
				try{
					rs.close();
				}catch(SQLException se){
					se.printStackTrace(System.err);
				}
			}
			if(pstmt!=null){
				try{
					pstmt.close();
				}catch(SQLException se){
					se.printStackTrace(System.err);
				}
			}				
			if(con != null){
				try{
					con.close();
				}catch(Exception e){
					e.printStackTrace(System.err);
				}
			}
		}
		return adm_photo;
	}	
}