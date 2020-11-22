package com.tools;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.meal.model.MealService;
import com.meal.model.MealVO;
import com.ord.model.OrdService;
import com.ord_meal.model.Ord_mealVO;
import com.store.model.StoreService;
import com.store.model.StoreVO;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;


@WebServlet("/back_end/index.html")
public class IndexBackEnd extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doPost(request,response);
	}

	protected void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
		String action = req.getParameter("action");
		if ("getAllStore".equals(action)) {
			StoreService storeSvc = new StoreService();
			List<StoreVO> storeList = storeSvc.getAll();

			JSONArray jsonArray = new JSONArray();
			
			for (StoreVO storeVO : storeList) {
				JSONObject obj = new JSONObject();
				try {
					
					obj.put("store_no", storeVO.getStore_no());
					obj.put("store_name", storeVO.getStore_name());
					obj.put("store_phone", storeVO.getStore_phone());
					obj.put("store_owner", storeVO.getStore_owner());
					obj.put("store_intro", storeVO.getStore_intro());
					obj.put("store_ads", storeVO.getStore_ads());
					obj.put("store_latlng", storeVO.getStore_latlng());


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
	}

}
