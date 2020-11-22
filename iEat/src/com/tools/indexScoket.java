package com.tools;

import java.io.Console;
import java.io.IOException;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.websocket.CloseReason;
import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;

import com.article.model.ArticleService;
import com.article.model.ArticleVO;
import com.article_report.model.Article_ReportService;
import com.article_report.model.Article_reportVO;
import com.article_response_report.model.Article_rs_reService;
import com.article_response_report.model.Article_rs_reVO;
import com.comment_report.model.CommentReportService;
import com.comment_report.model.CommentReportVO;
import com.member.model.MemberService;
import com.member.model.MemberVO;
import com.ord.model.OrdService;
import com.ord.model.OrdVO;
import com.reservation.model.ReservationService;
import com.reservation.model.ReservationVO;
import com.store.model.StoreService;
import com.store.model.StoreVO;
import com.store_report.model.Store_reportService;
import com.store_report.model.Store_reportVO;

import net.sf.json.JSONObject;

@ServerEndpoint("/indexScoket")
public class indexScoket {
	private static final Set<Session> allSessions = Collections.synchronizedSet(new HashSet<Session>());
	@OnOpen
	public void onOpen(Session userSession) throws IOException {
		allSessions.add(userSession);
		System.out.println(userSession.getId() + ": 已連線");
//		userSession.getBasicRemote().sendText("WebSocket 連線成功");
	}

	
	@OnMessage
	public void onMessage(Session userSession, String message) {
		System.out.println("Message received: " + message);
		
		JSONObject obj = this.getUntreated();
		
		System.out.println();
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
	
		List<OrdVO>	ord=ordSvc.getAll();
		
		obj.put("ord",ord.size());
		
		ReservationService reservationSvc = new ReservationService();
		
		List<ReservationVO> allreservation=reservationSvc.getAll();
		
		obj.put("allreservation", allreservation.size());
	
		
		
		StoreService storeSvc = new StoreService();
	
		List<StoreVO> allstoreList=storeSvc.getAll();
	
		obj.put("allstore", allstoreList.size());
		
	
		
		

		ArticleService artSvc = new ArticleService();
		List<ArticleVO> artList = artSvc.getAll();
		obj.put("art",artList.size());
		
		
		
		MemberService memsvc =new MemberService();
		List<MemberVO> memList=memsvc.getAll();
		obj.put("totalMember",memList.size());
		
		
		
		return obj;
	}
}
