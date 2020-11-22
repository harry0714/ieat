package jdbc.util.CompositeQuery;

import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

public class ResponseCompositeQuery_Article {

	public static String get_aCondition_For_Oracle(String columnName, String value) {

		String aCondition = null;

		if ("art_rs_no".equals(columnName)||"art_no".equals(columnName)|| "mem_no".equals(columnName)) // �Ω��L
			aCondition = columnName + "=" + "'" + value + "'";
		else if ("art_rs_context".equals(columnName)) // �Ω�varchar
			aCondition = columnName + " like '%" + value + "%'";
		else if ("art_rs_date".equals(columnName))
			aCondition = "to_char(" + columnName + ",'yyyy-MM-dd-hh-mm-ss')='" +value +"'";

		return aCondition + " ";
	}
	
	public static String get_WhereCondition(Map<String, String[]> map) {
		Set<String> keys = map.keySet();
		StringBuffer whereCondition = new StringBuffer();
		int count = 0;
		for (String key : keys) {
			String value = map.get(key)[0];
			if (value != null && value.trim().length() != 0	&& !"action".equals(key)) {
				count++;
				String aCondition = get_aCondition_For_Oracle(key, value.trim());

				if (count == 1)
					whereCondition.append(" where " + aCondition);
				else
					whereCondition.append(" and " + aCondition);

				System.out.println("���e�X�d�߸�ƪ�����count = " + count);
			}
		}
		
		return whereCondition.toString();
	}
	
	public static void main(String argv[]) {

		// �t�X req.getParameterMap()��k �^�� java.util.Map<java.lang.String,java.lang.String[]> ������
		Map<String, String[]> map = new TreeMap<String, String[]>();
		map.put("art_rs_no", new String[] { "AS0000001" });
		map.put("art_no", new String[] { "A000000001" });
		map.put("mem_no", new String[] { "M000000001" });
		map.put("art_rs_context", new String[] { "���T����" });
		map.put("art_rs_date", new String[] { "2016-04-23-00-00-00" });
		map.put("action", new String[] { "getXXX" }); // �`�NMap�̭��|�t��action��key

		String finalSQL = "select * from Article_Response "
				          + CompositeQuery_Article_rs.get_WhereCondition(map)
				          + " order by art_rs_no";
		System.out.println("����finalSQL = " + finalSQL);
	
	}
}
