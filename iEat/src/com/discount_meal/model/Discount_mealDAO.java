package com.discount_meal.model;

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

public class Discount_mealDAO implements Discount_mealDAO_interface {
	
	private static DataSource ds = null;
	static{
		try{
			Context ctx = new InitialContext();
			ds = (DataSource) ctx.lookup("java:comp/env/jdbc/iEatDB");
		} catch(NamingException e){
			e.printStackTrace();
		}
	}
	
	private static final String INSERT_STMT = 
			"INSERT INTO discount_meal (discount_no, meal_no, discount_meal_price) VALUES (?,?,?)";
	private static final String GET_ALL_STMT =
			"SELECT discount_no, meal_no, discount_meal_price FROM discount_meal order by discount_no";
	private static final String GET_ONE_STMT =
			"SELECT discount_no, meal_no, discount_meal_price FROM discount_meal where discount_no=? and meal_no=?";
	private static final String DELETE =
			"DELETE FROM discount_meal where discount_no=? and meal_no=?";
	private static final String UPDATE =
			"UPDATE discount_meal set discount_meal_price=? where discount_no=? and meal_no=?";
	private static final String GET_DISCOUNTMEALS_STMT =
			"SELECT discount_no, meal_no, discount_meal_price FROM discount_meal where discount_no=?";
	
	@Override
	public void insert(Discount_mealVO discount_mealVO) {

		Connection con = null;
		PreparedStatement pstmt = null;
		
		try{
			
			con = ds.getConnection();
			pstmt = con.prepareStatement(INSERT_STMT);
			
			pstmt.setString(1, discount_mealVO.getDiscount_no());
			pstmt.setString(2, discount_mealVO.getMeal_no());
			pstmt.setInt(3, discount_mealVO.getDiscount_meal_price());
			
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
	public void update(Discount_mealVO discount_mealVO) {

		Connection con = null;
		PreparedStatement pstmt = null;
		
		try{
			
			con = ds.getConnection();
			pstmt = con.prepareStatement(UPDATE);
			
			pstmt.setInt(1, discount_mealVO.getDiscount_meal_price());
			pstmt.setString(2, discount_mealVO.getDiscount_no());
			pstmt.setString(3, discount_mealVO.getMeal_no());
			
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
	public void delete(String discount_no, String meal_no) {
		
		Connection con = null;
		PreparedStatement pstmt = null;
		
		try{
			
			con = ds.getConnection();
			pstmt = con.prepareStatement(DELETE);
			
			pstmt.setString(1, discount_no);
			pstmt.setString(2, meal_no);
			
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
	public Discount_mealVO findByPrimaryKey(String discount_no, String meal_no) {
		
		Discount_mealVO discount_mealVO = null;
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		
		try{
			
			con = ds.getConnection();
			pstmt = con.prepareStatement(GET_ONE_STMT);
			
			pstmt.setString(1, discount_no);
			pstmt.setString(2, meal_no);
			
			rs = pstmt.executeQuery();
			
			while(rs.next()){
				
				discount_mealVO = new Discount_mealVO();
				discount_mealVO.setDiscount_no(rs.getString("discount_no"));
				discount_mealVO.setMeal_no(rs.getString("meal_no"));
				discount_mealVO.setDiscount_meal_price(rs.getInt("discount_meal_price"));
			}
			
		}catch(SQLException se){
			throw new RuntimeException("A database error occured. "
					+ se.getMessage());
		}finally{
			if(rs!=null){
				try{
					rs.close();
				}catch(SQLException se){
					se.printStackTrace(System.err);
				}
			}
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
		
		return discount_mealVO;
	}

	@Override
	public List<Discount_mealVO> getAll() {
		List<Discount_mealVO> list = new ArrayList<Discount_mealVO>();
		Discount_mealVO discount_mealVO = null;
		
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		
		try{

			con = ds.getConnection();
			pstmt = con.prepareStatement(GET_ALL_STMT);			
			rs = pstmt.executeQuery();
			
			while(rs.next()){
				
				discount_mealVO = new Discount_mealVO();
				discount_mealVO.setDiscount_no(rs.getString("discount_no"));
				discount_mealVO.setMeal_no(rs.getString("meal_no"));
				discount_mealVO.setDiscount_meal_price(rs.getInt("discount_meal_price"));
				
				list.add(discount_mealVO);
			}
			
		}catch(SQLException se){
			throw new RuntimeException("A database error occured. "
					+ se.getMessage());
		}finally{
			if(rs!=null){
				try{
					rs.close();
				}catch(SQLException se){
					se.printStackTrace(System.err);
				}
			}			
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
		
		return list;
	}

	@Override
	public List<Discount_mealVO> getDiscountMeals(String discount_no) {
		List<Discount_mealVO> list = new ArrayList<Discount_mealVO>();
		Discount_mealVO discount_mealVO = null;
		
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		
		try{

			con = ds.getConnection();
			pstmt = con.prepareStatement(GET_DISCOUNTMEALS_STMT);
			pstmt.setString(1, discount_no);
			rs = pstmt.executeQuery();
			
			while(rs.next()){
				
				discount_mealVO = new Discount_mealVO();
				discount_mealVO.setDiscount_no(rs.getString("discount_no"));
				discount_mealVO.setMeal_no(rs.getString("meal_no"));
				discount_mealVO.setDiscount_meal_price(rs.getInt("discount_meal_price"));
				
				list.add(discount_mealVO);
			}
			
		}catch(SQLException se){
			throw new RuntimeException("A database error occured. "
					+ se.getMessage());
		}finally{
			if(rs!=null){
				try{
					rs.close();
				}catch(SQLException se){
					se.printStackTrace(System.err);
				}
			}			
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
		
		return list;
	}	
	
	@Override
	public void insertMany(Discount_mealVO discount_mealVO, Connection con) {

		PreparedStatement pstmt = null;
		
		try{

			pstmt = con.prepareStatement(INSERT_STMT);
			
			pstmt.setString(1, discount_mealVO.getDiscount_no());
			pstmt.setString(2, discount_mealVO.getMeal_no());
			pstmt.setInt(3, discount_mealVO.getDiscount_meal_price());
			
			pstmt.executeUpdate();
			
		}catch(SQLException se){
			if(con!=null){
				try{
					System.err.print("Transaction is being ");
					System.err.println("rolled back-由-discount_meal");
					con.rollback();				
				}catch(SQLException excep){
					throw new RuntimeException("A database error occured. "
							+ excep.getMessage());
				}
			}
		}finally{
			if(pstmt !=null){
				try{
					pstmt.close();
				}catch(SQLException se){
					se.printStackTrace(System.err);
				}
			}
		}
		
	}
	
}
