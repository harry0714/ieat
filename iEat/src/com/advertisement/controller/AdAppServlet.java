package com.advertisement.controller;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.Map;
import java.util.Set;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.advertisement.model.AdAppService;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.meal.model.MealAppService;
import com.meal.model.MealVO;
import com.store.controller.ImageUtil;
import com.store.model.StoreVO;

@WebServlet("/AdAppServlet.do")
public class AdAppServlet extends HttpServlet {
	private final static String CONTENT_TYPE = "text/html; charset=UTF-8";
	
	protected void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
		doPost(req, res);
	}

	protected void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
		req.setCharacterEncoding("UTF-8");
		Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create();
		BufferedReader br = req.getReader();
		StringBuilder jsonIn = new StringBuilder();
		String line = null;
		while((line = br.readLine())!=null){
			jsonIn.append(line);
		}
		JsonObject jsonObject = gson.fromJson(jsonIn.toString(), JsonObject.class);
		AdAppService adSvc = new AdAppService();
		String action = jsonObject.get("action").getAsString();
		System.out.println("action:"+action);
		
		if("getAllAd".equals(action)){
			Map<String,String> adStoreMap = adSvc.getCurrentAd();
			System.out.println(adStoreMap.size());
			
			writeText(res,gson.toJson(adStoreMap));
			
		}else if("getAdImage".equals(action)){
			OutputStream os = res.getOutputStream();
			String ad_no = jsonObject.get("ad_no").getAsString();
			int imageSize = jsonObject.get("imageSize").getAsInt();
			byte[] image = adSvc.getAdImage(ad_no);
			if (image != null) {
				image = ImageUtil.shrink(image, imageSize);
				res.setContentType("image/jpeg");
				res.setContentLength(image.length);
				os.write(image);
				os.close();				
			}
		}
	}
	private void writeText(HttpServletResponse res, String outText) throws IOException {
		res.setContentType(CONTENT_TYPE);
		PrintWriter out = res.getWriter();
		out.print(outText);
	}
}
