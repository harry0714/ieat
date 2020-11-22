package com.discount.controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.*;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.discount.model.DiscountService;
import com.discount.model.DiscountVO;
import com.discount_meal.model.Discount_mealService;
import com.discount_meal.model.Discount_mealVO;
import com.meal.model.MealService;
import com.meal.model.MealVO;
import com.store.model.StoreService;
import com.store.model.StoreVO;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

public class DiscountServlet extends HttpServlet {

	protected void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {

		doPost(req, res);
	}

	protected void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
		req.setCharacterEncoding("UTF-8");
		String action = req.getParameter("action");
		HttpSession session = req.getSession();
		
		if("insert".equals(action)){
			List<String> errorMsgs = new LinkedList<String>();
			req.setAttribute("errorMsgs", errorMsgs);
			String url = req.getParameter("requestURL");
			
			try{
				Map<String, String> store = (Map<String, String>) session.getAttribute("store");
				String store_no = store.get("store_no");
				
				String discount_title = req.getParameter("discount_title");				
			
				if(discount_title==null||discount_title.trim().length()==0){
					discount_title = "優惠標題";
					errorMsgs.add("請輸入優惠標題");
				}
				
				java.sql.Date discount_startdate = null;
				java.sql.Date discount_enddate = null;
				
				try{
					discount_startdate = java.sql.Date.valueOf(req.getParameter("discount_startdate"));
				}catch(IllegalArgumentException e){
					discount_startdate = new java.sql.Date(System.currentTimeMillis());
					errorMsgs.add("請指定開始日期");
				}
				
				try{
					discount_enddate = java.sql.Date.valueOf(req.getParameter("discount_enddate"));
				}catch(IllegalArgumentException e){
					discount_enddate = new java.sql.Date(System.currentTimeMillis());
					errorMsgs.add("請指定結束日期");
				}
				DiscountVO discountVO =  new DiscountVO();
				discountVO.setDiscount_title(discount_title);
				discountVO.setDiscount_startdate(discount_startdate);
				discountVO.setDiscount_enddate(discount_enddate);
				discountVO.setStore_no(store_no);

			
				String[] meal_no = req.getParameterValues("meal_no");
				List<Discount_mealVO> meal_list = null;
				Discount_mealVO discount_mealVO = null;
				List<String> check_list = null;
				
				if(meal_no==null){
					errorMsgs.add("請至少指定一項餐點");
				}
				else{								
					meal_list = new ArrayList<Discount_mealVO>();
					check_list = new ArrayList<String>();
					MealService mealSvc = new MealService();
					MealVO mealVO = new MealVO();
					
					for(int i=0;i<meal_no.length;i++){
						discount_mealVO = new Discount_mealVO();
						discount_mealVO.setMeal_no(meal_no[i]);
						mealVO = mealSvc.getOneMeal(meal_no[i]);
						String meal_name = mealVO.getMeal_name();					
						
						Integer price = null;
						try{														
							price = new Integer(req.getParameter(meal_no[i]).trim());
							if(price<=0){
								price = 0;
								errorMsgs.add(meal_name+"的價格不可為0或負數");								
							}else if(mealVO.getMeal_price()<price){
								errorMsgs.add(meal_name+"的價格大於原價");	
							}
						}catch(NumberFormatException e){
							price = 0;
							errorMsgs.add(meal_name+"的優惠價格未填寫");
						}
						
						discount_mealVO.setDiscount_meal_price(price);
						meal_list.add(discount_mealVO);
						check_list.add(meal_no[i]);
					}						
				}
	
				if(!errorMsgs.isEmpty()){
					req.setAttribute("check_list", check_list);
					req.setAttribute("meal_list", meal_list);
					req.setAttribute("discountVO", discountVO);
					RequestDispatcher failureView = req.getRequestDispatcher(url);
					failureView.forward(req, res);
					return;
				}
				
				DiscountService discountSvc = new DiscountService();
				discountSvc.insertDiscountWithMeal(discountVO, meal_list);;

				url = "discount.do?action=get_store_discount&store_no="+store_no;
			
				req.setAttribute("success", "新增: "+discount_title);
				RequestDispatcher successView = req.getRequestDispatcher(url);
				successView.forward(req, res);

			}catch(Exception e){
				errorMsgs.add(e.getMessage());
				RequestDispatcher failureView = req.getRequestDispatcher(url);
				failureView.forward(req, res);
			}
		}		

		
		if("get_update_form".equals(action)){			
			String discount_no = req.getParameter("discount_no");			
			DiscountService discountSvc = new DiscountService();
			DiscountVO discountVO = discountSvc.getOneDiscount(discount_no);
			
			JSONObject discount = new JSONObject();
			SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
			
			discount.put("discount_no",discountVO.getDiscount_no());
			discount.put("discount_title", discountVO.getDiscount_title());
			discount.put("discount_startdate",df.format(discountVO.getDiscount_startdate()));
			discount.put("discount_enddate", df.format(discountVO.getDiscount_enddate()));
			discount.put("store_no",discountVO.getStore_no());
			
			
			Discount_mealService discount_mealSvc = new Discount_mealService();
			MealService mealSvc = new MealService();
			List<Discount_mealVO> list = discount_mealSvc.getDiscountMeals(discount_no);
			
			JSONArray meals = new JSONArray();

			for(Discount_mealVO aDiscount_meal : list){
				JSONObject obj = new JSONObject();
				MealVO mealVO = mealSvc.getOneMeal(aDiscount_meal.getMeal_no());
				try{
					obj.put("meal_no", aDiscount_meal.getMeal_no());
					obj.put("discount_meal_price", aDiscount_meal.getDiscount_meal_price());
					obj.put("meal_image",mealVO.getMeal_photo());
					obj.put("meal_name",mealVO.getMeal_name());
					obj.put("meal_price",mealVO.getMeal_price());
				}catch(Exception e){
					e.printStackTrace();
				}
				meals.add(obj);
			}
			
			JSONArray json = new JSONArray();
			json.add(discount);
			json.add(meals);
			
			res.setContentType("text/plain");
			res.setCharacterEncoding("UTF-8");
			PrintWriter out = res.getWriter();
			out.write(json.toString());
			out.flush();
			out.close();

		}		
				
		
		if("update".equals(action)){
			Map<String, String> store = (Map<String, String>) session.getAttribute("store");
			String store_no = store.get("store_no");			
			List<String> errorMsgs = new LinkedList<String>();
			req.setAttribute("errorMsgs", errorMsgs);
			String url = "listStoreDiscount.jsp";
			
			try{
				String discount_no = req.getParameter("discount_no");
				String discount_title = req.getParameter("discount_title");
				
				if(discount_title==null||discount_title.trim().length()==0){
					discount_title = "";
					errorMsgs.add("請輸入優惠標題");					
				}
				
				java.sql.Date discount_startdate = null;
				java.sql.Date discount_enddate = null;
	
				try{
					discount_startdate = java.sql.Date.valueOf(req.getParameter("discount_startdate").trim());
				} catch (IllegalArgumentException e){
					discount_startdate = new java.sql.Date(System.currentTimeMillis());
					errorMsgs.add("請指定開始日期");
				}
				
				try{
					discount_enddate = java.sql.Date.valueOf(req.getParameter("discount_enddate").trim());
				} catch (IllegalArgumentException e){
					discount_enddate = discount_startdate;
					errorMsgs.add("請指定結束日期");
				}			
				
				DiscountVO discountVO = new DiscountVO();		
				discountVO.setDiscount_no(discount_no);
				discountVO.setDiscount_title(discount_title);
				discountVO.setDiscount_startdate(discount_startdate);
				discountVO.setDiscount_enddate(discount_enddate);
				discountVO.setStore_no(store_no);
				
				String[] meal_no = req.getParameterValues("meal_no");
				List<Discount_mealVO> meal_list = null;
				Discount_mealVO discount_mealVO = null;
				List<String> check_list = null;
				
				if(meal_no==null){
					errorMsgs.add("請至少指定一項餐點");
				}
				else{								
					meal_list = new ArrayList<Discount_mealVO>();
					check_list = new ArrayList<String>();
					MealService mealSvc = new MealService();
					MealVO mealVO = new MealVO();
					
					for(int i=0;i<meal_no.length;i++){
						discount_mealVO = new Discount_mealVO();
						discount_mealVO.setMeal_no(meal_no[i]);
						discount_mealVO.setDiscount_no(discount_no);
						mealVO = mealSvc.getOneMeal(meal_no[i]);
						String meal_name = mealVO.getMeal_name();					
						
						Integer price = null;
						try{														
							price = new Integer(req.getParameter(meal_no[i]).trim());
							if(price<=0){
								price = 0;
								errorMsgs.add(meal_name+"的價格不可為0或負數");								
							}else if(mealVO.getMeal_price()<price){
								errorMsgs.add(meal_name+"的價格大於原價");	
							}
						}catch(NumberFormatException e){
							price = 0;
							errorMsgs.add(meal_name+"的優惠價格未填寫");
						}
						
						discount_mealVO.setDiscount_meal_price(price);
						meal_list.add(discount_mealVO);
						check_list.add(meal_no[i]);
					}						
				}
				
				if(!errorMsgs.isEmpty()){			
					req.setAttribute("check_list", check_list);
					req.setAttribute("meal_list", meal_list);
					req.setAttribute("discountVO", discountVO);
					RequestDispatcher failureView = req.getRequestDispatcher(url);
					failureView.forward(req, res);
					return;
				}							
				DiscountService discountSvc = new DiscountService();
				discountSvc.updateDiscountWithMeal(discountVO,meal_list);
				
				
				req.setAttribute("success", "更新: "+discount_title);
				RequestDispatcher successView = req.getRequestDispatcher(url);
				successView.forward(req, res);
				
			}catch(Exception e){			
				errorMsgs.add(e.getMessage());
				RequestDispatcher failureView = req.getRequestDispatcher(url);
				failureView.forward(req, res);
			}

		}
		
		
		if("delete_A".equals(action)||"delete_B".equals(action)){
			List<String> errorMsgs = new LinkedList<String>();
			req.setAttribute("errorMsgs", errorMsgs);
			String whichPage = req.getParameter("whichPage");
			String url = null;
			
			Map<String, String> store = (Map<String, String>) session.getAttribute("store");
			String store_no = store.get("store_no");			
			
			try{
				String discount_no = req.getParameter("discount_no");
				DiscountService discountSvc = new DiscountService();
				
				if("delete_B".equals(action)){	
					DiscountVO discountVO = discountSvc.getOneDiscount(discount_no);
					if(store_no.equals(discountVO.getStore_no())){
						discountSvc.deleteDiscount(discount_no);
					}
					url = "discount.do?action=get_store_discount&whichPage="+whichPage;
				}
				else{			
					discountSvc.deleteDiscount(discount_no);
					url = "listAllDiscount.jsp";
				}
				
				RequestDispatcher successView = req.getRequestDispatcher(url);
				successView.forward(req, res);
			
			}catch(Exception e){
				errorMsgs.add("刪除資料失敗:"+e.getMessage());
				RequestDispatcher failureView = req.getRequestDispatcher(url);
				failureView.forward(req, res);
			}
		}
		
		if("get_store_discount".equals(action)){
			List<String> errorMsgs = new LinkedList<String>();
			req.setAttribute("errorMsgs", errorMsgs);
			String whichPage = (req.getParameter("whichPage")!=null)?req.getParameter("whichPage"):"1";
			String url = "listStoreDiscount.jsp?whichPage="+whichPage;
			
			Map<String, String> store = (Map<String, String>) session.getAttribute("store");
			String store_no = store.get("store_no");
		
			try{				
				List<DiscountVO> list = new ArrayList<DiscountVO>();
				DiscountService discountSvc = new DiscountService();
				list = discountSvc.getStoreDiscount(store_no);
				
				req.setAttribute("list", list);

				RequestDispatcher successView = req.getRequestDispatcher(url);
				successView.forward(req, res);
				
			}catch(Exception e){
				errorMsgs.add("店家優惠查詢失敗"+e.getMessage());
				RequestDispatcher failureView = req.getRequestDispatcher(url);
				failureView.forward(req, res);
			}
		}
		
		
	}

}
