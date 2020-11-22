package com.tools;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import com.reservation.model.ReservationService;
import com.reservation.model.ReservationVO;

public class PicRead extends HttpServlet {
	private static final long serialVersionUID = 1L;

	protected void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
		OutputStream out = res.getOutputStream();

		try {
			String reservation_no = req.getParameter("reservation_no");
			ReservationService reservationSvc = new ReservationService();
			ReservationVO reservationVO = reservationSvc.getOneReservation(reservation_no);
			byte[] qrcode = reservationVO.getReservation_qrcode();
			res.setContentType("image/png");
			out.write(qrcode);

			out.flush();
			out.close();

		} catch (Exception e) {
			// 若沒有抓到圖片 就給他一張圖
			InputStream in = getServletContext().getResourceAsStream("/images/default.png");
			byte[] buf = new byte[in.available()];
			in.read(buf);
			res.setContentType("image/jpg");
			out.write(buf);
			in.close();
			out.close();
		}
	}
}
