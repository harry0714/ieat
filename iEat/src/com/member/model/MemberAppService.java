package com.member.model;

import java.sql.Date;
import java.util.List;
import java.util.Map;

public class MemberAppService {
	private MemberAppDAO_interface dao;
	
	public MemberAppService(){
		dao = new MemberAppDAO();
	}
	
	public String addMember(String mem_name,String mem_acct,String mem_pwd,String mem_sex,Date mem_bd,String mem_email,String mem_phone,String mem_zip,String mem_addr,byte[] mem_photo){
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
		
		return dao.insert(memberVO);
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
	

	
	public MemberVO getOneMember(String mem_no){
		return dao.findByPrimaryKey(mem_no);
	}
	


	public String fingByAcctPwd(String mem_acct,String mem_pwd){
		return dao.fingByAcctPwd(mem_acct, mem_pwd);
	}
	
	public byte[] getMemImage(String mem_no){
		return dao.getMemImage(mem_no);
	}
	
	public MemberVO findMemberByNo(String mem_no){
		return dao.findMemberByNo(mem_no);
	}
	
	public int checkMemberByAcct(String mem_acct){
		return dao.findMemberByAcct(mem_acct);
	}

	public void updateMemberKey(String mem_no,String mem_key){
		dao.updateMemberKey(mem_no,mem_key);
	}
	
	public String getMemberKey(String mem_no){
		return dao.getMemberKey(mem_no);
	}
	
	public void updatePwd(String mem_no,String new_pwd){
		dao.updatePwd(mem_no,new_pwd);
	}
}
