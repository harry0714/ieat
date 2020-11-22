package com.meal.model;

import java.sql.*;
import java.io.*;

class PhotoWrite {

        static {
            try {
                 Class.forName("oracle.jdbc.driver.OracleDriver").newInstance();
            } catch (Exception e) {
                 e.printStackTrace();
            }
        }

        public static void main(String argv[]) {
              Connection con = null;
              PreparedStatement pstmt = null;
              String url = "jdbc:oracle:thin:@localhost:1521:XE";
              String userid = "iEat";
              String passwd = "iEat";
              String picName = "back1.gif";
	        
              try {
                con = DriverManager.getConnection(url, userid, passwd);
                File pic = new File("picFrom", picName); //相對路徑- picFrom
                                                         //絕對路徑- 譬如:
                                                         //File pic = new File("x:\\aa\\bb\\picFrom", picName);
                long flen = pic.length();
                String fileName = pic.getName();   // 模擬遠端使用者上傳,所以這裡加上這一行程式碼
                int dotPos = fileName.indexOf('.');
                String fno = fileName.substring(0, dotPos);
                String format = fileName.substring(dotPos + 1);
                InputStream fin = new FileInputStream(pic);  

                System.out.println("\n\nUpdate the database... ");
                pstmt = con.prepareStatement(
                 "insert into EMP_PHOTO (empno, photo_format, picture)  values(?, ?, ?)");
                pstmt.setString(1, fno);
                pstmt.setString(2, format);
                pstmt.setBinaryStream(3, fin, (int)flen); //void pstmt.setBinaryStream(int parameterIndex, InputStream x, int length) throws SQLException
                int rowsUpdated = pstmt.executeUpdate();
			
                System.out.print("Changed " + rowsUpdated);

                if (1 == rowsUpdated)
                    System.out.println(" row.");
                else
                    System.out.println(" rows.");

                fin.close();
                pstmt.close();

              } catch (Exception e) {
                    e.printStackTrace();
              } finally {
                    try {
                      con.close();
                    } catch (SQLException e) {
                    }
             }
      }
}