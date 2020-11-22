package com.tools;

import java.io.IOException;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import javax.servlet.http.HttpSession;
import javax.websocket.CloseReason;
import javax.websocket.EndpointConfig;
import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;

import com.article_report.model.Article_ReportService;
import com.article_report.model.Article_reportVO;
import com.article_response_report.model.Article_rs_reService;
import com.article_response_report.model.Article_rs_reVO;
import com.comment_report.model.CommentReportService;
import com.comment_report.model.CommentReportVO;
import com.ord.model.OrdService;
import com.ord.model.OrdVO;
import com.reservation.model.ReservationService;
import com.reservation.model.ReservationVO;
import com.store.model.StoreService;
import com.store.model.StoreVO;
import com.store_report.model.Store_reportService;
import com.store_report.model.Store_reportVO;

import net.sf.json.JSONObject;

@ServerEndpoint(value="/ReportScoket", configurator=ServletAwareConfig.class)
public class ReportScoket {
	private static final Set<Session> allSessions = Collections.synchronizedSet(new HashSet<Session>());
	private static final Map<Session,HttpSession> user = Collections.synchronizedMap(new TreeMap<Session,HttpSession>());
	@OnOpen
	public void onOpen(Session userSession,EndpointConfig config) throws IOException {
		allSessions.add(userSession);
		HttpSession httpSession = (HttpSession) config.getUserProperties().get("httpSession");
        if(httpSession.getAttribute("user")!=null){
        	Map<String,String> user = (Map<String,String>)httpSession.getAttribute("user");
        	System.out.println(user.get("no"));
        }
		JSONObject obj = this.getUntreated();
		
		for (Session session : allSessions) {
			if (session.isOpen())
				session.getAsyncRemote().sendText(obj.toString());
		}

	}

	
	@OnMessage
	public void onMessage(Session userSession, String message) {
		System.out.println("Message received: " + message);
		
		JSONObject obj = this.getUntreated();
		
		for (Session session : allSessions) {
			if (session.isOpen())
				session.getAsyncRemote().sendText(obj.toString());
		}
	}
	
	@OnError
	public void onError(Session userSession, Throwable e){
//		e.printStackTrace();
	}
	
	@OnClose
	public void onClose(Session userSession, CloseReason reason) {
		allSessions.remove(userSession);
		System.out.println(userSession.getId() + ": Disconnected: " + Integer.toString(reason.getCloseCode().getCode()));
	}
	
	private JSONObject getUntreated(){
		JSONObject obj = new JSONObject();
		
		OrdService  ordSvc = new OrdService();
		List<OrdVO> ordList = ordSvc.getgetMoreByOrderReportStatus("0");
		obj.put("orUntreated", ordList.size());
		
		ReservationService reservationSvc = new ReservationService();
		List<ReservationVO> reservationList = reservationSvc.getMoreByReservationReportStatus("0");
		obj.put("rrUntreated", reservationList.size());
		
		Store_reportService srSvc = new Store_reportService();
		List<Store_reportVO> srList = srSvc.getMoreByStoreReportStatus("0");
		obj.put("srUntreated", srList.size());
		
		CommentReportService crSvc = new CommentReportService();
		List<CommentReportVO> crList = crSvc.getMoreByCommentReportStatus("0");
		obj.put("crUntreated", crList.size());
		
		Article_ReportService arSvc = new Article_ReportService();
		List<Article_reportVO> arList = arSvc.getMoreByArticleReportStatus("0");
		obj.put("arUntreated",arList.size());
		
		Article_rs_reService arrSvc = new Article_rs_reService();
		List<Article_rs_reVO> arrList = arrSvc.getMoreByArticelResponseReportStatus("0");
		obj.put("arrUntreated", arrList.size());
		obj.put("totalUntreated", ordList.size()+reservationList.size()+srList.size()+crList.size()+arList.size()+arrList.size());
		
		//取得尚未審核的店家數量
		StoreService storeSvc = new StoreService();
		List<StoreVO> storeList = storeSvc.getMoreByStoreStatus("2");
		obj.put("storeUntreated", storeList.size());
		obj.put("storetotalUntreated",storeList.size());
		obj.put("totalUntreated", ordList.size()+reservationList.size()+srList.size()+crList.size()+arList.size()+arrList.size());
		return obj;
	}
}
