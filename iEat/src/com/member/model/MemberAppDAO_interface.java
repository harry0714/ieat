package com.member.model;

import java.util.List;
import java.util.Map;

public interface MemberAppDAO_interface {
	public void update(MemberVO memberVO);
	public void updateForMember(MemberVO memberVO);

	public MemberVO findByPrimaryKey(String mem_no);

	public String fingByAcctPwd(String mem_acct,String mem_pwd);
	public byte[] getMemImage(String mem_no);
	public MemberVO findMemberByNo(String mem_no);
	public int findMemberByAcct(String mem_acct);
	public String insert(MemberVO memberVO);
	public void updateMemberKey(String mem_no,String mem_key);
	public String getMemberKey(String mem_no);
	public void updatePwd(String mem_no,String new_pwd);
}
