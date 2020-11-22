package com.advertisement.controller;

import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.io.*;
import java.text.SimpleDateFormat;
import java.util.*;

import javax.imageio.ImageIO;
import javax.servlet.*;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.http.*;

import net.sf.json.JSONObject;
import net.sf.json.JSONArray;

import com.advertisement.model.*;
import com.store.model.StoreService;
import com.store.model.StoreVO;

@MultipartConfig(fileSizeThreshold = 1024 * 1024, maxFileSize = 5 * 1024 * 1024, maxRequestSize = 5 * 5 * 1024 * 1024)

public class AdvertisementServlet extends HttpServlet {

	protected void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
		doPost(req, res);
	}

	protected void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
		req.setCharacterEncoding("UTF-8");
		String action = req.getParameter("action");	
		String url ="/back-end/advertisement/listAllAd.jsp";
		//getList(req);//先搜尋存資料
	
		if("insert".equals(action)){
			List<String> errorMsgs = new LinkedList<String>();
			req.setAttribute("errorMsgs", errorMsgs);	
			getList(req);//先搜尋存資料
			try{
				String ad_imagetitle = req.getParameter("ad_imagetitle");
				if(ad_imagetitle==null||ad_imagetitle.trim().length()==0){
					ad_imagetitle = "";
					errorMsgs.add("請輸入廣告標題");
				}
				java.sql.Date ad_startdate = null;
				java.sql.Date ad_enddate = null;
				try{
					ad_startdate = java.sql.Date.valueOf(req.getParameter("ad_startdate").trim());
				} catch (IllegalArgumentException e){
					ad_startdate = new java.sql.Date(System.currentTimeMillis());
					errorMsgs.add("請指定開始日期");
				}
				try{
					ad_enddate = java.sql.Date.valueOf(req.getParameter("ad_enddate").trim());
				} catch (IllegalArgumentException e){
					ad_enddate = ad_startdate;
					errorMsgs.add("請指定結束日期");
				}
				Part part = req.getPart("ad_image");
				if(part.getSize()==0){
					errorMsgs.add("請選擇廣告圖片");						
				}else if(!("image/jpeg".equals(part.getContentType()))&&!("image/png".equals(part.getContentType()))){
					errorMsgs.add("請選擇jpg或png圖檔");
				}
				
				String store_no = req.getParameter("store_no");
				AdvertisementVO advertisementVO = new AdvertisementVO();
				advertisementVO.setAd_imagetitle(ad_imagetitle);
				advertisementVO.setAd_startdate(ad_startdate);
				advertisementVO.setAd_enddate(ad_enddate);
				advertisementVO.setStore_no(store_no);
				if(!errorMsgs.isEmpty()){					
					req.setAttribute("advertisementVO", advertisementVO);
					RequestDispatcher failureView = req.getRequestDispatcher(url);
					failureView.forward(req, res);
					return;
				}				
				InputStream in = part.getInputStream();
				byte[] ad_image = new byte[in.available()];
				in.read(ad_image);
				in.close();
				AdvertisementService adSvc = new AdvertisementService();
				adSvc.addAdvertisement(ad_image, ad_imagetitle, ad_startdate, ad_enddate, store_no);				
				getList(req);			
				req.setAttribute("success", "新增: "+ad_imagetitle);
				RequestDispatcher successView = req.getRequestDispatcher(url);
				successView.forward(req,res);
			}catch(Exception e){
				errorMsgs.add(e.getMessage());
				RequestDispatcher failureView = req.getRequestDispatcher(url);
				failureView.forward(req, res);
			}
		}

		if("get_update_form".equals(action)){
			List<String> errorMsgs = new LinkedList<String>();
			req.setAttribute("errorMsgs", errorMsgs);		
			
			try{			
				String ad_no = req.getParameter("ad_no");
				AdvertisementService adSvc = new AdvertisementService();
				AdvertisementVO advertisementVO = adSvc.getOneAdvertisement(ad_no);
				StoreService storeSvc = new StoreService();
				StoreVO storeVO = storeSvc.getOneStore(advertisementVO.getStore_no());
				JSONObject ad = new JSONObject();
				SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
				ad.put("ad_no", advertisementVO.getAd_no());			
				ad.put("ad_imagetitle", advertisementVO.getAd_imagetitle());			
				ad.put("ad_startdate", df.format(advertisementVO.getAd_startdate()));							
				ad.put("ad_enddate", df.format(advertisementVO.getAd_enddate()));
				ad.put("store_no", advertisementVO.getStore_no());
				ad.put("store_name", storeVO.getStore_name());

				res.setContentType("text/plain");
				res.setCharacterEncoding("UTF-8");
				PrintWriter out = res.getWriter();
				out.write(ad.toString());
				out.flush();
				out.close();
			}catch(Exception e){
				errorMsgs.add("無法取得表單:"+e.getMessage());
			}
		}		
				
		
		if("update".equals(action)){
			List<String> errorMsgs = new LinkedList<String>();
			req.setAttribute("errorMsgs", errorMsgs);
			getList(req);//先搜尋存資料		
			try{
				String ad_no = req.getParameter("ad_no").trim();
				String ad_imagetitle = req.getParameter("ad_imagetitle").trim();
				if(ad_imagetitle==null||ad_imagetitle.trim().length()==0){
					ad_imagetitle = "廣告標題";
					errorMsgs.add("請輸入廣告標題");
				}
				java.sql.Date ad_startdate = null;
				java.sql.Date ad_enddate = null;
				try{
					ad_startdate = java.sql.Date.valueOf(req.getParameter("ad_startdate").trim());
				} catch (IllegalArgumentException e){
					ad_startdate = new java.sql.Date(System.currentTimeMillis());
					errorMsgs.add("請指定開始日期");
				}
				try{
					ad_enddate = java.sql.Date.valueOf(req.getParameter("ad_enddate").trim());
				} catch (IllegalArgumentException e){
					ad_enddate = new java.sql.Date(ad_startdate.getTime());
					errorMsgs.add("請指定結束日期");
				}
				Part part = req.getPart("ad_image");
				byte[] ad_image = null;
				if(part.getSize()==0){
					AdvertisementService adSvc = new AdvertisementService();
					ad_image = adSvc.getAdImage(ad_no);				
				}else if((part.getSize()!=0)&&(!("image/jpeg".equals(part.getContentType()))&&!("image/png".equals(part.getContentType())))){
					errorMsgs.add("請選擇jpg或png圖檔");
				}else{
					InputStream in = part.getInputStream();
					ad_image = new byte[in.available()];
					in.read(ad_image);
					in.close();
				}
				String store_no = req.getParameter("store_no");			
				AdvertisementVO advertisementVO = new AdvertisementVO();
				advertisementVO.setAd_no(ad_no);
				advertisementVO.setAd_image(ad_image);
				advertisementVO.setAd_imagetitle(ad_imagetitle);
				advertisementVO.setAd_startdate(ad_startdate);
				advertisementVO.setAd_enddate(ad_enddate);
				advertisementVO.setStore_no(store_no);
				if(!errorMsgs.isEmpty()){
					req.setAttribute("advertisementVO", advertisementVO);
					RequestDispatcher failureView = req.getRequestDispatcher(url);			
					failureView.forward(req, res);
					return;
				}
				AdvertisementService adSvc = new AdvertisementService();
				adSvc.updateAdvertisement(ad_no, ad_image, ad_imagetitle, ad_startdate, ad_enddate, store_no);
				getList(req);//更新完再搜尋
				req.setAttribute("success", "更新: "+ad_imagetitle);
				RequestDispatcher successView = req.getRequestDispatcher(url);
				successView.forward(req, res);
			}catch(Exception e){
				errorMsgs.add("修改資料失敗:"+e.getMessage());
				RequestDispatcher failureView = req.getRequestDispatcher(url);
				failureView.forward(req, res);				
			}
		}
				
		
		if("delete".equals(action)){
			List<String> errorMsgs = new LinkedList<String>();
			req.setAttribute("errorMsgs", errorMsgs);
			getList(req);//先搜尋存資料
			try{
				String ad_no = req.getParameter("ad_no");
				AdvertisementService adSvc = new AdvertisementService();
				adSvc.deleteAdvertisement(ad_no);	
				getList(req);//刪除後再重新搜尋
				RequestDispatcher successView = req.getRequestDispatcher(url);
				successView.forward(req,res);
			}catch(Exception e){
				errorMsgs.add("刪除資料失敗:"+e.getMessage());
				RequestDispatcher failureView = req.getRequestDispatcher(url);
				failureView.forward(req,res);
			}
		}
	
		if("filter_ads".equals(action)){
			getList(req);//先搜尋存資料
			List<String> errorMsgs = new LinkedList<String>();
			req.setAttribute("errorMsgs", errorMsgs);
			try{		
				RequestDispatcher successView = req.getRequestDispatcher(url);
				successView.forward(req,res);
			}catch(Exception e){
				errorMsgs.add("資料無法過濾:"+e.getMessage());
				RequestDispatcher failureView = req.getRequestDispatcher(url);
				failureView.forward(req,res);
			}
		}
	}//end doPost

	protected void getList(HttpServletRequest req){
		List<AdvertisementVO> list = new ArrayList<AdvertisementVO>();
		AdvertisementService adSvc = new AdvertisementService();
		String ad_filter = "on";
		if(req.getParameter("ad_filter")!=null){
			try{
				int status = Integer.parseInt(req.getParameter("ad_filter"));
				list = adSvc.getSome(status);
				ad_filter = req.getParameter("ad_filter");
			}catch(Exception e){
				list = adSvc.getAll();
			}
		}else{
			list = adSvc.getAll();
		}			
		req.setAttribute("ad_filter", ad_filter);
		req.setAttribute("list",list);				
	}
}
