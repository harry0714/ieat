package com.qa_type.model;

import java.util.List;

public class Qa_typeService {

	private Qa_typeDAO_interface dao;

	public Qa_typeService() {
		dao = new Qa_typeDAO();
	}

	public Qa_typeVO addQa_type(String qa_type_no,String qa_type_name) {

		Qa_typeVO qa_typeVO = new Qa_typeVO();


		qa_typeVO.setQa_type_no(qa_type_no);
		qa_typeVO.setQa_type_name(qa_type_name);
		
		
		dao.insert(qa_typeVO);

		return qa_typeVO;
	}

	public Qa_typeVO updateQa_type(String qa_type_no,String qa_type_name) {

		Qa_typeVO qa_typeVO = new Qa_typeVO();

		
		qa_typeVO.setQa_type_no(qa_type_no);
		qa_typeVO.setQa_type_name(qa_type_name);

		return qa_typeVO;
	}

	public void deleteQa_type(String qa_type_no) {
		dao.delete(qa_type_no);
	}

	public Qa_typeVO getOneQa(String qa_type_no) {
		return dao.findByPrimaryKey(qa_type_no);
	}

	public List<Qa_typeVO> getAll() {
		return dao.getAll();
	}
}
