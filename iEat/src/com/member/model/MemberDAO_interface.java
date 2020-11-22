package com.member.model;

import java.sql.Date;
import java.util.List;
import java.util.Map;

import com.article.model.ArticleVO;
import com.ord.model.OrdVO;


public interface MemberDAO_interface {
	public void insert(MemberVO memberVO);
	public void update(MemberVO memberVO);
	public void updateForMember(MemberVO memberVO);
	public void delete(String mem_no);
	public MemberVO findByPrimaryKey(String mem_no);
	public List<MemberVO> getAll();
	public List<MemberVO> getAll(Map<String,String[]> map);
	public MemberVO fingByAcctPwd(String mem_acct,String mem_pwd);
	public List<ArticleVO> getArtByMember(String mem_no);
	public List<OrdVO> getMoreByMember(String mem_no);
	public void updateDate(Date mem_validate, String mem_no);
	 
}
