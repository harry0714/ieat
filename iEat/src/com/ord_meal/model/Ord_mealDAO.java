package com.ord_meal.model;

import java.util.*;
import java.sql.*;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;


public class Ord_mealDAO implements Ord_mealDAO_interface{
	
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
			"INSERT INTO ord_meal(ORD_MEAL_NO,ORD_NO,MEAL_NO,ORD_MEAL_QTY,ORD_MEAL_PRICE) VALUES ('ORDML'||TRIM(TO_CHAR(ORD_MEAL_NO_SQ.NEXTVAL,'00000')),('O'||LPAD(ORD_NO_SQ.CURRVAL,9,'0')),?,?,?)";
	private static final String INSERT_STMT2 = 
			"INSERT INTO ord_meal(ORD_MEAL_NO,ORD_NO,MEAL_NO,ORD_MEAL_QTY,ORD_MEAL_PRICE) VALUES ('ORDML'||TRIM(TO_CHAR(ORD_MEAL_NO_SQ.NEXTVAL,'00000')),?,?,?,?)";	
	private static final String GET_ALL_STMT = 
			"SELECT ORD_MEAL_NO,ORD_NO,MEAL_NO,ORD_MEAL_QTY,ORD_MEAL_PRICE FROM ORD_MEAL";
	
	private static final String GET_ONE_STMT = 
			"SELECT ORD_NO,MEAL_NO,ORD_MEAL_QTY,ORD_MEAL_PRICE FROM ORD_MEAL WHERE ORD_MEAL_NO=?";
	
	private static final String DELETE = 
			"DELETE FROM ORD_MEAL where ORD_MEAL_NO = ?";
	
	private static final String UPDATE = 
			"UPDATE ORD_MEAL set ORD_NO=?,MEAL_NO=?,ORD_MEAL_QTY=?,ORD_MEAL_PRICE=? WHERE ORD_MEAL_NO=?";
	private static final String TOPFIVE_MEAL = 
			"select rownum rank , meal_no from (select  meal_no, sum(ord_meal_qty) a from ord_meal group by meal_no order by a desc) where rownum <= 5";
	
	
	@Override
	public void insert(Ord_mealVO ord_mealVO) {
		// TODO Auto-generated method stub
		
		Connection con = null;
		PreparedStatement pstmt = null;
		
		try {

			con = ds.getConnection();
			pstmt = con.prepareStatement(INSERT_STMT);

			//pstmt.setString(1,ord_mealVO.getOrd_no());
			pstmt.setString(1,ord_mealVO.getMeal_no());
			pstmt.setInt(2,ord_mealVO.getOrd_meal_qty());
			pstmt.setInt(3, ord_mealVO.getOrd_meal_price());
			pstmt.executeUpdate();

			// Handle any driver errors
		}  catch (SQLException se) {
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
	public void update(Ord_mealVO ord_mealVO) {
		// TODO Auto-generated method stub
		Connection con = null;
		PreparedStatement pstmt = null;
		
		try {
			
			
			con = ds.getConnection();
			pstmt = con.prepareStatement(UPDATE);
			
			
			pstmt.setString(1,ord_mealVO.getOrd_no());
			pstmt.setString(2,ord_mealVO.getMeal_no());
			pstmt.setInt(3,ord_mealVO.getOrd_meal_qty());
			pstmt.setInt(4,ord_mealVO.getOrd_meal_price());
			pstmt.setString(5,ord_mealVO.getOrd_meal_no());
			pstmt.executeUpdate();

			// Handle any driver errors
		}  catch (SQLException se) {
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
	public void delete(String ord_mealVO) {
		// TODO Auto-generated method stub
		Connection con = null;
		PreparedStatement pstmt = null;

		try {

			con = ds.getConnection();
			pstmt = con.prepareStatement(DELETE);

			pstmt.setString(1, ord_mealVO);

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
	public List<Ord_mealVO> getAll() {
		// TODO Auto-generated method stub
		
		List<Ord_mealVO> list = new ArrayList<Ord_mealVO>();
		Ord_mealVO ord_mealVO = null;

		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;

		try {

			con = ds.getConnection();
			pstmt = con.prepareStatement(GET_ALL_STMT);
			rs = pstmt.executeQuery();

			while (rs.next()) {
				// empVO �]�٬� Domain objects
				ord_mealVO = new Ord_mealVO();
				ord_mealVO.setOrd_meal_no(rs.getString("ord_meal_no"));
				ord_mealVO.setOrd_no(rs.getString("ord_no"));
				ord_mealVO.setMeal_no(rs.getString("meal_no"));
				ord_mealVO.setOrd_meal_qty(rs.getInt("ord_meal_qty"));
				ord_mealVO.setOrd_meal_price(rs.getInt("ord_meal_price"));
				list.add(ord_mealVO); // Store the row in the list
			}

			// Handle any driver errors
		}  catch (SQLException se) {
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
	public Ord_mealVO findByPrimaryKey(String ord_meal_no) {
		// TODO Auto-generated method stub
		Ord_mealVO ord_mealVO = null;
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		
		try {
			
			con = ds.getConnection();
			pstmt = con.prepareStatement(GET_ONE_STMT);

			pstmt.setString(1,ord_meal_no);

			rs = pstmt.executeQuery();
			
			
		while (rs.next()) {
			// empVo �]�٬� Domain objects
			ord_mealVO = new Ord_mealVO();
			ord_mealVO.setOrd_no(rs.getString("ord_no"));
			ord_mealVO.setMeal_no(rs.getString("meal_no"));
			ord_mealVO.setOrd_meal_qty(rs.getInt("ord_meal_qty"));
			ord_mealVO.setOrd_meal_price(rs.getInt("ord_meal_price"));
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
	return ord_mealVO;
	}

	@Override
	public void insertMany(Ord_mealVO ord_mealVO, Connection con) {
		
		PreparedStatement pstmt = null;
		
		try {

			pstmt = con.prepareStatement(INSERT_STMT2);
			pstmt.setString(1,ord_mealVO.getOrd_no());
			pstmt.setString(2,ord_mealVO.getMeal_no());
			pstmt.setInt(3,ord_mealVO.getOrd_meal_qty());
			pstmt.setInt(4, ord_mealVO.getOrd_meal_price());
			pstmt.executeUpdate();

			
			// Handle any driver errors
		} catch (SQLException se) {
			if (con != null) {
				try {
					// 3●設定於當有exception發生時之catch區塊內
					System.err.print("Transaction is being ");
					System.err.println("rolled back-由-ord_meal");
					con.rollback();
				} catch (SQLException excep) {
					throw new RuntimeException("rollback error occured. "
							+ excep.getMessage());
				}
			}
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
		}		
	}

	@Override
	public List<Ord_mealVO> getTopFiveMeal() {
		// TODO Auto-generated method stub
		List<Ord_mealVO> list = new ArrayList<Ord_mealVO>();
		Ord_mealVO ord_mealVO = null;

		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;

		try {

			con = ds.getConnection();
			pstmt = con.prepareStatement(TOPFIVE_MEAL);
			rs = pstmt.executeQuery();

			while (rs.next()) {
				// empVO �]�٬� Domain objects
				ord_mealVO = new Ord_mealVO();
				ord_mealVO.setMeal_no(rs.getString("meal_no"));
				list.add(ord_mealVO); // Store the row in the list
			}

			// Handle any driver errors
		}  catch (SQLException se) {
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
}
