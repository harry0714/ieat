package com.store.controller;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.meal.model.MealVO;
import com.store.model.StoreAppService;
import com.store.model.StoreVO;

//import net.sf.json.JSONArray;
//import net.sf.json.JSONObject;

@WebServlet("/StoreAppServlet.do")
public class StoreAppServlet extends HttpServlet {

	private final static String CONTENT_TYPE = "text/html; charset=UTF-8";

	public void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {

		req.setCharacterEncoding("UTF-8");
		Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create();
		BufferedReader br = req.getReader();
		StringBuilder jsonIn = new StringBuilder();
		String line = null;
		while ((line = br.readLine()) != null) {
			jsonIn.append(line);
		}
		JsonObject jsonObject = gson.fromJson(jsonIn.toString(), JsonObject.class);
		StoreAppService storeSvc = new StoreAppService();
		String action = jsonObject.get("action").getAsString();
		System.out.println("action: " + action);

		if ("getAllStore".equals(action)) {
			String keyword = jsonObject.get("keyword").getAsString();
			List<StoreVO> stores = null;
			if (keyword.trim().length() > 0) {
				stores = storeSvc.getAllKeyword(keyword);
			} else {
				stores = storeSvc.getOpenStore();
			}
			writeText(res, gson.toJson(stores));
			// JSONArray stores = new JSONArray();
			//
			// for(StoreVO storeVO : stores){
			// JSONObject obj = new JSONObject();
			//
			// try{
			// obj.put("store_id", storeVO.getStore_id());
			// obj.put("store_name", storeVO.getStore_name());
			// obj.put("store_phone", storeVO.getStore_phone());
			// obj.put("store_owner", storeVO.getStore_owner());
			// obj.put("store_ads", storeVO.getStore_ads());
			// obj.put("store_type_no", storeVO.getStore_type_no());
			// obj.put("store_booking", storeVO.getStore_booking());
			// obj.put("store_open", storeVO.getStore_open());
			// obj.put("store_book_amt", storeVO.getStore_book_amt());
			// obj.put("store_email", storeVO.getStore_email());
			// obj.put("store_star",storeVO.getStore_star());
			//
			// }catch(Exception e){
			// e.printStackTrace();
			// }
			// stores.add(obj);
			// }
			// writeText(res, stores.toString());

		} else if ("getStoreImage".equals(action)) {
			OutputStream os = res.getOutputStream();
			String store_no = jsonObject.get("store_no").getAsString();
			int imageSize = jsonObject.get("imageSize").getAsInt();
			byte[] image = storeSvc.getStoreImg(store_no);
			if (image != null) {
				image = ImageUtil.shrink(image, imageSize);
				res.setContentType("image/jpeg");
				res.setContentLength(image.length);
				os.write(image);
				os.close();
			}
		} else if ("getOneStore".equals(action)) {
			System.out.println(jsonObject.get("store_no").getAsString());
			StoreVO store = storeSvc.getOneAvailableStore(jsonObject.get("store_no").getAsString());
			writeText(res, gson.toJson(store));
		} else if ("checkStore".equals(action)) {
			String store_no = null;
			String store_id = jsonObject.get("store_id").getAsString();
			String store_psw = jsonObject.get("store_psw").getAsString();

			try {
				StoreVO store = storeSvc.getOneStoreIdAndPsw(store_id,store_psw);
				if (store != null) {
					store_no = store.getStore_no();
					String store_key = jsonObject.get("store_key").getAsString();
					storeSvc.updateStoreKey(store_no, store_key);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			writeText(res, store_no);
		} else if ("uploadKey".equals(action)) {
			String result = "fail";
			String store_no = jsonObject.get("store_no").getAsString();
			String store_key = jsonObject.get("store_key").getAsString();
			try {
				storeSvc.updateStoreKey(store_no, store_key);
				result = "success";
			} catch (Exception e) {
				e.printStackTrace();
			}
			writeText(res, result);
		} else if("getTopStore".equals(action)){
			List<StoreVO> stores = storeSvc.getTopStores();
			writeText(res, gson.toJson(stores));
		} else if ("updatePassword".equals(action)) {
			String result = "fail";
			String store_id = jsonObject.get("store_id").getAsString();
			String store_psw = jsonObject.get("store_psw").getAsString();
			String new_pwd = jsonObject.get("new_pwd").getAsString();

			StoreVO store = storeSvc.getOneStoreIdAndPsw(store_id,store_psw);
			if (store != null && !(store.getStore_no().length() < 10)) {
				try {
					storeSvc.updatePwd(store.getStore_no(), new_pwd);
					result = "success";
				} catch (Exception e) {
					e.printStackTrace();
				}
			}

			writeText(res, result);
		}
	}

	public void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
		doPost(req, res);
	}

	private void writeText(HttpServletResponse res, String outText) throws IOException {
		res.setContentType(CONTENT_TYPE);
		PrintWriter out = res.getWriter();
		out.print(outText);
	}

}
