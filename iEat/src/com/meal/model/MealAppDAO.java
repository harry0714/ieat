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

public class MealAppDAO implements MealAppDAO_interface{
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
					"INSERT INTO MEAL(MEAL_NO,STORE_NO,MEAL_PHOTO,MEAL_NAME,MEAL_DESCR,MEAL_PRICE,MEAL_STATUS,MEAL_DISCOUNT)VALUES('ML'||TRIM(TO_CHAR(MEAL_NO_SQ.NEXTVAL,'00000000')),?,?,?,?,?,?,?)";
				private static final String GET_ALL_STMT = 
						"SELECT meal_no,store_no,meal_photo,meal_name,meal_descr,meal_price,meal_status,meal_discount FROM meal order by meal_no";
				private static final String GET_ONE_STMT = 
						"SELECT m.meal_no,m.store_no,m.meal_photo,m.meal_name,m.meal_descr,m.meal_price,m.meal_status, NVL(price,0) meal_discount "
						+ "from meal m left join (select min (discount_meal_price) as price, dm.meal_no from  discount_meal dm left join discount d on (dm.discount_no=d.discount_no) "
						+ "where sysdate between d.DISCOUNT_STARTDATE and d.DISCOUNT_ENDDATE group by dm.meal_no) abc on (abc.meal_no = m.meal_no) where m.meal_no = ? "; 
				private static final String DELETE = 
					"DELETE FROM meal where meal_no = ?";
				private static final String UPDATE = 
						"UPDATE meal set store_no=?,meal_photo=?,meal_name=?, meal_descr=?, meal_price=? ,meal_status=? ,meal_discount=? where meal_no = ?";
				private static final String GET_STOREMEAL_STMT = 
						"Select m.meal_no, m.store_no, m.meal_name, m.meal_descr, m.meal_price, m.meal_status, NVL(price,0) meal_discount "
						+ "from meal m left join (select min (discount_meal_price) as price, dm.meal_no from  discount_meal dm left join "
						+ "discount d on (dm.discount_no=d.discount_no) where sysdate between d.DISCOUNT_STARTDATE and d.DISCOUNT_ENDDATE"
						+ " group by dm.meal_no) abc on (abc.meal_no = m.meal_no) where m.store_no = ? ";
				
				
				private static final String FINDBYMEAL = 
						"Select m.meal_no, m.store_no, m.meal_name, m.meal_descr, m.meal_price, m.meal_status, NVL(price,0) meal_discount "
						+ "from meal m left join (select min (discount_meal_price) as price, dm.meal_no from  discount_meal dm left join "
						+ "discount d on (dm.discount_no=d.discount_no) where sysdate between d.DISCOUNT_STARTDATE and d.DISCOUNT_ENDDATE"
						+ " group by dm.meal_no) abc on (abc.meal_no = m.meal_no) where m.meal_status = 0 and m.store_no = ? ";	//有上架的商品
				private static final String GET_MEAL_IMAGE = 
						"Select meal_photo from meal WHERE meal_no = ?";
				
				private static final String GET_ONE_AVAILABLE_MEAL = 
						"SELECT m.meal_no,m.store_no,m.meal_name,m.meal_descr,m.meal_price,m.meal_status, NVL(price,0) meal_discount "
						+ "from meal m left join (select min (discount_meal_price) as price, dm.meal_no from  discount_meal dm left join discount d on (dm.discount_no=d.discount_no) "
						+ "where sysdate between d.DISCOUNT_STARTDATE and d.DISCOUNT_ENDDATE group by dm.meal_no) abc on (abc.meal_no = m.meal_no) where m.meal_no = ? and m.meal_status = 0";//回傳餐點並確認是否有上架 
						
				private static final String GET_TOP_MEAL = "select * from (select ddd.meal_no,ddd.meal_price,ddd.meal_discount, ddd.meal_name, store_name, store_no from (select * from (select s.store_name, om.meal_no, sum(ord_meal_qty) amount "
						+ "from ord_meal om left join meal m on om.meal_no=m.meal_no left join store s on s.store_no = m.store_no group by om.meal_no, s.store_name order by amount DESC)) bbb left join (SELECT m.meal_no,m.store_no,m.meal_name,m.meal_descr,m.meal_price,m.meal_status, NVL(price,0) meal_discount "
						+ "from meal m left join (select min (discount_meal_price) as price, dm.meal_no from  discount_meal dm left join discount d on (dm.discount_no=d.discount_no) where sysdate between d.DISCOUNT_STARTDATE and d.DISCOUNT_ENDDATE group by dm.meal_no) abc on (abc.meal_no = m.meal_no)) ddd on bbb.meal_no = ddd.meal_no where ddd.meal_status = 0) ccc where rownum<=10";
				
				private static final String UPDATE_MEAL_STATUS = 
						"UPDATE meal set meal_status=? where meal_no = ?";
				
				@Override
				public void insert(MealVO mealVO) {

					Connection con = null;
					PreparedStatement pstmt = null;

					try {

						con = ds.getConnection();
						pstmt = con.prepareStatement(INSERT_STMT);

						pstmt.setString(1,mealVO.getStore_no());
						pstmt.setBytes(2,mealVO.getMeal_photo());
						pstmt.setString(3,mealVO.getMeal_name());
						pstmt.setString(4,mealVO.getMeal_descr());
						pstmt.setInt(5, mealVO.getMeal_price());
						pstmt.setInt(6, mealVO.getMeal_status());
						pstmt.setInt(7, mealVO.getMeal_discount());
						
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
				public void update(MealVO mealVO) {

					Connection con = null;
					PreparedStatement pstmt = null;

					try {
						
						
						con = ds.getConnection();
						pstmt = con.prepareStatement(UPDATE);
						
						
						pstmt.setString(1, mealVO.getStore_no());
						pstmt.setBytes(2,mealVO.getMeal_photo());
						pstmt.setString(3, mealVO.getMeal_name());
						pstmt.setString(4, mealVO.getMeal_descr());
						pstmt.setInt(5, mealVO.getMeal_price());
						pstmt.setInt(6, mealVO.getMeal_status());
						pstmt.setInt(7, mealVO.getMeal_discount());
						pstmt.setString(8, mealVO.getMeal_no());
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
				public MealVO findByPrimaryKey(String meal_no) {

					MealVO mealVO = null;
					Connection con = null;
					PreparedStatement pstmt = null;
					ResultSet rs = null;

					try {

						
						con = ds.getConnection();
						pstmt = con.prepareStatement(GET_ONE_STMT);

						pstmt.setString(1,meal_no);

						rs = pstmt.executeQuery();

						while (rs.next()) {
							// empVo 也稱為 Domain objects
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
					return mealVO;
				}


				@Override
				public List<MealVO> getAllStoreMeal(String store_no) {
					List<MealVO> list = new ArrayList<MealVO>();
					MealVO mealVO = null;
					
					Connection con = null;
					PreparedStatement pstmt = null;
					ResultSet rs = null;
					
					try {
						con = ds.getConnection();
						pstmt = con.prepareStatement(GET_STOREMEAL_STMT);
						pstmt.setString(1, store_no);
						rs = pstmt.executeQuery();
						
						while(rs.next()){
							mealVO = new MealVO();
							mealVO.setMeal_no(rs.getString("meal_no"));
							mealVO.setStore_no(rs.getString("store_no"));
							mealVO.setMeal_name(rs.getString("meal_name"));
							mealVO.setMeal_descr(rs.getString("meal_descr"));
							mealVO.setMeal_price(rs.getInt("meal_price"));
							mealVO.setMeal_status(rs.getInt("meal_status"));
							mealVO.setMeal_discount(rs.getInt("meal_discount"));
							list.add(mealVO); // Store the row in the list
						}
						
					} catch (SQLException se) {
						throw new RuntimeException("A database error occured. "
								+ se.getMessage());
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
							mealVO.setMeal_name(rs.getString("meal_name"));
							mealVO.setMeal_descr(rs.getString("meal_descr"));
							mealVO.setMeal_price(rs.getInt("meal_price"));
							mealVO.setMeal_status(rs.getInt("meal_status"));
							mealVO.setMeal_discount(rs.getInt("meal_discount"));
							set.add(mealVO);
						}
						
					} catch (SQLException se) {
						throw new RuntimeException("A database error occured. "
								+ se.getMessage());
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
				public byte[] getMealImg(String meal_no) {

					Connection con = null;
					PreparedStatement pstmt = null;
					ResultSet rs = null;
					byte[] photo = null;

					try {

						
						con = ds.getConnection();
						pstmt = con.prepareStatement(GET_MEAL_IMAGE);

						pstmt.setString(1,meal_no);

						rs = pstmt.executeQuery();

						while (rs.next()) {
							// empVo 也稱為 Domain objects
							photo = rs.getBytes("meal_photo");
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
					return photo;
				}
				
				@Override
				public MealVO getOneAvailableMeal(String meal_no) {

					MealVO mealVO = null;
					Connection con = null;
					PreparedStatement pstmt = null;
					ResultSet rs = null;

					try {

						
						con = ds.getConnection();
						pstmt = con.prepareStatement(GET_ONE_AVAILABLE_MEAL);

						pstmt.setString(1,meal_no);

						rs = pstmt.executeQuery();

						while (rs.next()) {
							// empVo 也稱為 Domain objects
							mealVO = new MealVO();
							mealVO.setMeal_no(rs.getString("meal_no"));
							mealVO.setStore_no(rs.getString("store_no"));
							mealVO.setMeal_name(rs.getString("meal_name"));
							mealVO.setMeal_descr(rs.getString("meal_descr"));
							mealVO.setMeal_price(rs.getInt("meal_price"));
							mealVO.setMeal_status(rs.getInt("meal_status"));
							mealVO.setMeal_discount(rs.getInt("meal_discount"));
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
					return mealVO;
				}				
				
				@Override
				public Map<String,MealVO> getTopMeals() {
					Map<String,MealVO> map = null;
					MealVO mealVO = null;
					Connection con = null;
					PreparedStatement pstmt = null;
					ResultSet rs = null;

					try {

						
						con = ds.getConnection();
						pstmt = con.prepareStatement(GET_TOP_MEAL);

						rs = pstmt.executeQuery();
						map = new LinkedHashMap<String,MealVO>();
						while (rs.next()) {
							// empVo 也稱為 Domain objects
							mealVO = new MealVO();
							mealVO.setMeal_no(rs.getString("meal_no"));
							mealVO.setStore_no(rs.getString("store_no"));
							mealVO.setMeal_name(rs.getString("meal_name"));
							mealVO.setMeal_price(rs.getInt("meal_price"));
							mealVO.setMeal_discount(rs.getInt("meal_discount"));
							map.put(mealVO.getStore_no()+","+rs.getString("store_name"), mealVO);
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
					return map;
				}
				
				@Override
				public void updateMealStatus(String meal_no, int status) {

					Connection con = null;
					PreparedStatement pstmt = null;

					try {
						
						
						con = ds.getConnection();
						pstmt = con.prepareStatement(UPDATE_MEAL_STATUS);
						
						
						pstmt.setInt(1, status);
						pstmt.setString(2, meal_no);

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
			}
