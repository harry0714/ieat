package com.ord.controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Timestamp;
import java.util.*;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.meal.model.MealService;
import com.meal.model.MealVO;
import com.ord.model.OrdService;
import com.ord.model.OrdVO;
import com.ord_meal.model.Ord_mealVO;
import com.store.model.StoreVO;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;


public class OrdServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doPost(request, response);
	}

	protected void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
		String action = req.getParameter("action");
		
		/****************** 會員專區 取得會員 訂餐紀錄列表 ******************************/ 
		if ("memberord".equals(action)) {
			Map<String, String> errorMessage = new TreeMap<>();
			req.setAttribute("errorMessage", errorMessage);
			try {
				OrdService ordSvc = new OrdService();
				HttpSession session = req.getSession();
				Map<String, String> user = (Map<String, String>) session.getAttribute("user");
				
				List<OrdVO> ordList = ordSvc.getMoreByMember(user.get("no"));
				req.setAttribute("ordList", ordList);
				
				RequestDispatcher failureView = req.getRequestDispatcher("/front-end/ord/memberorder.jsp");
				failureView.forward(req, res);
			} catch (Exception e) {
				errorMessage.put("elseError", "有錯誤:" + e.getMessage());
				RequestDispatcher failureView = req
						.getRequestDispatcher("/front-end/ord/memberorder.jsp");
				failureView.forward(req, res);
			}
		}
		
		/****************** 店家專區 取得店家所有訂餐資訊 ******************************/ 
		if ("storeord".equals(action)) {
			Map<String, String> errorMessage = new TreeMap<>();
			req.setAttribute("errorMessage", errorMessage);
			try {
				OrdService ordSvc = new OrdService();
				HttpSession session = req.getSession();
				Map<String, String> store = (Map<String, String>) session.getAttribute("store");
				
				List<OrdVO> ordList = ordSvc.getMoreByStore(store.get("store_no"));
				req.setAttribute("ordList", ordList);
				
				RequestDispatcher failureView = req.getRequestDispatcher("/front-end/ord/storeorder.jsp");
				failureView.forward(req, res);
			} catch (Exception e) {
				errorMessage.put("elseError", "有錯誤:" + e.getMessage());
				RequestDispatcher failureView = req
						.getRequestDispatcher("/front-end/ord/storeorder.jsp");
				failureView.forward(req, res);
			}
		}

		if ("getOrdMeal".equals(action)) {
			String ord_no = req.getParameter("ord_no1");
			OrdService ordSvc = new OrdService();
			List<Ord_mealVO> ord_mealList = ordSvc.getOrdMealsByOrdno(ord_no);

			JSONArray jsonArray = new JSONArray();
			MealService mealSvc = new MealService();

			for (Ord_mealVO ord_mealVO : ord_mealList) {
				JSONObject obj = new JSONObject();
				try {
					MealVO mealVO = mealSvc.getOneMeal(ord_mealVO.getMeal_no());
					obj.put("ord_meal_no", ord_mealVO.getOrd_meal_no());
					obj.put("ord_no", ord_mealVO.getOrd_no());
					obj.put("meal_no", ord_mealVO.getMeal_no());
					obj.put("meal_name", mealVO.getMeal_name());
					obj.put("ord_meal_qty", ord_mealVO.getOrd_meal_qty());
					obj.put("ord_meal_price", ord_mealVO.getOrd_meal_price());

				} catch (Exception e) {
					System.out.println(e.getMessage());
				}
				jsonArray.add(obj);
			}
			res.setContentType("text/plain");
			res.setCharacterEncoding("UTF-8");
			PrintWriter out = res.getWriter();
			out.write(jsonArray.toString());
			out.flush();
			out.close();
		}
		if ("cancel".equals(action)) {
			Map<String, String> errorMessage = new TreeMap<>();
			req.setAttribute("errorMessage", errorMessage);
			String requestURL = req.getParameter("requestURL");
			//System.out.println(requestURL);
			try {
				HttpSession session = req.getSession();
				Map<String, String> user = (Map<String, String>) session.getAttribute("user");

				String ord_no = req.getParameter("ord_no");
				OrdService ordSvc = new OrdService();
				OrdVO ordVO = ordSvc.getOneMeal(ord_no);
				String ord_qrcode_status = ordVO.getOrd_qrcode_status();
				//System.out.println(ord_qrcode_status);
				if (!ord_qrcode_status.equals("4")) {
					errorMessage.put("invalid", "訂單已被確認，無法被取消");
					List<OrdVO> ordList = ordSvc.getMoreByMember(user.get("no"));
					req.setAttribute("ordList", ordList);
					RequestDispatcher failureView = req.getRequestDispatcher(requestURL);
					failureView.forward(req, res);
					return;
				}

				ordVO.setOrd_qrcode_status("3");
				ordSvc.updateOrd(ordVO.getMem_no(), ordVO.getStore_no(), ordVO.getOrd_date(), ordVO.getOrd_pickup(),
						ordVO.getOrd_qrcode_status(), ordVO.getOrd_paid(), ordVO.getOrd_report(),
						ordVO.getOrd_report_status(), ordVO.getOrd_no());

				List<OrdVO> ordList = ordSvc.getMoreByMember(user.get("no"));
				req.setAttribute("ordList", ordList);
				RequestDispatcher failureView = req.getRequestDispatcher(requestURL);
				failureView.forward(req, res);
				
			} catch (Exception e) {
				errorMessage.put("elseError", "有錯誤:" + e.getMessage());
				//System.out.println(e.getMessage());
				RequestDispatcher failureView = req.getRequestDispatcher("/front-end/ord/memberorder.jsp");
				failureView.forward(req, res);
			}
		}
		
		
		if ("insert".equals(action)) {
			HttpSession session = req.getSession();
			Map<String, Map<String, Integer>> shoppinglist = (Map<String, Map<String, Integer>>) session.getAttribute("shoppinglist");
			List<String> errorMsgs = new LinkedList<String>();
			req.setAttribute("errorMsgs", errorMsgs);
			try {
				/***************************
				 * 1.接收請求參數 - 輸入格式的錯誤處理(訂單)
				 ****************************************/
				String mem_no = req.getParameter("mem_no");// 會員編號
				String ord_qrcode_status = "4";// qrcode狀態
				String pickup = req.getParameter("pickup");
				String time = req.getParameter("time");
				Integer ord_paid = Integer.parseInt(req.getParameter("ord_paid"));// 訂單付款狀態
				String od = req.getParameter("ord_date");
				Timestamp ord_date = Timestamp.valueOf(od);// 訂單成立時間
				String ord_report = null;
				String ord_report_status = null;
				// 時段處理
				Timestamp ord_pickup = null;

				if (!pickup.equals("on")) {
					ord_pickup = Timestamp.valueOf(pickup);// 取餐時間
				} else if (time.isEmpty()) {
					errorMsgs.add("請選擇取餐時段");
				} else {
					ord_pickup = Timestamp.valueOf(time);// 取餐時間
				}

				// 卡號錯誤處理
				if (ord_paid == 0) {
					String A = req.getParameter("A").trim();
					String B = req.getParameter("B").trim();
					String C = req.getParameter("C").trim();
					String D = req.getParameter("D").trim();
					if(A.isEmpty()&&B.isEmpty()&&C.isEmpty()&&D.isEmpty()) {
						errorMsgs.add("請輸入卡號");
					}else if((A+B+C+D).trim().length() < 16) {
						errorMsgs.add("輸入卡號錯誤");
					}
				}

				/*************************** 錯誤轉交處理 ****************************************/
				if (!errorMsgs.isEmpty()) {
					RequestDispatcher failureView = req.getRequestDispatcher("/checkout/CheckOut.jsp");
					failureView.forward(req, res);
					return;
				}

				/***************************
				 * 開始新增資料(訂單)
				 ****************************************/
				Map<String, OrdVO> ordlist = new LinkedHashMap<String, OrdVO>();
				Map<String, List<Ord_mealVO>> itemlist = new LinkedHashMap<String, List<Ord_mealVO>>();

				for (String shoppinglistkey : shoppinglist.keySet()) {// 取得訂單的店家編號
				
					String store_no = shoppinglistkey; // 店家編號
					
					OrdVO ordVO = new OrdVO();
					ordVO.setMem_no(mem_no);				
					ordVO.setStore_no(store_no);
					ordVO.setOrd_date(ord_date);
					ordVO.setOrd_pickup(ord_pickup);
					ordVO.setOrd_qrcode_status(ord_qrcode_status);
					ordVO.setOrd_paid(ord_paid);
					ordVO.setOrd_report(ord_report);
					ordVO.setOrd_report_status(ord_report_status);
					ordlist.put(store_no, ordVO);
						
					// ordSvc.addOrd(mem_no, store_no, ord_date, ord_pickup,
					// ord_qrcode_status, ord_paid, ord_report,
					// ord_report_status);

					/***************************
					 * 1.接收請求參數 - 輸入格式的錯誤處理(訂單明細)
					 ****************************************/
					MealService mealSvc = new MealService();
					MealVO mealVO = new MealVO();
					Map<String, Integer> meallist = shoppinglist.get(store_no);// 取得該店家訂單的餐點
					String meal_no = null;
					int ord_meal_qty = 0;
					int ord_meal_price = 0;
					List<Ord_mealVO> ord_meallist = new ArrayList<Ord_mealVO>();
					for (String meallistkey : meallist.keySet()) {
						meal_no = meallistkey;// 取得餐點編號
						int value = meallist.get(meallistkey);
						ord_meal_qty = value;// 取得該餐點的訂購數量
						mealVO = mealSvc.getOneMeal(meal_no);
						ord_meal_price = (mealVO.getMeal_discount()>0&&mealVO.getMeal_discount()<mealVO.getMeal_price())?mealVO.getMeal_discount():mealVO.getMeal_price();
						//ord_meal_price = value * meal_price;// 取得該餐點訂購數量乘上單價的價錢

						/***************************
						 * 開始新增資料(訂單明細)
						 ****************************************/

						Ord_mealVO ord_mealVO = new Ord_mealVO();
						ord_mealVO.setMeal_no(meal_no);
						ord_mealVO.setOrd_meal_qty(ord_meal_qty);
						ord_mealVO.setOrd_meal_price(ord_meal_price);
						ord_meallist.add(ord_mealVO);					
					}
					itemlist.put(store_no, ord_meallist);	
				}
				
				
				
				
				OrdService ordSvc = new OrdService();
				ordSvc.insertOrd(ordlist, itemlist);
				session.removeAttribute("shoppinglist");// 訂單送出後刪除購物車
				
				/***************************
				 *新增完成,準備轉交
				 ****************************************/
				String url = "/checkout/OrdOK.jsp";
				RequestDispatcher successView = req.getRequestDispatcher(url); 
				successView.forward(req, res);
				
			} catch (Exception e) {
				errorMsgs.add(e.getMessage());
				RequestDispatcher failureView = req.getRequestDispatcher("checkout/CheckOut.jsp");
				failureView.forward(req, res);
			}
		}
		
		if ("cancelOrd".equals(action)) {
			Map<String, String> errorMessage = new TreeMap<>();
			req.setAttribute("errorMessage", errorMessage);

			//System.out.println(requestURL);
			try {
				
				String ord_no = req.getParameter("ord_no");
				OrdService ordSvc = new OrdService();
				OrdVO ordVO = ordSvc.getOneMeal(ord_no);
				String ord_qrcode_status = ordVO.getOrd_qrcode_status();
				System.out.println(ord_qrcode_status);

				ordVO.setOrd_qrcode_status("3");
				ordSvc.updateOrd(ordVO.getMem_no(), ordVO.getStore_no(), ordVO.getOrd_date(), ordVO.getOrd_pickup(),
						ordVO.getOrd_qrcode_status(), ordVO.getOrd_paid(), ordVO.getOrd_report(),
						ordVO.getOrd_report_status(), ordVO.getOrd_no());

				RequestDispatcher successView = req.getRequestDispatcher("/ord/ord.do?action=storeord");
				successView.forward(req, res);
				
			} catch (Exception e) {
				errorMessage.put("elseError", "��隤�:" + e.getMessage());
				RequestDispatcher failureView = req.getRequestDispatcher("/ord/ord.do?action=storeord");
				failureView.forward(req, res);
			}
		}
	
		if ("enterOrd".equals(action)) {
			Map<String, String> errorMessage = new TreeMap<>();
			req.setAttribute("errorMessage", errorMessage);
			//System.out.println(requestURL);
			try {

				String ord_no = req.getParameter("ord_no");
				OrdService ordSvc = new OrdService();
				OrdVO ordVO = ordSvc.getOneMeal(ord_no);
				String ord_qrcode_status = ordVO.getOrd_qrcode_status();
				System.out.println(ord_qrcode_status);

				ordVO.setOrd_qrcode_status("1");
				ordSvc.updateOrd(ordVO.getMem_no(), ordVO.getStore_no(), ordVO.getOrd_date(), ordVO.getOrd_pickup(),
						ordVO.getOrd_qrcode_status(), ordVO.getOrd_paid(), ordVO.getOrd_report(),
						ordVO.getOrd_report_status(), ordVO.getOrd_no());

				RequestDispatcher successView = req.getRequestDispatcher("/ord/ord.do?action=storeord");
				successView.forward(req, res);
				
			} catch (Exception e) {
				errorMessage.put("elseError", "��隤�:" + e.getMessage());
				//System.out.println(e.getMessage());
				RequestDispatcher failureView = req.getRequestDispatcher("/ord/ord.do?action=storeord");
				failureView.forward(req, res);
			}
		}

	}

}
