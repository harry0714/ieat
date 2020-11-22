package com.checkout.controller;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Vector;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;


import com.meal.model.*;
import com.store.model.*;


public class ShoppingServlet extends HttpServlet {
	
	protected void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
		doPost(req, res);
	}

	protected void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
		req.setCharacterEncoding("UTF-8");
		
		HttpSession session = req.getSession();
		
		String action = req.getParameter("action");
		Map<String,Map<String,Integer>> shoppinglist = (Map<String,Map<String,Integer>>) session.getAttribute("shoppinglist");
//		Map<String,Map<String,Map<Integer,Integer>>> shoppinglist = (Map<String,Map<String,Map<Integer,Integer>>>) session.getAttribute("shoppinglist");	
			
			if("changeQty".equals(action)) {
				
			String meal_no = req.getParameter("meal_no");
			String store_no = req.getParameter("store_no");
			Integer oldAmount = shoppinglist.get(store_no).get(meal_no);
			
			Integer amount = null;
			
				try{
					amount = new Integer(req.getParameter("qty"));		
					if(amount>0){
						shoppinglist.get(store_no).put(meal_no, amount);
						
					}else{
						Map<String,Integer> meallist = shoppinglist.get(store_no);
						meallist.remove(meal_no);							
						if(meallist.size()<=0){							
							meallist=null;
							shoppinglist.remove(store_no);							
							if(shoppinglist.size()<=0){
								shoppinglist=null;							
								session.removeAttribute("shoppinglist");
								String url = "/checkout/Cart.jsp";
								RequestDispatcher rd = req.getRequestDispatcher(url);
								rd.forward(req, res);
								return;
							}						
						}
					}
					
				}catch (NumberFormatException e){
					amount = oldAmount;
					shoppinglist.get(store_no).put(meal_no, amount);
				}
			
					
			session.setAttribute("shoppinglist", shoppinglist);
			String url = "/checkout/Cart.jsp";
			RequestDispatcher rd = req.getRequestDispatcher(url);
			rd.forward(req, res);
			
			}
			
				
			if("DELETE".equals(action)) {
				
			String meal_no = req.getParameter("meal_no");
			
			MealService mealSvc = new MealService();
			MealVO mealVO = new MealVO();
			mealVO = mealSvc.getOneMeal(meal_no);
			String store_no = mealVO.getStore_no();
			
			Map<String,Integer> meallist = shoppinglist.get(store_no);		
			meallist.remove(meal_no);
			
				if(meallist.size()<=0){
					meallist=null;
					shoppinglist.remove(store_no);
					if(shoppinglist.size()<=0){
						shoppinglist=null;
						session.removeAttribute("shoppinglist");
					}
				}

			String url = "/checkout/Cart.jsp";
			RequestDispatcher rd = req.getRequestDispatcher(url);
			rd.forward(req, res);
			}	
			
			if("DELETE_ORD".equals(action)) {
				
				String store_no = req.getParameter("store_no");
				
					shoppinglist.remove(store_no);
					if(shoppinglist.size()<=0){
						shoppinglist=null;
						session.removeAttribute("shoppinglist");
					}
				
				String url = "/checkout/Cart.jsp";
				RequestDispatcher rd = req.getRequestDispatcher(url);
				rd.forward(req, res);
			}	
		
		
//			if("DELETE".equals(action)) {
//				
//			String meal_no = req.getParameter("meal_no");
//			
//			MealService mealSvc = new MealService();
//			MealVO mealVO = new MealVO();
//			mealVO = mealSvc.getOneMeal(meal_no);
//			String store_no = mealVO.getStore_no();
//			
//			Map<String,Map<Integer,Integer>> meallist = shoppinglist.get(store_no);		
//			meallist.remove(meal_no);
//			
//				if(meallist.size()<=0){
//					meallist=null;
//					shoppinglist.remove(store_no);
//					if(shoppinglist.size()<=0){
//						shoppinglist=null;
//						session.removeAttribute("shoppinglist");
//					}
//				}
//
//			String url = "/checkout/Cart.jsp";
//			RequestDispatcher rd = req.getRequestDispatcher(url);
//			rd.forward(req, res);
//			}			
		
		
			if("ADD".equals(action)) {			
				String meal_no = req.getParameter("meal_no");
				
				MealService mealSvc = new MealService();
				MealVO mealVO = new MealVO();
				mealVO = mealSvc.getOneMeal(meal_no);
				
				String store_no = mealVO.getStore_no();

			

				if(shoppinglist == null) {
					shoppinglist = new LinkedHashMap<String,Map<String,Integer>>();
					Map<String,Integer> meallist = new LinkedHashMap<String,Integer>();
					meallist.put(meal_no, 1);
					shoppinglist.put(store_no, meallist);
					
				} else {					
					Set<String> storeKey = shoppinglist.keySet();
					if(storeKey.contains(store_no)){
						Map<String,Integer> meallist = shoppinglist.get(store_no);
						Set<String> mealKey = meallist.keySet();
						if(mealKey.contains(meal_no)){
							meallist.put(meal_no, meallist.get(meal_no)+1);
						}
						else{
							meallist.put(meal_no,1);
						}
					}else{
						Map<String,Integer> meallist = new LinkedHashMap<String,Integer>();
						meallist.put(meal_no, 1);
						shoppinglist.put(store_no, meallist);
					}

				}
				
//				if("ADD".equals(action)) {
//					
//					String meal_no = req.getParameter("meal_no");
//					Integer meal_price = Integer.valueOf(req.getParameter("meal_price"));
//					MealService mealSvc = new MealService();
//					MealVO mealVO = new MealVO();
//					mealVO = mealSvc.getOneMeal(meal_no);
//					
//					String store_no = mealVO.getStore_no();
//					
//					
//					
//					if(shoppinglist == null) {
//						System.out.println("如果購物車沒東西");
//						shoppinglist = new LinkedHashMap<String, Map<String,Map<Integer,Integer>>>();//如果購物車為null就new一個購物車(裝店家編號&餐點編號&餐點數量&餐點價錢)
//						Map<String,Map<Integer,Integer>> meallist = new LinkedHashMap<String,Map<Integer,Integer>>();
//						Map<Integer,Integer> mealNumPrice = new LinkedHashMap<Integer,Integer>();
//						mealNumPrice.put(1, meal_price);//放入新的數量及價格
//						meallist.put(meal_no, mealNumPrice);//放入新的餐點及數量和價格
//						shoppinglist.put(store_no, meallist);//放入新的店家及餐點及數量和價格
//						
//						System.out.println("shoppinglist="+shoppinglist);
//						
//					} else {	
//						Set<String> storeKey = shoppinglist.keySet();//取得店家的所有key
//						if(storeKey.contains(store_no)){//如果店家KEY跟新增的店家編號一樣的話
//							System.out.println("如果店家一樣");
//							Map<String,Map<Integer,Integer>> meallist = shoppinglist.get(store_no);//取得這個店家所訂的餐點內容
//							Set<String> mealKey = meallist.keySet();//取得餐點的value的所有key
//							if(mealKey.contains(meal_no)){//如果餐點的key跟新增的餐點編號一樣的話
//								System.out.println("如果店家一樣，餐點又一樣");
//								Map<Integer,Integer> mealNumPrice = meallist.get(meal_no);//取得購物車裡該店家該餐點的數量及價格
//								Iterator iterator = mealNumPrice.entrySet().iterator();
//								while (iterator.hasNext()) {
//								   Map.Entry mapEntry = (Map.Entry) iterator.next();
//								   Integer key= (Integer) mapEntry.getKey();
//								   mealNumPrice.put(key+1, mealNumPrice.get(key)+meal_price);//將該重複的餐點數量及價格加值
//								   mealNumPrice.remove(key);//因為新的的key是新值所以不會覆蓋舊值，所以透過remove移除餐點數量及價格的舊值
//								  
//								}
//								meallist.put(meal_no, mealNumPrice);//將新增玩得值放進meallist
//								
//								System.out.println("shoppinglist="+shoppinglist);
//							}
//							else{//店家一樣，餐點不一樣
//								Map<Integer,Integer> mealNumPrice = new LinkedHashMap<Integer,Integer>();//因為餐點不一樣所以數量及價格必須new
//								System.out.println("如果店家一樣，餐點不一樣");
//								mealNumPrice.put(1, meal_price);//放入新的數量及價格
//								meallist.put(meal_no,mealNumPrice);//放入新的餐點及數量和價格
//								
//								System.out.println("shoppinglist="+shoppinglist);
//							}
//						}else{//店家不一樣，餐點不一樣
//							System.out.println("如果店家不一樣，餐點也不一樣");
//							Map<String,Map<Integer,Integer>> meallist = new LinkedHashMap<String,Map<Integer,Integer>>();//因為店家不一樣所以餐點及數量和價格必須new
//							Map<Integer,Integer> mealNumPrice = new LinkedHashMap<Integer,Integer>();
//							mealNumPrice.put(1, meal_price);//放入新的數量及價格
//							meallist.put(meal_no, mealNumPrice);//放入新的餐點及數量和價格
//							shoppinglist.put(store_no, meallist);//放入新的店家及餐點及數量和價格
//							
//							System.out.println("shoppinglist="+shoppinglist);
//						}
//						System.out.println("-------------------------");
//					}

			session.setAttribute("shoppinglist", shoppinglist);
//			String url = "/checkout/Orders.jsp?store_no="+store_no;
//			RequestDispatcher rd = req.getRequestDispatcher(url);
//			rd.forward(req, res);

			}
				
		}//enddoPost
	
}


