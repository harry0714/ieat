package com.comment_report.model;

import java.util.List;

public interface CommentReportDAO_interface {
	public void insert(CommentReportVO commentreportVO);
	public void update(CommentReportVO commentreportVO);
	public void delete(String comment_report_no);
	public CommentReportVO findByPrimaryKey(String comment_report_no);
	public List<CommentReportVO> getAll();
	public List<CommentReportVO> getMoreByCommentReportStatus(String comment_report_status);
}
