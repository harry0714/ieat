package com.adm.model;

import java.sql.Date;
import java.util.List;

import com.adm.model.AdmDAO;

public class AdmService {

	private AdmDAO_interface dao;

	public AdmService() {
		dao = new AdmDAO();
	}

	public AdmVO addAdm(String adm_name, String adm_user, String adm_psd, Date adm_bd, String adm_sex, String adm_email,
			String adm_phone, String adm_level, String adm_addr, byte[] adm_photo) {

		AdmVO admVO = new AdmVO();

		
		admVO.setAdm_name(adm_name);
		admVO.setAdm_user(adm_user);
		admVO.setAdm_psd(adm_psd);
		admVO.setAdm_sex(adm_sex);
		admVO.setAdm_bd(adm_bd);
		admVO.setAdm_email(adm_email);
		admVO.setAdm_phone(adm_phone);
		admVO.setAdm_level(adm_level);
		admVO.setAdm_addr(adm_addr);
		admVO.setAdm_photo(adm_photo);
		
		
		dao.insert(admVO);

		return admVO;
	}

	public AdmVO updateAdm( String adm_no,String adm_name,String adm_user, String adm_psd ,String adm_sex,java.sql.Date adm_bd,String adm_email,String adm_phone,String adm_level,String adm_addr,byte[] adm_photo) {

		AdmVO admVO = new AdmVO();
		admVO.setAdm_no(adm_no);
		admVO.setAdm_name(adm_name);
		admVO.setAdm_user(adm_user);
		admVO.setAdm_psd(adm_psd);
		admVO.setAdm_sex(adm_sex);
		admVO.setAdm_bd(adm_bd);
		admVO.setAdm_email(adm_email);
		admVO.setAdm_phone(adm_phone);
		admVO.setAdm_level(adm_level);
		admVO.setAdm_addr(adm_addr);
		admVO.setAdm_photo(adm_photo);
		
		dao.update(admVO);

		return admVO;
	}

	public void deleteAdm(String adm_no) {
		dao.delete(adm_no);
	}

	public AdmVO getOneAdm(String adm_no) {
		return dao.findByPrimaryKey(adm_no);
	}

	public List<AdmVO> getAll() {
		return dao.getAll();
	}

	public AdmVO getUserAdm(String adm_user) {
		return dao.findByUser(adm_user);
	}
	public byte[] getAdmImage(String adm_no){
		return dao.getAdmImage(adm_no);
	}
}
