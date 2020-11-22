package com.article.controller;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.article.model.ArticleAppService;
import com.article.model.ArticleVO;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;

import com.article.model.*;

@WebServlet("/ArticleAppServlet")
public class ArticleAppServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private final static String CONTENT_TYPE = "text/html; charset=UTF-8";

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		doPost(request, response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// TODO Auto-generated method stub

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

		ArticleAppService artSvc = new ArticleAppService();

		if (action.equals("articleGetAll")) {

			Map<String,ArticleVO> list = artSvc.getAllWithName();

			writeText(response, gson.toJson(list));

		}
		if (action.equals("articleGetImage")) {

			OutputStream os = response.getOutputStream();

			String art_no = jsonObject.get("art_no").getAsString();
			int imageSize = jsonObject.get("imageSize").getAsInt();

			System.out.println("***************************");
			System.out.println("art_no: " + art_no);
			System.out.println("imageSize(照片的大小，供工具類別使用)" + imageSize);
			byte[] image = null;
			image = artSvc.getArticelImageByArt_no(art_no);

			System.out.println("image(從dao拿回來的byte[]:) " + image);

			if (image != null) {
				image = ImageUtil.shrink(image, imageSize);
				response.setContentType("image/jpeg");
				response.setContentLength(image.length);
				System.out.println("寫回byte[]資料");

				os.write(image);

				System.out.println();
			}
		}
	}

	private void writeText(HttpServletResponse response, String outText) throws IOException {
		response.setContentType(CONTENT_TYPE);
		PrintWriter out = response.getWriter();
		out.print(outText);

	}
}
