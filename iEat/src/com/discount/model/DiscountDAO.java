package com.discount.model;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

import com.discount_meal.model.Discount_mealDAO;
import com.discount_meal.model.Discount_mealVO;

public class DiscountDAO implements DiscountDAO_interface{
	
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
		"INSERT INTO discount (discount_no,discount_title,discount_startdate,discount_enddate,store_no) VALUES ('D'||LPAD(DISCOUNT_SEQ.NEXTVAL,9,'0'), ?, ?, ?, ?)";
	private static final String GET_ALL_STMT = 
		"SELECT discount_no,discount_title,to_char(discount_startdate,'yyyy-mm-dd') discount_startdate,to_char(discount_enddate,'yyyy-mm-dd') discount_enddate,store_no FROM discount order by discount_enddate DESC";
	private static final String GET_ONE_STMT = 
		"SELECT discount_no,discount_title,to_char(discount_startdate,'yyyy-mm-dd') discount_startdate,to_char(discount_enddate,'yyyy-mm-dd') discount_enddate,store_no FROM discount where discount_no = ?";
	private static final String DELETE = 
		"DELETE FROM discount where discount_no = ?";
	private static final String UPDATE = 
		"UPDATE discount set discount_title=?, discount_startdate=?, discount_enddate=?, store_no=? where discount_no = ?";
	private static final String GET_ALLSTORE_STMT = 
		"SELECT discount_no,discount_title,to_char(discount_startdate,'yyyy-mm-dd') discount_startdate,to_char(discount_enddate,'yyyy-mm-dd') discount_enddate,store_no FROM discount WHERE store_no = ? order by discount_enddate DESC";
	private static final String COUNTMEAL =
		"SELECT COUNT(*) FROM discount_meal where discount_no=?";
	private static final String DELETE_DISCOUNTMEAL =
		"DELETE FROM discount_meal where discount_no = ?";		


	@Override
	public void insert(DiscountVO discountVO) {

		Connection con = null;
		PreparedStatement pstmt = null;

		try {

			con = ds.getConnection();
			pstmt = con.prepareStatement(INSERT_STMT);

			pstmt.setString(1, discountVO.getDiscount_title());
			pstmt.setDate(2, discountVO.getDiscount_startdate());
			pstmt.setDate(3, discountVO.getDiscount_enddate());
			pstmt.setString(4, discountVO.getStore_no());

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
	public void update(DiscountVO discountVO) {

		Connection con = null;
		PreparedStatement pstmt = null;

		try {

			con = ds.getConnection();
			pstmt = con.prepareStatement(UPDATE);


			pstmt.setString(1, discountVO.getDiscount_title());
			pstmt.setDate(2, discountVO.getDiscount_startdate());
			pstmt.setDate(3, discountVO.getDiscount_enddate());
			pstmt.setString(4, discountVO.getStore_no());
			pstmt.setString(5, discountVO.getDiscount_no());			

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
	public void delete(String discount_no) {
		int updateCount_Meals = 0;
		
		Connection con = null;
		PreparedStatement pstmt = null;

		try {
			con = ds.getConnection();
			
			// 1●設定於 pstm.executeUpdate()之前
			con.setAutoCommit(false);
			
			// 先刪除優惠餐點
			pstmt = con.prepareStatement(DELETE_DISCOUNTMEAL);
			pstmt.setString(1,discount_no);
			updateCount_Meals = pstmt.executeUpdate();
			// 再刪除優惠
			pstmt = con.prepareStatement(DELETE);
			pstmt.setString(1, discount_no);	
			pstmt.executeUpdate();

			// 2●設定於 pstm.executeUpdate()之後
			con.commit();
			con.setAutoCommit(true);
			System.out.println("刪除優惠編號" + discount_no + "時,共有優惠" + updateCount_Meals
					+ "同時被刪除");			
			// Handle any SQL errors
		} catch (SQLException se) {
			if(con!=null){
				try{
					// 3●設定於當有exception發生時之catch區塊內
					con.rollback();
				}catch(SQLException excep){
					throw new RuntimeException("A database error occured. "
						+ se.getMessage());
				}
			}
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
	public DiscountVO findByPrimaryKey(String discount_no) {

		DiscountVO discountVO = null;
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;

		try {

			con = ds.getConnection();
			pstmt = con.prepareStatement(GET_ONE_STMT);

			pstmt.setString(1, discount_no);

			rs = pstmt.executeQuery();

			while (rs.next()) {
				// discountVO 也稱為 Domain objects
				discountVO = new DiscountVO();
				discountVO.setDiscount_no(rs.getString("discount_no"));
				discountVO.setDiscount_title(rs.getString("discount_title"));
				discountVO.setDiscount_startdate(rs.getDate("discount_startdate"));
				discountVO.setDiscount_enddate(rs.getDate("discount_enddate"));
				discountVO.setStore_no(rs.getString("store_no"));
			}

			// Handle any SQL errors
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
		return discountVO;
	}

	@Override
	public List<DiscountVO> getAll() {
		List<DiscountVO> list = new ArrayList<DiscountVO>();
		DiscountVO discountVO = null;

		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;

		try {

			con = ds.getConnection();
			pstmt = con.prepareStatement(GET_ALL_STMT);
			rs = pstmt.executeQuery();

			while (rs.next()) {
				// discountVO 也稱為 Domain objects
				discountVO = new DiscountVO();
				discountVO.setDiscount_no(rs.getString("discount_no"));
				discountVO.setDiscount_title(rs.getString("discount_title"));
				discountVO.setDiscount_startdate(rs.getDate("discount_startdate"));
				discountVO.setDiscount_enddate(rs.getDate("discount_enddate"));
				discountVO.setStore_no(rs.getString("store_no"));

				list.add(discountVO); // Store the row in the list
			}

			// Handle any SQL errors
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
	public List<DiscountVO> getAllByStore(String store_no) {
		List<DiscountVO> list  =  new ArrayList<DiscountVO>();
		DiscountVO discountVO = null;
		
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		
		try{
			con = ds.getConnection();
			pstmt = con.prepareStatement(GET_ALLSTORE_STMT);

			pstmt.setString(1, store_no);			
			rs = pstmt.executeQuery();		
			while(rs.next()){
			
				discountVO = new DiscountVO();
				discountVO.setDiscount_no(rs.getString("discount_no"));	
				discountVO.setDiscount_title(rs.getString("discount_title"));
				discountVO.setDiscount_startdate(rs.getDate("discount_startdate"));
				discountVO.setDiscount_enddate(rs.getDate("discount_enddate"));
				discountVO.setStore_no(rs.getString("store_no"));
				
				list.add(discountVO);
			}
				
		}catch(SQLException se){
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
	public int getCountMeal(String discount_no) {
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		int count = 0;

		try{

			con = ds.getConnection();
			pstmt = con.prepareStatement(COUNTMEAL);
			pstmt.setString(1, discount_no);
			rs = pstmt.executeQuery();
			
			while(rs.next()){
				count = rs.getInt(1);
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
		return count;
	}

	@Override
	public void updateDiscountWithMeal(DiscountVO discountVO, List<Discount_mealVO> meal_list) {

		List<Discount_mealVO> list  =  new ArrayList<Discount_mealVO>();
		Connection con = null;
		PreparedStatement pstmt = null;
		
		try{
			con = ds.getConnection();
			
			con.setAutoCommit(false);
			
			pstmt = con.prepareStatement(UPDATE);//更新優惠
			pstmt.setString(1, discountVO.getDiscount_title());
			pstmt.setDate(2, discountVO.getDiscount_startdate());
			pstmt.setDate(3, discountVO.getDiscount_enddate());
			pstmt.setString(4, discountVO.getStore_no());
			pstmt.setString(5, discountVO.getDiscount_no());			
			pstmt.executeUpdate();
			
			pstmt = con.prepareStatement(DELETE_DISCOUNTMEAL);//刪除優惠餐點
			pstmt.setString(1, discountVO.getDiscount_no());
			pstmt.executeUpdate();
			
			Discount_mealDAO dao = new Discount_mealDAO();
			
			for(Discount_mealVO discount_mealVO : meal_list){//新增優惠餐點
				dao.insertMany(discount_mealVO,con);		
			}
			
			con.commit();
			con.setAutoCommit(true);
			
		} catch (SQLException se) {
			if(con!=null){
				try{
					se.printStackTrace();
					con.rollback();
				}catch(SQLException excep){
					throw new RuntimeException("A database error occured. "
					+ se.getMessage());
				}
			}
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
	public void insertDiscountWithMeal(DiscountVO discountVO, List<Discount_mealVO> meal_list) {
		
		Connection con = null;
		PreparedStatement pstmt = null;

		try {

			con = ds.getConnection();
			
			con.setAutoCommit(false);
			
			String[] cols = {"DISCOUNT_NO"};
			pstmt = con.prepareStatement(INSERT_STMT,cols);
			
			pstmt.setString(1, discountVO.getDiscount_title());
			pstmt.setDate(2, discountVO.getDiscount_startdate());
			pstmt.setDate(3, discountVO.getDiscount_enddate());
			pstmt.setString(4, discountVO.getStore_no());

			pstmt.executeUpdate();
			
			String next_discount_no = null;
			
			ResultSet rs = pstmt.getGeneratedKeys();
			if(rs.next()){
				next_discount_no = rs.getString(1);
				System.out.println("自增主鍵值= " + next_discount_no +"(剛新增成功的優惠編號)");
			}else{
				System.out.println("未取得自增主鍵值");
			}

			rs.close();
			
			Discount_mealDAO dao = new Discount_mealDAO();
			
			for(Discount_mealVO discount_mealVO : meal_list){//新增優惠餐點
				discount_mealVO.setDiscount_no(next_discount_no);
				dao.insertMany(discount_mealVO,con);
			}
			
			System.out.println("list.size()="+meal_list.size());
			System.out.println("新增優惠編號" + next_discount_no + "時,共有優惠餐點" + meal_list.size()
					+ "樣同時被新增");
			
			con.commit();
			con.setAutoCommit(true);			
					
			// Handle any SQL errors
		} catch (SQLException se) {
			if(con!=null){
				try{
					se.printStackTrace();
					con.rollback();
				}catch(SQLException excep){
					throw new RuntimeException("A database error occured. "
					+ se.getMessage());
				}
			}
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
