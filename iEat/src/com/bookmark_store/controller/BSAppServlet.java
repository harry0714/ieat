package com.bookmark_store.controller;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.bookmark_store.model.BSAppService;
import com.bookmark_store.model.Bookmark_StoreVO;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.store.model.StoreAppService;
import com.store.model.StoreVO;

@WebServlet("/Bookmark_StoreAppServlet")
public class BSAppServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private final static String CONTENT_TYPE = "text/html; charset=UTF-8";

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doPost(request, response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		request.setCharacterEncoding("UTF-8"); // 先
		Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd").create(); // 把傳過來的資料轉換回來

		BufferedReader br = request.getReader();

		StringBuilder jsonIn = new StringBuilder(); // 把資料用BufferedReader的readLine()去取出來
		String line = null;
		while ((line = br.readLine()) != null) {
			jsonIn.append(line); // 把資料用StringBuilder串起來
		}

		// 從JsonObject裡拿出Json字串
		JsonObject jsonObject = gson.fromJson(jsonIn.toString(), JsonObject.class); // 這邊toString()是為了把他轉成String類別

		String action = jsonObject.get("action").getAsString(); // 取JsonObject裡面value的值，再把他轉成json字串

		System.out.println("action =" + action);

		BSAppService bookmark_storeSvc = new BSAppService();

		if (action.equals("bookmark_storeGetAllByMem_no")) {
			String mem_no = jsonObject.get("mem_no").getAsString();
			List<String> list = bookmark_storeSvc.getStoreNoByMemNo(mem_no);
			List<StoreVO> storelist = null;
			if (list != null) {
				StoreAppService storeSvc = new StoreAppService();
				storelist = new ArrayList<StoreVO>();
				for (String store_no : list) {
					StoreVO store = storeSvc.getOneAvailableStore(store_no);
					if (store != null) {
						storelist.add(store);
					}
				}

			}

			writeText(response, gson.toJson(storelist));
		}else if (action.equals("bsDeleteByMem_and_store_no")) {
			
			String mem_no = jsonObject.get("mem_no").getAsString();
			String store_no = jsonObject.get("store_no").getAsString();
			String result = "fail";
			try {
				bookmark_storeSvc.deleteBookmark_Store(mem_no, store_no);
				result = "success";
			} catch (Exception e) {
				e.printStackTrace();
			}
			System.out.println("成功刪除一筆最愛餐廳");

			writeText(response, result);
			
		}else if (action.equals("bsAddByMem_and_store_no")) {
			String mem_no = jsonObject.get("mem_no").getAsString();
			String store_no = jsonObject.get("store_no").getAsString();
			String result = "fail";
			try {
				bookmark_storeSvc.addBookmark_Store(mem_no, store_no);
				result = "success";
			} catch (Exception e) {
				e.printStackTrace();
			}
			System.out.println("成功刪除一筆最愛餐廳");

			writeText(response, result);
		}

	}

	private void writeText(HttpServletResponse response, String outText) throws IOException {
		response.setContentType(CONTENT_TYPE);
		PrintWriter out = response.getWriter();
		out.print(outText);
	}
}
