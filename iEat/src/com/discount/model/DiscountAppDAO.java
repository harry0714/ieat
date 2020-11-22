package com.discount.model;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

import com.advertisement.tools.AdStatusType;
import com.discount_meal.model.Discount_mealDAO;
import com.discount_meal.model.Discount_mealVO;

public class DiscountAppDAO implements DiscountAppDAO_interface{
	
	private static DataSource ds = null;
	static {
		try {
			Context ctx = new InitialContext();
			ds = (DataSource) ctx.lookup("java:comp/env/jdbc/iEatDB");
		} catch (NamingException e) {
			e.printStackTrace();
		}
	}
	

	private static final String GET_CURRENT_STMT = 
		"SELECT discount_no,discount_title,to_char(discount_startdate,'yyyy-mm-dd') discount_startdate,to_char(discount_enddate,'yyyy-mm-dd') discount_enddate, discount.store_no, store_name FROM discount left join store on discount.store_no = store.store_no WHERE to_char(discount_startdate, 'yyyy-mm-dd') <= to_char(sysdate, 'yyyy-mm-dd') and to_char(discount_enddate, 'yyyy-mm-dd') >= to_char(sysdate, 'yyyy-mm-dd') and store_status=1 order by discount_startdate DESC";



	@Override
	public Map<String,DiscountVO> getCurrentDiscount() {
		Map<String,DiscountVO> list = new LinkedHashMap<String,DiscountVO>();
		DiscountVO discountVO = null;
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;

		try {

			con = ds.getConnection();
			pstmt = con.prepareStatement(GET_CURRENT_STMT);
			rs = pstmt.executeQuery();

			while (rs.next()) {
				// discountVO 也稱為 Domain objects
				String id = rs.getString("discount_no")+","+rs.getString("store_name");
				discountVO = new DiscountVO();
				discountVO.setDiscount_no(rs.getString("discount_no"));
				discountVO.setDiscount_title(rs.getString("discount_title"));
				discountVO.setDiscount_startdate(rs.getDate("discount_startdate"));
				discountVO.setDiscount_enddate(rs.getDate("discount_enddate"));
				discountVO.setStore_no(rs.getString("store_no"));

				list.put(id,discountVO); // Store the row in the list
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
	
	
}
