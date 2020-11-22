package com.comment_report.model;

import java.util.List;

public class CommentReportService {
	private CommentReportDAO_interface dao;
	
	public CommentReportService() {
		dao = new CommentReportDAO();
	}
	
	public CommentReportVO addCommentReport(String comment_no, String mem_no,
			String comment_report_context, String comment_report_status) {
		
		CommentReportVO commentreportVO = new CommentReportVO();
		
		commentreportVO.setComment_no(comment_no);
		commentreportVO.setMem_no(mem_no);
		commentreportVO.setComment_report_context(comment_report_context);
		commentreportVO.setComment_report_status(comment_report_status);
		dao.insert(commentreportVO);
		
		return commentreportVO;
	}
	
	
	public CommentReportVO updateCommentReportVO(String comment_report_no, String comment_no, String mem_no,
			String comment_report_context, java.sql.Timestamp comment_report_date, String comment_report_status) {
		
		CommentReportVO commentreportVO = new CommentReportVO();
		
		commentreportVO.setComment_report_no(comment_report_no);
		commentreportVO.setComment_no(comment_no);
		commentreportVO.setMem_no(mem_no);
		commentreportVO.setComment_report_context(comment_report_context);
		commentreportVO.setComment_report_date(comment_report_date);
		commentreportVO.setComment_report_status(comment_report_status);
		dao.update(commentreportVO);
		
		return commentreportVO;
	}
	
	public void deleteCommentReport(String comment_report_no) {
		dao.delete(comment_report_no);
	}

	public CommentReportVO getOneCommentReport(String comment_report_no) {
		return dao.findByPrimaryKey(comment_report_no);
	}
	
	public List<CommentReportVO> getAll() {
		return dao.getAll();
	}
	
	public List<CommentReportVO> getMoreByCommentReportStatus(String comment_report_status) {
		return dao.getMoreByCommentReportStatus(comment_report_status);
	}
}
