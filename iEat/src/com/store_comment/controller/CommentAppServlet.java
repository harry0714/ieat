package com.store_comment.controller;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Timestamp;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.store_comment.model.StoreCommentAppService;
import com.store_comment.model.StoreCommentVO;

@WebServlet("/CommentAppServlet.do")
public class CommentAppServlet extends HttpServlet {

	private final static String CONTENT_TYPE = "text/html; charset=UTF-8";

	protected void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
		doGet(req, res);
	}

	protected void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
		req.setCharacterEncoding("UTF-8");
		Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create();
		BufferedReader br = req.getReader();
		StringBuilder jsonIn = new StringBuilder();
		String line = null;
		while ((line = br.readLine()) != null) {
			jsonIn.append(line);
		}
		JsonObject jsonObject = gson.fromJson(jsonIn.toString(), JsonObject.class);
		StoreCommentAppService storeCommentSvc = new StoreCommentAppService();
		String action = jsonObject.get("action").getAsString();
		System.out.println("action: " + action);

		if ("getStoreComment".equals(action)) {
			String store_no = jsonObject.get("store_no").getAsString();
			Map<String, StoreCommentVO> storeComments = storeCommentSvc.getStoreComment(store_no);
			writeText(res, gson.toJson(storeComments));
		} else if ("addStoreComment".equals(action)) {
			String store_no = jsonObject.get("store_no").getAsString();
			String mem_no = jsonObject.get("mem_no").getAsString();
			Double comment_level = jsonObject.get("rate").getAsDouble();
			String comment_conect = jsonObject.get("context").getAsString();
			Timestamp comment_time = new java.sql.Timestamp(System.currentTimeMillis());

			String result = "fail";
			try {
				storeCommentSvc.addStoreComment(store_no, mem_no, comment_conect, comment_time, comment_level);
				result = "success";
			} catch (Exception e) {
				e.printStackTrace();
			}
			writeText(res, result);
		}
	}

	private void writeText(HttpServletResponse res, String outText) throws IOException {
		res.setContentType(CONTENT_TYPE);
		PrintWriter out = res.getWriter();
		out.print(outText);
	}
}
