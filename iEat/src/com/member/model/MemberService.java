package com.member.model;

import java.sql.Date;
import java.util.List;
import java.util.Map;

import com.article.model.ArticleVO;
import com.ord.model.OrdVO;

public class MemberService {
	private MemberDAO_interface dao;
	
	public MemberService(){
		dao = new MemberDAO();
	}
	
	public MemberVO addMember(String mem_name,String mem_acct,String mem_pwd,String mem_sex,Date mem_bd,String mem_email,String mem_phone,String mem_zip,String mem_addr,byte[] mem_photo){
		MemberVO memberVO = new MemberVO();
		//memberVO.setMem_no(mem_no);
		memberVO.setMem_name(mem_name);
		memberVO.setMem_acct(mem_acct);
		memberVO.setMem_pwd(mem_pwd);
		memberVO.setMem_sex(mem_sex);
		memberVO.setMem_bd(mem_bd);
		memberVO.setMem_email(mem_email);
		memberVO.setMem_phone(mem_phone);
		memberVO.setMem_zip(mem_zip);
		memberVO.setMem_addr(mem_addr);
		memberVO.setMem_photo(mem_photo);
		//memberVO.setMem_validate(mem_validate);
		dao.insert(memberVO);
		return memberVO;
	}
	
	public MemberVO updateMember(String mem_no,String mem_name,String mem_acct,String mem_pwd,String mem_sex,Date mem_bd,String mem_email,String mem_phone,String mem_zip,String mem_addr,byte[] mem_photo,Date mem_validate){
		MemberVO memberVO = new MemberVO();
		memberVO.setMem_no(mem_no);
		memberVO.setMem_name(mem_name);
		memberVO.setMem_acct(mem_acct);
		memberVO.setMem_pwd(mem_pwd);
		memberVO.setMem_sex(mem_sex);
		memberVO.setMem_bd(mem_bd);
		memberVO.setMem_email(mem_email);
		memberVO.setMem_phone(mem_phone);
		memberVO.setMem_zip(mem_zip);
		memberVO.setMem_addr(mem_addr);
		memberVO.setMem_photo(mem_photo);
		memberVO.setMem_validate(mem_validate);
		dao.update(memberVO);
		return memberVO;
	}
	
	public MemberVO updateMemberForMember(String mem_no,String mem_name,String mem_acct,String mem_pwd,String mem_sex,Date mem_bd,String mem_email,String mem_phone,String mem_zip,String mem_addr,byte[] mem_photo,Date mem_validate){
		MemberVO memberVO = new MemberVO();
		memberVO.setMem_no(mem_no);
		memberVO.setMem_name(mem_name);
		memberVO.setMem_acct(mem_acct);
		memberVO.setMem_pwd(mem_pwd);
		memberVO.setMem_sex(mem_sex);
		memberVO.setMem_bd(mem_bd);
		memberVO.setMem_email(mem_email);
		memberVO.setMem_phone(mem_phone);
		memberVO.setMem_zip(mem_zip);
		memberVO.setMem_addr(mem_addr);
		memberVO.setMem_photo(mem_photo);
		memberVO.setMem_validate(mem_validate);
		dao.updateForMember(memberVO);
		return memberVO;
	}
	
	public void deleteMember(String mem_no){
		dao.delete(mem_no);
	}
	
	public MemberVO getOneMember(String mem_no){
		return dao.findByPrimaryKey(mem_no);
	}
	
	public List<MemberVO> getAll(){
		return dao.getAll();
	}
	
	public List<MemberVO> getAll(Map<String,String[]> map){
		return dao.getAll(map);
	}
	
	public MemberVO getOneMemberByAcctPwd(String mem_acct,String mem_pwd){
		return dao.fingByAcctPwd(mem_acct, mem_pwd);
	}
	
	public List<ArticleVO> getArtByMemno(String mem_no){
		return dao.getArtByMember(mem_no);
	}
	public List<OrdVO> getMoreByMember(String mem_no){
		return dao.getMoreByMember(mem_no);
	}
	// 後端用到的方法
	public MemberVO updateDate(String mem_no,Date mem_validate){
			dao.updateDate(mem_validate,mem_no);
			return  dao.findByPrimaryKey(mem_no);
	}
}
