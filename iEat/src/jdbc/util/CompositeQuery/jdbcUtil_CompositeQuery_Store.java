/*
 *  1. 萬用複合查詢-可由客戶端隨意增減任何想查詢的欄位
 *  2. 為了避免影響效能:
 *        所以動態產生萬用SQL的部份,本範例無意採用MetaData的方式,也只針對個別的Table自行視需要而個別製作之
 * */

package jdbc.util.CompositeQuery;

import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

public class jdbcUtil_CompositeQuery_Store {

	public static String get_aCondition_For_Oracle(String columnName, String value) {
		String aCondition = null; 
		
		if("store_no".equals(columnName) || "store_type_no".equals(columnName) 
			|| "store_id".equals(columnName) || "store_email".equals(columnName)
			|| "store_phone".equals(columnName)
			|| "store_status".equals(columnName)) {//用於不重複的資料查詢  主鍵為字串
			aCondition = columnName + "=" + "'"+ value + "'"; 
		}
		else if("store_name".equals(columnName) || "store_ads".equals(columnName)) { //用於varchar2
			aCondition = columnName + " like '%" + value +"%'";
		}
		return aCondition + " "; 
	}
	public static String getWhereCondition(Map<String, String[]> map) {
		Set<String> keys = map.keySet(); 
		StringBuffer whereCondition = new StringBuffer(); 
		int count = 0; 
		for(String key : keys) {
			String value=map.get(key)[0]; 
			if(value != null && value.trim().length() !=0 && !"action".equals(key)) {
				count++; 
				String aCondition = get_aCondition_For_Oracle(key, value.trim()); 
				
				if(count == 1) 
					whereCondition.append(" where " + aCondition); 
				else
					whereCondition.append(" and " + aCondition); 
				
				System.out.println("有送出查詢資料的欄位數 count = " +count); 
			}
		}
		return whereCondition.toString(); 
	}
	/***************** main function 測試用**********************/
//	public static void main(String args[]) {
//		// 配合 req.getParameterMap()方法 回傳 java.util.Map<java.lang.String,java.lang.String[]> 之測試 測試sql指令
//		Map<String,String[]> map = new TreeMap<String, String[]>(); 
//		map.put("store_no", new String[]{"S000000001"}); 
//		map.put("store_name", new String[]{"資策會牛肉麵"});
//		map.put("store_type_no", new String[]{"ST04"}); 
//		map.put("action", new String[]{"getXXX"}); // 注意Map裡面會含有action的key 
//		
//		String finalSQL = "select * from store " 
//				+ jdbcUtil_CompositeQuery_Store.getWhereCondition(map)
//				+ " order by store_no"; 
//		System.out.println("●●finalSQL = " + finalSQL);
//	}
}
