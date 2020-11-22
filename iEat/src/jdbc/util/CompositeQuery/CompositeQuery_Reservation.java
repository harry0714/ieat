package jdbc.util.CompositeQuery;

import java.util.Map;
import java.util.Set;

public class CompositeQuery_Reservation {
	public static String get_aCondition_For_Oracle(String columnName, String value) {

		String aCondition = null;

		if ("reservation_no".equals(columnName) || "mem_no".equals(columnName) || "store_no".equals(columnName) || "reservation_hour".equals(columnName)|| "reservation_guests".equals(columnName) || "reservation_qrcode_status".equals(columnName)||"reservation_report_status".equals(columnName)) // 用於其他
			aCondition = columnName + "=" + "'" + value + "'";
		else if ("reservation_report".equals(columnName)) // 用於varchar
			aCondition = columnName + " like '%" + value + "%'";
		else if ("reservation_date".equals(columnName))                          // 用於Oracle的date
			aCondition = "to_char(" + columnName + ",'yyyy-mm-dd')='" + value + "'";
		else if ("reservation_time".equals(columnName))
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

				System.out.println("有送出查詢資料的欄位數count = " + count);
			}
		}
		
		return whereCondition.toString();
	}
}
