package com.tools;
import java.io.*;

public class Send {

  public void sendMessage(String[] tel , String message)  {

  try {
      String server  = "203.66.172.131"; //Socket to Air Gateway IP
      int port	     = 8000;            //Socket to Air Gateway Port

      String user    = "85559671"; //�b��
      String passwd  = "2irioiai"; //�K�X
      String messageBig5 = new String(message.getBytes(),"big5"); //²�T���e

      //----�إ߳s�u and �ˬd�b���K�X�O�_���~
      sock2air mysms = new sock2air();
      int ret_code = mysms.create_conn(server,port,user,passwd) ;
      if( ret_code == 0 ) {
           System.out.println("�b���K�XLogin OK!");
      } else {
      	   System.out.println("�b���K�XLogin Fail!");
           System.out.println("ret_code="+ret_code + ",ret_content=" + mysms.get_message());
           //�����s�u
           mysms.close_conn();
           return ;
      }

      //�ǰe��r²�T
      //�p�ݦP�ɶǰe�h��²�T�A�Цh���I�ssend_message()�Y�i�C
    for(int i=0 ; i<tel.length ; i++){  
      ret_code=mysms.send_message(tel[i],messageBig5);
      if( ret_code == 0 ) {
           System.out.println("²�T�w�e��²�T����!");
           System.out.println("MessageID="+mysms.get_message()); //���oMessageID
      } else {
           System.out.println("²�T�ǰe�o�Ϳ��~!");
           System.out.print("ret_code="+ret_code+",");
           System.out.println("ret_content="+mysms.get_message());//���o���~���T��
           //�����s�u
           mysms.close_conn();
           return ;
      }
    }

      //�����s�u
      mysms.close_conn();

  }catch (Exception e)  {

      System.out.println("I/O Exception : " + e);
   }
 }

 public static void main(String[] args) {
 	Send se = new Send();
 	String[] tel ={"0966666666","0977777777","0988888888"};
 	String message = "�Ƶ{�T������";
 	se.sendMessage(tel , message);
 }	

}//end of class
