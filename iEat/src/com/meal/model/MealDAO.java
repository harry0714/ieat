package com.meal.model;

import java.util.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.*;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

public class MealDAO implements MealDAO_interface {
	private static DataSource ds = null;
	static {
		try {
			Context ctx = new InitialContext();
			ds = (DataSource) ctx.lookup("java:comp/env/jdbc/iEatDB");
		} catch (NamingException e) {
			e.printStackTrace();
		}
	}
	private static final String INSERT_STMT = "INSERT INTO MEAL(MEAL_NO,STORE_NO,MEAL_PHOTO,MEAL_NAME,MEAL_DESCR,MEAL_PRICE,MEAL_STATUS,MEAL_DISCOUNT)VALUES('ML'||TRIM(TO_CHAR(MEAL_NO_SQ.NEXTVAL,'00000000')),?,?,?,?,?,?,?)";
	private static final String GET_ALL_STMT = "SELECT meal_no,store_no,meal_photo,meal_name,meal_descr,meal_price,meal_status,meal_discount FROM meal order by meal_no";
	private static final String GET_ONE_STMT = "SELECT m.meal_no,m.store_no,m.meal_photo,m.meal_name,m.meal_descr,m.meal_price,m.meal_status, NVL(price,0) meal_discount "
			+ "from meal m left join (select min (discount_meal_price) as price, dm.meal_no from  discount_meal dm left join discount d on (dm.discount_no=d.discount_no) "
			+ "where sysdate between d.DISCOUNT_STARTDATE and d.DISCOUNT_ENDDATE group by dm.meal_no) abc on (abc.meal_no = m.meal_no) where m.meal_no = ? ";
	private static final String DELETE = "DELETE FROM meal where meal_no = ?";
	private static final String UPDATE = "UPDATE meal set store_no=?,meal_photo=?,meal_name=?, meal_descr=?, meal_price=? ,meal_status=? ,meal_discount=? where meal_no = ?";
	private static final String FINDBYMEAL = "Select m.meal_no, m.store_no, m.meal_photo, m.meal_name, m.meal_descr, m.meal_price, m.meal_status, NVL(price,0) meal_discount "
			+ "from meal m left join (select min (discount_meal_price) as price, dm.meal_no from  discount_meal dm left join "
			+ "discount d on (dm.discount_no=d.discount_no) where sysdate between d.DISCOUNT_STARTDATE and d.DISCOUNT_ENDDATE"
			+ " group by dm.meal_no) abc on (abc.meal_no = m.meal_no) where m.meal_status = 0 and m.store_no = ? "; // 有上架的商品
	// 首頁推薦餐點
	private static final String RANDOMMEAL = "select A.meal_no, A.meal_photo, A.meal_name, A.meal_descr, A.meal_price, A.meal_discount from"
			+ "(select m.meal_no, m.meal_photo, m.store_no, m.meal_name, m.meal_descr, m.meal_price, m.meal_status, NVL(price,0) meal_discount from meal m left join"
			+ "(select min (discount_meal_price) as price, dm.meal_no from  discount_meal dm left join discount d on (dm.discount_no=d.discount_no)"
			+ "where sysdate between d.DISCOUNT_STARTDATE and d.DISCOUNT_ENDDATE group by dm.meal_no) abc on (abc.meal_no = m.meal_no) where m.meal_status = 0"
			+ "order by dbms_random.value)A where rownum <= 3";// 首頁推薦餐點
	private static final String GET_OneStoreNoTop_STMT = "SELECT meal_no,store_no,meal_photo,meal_name,meal_descr,meal_price,meal_status,meal_discount FROM meal where store_no=? and meal_status=?";

	private static final String UPDATE_MEAL_STATUS = "UPDATE meal set meal_status=?  where meal_no = ?";
	private static final String GET_RANDOM = "SELECT * FROM ( SELECT * FROM meal WHERE meal_status = 0 ORDER BY dbms_random.value) WHERE rownum <= ? ";

	@Override
	public void insert(MealVO mealVO) {

		Connection con = null;
		PreparedStatement pstmt = null;

		try {

			con = ds.getConnection();
			pstmt = con.prepareStatement(INSERT_STMT);

			pstmt.setString(1, mealVO.getStore_no());
			pstmt.setBytes(2, mealVO.getMeal_photo());
			pstmt.setString(3, mealVO.getMeal_name());
			pstmt.setString(4, mealVO.getMeal_descr());
			pstmt.setInt(5, mealVO.getMeal_price());
			pstmt.setInt(6, mealVO.getMeal_status());
			pstmt.setInt(7, mealVO.getMeal_discount());

			pstmt.executeUpdate();

			// Handle any driver errors
		} catch (SQLException se) {
			throw new RuntimeException("A database error occured. " + se.getMessage());
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
	public void update(MealVO mealVO) {

		Connection con = null;
		PreparedStatement pstmt = null;

		try {

			con = ds.getConnection();
			pstmt = con.prepareStatement(UPDATE);

			pstmt.setString(1, mealVO.getStore_no());
			pstmt.setBytes(2, mealVO.getMeal_photo());
			pstmt.setString(3, mealVO.getMeal_name());
			pstmt.setString(4, mealVO.getMeal_descr());
			pstmt.setInt(5, mealVO.getMeal_price());
			pstmt.setInt(6, mealVO.getMeal_status());
			pstmt.setInt(7, mealVO.getMeal_discount());
			pstmt.setString(8, mealVO.getMeal_no());
			pstmt.executeUpdate();

			// Handle any driver errors
		} catch (SQLException se) {
			throw new RuntimeException("A database error occured. " + se.getMessage());
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
	public void delete(String meal_no) {

		Connection con = null;
		PreparedStatement pstmt = null;

		try {

			con = ds.getConnection();
			pstmt = con.prepareStatement(DELETE);

			pstmt.setString(1, meal_no);

			pstmt.executeUpdate();

			// Handle any driver errors
		} catch (SQLException se) {
			throw new RuntimeException("A database error occured. " + se.getMessage());
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
	public MealVO findByPrimaryKey(String meal_no) {

		MealVO mealVO = null;
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;

		try {

			con = ds.getConnection();
			pstmt = con.prepareStatement(GET_ONE_STMT);

			pstmt.setString(1, meal_no);

			rs = pstmt.executeQuery();

			while (rs.next()) {
				// empVo �]�٬� Domain objects
				mealVO = new MealVO();
				mealVO.setMeal_no(rs.getString("meal_no"));
				mealVO.setStore_no(rs.getString("store_no"));
				mealVO.setMeal_photo(rs.getBytes("meal_photo"));
				mealVO.setMeal_name(rs.getString("meal_name"));
				mealVO.setMeal_descr(rs.getString("meal_descr"));
				mealVO.setMeal_price(rs.getInt("meal_price"));
				mealVO.setMeal_status(rs.getInt("meal_status"));
				mealVO.setMeal_discount(rs.getInt("meal_discount"));
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
		return mealVO;
	}

	@Override
	public List<MealVO> getAll() {
		List<MealVO> list = new ArrayList<MealVO>();
		MealVO mealVO = null;

		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;

		try {

			con = ds.getConnection();
			pstmt = con.prepareStatement(GET_ALL_STMT);
			rs = pstmt.executeQuery();

			while (rs.next()) {
				// empVO �]�٬� Domain objects
				mealVO = new MealVO();
				mealVO.setMeal_no(rs.getString("meal_no"));
				mealVO.setStore_no(rs.getString("store_no"));
				mealVO.setMeal_photo(rs.getBytes("meal_photo"));
				mealVO.setMeal_name(rs.getString("meal_name"));
				mealVO.setMeal_descr(rs.getString("meal_descr"));
				mealVO.setMeal_price(rs.getInt("meal_price"));
				mealVO.setMeal_status(rs.getInt("meal_status"));
				mealVO.setMeal_discount(rs.getInt("meal_discount"));
				list.add(mealVO); // Store the row in the list
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
	public Set<MealVO> getFindByMeal(String store_no) {
		Set<MealVO> set = new LinkedHashSet<MealVO>();
		MealVO mealVO = null;

		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;

		try {
			con = ds.getConnection();
			pstmt = con.prepareStatement(FINDBYMEAL);
			pstmt.setString(1, store_no);
			rs = pstmt.executeQuery();

			while (rs.next()) {
				mealVO = new MealVO();
				mealVO.setMeal_no(rs.getString("meal_no"));
				mealVO.setStore_no(rs.getString("store_no"));
				mealVO.setMeal_photo(rs.getBytes("meal_photo"));
				mealVO.setMeal_name(rs.getString("meal_name"));
				mealVO.setMeal_descr(rs.getString("meal_descr"));
				mealVO.setMeal_price(rs.getInt("meal_price"));
				mealVO.setMeal_status(rs.getInt("meal_status"));
				mealVO.setMeal_discount(rs.getInt("meal_discount"));
				set.add(mealVO);
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
		return set;
	}

	@Override
	public List<MealVO> getRandomMeal() {
		// TODO Auto-generated method stub
		List<MealVO> list = new ArrayList<MealVO>();
		MealVO mealVO = null;

		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;

		try {

			con = ds.getConnection();
			pstmt = con.prepareStatement(RANDOMMEAL);
			rs = pstmt.executeQuery();

			while (rs.next()) {
				// empVO �]�٬� Domain objects
				mealVO = new MealVO();
				mealVO.setMeal_no(rs.getString("meal_no"));
				mealVO.setMeal_photo(rs.getBytes("meal_photo"));
				mealVO.setMeal_name(rs.getString("meal_name"));
				mealVO.setMeal_descr(rs.getString("meal_descr"));
				mealVO.setMeal_price(rs.getInt("meal_price"));
				mealVO.setMeal_discount(rs.getInt("meal_discount"));
				list.add(mealVO); // Store the row in the list
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
	public List<MealVO> getOneStoreNoTop(String store_no, Integer meal_status) {
		List<MealVO> notop = new ArrayList<MealVO>();
		MealVO mealVO = null;

		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;

		try {

			con = ds.getConnection();
			pstmt = con.prepareStatement(GET_OneStoreNoTop_STMT);
			pstmt.setString(1, store_no);
			pstmt.setInt(2, meal_status);
			rs = pstmt.executeQuery();

			while (rs.next()) {
				// empVO 也稱為 Domain objects
				mealVO = new MealVO();
				mealVO.setMeal_no(rs.getString("meal_no"));
				mealVO.setStore_no(rs.getString("store_no"));
				mealVO.setMeal_photo(rs.getBytes("meal_photo"));
				mealVO.setMeal_name(rs.getString("meal_name"));
				mealVO.setMeal_descr(rs.getString("meal_descr"));
				mealVO.setMeal_price(rs.getInt("meal_price"));
				mealVO.setMeal_status(rs.getInt("meal_status"));
				mealVO.setMeal_discount(rs.getInt("meal_discount"));
				notop.add(mealVO); // Store the row in the list
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
		return notop;
	}

	@Override
	public void updateMealStatus(Integer meal_status, String meal_no) {
		Connection con = null;
		PreparedStatement pstmt = null;
		try {
			con = ds.getConnection();
			pstmt = con.prepareStatement(UPDATE_MEAL_STATUS);
			pstmt.setInt(1, meal_status);
			pstmt.setString(2, meal_no);
			pstmt.executeUpdate();
		} catch (SQLException se) {
			throw new RuntimeException("A database error occured. " + se.getMessage());
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
	public List<MealVO> getRandom(int quantity) {
		List<MealVO> list = new ArrayList<MealVO>();
		MealVO mealVO = null;

		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;

		try {

			con = ds.getConnection();
			pstmt = con.prepareStatement(GET_RANDOM);
			pstmt.setInt(1, quantity);
			rs = pstmt.executeQuery();

			while (rs.next()) {

				mealVO = new MealVO();
				mealVO.setMeal_no(rs.getString("meal_no"));
				mealVO.setStore_no(rs.getString("store_no"));
				mealVO.setMeal_photo(rs.getBytes("meal_photo"));
				mealVO.setMeal_name(rs.getString("meal_name"));
				mealVO.setMeal_descr(rs.getString("meal_descr"));
				mealVO.setMeal_price(rs.getInt("meal_price"));
				mealVO.setMeal_status(rs.getInt("meal_status"));
				mealVO.setMeal_discount(rs.getInt("meal_discount"));
				list.add(mealVO); // Store the row in the list
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
}
