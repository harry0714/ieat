import java.sql.*;
import java.io.*;

class PhotoWrite_Ad {
static String filePathString = "/Users/lianghao/Downloads/資料庫建立/insertpic_all/";
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
              String url = "jdbc:oracle:thin:@35.201.185.79:49161:XE";
              String userid = "system";
              String passwd = "oracle";              
              String[] pics = {"S000000001_1.png", "S000000001_2.jpg", "S000000001_3.jpg", "S000000002_1.jpg", "S000000002_2.jpg","S000000003_1.jpg", 
            		  						"S000000003_2.jpg", "S000000004_1.jpg", "S000000005_1.jpg", "S000000006_1.jpg", "S000000007_1.jpg", "S000000007_2.jpg", 
            		  						"S000000008_1.jpg", "S000000009_1.jpg", "S000000010_1.jpg", "S000000011_1.jpg", "S000000012_1.jpg"};                
	        
              try {
                con = DriverManager.getConnection(url, userid, passwd);
                InputStream fin = null;
                // �s�i�Ϥ�
                for(int i = 1; i<7 ; i++){//�Цۦ�p�⦳�X�i�Ϥ��n�]�X��
                	File pic = new File(filePathString+"pic/images_ad",i+".jpg"); //�۹�java�ɪ����|- pic/images_XXX �Ш�table�W�٫إ� �ñN�Ϥ��s�� 1.jpg 2.jpg .......
                                               
	                long flen = pic.length();
	                fin = new FileInputStream(pic);              

									
	                pstmt = con.prepareStatement(
	                 "update ADVERTISEMENT set AD_IMAGE=? where AD_NO='A00000000"+i+"'");//�ק�SQL���O �ثe�Ϥ����̫�s���|��WFK���̫�s�X
	                pstmt.setBinaryStream(1, fin, (int)flen);
	                int rowsUpdated = pstmt.executeUpdate();
				
	                if (1 == rowsUpdated)
	                    System.out.println(" row.");
	                else
	                    System.out.println("updated 'A00000000"+i+"' is fail");   //���~�T��                  	
	                }
	                
	              fin.close();
                	pstmt.close();   
	                
	                //�|���Ϥ�
	                for(int i = 1; i<=7 ; i++){//�Цۦ�p�⦳�X�i�Ϥ��n�]�X��
                	File pic = new File(filePathString+"pic/images_member",i+".jpg"); //�۹�java�ɪ����|- pic/images_XXX �Ш�table�W�٫إ� �ñN�Ϥ��s�� 1.jpg 2.jpg .......
                                               
	                long flen = pic.length();
	                fin = new FileInputStream(pic);              

									
	                pstmt = con.prepareStatement(
	                 "update member set mem_photo=? where MEM_NO='M00000000"+i+"'");//�ק�SQL���O �ثe�Ϥ����̫�s���|��WFK���̫�s�X
	                pstmt.setBinaryStream(1, fin, (int)flen);
	                int rowsUpdated = pstmt.executeUpdate();
				
	                if (1 == rowsUpdated)
	                    System.out.println(" row.");
	                else
	                    System.out.println("updated 'M00000000"+i+"' is fail");                     	
	                }
	                fin.close();
                	pstmt.close();                                     
               
              //���a�Ӥ�
				for(int i =0; i<pics.length; i++) {
                	File pic = new File(filePathString+"pic/images_storePhoto/" + pics[i]); 
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

				//�q��QRcode1 
				                for(int i =1; i<=9; i++) {
                	File pic = new File(filePathString+"pic/images_reservation/R00000000"+i+".png"); 
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
                
                //�q��QRcode2 
				                for(int i =10; i<=15; i++) {
                	File pic = new File(filePathString+"pic/images_reservation/R0000000"+i+".png"); 
                	long flen = pic.length(); 
                	fin = new FileInputStream(pic);
                	pstmt = con.prepareStatement(
                			"UPDATE reservation SET reservation_qrcode=? where reservation_no='R0000000" + i + "'"); 
                	pstmt.setBinaryStream(1, fin,  (int)flen);
                    int rowsUpdated = pstmt.executeUpdate();
    				
	                if (1 == rowsUpdated)
	                    System.out.println(" row.");
	                else
	                    System.out.println("updated 'M00000000"+i+"' is fail");   
                }
                
                fin.close(); 
                pstmt.close(); 
				
				
                //�\�I1 
				          for(int i =1; i<=9; i++) {
                	File pic = new File(filePathString+"pic/images_meal/"+i+".jpg"); 
                	long flen = pic.length(); 
                	fin = new FileInputStream(pic);
                	pstmt = con.prepareStatement(
                			"UPDATE meal SET meal_photo=? where meal_no='ML0000000" + i + "'"); 
                	pstmt.setBinaryStream(1, fin,  (int)flen);
                    int rowsUpdated = pstmt.executeUpdate();
    				
	                if (1 == rowsUpdated)
	                    System.out.println(" row.");
	                else
	                    System.out.println("updated 'M00000000"+i+"' is fail");   
                }
                
                fin.close(); 
                pstmt.close();
                
                //�\�I2 
				          for(int i =10; i<=19; i++) {
                	File pic = new File(filePathString+"pic/images_meal/"+i+".jpg"); 
                	long flen = pic.length(); 
                	fin = new FileInputStream(pic);
                	pstmt = con.prepareStatement(
                			"UPDATE meal SET meal_photo=? where meal_no='ML000000" + i + "'"); 
                	pstmt.setBinaryStream(1, fin,  (int)flen);
                    int rowsUpdated = pstmt.executeUpdate();
    				
	                if (1 == rowsUpdated)
	                    System.out.println(" row.");
	                else
	                    System.out.println("updated 'M00000000"+i+"' is fail");   
                }
                
                fin.close(); 
                pstmt.close();
                
                //�\�I3 
				          for(int i =20; i<=29; i++) {
                	File pic = new File(filePathString+"pic/images_meal/"+i+".jpg"); 
                	long flen = pic.length(); 
                	fin = new FileInputStream(pic);
                	pstmt = con.prepareStatement(
                			"UPDATE meal SET meal_photo=? where meal_no='ML000000" + i + "'"); 
                	pstmt.setBinaryStream(1, fin,  (int)flen);
                    int rowsUpdated = pstmt.executeUpdate();
    				
	                if (1 == rowsUpdated)
	                    System.out.println(" row.");
	                else
	                    System.out.println("updated 'M00000000"+i+"' is fail");   
                }
                
                fin.close(); 
                pstmt.close();
                
                //�\�I4 
				          for(int i =30; i<=39; i++) {
                	File pic = new File(filePathString+"pic/images_meal/"+i+".jpg"); 
                	long flen = pic.length(); 
                	fin = new FileInputStream(pic);
                	pstmt = con.prepareStatement(
                			"UPDATE meal SET meal_photo=? where meal_no='ML000000" + i + "'"); 
                	pstmt.setBinaryStream(1, fin,  (int)flen);
                    int rowsUpdated = pstmt.executeUpdate();
    				
	                if (1 == rowsUpdated)
	                    System.out.println(" row.");
	                else
	                    System.out.println("updated 'M00000000"+i+"' is fail");   
                }
                
                fin.close(); 
                pstmt.close();
                
                //�\�I5 
				          for(int i =40; i<=49; i++) {
                	File pic = new File(filePathString+"pic/images_meal/"+i+".jpg"); 
                	long flen = pic.length(); 
                	fin = new FileInputStream(pic);
                	pstmt = con.prepareStatement(
                			"UPDATE meal SET meal_photo=? where meal_no='ML000000" + i + "'"); 
                	pstmt.setBinaryStream(1, fin,  (int)flen);
                    int rowsUpdated = pstmt.executeUpdate();
    				
	                if (1 == rowsUpdated)
	                    System.out.println(" row.");
	                else
	                    System.out.println("updated 'M00000000"+i+"' is fail");   
                }
                
                fin.close(); 
                pstmt.close();
                
                //�\�I6 
				          for(int i =50; i<=59; i++) {
                	File pic = new File(filePathString+"pic/images_meal/"+i+".jpg"); 
                	long flen = pic.length(); 
                	fin = new FileInputStream(pic);
                	pstmt = con.prepareStatement(
                			"UPDATE meal SET meal_photo=? where meal_no='ML000000" + i + "'"); 
                	pstmt.setBinaryStream(1, fin,  (int)flen);
                    int rowsUpdated = pstmt.executeUpdate();
    				
	                if (1 == rowsUpdated)
	                    System.out.println(" row.");
	                else
	                    System.out.println("updated 'M00000000"+i+"' is fail");   
                }
                
                fin.close(); 
                pstmt.close();
                
                //�\�I7 
				          for(int i =60; i<=67; i++) {
                	File pic = new File(filePathString+"pic/images_meal/"+i+".jpg"); 
                	long flen = pic.length(); 
                	fin = new FileInputStream(pic);
                	pstmt = con.prepareStatement(
                			"UPDATE meal SET meal_photo=? where meal_no='ML000000" + i + "'"); 
                	pstmt.setBinaryStream(1, fin,  (int)flen);
                    int rowsUpdated = pstmt.executeUpdate();
    				
	                if (1 == rowsUpdated)
	                    System.out.println(" row.");
	                else
	                    System.out.println("updated 'M00000000"+i+"' is fail");   
                }
                
                fin.close(); 
                pstmt.close();
                
                //QRCode1 
				          for(int i =1; i<=9; i++) {
                	File pic = new File(filePathString+"pic/images_QRCode/O00000000"+i+".png"); 
                	long flen = pic.length(); 
                	fin = new FileInputStream(pic);
                	pstmt = con.prepareStatement(
                			"UPDATE ord SET ord_qrcode=? where ord_no='O00000000" + i + "'"); 
                	pstmt.setBinaryStream(1, fin,  (int)flen);
                    int rowsUpdated = pstmt.executeUpdate();
    				
	                if (1 == rowsUpdated)
	                    System.out.println(" row.");
	                else
	                    System.out.println("updated 'M00000000"+i+"' is fail");   
                }
                
                fin.close(); 
                pstmt.close();
                
                //QRCode2 
				          for(int i =10; i<=19; i++) {
                	File pic = new File(filePathString+"pic/images_QRCode/O0000000"+i+".png"); 
                	long flen = pic.length(); 
                	fin = new FileInputStream(pic);
                	pstmt = con.prepareStatement(
                			"UPDATE ord SET ord_qrcode=? where ord_no='O0000000" + i + "'"); 
                	pstmt.setBinaryStream(1, fin,  (int)flen);
                    int rowsUpdated = pstmt.executeUpdate();
    				
	                if (1 == rowsUpdated)
	                    System.out.println(" row.");
	                else
	                    System.out.println("updated 'M00000000"+i+"' is fail");   
                }
                
                fin.close(); 
                pstmt.close();

								//QRCode3 
				          for(int i =20; i<=23; i++) {
                	File pic = new File(filePathString+"pic/images_QRCode/O0000000"+i+".png"); 
                	long flen = pic.length(); 
                	fin = new FileInputStream(pic);
                	pstmt = con.prepareStatement(
                			"UPDATE ord SET ord_qrcode=? where ord_no='O0000000" + i + "'"); 
                	pstmt.setBinaryStream(1, fin,  (int)flen);
                    int rowsUpdated = pstmt.executeUpdate();
    				
	                if (1 == rowsUpdated)
	                    System.out.println(" row.");
	                else
	                    System.out.println("updated 'M00000000"+i+"' is fail");   
                }
                
                fin.close(); 
                pstmt.close();
                
                //�޲z�� 
				          for(int i =1; i<=4; i++) {
                	File pic = new File(filePathString+"pic/images_administrator/"+i+".jpg"); 
                	long flen = pic.length(); 
                	fin = new FileInputStream(pic);
                	pstmt = con.prepareStatement(
                			"UPDATE administrator SET adm_photo=? where adm_no='A00000000" + i + "'"); 
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