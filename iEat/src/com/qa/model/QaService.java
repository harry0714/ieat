package com.qa.model;

import java.util.List;

public class QaService {

	private QaDAO_interface dao;

	public QaService() {
		dao = new QaDAO();
	}

	public QaVO addQa(String qa_no, String qa_type_no,String q_context,String a_context) {

		QaVO qaVO = new QaVO();

		
		qaVO.setQa_type_no(qa_type_no);
		qaVO.setQ_context(q_context);;
		qaVO.setA_context(a_context);;
		
		dao.insert(qaVO);

		return qaVO;
	}

	public QaVO updateQa(String qa_no, String qa_type_no,String q_context,String a_context) {

		QaVO qaVO = new QaVO();

		qaVO.setQa_no(qa_no);
		qaVO.setQa_type_no(qa_type_no);
		qaVO.setQ_context(q_context);;
		qaVO.setA_context(a_context);;

		return qaVO;
	}

	public void deleteQa(String qa_no) {
		dao.delete(qa_no);
	}

	public QaVO getOneQa(String qa_no) {
		return dao.findByPrimaryKey(qa_no);
	}

	public List<QaVO> getAll() {
		return dao.getAll();
	}
	
	// 後端用到的方法 新增QA	
	public QaVO addQa(String qa_type_no,String q_context,String a_context) {
		QaVO qaVO = new QaVO();
		qaVO.setQa_type_no(qa_type_no);
		qaVO.setQ_context(q_context);;
		qaVO.setA_context(a_context);;
		dao.insert(qaVO);
		return qaVO;
	}
}
