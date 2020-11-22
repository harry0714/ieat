package com.advertisement.model;

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

public class AdvertisementDAO implements AdvertisementDAO_interface{
	
	private static DataSource ds = null;
	
	static{	
		try {
			Context ctx = new InitialContext();
			ds = (DataSource) ctx.lookup("java:comp/env/jdbc/iEatDB");
		} catch (NamingException e) {
			e.printStackTrace();
		}
		
	}
	private static final String INSERT_STMT =
			"INSERT INTO advertisement (ad_no,ad_image,ad_imagetitle,ad_startdate,ad_enddate,store_no) VALUES ('A'||LPAD(ADVERTISEMENT_SEQ.NEXTVAL,9,'0'),?,?,?,?,?)";
	private static final String GET_ALL_STMT =
			"SELECT ad_no,ad_imagetitle, to_char(ad_startdate,'yyyy-mm-dd') ad_startdate, to_char(ad_enddate,'yyyy-mm-dd') ad_enddate, store_no FROM advertisement order by ad_enddate DESC";
	private static final String GET_ONE_STMT =
			"SELECT ad_no,ad_imagetitle, to_char(ad_startdate,'yyyy-mm-dd') ad_startdate, to_char(ad_enddate,'yyyy-mm-dd') ad_enddate, store_no FROM advertisement where ad_no=? order by ad_enddate DESC";
	private static final String DELETE =
			"DELETE FROM advertisement where ad_no=?";
	private static final String UPDATE =
			"UPDATE advertisement set ad_image=?, ad_imagetitle=?, ad_startdate=?, ad_enddate=?, store_no=? where ad_no=?";
	private static final String GET_AD_IMAGE =
			"SELECT ad_image FROM advertisement where ad_no=?";
	private static final String GET_RANDOM = 
			"SELECT * FROM ( SELECT * FROM ADVERTISEMENT WHERE NOT (AD_IMAGE IS NULL) AND  (SYSDATE between ad_startdate and AD_ENDDATE) ORDER BY dbms_random.value) WHERE rownum <= ? ";
	@Override
	public void insert(AdvertisementVO advertisementVO) {

		Connection con = null;
		PreparedStatement pstmt = null;
		
		try{
			con = ds.getConnection();
			pstmt = con.prepareStatement(INSERT_STMT);
			
			pstmt.setBytes(1, advertisementVO.getAd_image());
			pstmt.setString(2, advertisementVO.getAd_imagetitle());;
			pstmt.setDate(3, advertisementVO.getAd_startdate());
			pstmt.setDate(4, advertisementVO.getAd_enddate());
			pstmt.setString(5, advertisementVO.getStore_no());
			
			pstmt.executeUpdate();
			
		}catch(SQLException se){
			throw new RuntimeException("A database error occured. "
					+ se.getMessage());
		}finally{
			if(pstmt !=null){
				try{
					pstmt.close();
				}catch(SQLException se){
					se.printStackTrace(System.err);
				}
			}
			if(con !=null){
				try{
					con.close();
				}catch(Exception e){
					e.printStackTrace(System.err);
				}
			}
		}
	}

	@Override
	public void update(AdvertisementVO advertisementVO) {
		Connection con = null;
		PreparedStatement pstmt = null;
		
		try{
			con = ds.getConnection();
			pstmt = con.prepareStatement(UPDATE);
			
			pstmt.setBytes(1, advertisementVO.getAd_image());
			pstmt.setString(2, advertisementVO.getAd_imagetitle());
			pstmt.setDate(3, advertisementVO.getAd_startdate());
			pstmt.setDate(4, advertisementVO.getAd_enddate());
			pstmt.setString(5, advertisementVO.getStore_no());
			pstmt.setString(6,advertisementVO.getAd_no());
					
			pstmt.executeUpdate();
			
		} catch (SQLException se) {
			throw new RuntimeException("A database error occured. "
					+ se.getMessage());
		}finally{
			if(pstmt !=null){
				try{
					pstmt.close();
				}catch(SQLException se){
					se.printStackTrace(System.err);
				}
			}
			if(con !=null){
				try{
					con.close();
				}catch(Exception e){
					e.printStackTrace(System.err);
				}
			}
		}
		
	}

	@Override
	public void delete(String ad_no) {
		Connection con = null;
		PreparedStatement pstmt = null;
		
		try{
			con = ds.getConnection();
			pstmt = con.prepareStatement(DELETE);
			
			pstmt.setString(1, ad_no);
			
			pstmt.executeUpdate();
			
		} catch (SQLException se) {
			throw new RuntimeException("A database error occured. "
					+ se.getMessage());
		}finally{
			if(pstmt!=null){
				try{
					pstmt.close();
				}catch(SQLException se){
					se.printStackTrace(System.err);
				}
			}
			if(con!=null){
				try{
					con.close();
				}catch(Exception e){
					e.printStackTrace(System.err);
				}				
			}
		}
	}

	@Override
	public AdvertisementVO findByPrimaryKey(String ad_no) {
		AdvertisementVO advertisementVO = null;
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;

		try {

			con = ds.getConnection();
			pstmt = con.prepareStatement(GET_ONE_STMT);
			
			pstmt.setString(1, ad_no);
			
		rs = pstmt.executeQuery();
		
		while(rs.next()){
			
			advertisementVO = new AdvertisementVO();
			advertisementVO.setAd_no(rs.getString("ad_no"));
			advertisementVO.setAd_imagetitle(rs.getString("ad_imagetitle"));
			advertisementVO.setAd_startdate(rs.getDate("ad_startdate"));
			advertisementVO.setAd_enddate(rs.getDate("ad_enddate"));
			advertisementVO.setStore_no(rs.getString("store_no"));
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
		return advertisementVO;
	}

	@Override
	public List<AdvertisementVO> getAll() {
		List<AdvertisementVO> list = new ArrayList<AdvertisementVO>();
		AdvertisementVO advertisementVO = null;
		
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		
		
		try {
			con = ds.getConnection();
			pstmt = con.prepareStatement(GET_ALL_STMT);
			rs = pstmt.executeQuery();
			
			while(rs.next()){
				
				advertisementVO = new AdvertisementVO();
				advertisementVO.setAd_no(rs.getString("ad_no"));
				advertisementVO.setAd_imagetitle(rs.getString("ad_imagetitle"));
				advertisementVO.setAd_startdate(rs.getDate("ad_startdate"));
				advertisementVO.setAd_enddate(rs.getDate("ad_enddate"));
				advertisementVO.setStore_no(rs.getString("store_no"));
				
				list.add(advertisementVO);
			}
			
		} catch (SQLException se) {
			throw new RuntimeException("A database error occured. "
					+ se.getMessage());
		}finally{
			if(rs!=null){
				try {
					rs.close();
				} catch (SQLException se) {
					se.printStackTrace(System.err);
				}
			}
			if(pstmt!=null){
				try {
					pstmt.close();
				} catch (SQLException se) {
					se.printStackTrace(System.err);
				}
			}
			if(con!=null){
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
	public List<AdvertisementVO> getSome(String stmt) {
		List<AdvertisementVO> list = new ArrayList<AdvertisementVO>();
		AdvertisementVO advertisementVO = null;
		
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		
		try {
			con = ds.getConnection();
			pstmt = con.prepareStatement("SELECT ad_no, ad_imagetitle, to_char(ad_startdate,'yyyy-mm-dd') ad_startdate, to_char(ad_enddate,'yyyy-mm-dd') ad_enddate, store_no FROM advertisement "+ stmt +" order by ad_startdate DESC");
			rs = pstmt.executeQuery();
			
			while(rs.next()){
				
				advertisementVO = new AdvertisementVO();
				advertisementVO.setAd_no(rs.getString("ad_no"));
				advertisementVO.setAd_imagetitle(rs.getString("ad_imagetitle"));
				advertisementVO.setAd_startdate(rs.getDate("ad_startdate"));
				advertisementVO.setAd_enddate(rs.getDate("ad_enddate"));
				advertisementVO.setStore_no(rs.getString("store_no"));
				
				list.add(advertisementVO);

			}
			
		} catch (SQLException se) {
			throw new RuntimeException("A database error occured. "
					+ se.getMessage());
		}finally{
			if(rs!=null){
				try {
					rs.close();
				} catch (SQLException se) {
					se.printStackTrace(System.err);
				}
			}
			if(pstmt!=null){
				try {
					pstmt.close();
				} catch (SQLException se) {
					se.printStackTrace(System.err);
				}
			}
			if(con!=null){
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
		
		while(rs.next()){		
			ad_image = rs.getBytes("ad_image");
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
		return ad_image;
	}	
	@Override
	public List<AdvertisementVO> getRandom(int quantity) {
		List<AdvertisementVO> list = new ArrayList<AdvertisementVO>();
		AdvertisementVO advertisementVO = null;
		
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		
		
		try {
			
			con = ds.getConnection();
			pstmt = con.prepareStatement(GET_RANDOM);
			pstmt.setInt(1, quantity);
			rs = pstmt.executeQuery();
			
			while(rs.next()){
				
				advertisementVO = new AdvertisementVO();
				advertisementVO.setAd_no(rs.getString("ad_no"));
				advertisementVO.setAd_imagetitle(rs.getString("ad_imagetitle"));
				advertisementVO.setAd_startdate(rs.getDate("ad_startdate"));
				advertisementVO.setAd_enddate(rs.getDate("ad_enddate"));
				advertisementVO.setStore_no(rs.getString("store_no"));
				
				list.add(advertisementVO);
			}
			
		} catch (SQLException se) {
			throw new RuntimeException("A database error occured. "
					+ se.getMessage());
		}finally{
			if(rs!=null){
				try {
					rs.close();
				} catch (SQLException se) {
					se.printStackTrace(System.err);
				}
			}
			if(pstmt!=null){
				try {
					pstmt.close();
				} catch (SQLException se) {
					se.printStackTrace(System.err);
				}
			}
			if(con!=null){
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
