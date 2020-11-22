package com.store_photo.model;

import java.sql.*;
import java.io.*;

class PhotoWrite_StorePhoto {

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
              String[] pics = {"S000000001_1.png", "S000000001_2.jpg", "S000000001_3.jpg", "S000000002_1.jpg", "S000000002_2.jpg","S000000003_1.gif", 
            		  						"S000000003_2.jpg", "S000000004_1.jpg", "S000000005_1.jpg", "S000000006_1.jpg", "S000000007_1.jpg", "S000000007_2.jpg", 
            		  						"S000000008_1.jpg", "S000000009_1.jpg", "S000000010_1.jpg", "S000000011_1.jpg", "S000000012_1.jpg"};  
              
              InputStream fin = null; 
              try {
                con = DriverManager.getConnection(url, userid, passwd);
                
                for(int i =0; i<pics.length; i++) {
                	File pic = new File("C:\\images_storePhoto\\" + pics[i]); 
                	long flen = pic.length(); 
                	String fileName = pic.getName(); 
                	int underline = fileName.indexOf('_'); 
                	String store_no = fileName.substring(0, underline); 
                	fin = new FileInputStream(pic);
                	
                	System.out.println("\n\nUpdate the database....." );
                	pstmt = con.prepareStatement(
                			"INSERT INTO store_photo (photo_no, store_no, photo_name, photo) VALUES ('P'||LPAD(PHOTO_SEQ.NEXTVAL,9,'0'), ?, ?, ?)");
                	pstmt.setString(1, store_no); 
                	pstmt.setString(2, fileName);
                	pstmt.setBinaryStream(3, fin,  (int)flen);
                	pstmt.executeUpdate(); 
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
