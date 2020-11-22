package com.reservation.model;

import java.sql.*;
import java.io.*;

class PhotoWrite_Reservation {

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
              
              InputStream fin = null; 
              try {
                con = DriverManager.getConnection(url, userid, passwd);
                
                for(int i =1; i<5; i++) {
                	File pic = new File("C:\\images\\images_reservation\\"+i+".png"); 
                	long flen = pic.length(); 
                	fin = new FileInputStream(pic);
                	pstmt = con.prepareStatement(
                			"UPDATE reservation SET reservation_qrcode=? where reservation_no='R00000000" + i + "'"); 
                	pstmt.setBinaryStream(1, fin,  (int)flen);
                    int rowsUpdated = pstmt.executeUpdate();
    				
	                if (1 == rowsUpdated)
	                    System.out.println(" row.");
	                else
	                    System.out.println("updated 'M00000000"+i+"' is fail");   
                }
                
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

