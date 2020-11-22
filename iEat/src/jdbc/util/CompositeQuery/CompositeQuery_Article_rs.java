package jdbc.util.CompositeQuery;
import java.util.*;







public class CompositeQuery_Article_rs {

	
	
	public static String get_aCondition_For_Oracle(String columnName, String value) {

		String aCondition = null;

		if ("art_no".equals(columnName)  || "mem_no".equals(columnName)) // �Ω��L
			aCondition = columnName + "=" + "'" + value + "'";
		else if ("art_name".equals(columnName) || "art_context".equals(columnName)) // �Ω�varchar
			aCondition = columnName + " like '%" + value + "%'";
		else if ("art_date".equals(columnName))
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
		map.put("art_no", new String[] { "A000000001" });
		map.put("art_name", new String[] { "���y�n�Y" });
		map.put("art_date", new String[] { "2016-05-09:10-06-11" });
		map.put("art_context", new String[] { "�Ӳ��F" });
		map.put("mem_no", new String[] { "M000000001" });
		map.put("action", new String[] { "getXXX" }); // �`�NMap�̭��|�t��action��key

		String finalSQL = "select * from Article "
				          + CompositeQuery_Article_rs.get_WhereCondition(map)
				          + " order by art_no";
		System.out.println("����finalSQL = " + finalSQL);
	
	}
}