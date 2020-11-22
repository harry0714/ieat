package com.advertisement.model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

import com.store.model.StoreVO;

public class AdAppDAO implements AdAppDAO_interface {

	private static DataSource ds = null;

	static {
		try {
			Context ctx = new InitialContext();
			ds = (DataSource) ctx.lookup("java:comp/env/jdbc/iEatDB");
		} catch (NamingException e) {
			e.printStackTrace();
		}

	}

	private static final String GET_AD_IMAGE = "SELECT ad_image FROM advertisement where ad_no=?";
	private static final String GET_CURRENT_AD = "SELECT ad_no, store.store_no from advertisement left join store on advertisement.store_no = store.store_no WHERE to_char(ad_startdate, 'yyyy-mm-dd') <= to_char(sysdate, 'yyyy-mm-dd') and to_char(ad_enddate, 'yyyy-mm-dd') >= to_char(sysdate, 'yyyy-mm-dd') and store_status=1";

	
	@Override
	public byte[] getAdImage(String ad_no) {
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		byte[] ad_image = null;

		try {

			con = ds.getConnection();
			pstmt = con.prepareStatement(GET_AD_IMAGE);

			pstmt.setString(1, ad_no);

			rs = pstmt.executeQuery();

			while (rs.next()) {
				ad_image = rs.getBytes("ad_image");
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
		return ad_image;
	}
	
	@Override
	public Map<String,String> getCurrentAd() {
		Map<String,String> adStoreMap = new HashMap<String,String>();
		StoreVO storeVO = null;
		
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;

		try {
			con = ds.getConnection();
			pstmt = con.prepareStatement(GET_CURRENT_AD);
			rs = pstmt.executeQuery();

			while (rs.next()) {
				adStoreMap.put(rs.getString("ad_no"), rs.getString("store_no"));

			}

		} catch (SQLException se) {
			throw new RuntimeException("A database error occured. " + se.getMessage());
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
		return adStoreMap;
	}

}
