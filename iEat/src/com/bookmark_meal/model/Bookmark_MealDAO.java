package com.bookmark_meal.model;

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

public class Bookmark_MealDAO implements Bookmark_MealDAO_interface{
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
			"INSERT INTO bookmark_meal VALUES (?, ?)";
		private static final String GET_ALL_STMT = 
			"SELECT * FROM bookmark_meal order by mem_no";
		private static final String GET_ONE_STMT = 
			"SELECT * FROM bookmark_meal where mem_no = ? and meal_no = ? ";
		private static final String DELETE = 
			"DELETE FROM bookmark_meal where mem_no = ? and meal_no = ?";
		private static final String UPDATE = 
			"UPDATE bookmark_meal set MEM_NO=?, meal_no = ? where mem_no = ? and meal_no = ?";
		private static final String GET_ALL_STMT_BY_MEMNO = 
				"SELECT * FROM bookmark_meal where mem_no=? order by meal_no";
		private static final String GET_MEALNO_STMT_BY_MEMNO = 
				"SELECT meal_no FROM bookmark_meal where mem_no=? order by meal_no";
	@Override
	public void insert(Bookmark_MealVO bookmark_mealVO) {
		Connection con = null;
		PreparedStatement pstmt = null;
		
		try {
			con = ds.getConnection();
			pstmt = con.prepareStatement(INSERT_STMT);
			pstmt.setString(1, bookmark_mealVO.getMem_no());
			pstmt.setString(2, bookmark_mealVO.getMeal_no());
			
			pstmt.executeUpdate();
		} catch (SQLException e) {
			throw new RuntimeException("A database error occured. "
					+ e.getMessage());
		}finally{
			if(pstmt!=null){
				try {
					pstmt.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			if(con!=null){
				try {
					con.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}
		
		
	}

	@Override
	public void update(Bookmark_MealVO bookmark_mealVO) {
		
		
	}

	@Override
	public void delete(String mem_no, String meal_no) {
		Connection con = null;
		PreparedStatement pstmt = null;
		try {
			con = ds.getConnection();
			pstmt = con.prepareStatement(DELETE);
			pstmt.setString(1, mem_no);
			pstmt.setString(2, meal_no);
			
			pstmt.executeUpdate();
		} catch (SQLException e) {
			throw new RuntimeException("A database error occured. "
					+ e.getMessage());
		}finally{
			if(pstmt!=null){
				try {
					pstmt.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			if(con!=null){
				try {
					con.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}
		
		
	}

	@Override
	public Bookmark_MealVO findByPrimaryKey(String mem_no, String meal_no) {
		Connection con = null;
		PreparedStatement pstmt = null;
		Bookmark_MealVO bookmark_mealVO = null;
		ResultSet rs = null;
		try {
			con = ds.getConnection();
			pstmt = con.prepareStatement(GET_ONE_STMT);
			pstmt.setString(1,mem_no);
			pstmt.setString(2, meal_no);
			rs = pstmt.executeQuery();
			
			while(rs.next()){
				bookmark_mealVO = new Bookmark_MealVO();
				bookmark_mealVO.setMem_no(rs.getString("mem_no"));
				bookmark_mealVO.setMeal_no(rs.getString("meal_no"));
			}
		}  catch (SQLException e) {
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
		
		return bookmark_mealVO;
	}

	@Override
	public List<Bookmark_MealVO> findByMemNo(String mem_no) {
		List<Bookmark_MealVO> list = new ArrayList<>();
		Connection con = null;
		PreparedStatement pstmt = null;
		Bookmark_MealVO bookmark_mealVO = null;
		ResultSet rs = null;
		
		try {
			con = ds.getConnection();
			pstmt = con.prepareStatement(GET_ALL_STMT_BY_MEMNO);
			pstmt.setString(1, mem_no);
			
			rs = pstmt.executeQuery();
			
			while(rs.next()){
				bookmark_mealVO = new Bookmark_MealVO();
				bookmark_mealVO.setMem_no(rs.getString("mem_no"));
				bookmark_mealVO.setMeal_no(rs.getString("meal_no"));
				list.add(bookmark_mealVO);
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
	public List<Bookmark_MealVO> getAll() {
		List<Bookmark_MealVO> list = new ArrayList<>();
		Connection con = null;
		PreparedStatement pstmt = null;
		Bookmark_MealVO bookmark_mealVO = null;
		ResultSet rs = null;
		
		try {
			con = ds.getConnection();
			pstmt = con.prepareStatement(GET_ALL_STMT);
			
			rs = pstmt.executeQuery();
			
			while(rs.next()){
				bookmark_mealVO = new Bookmark_MealVO();
				bookmark_mealVO.setMem_no(rs.getString("mem_no"));
				bookmark_mealVO.setMeal_no(rs.getString("meal_no"));
				list.add(bookmark_mealVO);
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
	public List<String> findMealNoByMemNo(String mem_no) {
		List<String> list = new ArrayList<>();
		Connection con = null;
		PreparedStatement pstmt = null;
		String meal_no = null;
		ResultSet rs = null;
		
		try {
			con = ds.getConnection();
			pstmt = con.prepareStatement(GET_MEALNO_STMT_BY_MEMNO);
			pstmt.setString(1, mem_no);
			
			rs = pstmt.executeQuery();
			
			while(rs.next()){

				meal_no = rs.getString("meal_no");
				list.add(meal_no);
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
}
