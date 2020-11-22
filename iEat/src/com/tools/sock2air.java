package com.tools;
import java.io.*;
import java.net.*;
import java.lang.String.*;

public class sock2air {

private Socket sock ;
private DataInputStream  din ;
private DataOutputStream dout ;
private String ret_message = "" ;

  public sock2air() {} ;

  //�إ�Socket�s�u�A�ð��b���K�X�ˬd
  public int create_conn(String host, int port, String user, String passwd) {

    //---�]�w�e�X�T���T����buffer
    byte out_buffer[] = new byte[258]; //�ǰe���׬�258

    //---�]�w�����T����buffer
    byte ret_code = 99;
    byte ret_content[] = new byte[128];

     try {
         //---�� socket
         this.sock = new Socket(host , port);

         this.din  = new DataInputStream(this.sock.getInputStream());
         this.dout = new DataOutputStream(this.sock.getOutputStream());

        //---�}�l�b���K�X�ˬd
        int i;
        //----�M�� buffer
        for( i=0 ; i < 258 ; i++ ) out_buffer[i] = 0 ;
        for( i=0 ; i < 128 ; i++ ) ret_content[i] = 0 ;

        //---�]�w�b���P�K�X
        String acc_pwd_str = user.trim() + "\0" + passwd.trim() + "\0" ;
        byte   acc_pwd_byte[] = acc_pwd_str.getBytes();
        byte   acc_pwd_size = (byte)acc_pwd_byte.length ;
 
        out_buffer[0] = 0; //interface_type=0
        out_buffer[1] = 0; //msg_type=0,�ˬd�K�X
        out_buffer[2] = acc_pwd_size ; // msg_content_len
	//�]�wmsg_set ���e "�b��"+"�K�X"
        for( i = 0; i < acc_pwd_size ; i++ )
              out_buffer[i + 3] = acc_pwd_byte[i] ;
        
        //----�e�X�T��
        //this.dout.write(out_buffer , 0 , acc_pwd_size + 3 );
        this.dout.write(out_buffer );

        //---Ū return code
        ret_code = this.din.readByte();
        
        //---Ū return message
        this.din.read(ret_content,0,128);
        this.ret_message = new String(ret_content);
        return ret_code ;

     } catch( UnknownHostException e) {
          this.ret_message = "Cannot find the host!";
          return 70 ;
     } catch( IOException ex) {
          this.ret_message = "Socket Error: " + ex.getMessage();
          return 71 ;
     }

  }//end of function

  //����Socket�s�u
  public void close_conn() {
     try {
         if( this.din  != null) this.din.close();
         if( this.dout != null) this.dout.close();
         if( this.sock != null) this.sock.close();

         this.din = null ;
         this.dout = null;
         this.sock = null ;

     } catch( UnknownHostException e) {
          this.ret_message = "Cannot find the host!";
     } catch( IOException ex) {
          this.ret_message = "Socket Error: " + ex.getMessage();
     }

  }//end of function


  //�ǰe��r²�T (�Y�ɶǰe)
  public int send_message( String sms_tel, String message) {

    //---�]�w�e�X�T���T����buffer
    byte out_buffer[] = new byte[258]; //�ǰe���׬�258

    //----�]�w������buffer
    byte ret_code = 99;
    byte ret_content[] = new byte[128];

    if(message.length() > 160){
       this.ret_message = "msg_content > max limit!";
       return 80 ;
    }

    try {
        int i ;
        //----�M�� buffer
        for( i=0 ; i < 258 ; i++ ) out_buffer[i] = 0 ;
        for( i=0 ; i < 128 ; i++ ) ret_content[i] = 0 ;
      
        //---�]�w�ǰe�T�������e 01:�Y�ɶǰe
        String msg_content = sms_tel.trim() + "\0" + message.trim() + "\0" ;
        byte msg_content_byte[] = msg_content.getBytes("Big5"); //�ݫ��w��X��Big5�A���M�|�L�X??
        int msg_content_size = msg_content_byte.length;

        //---�]�w�e�X�T���� buffer
        out_buffer[0] = 0; //interface_type default=0
        out_buffer[1] = 1; //msg_type=1,�ǰe��r�T��
        out_buffer[2] = (byte)(msg_content_size+2); //(msg_content_byte length + send_type length + '\0' length)
	//�]�wmsg_content ���e "�T�����e"
        for( i = 0; i < msg_content_size ; i++ )
              out_buffer[i+3] = msg_content_byte[i] ;

        //---�]�w�Y�ɶǰe send_type = 100
        out_buffer[msg_content_size + 3 ] = 100;
        out_buffer[msg_content_size + 4 ] = 0 ; // ='\0'

        //----�e�X�T��
        this.dout.write(out_buffer);

        //---Ū return code
        ret_code = this.din.readByte();
       
        //---Ū return message
        this.din.read(ret_content,0,128);
        this.ret_message = new String(ret_content);
        this.ret_message = this.ret_message.trim();
        return ret_code ;

    } catch( UnknownHostException eu) {
         this.ret_message = "Cannot find the host!";
         return 70 ;
    } catch( IOException ex) {
         this.ret_message = " Socket Error: " + ex.getMessage();
         return 71 ;
    }
  }//end of function


  //�ǰe��r²�T (�Y�ɶǰe)
  public int send_message( String sms_tel, String message, String order_time) {

    //---�]�w�e�X�T���T����buffer
    byte out_buffer[] = new byte[258]; //�ǰe���׬�258

    //----�]�w������buffer
    byte ret_code = 99;
    byte ret_content[] = new byte[128];

    if(message.length() > 160){
       this.ret_message = "msg_content > max limit!";
       return 80 ;
    }

    try {
        int i ;
        //----�M�� buffer
        for( i=0 ; i < 258 ; i++ ) out_buffer[i] = 0 ;
        for( i=0 ; i < 128 ; i++ ) ret_content[i] = 0 ;
      
        //---�]�w�ǰe�T�������e
        String msg_content = sms_tel.trim() + "\0" + message.trim() + "\0" ;
        byte msg_content_byte[] = msg_content.getBytes("Big5"); //�ݫ��w��X��Big5�A���M�|�L�X??
        int msg_content_size = msg_content_byte.length;

       //---�]�w�w���ǰe�ɶ�
        String send_time = order_time.trim() + "\0";
        byte send_time_byte[] = send_time.getBytes();
        int send_time_size = send_time_byte.length ;

        //---�]�w�e�X�T���� buffer
        out_buffer[0] = 0; //interface_type default=0
        out_buffer[1] = 1; //msg_type=1,�ǰe��r�T��
        out_buffer[2] = (byte)(msg_content_size+2+send_time_size); //(msg_content_byte length + send_type length + '\0' length)
	//�]�wmsg_content ���e "�T�����e"
        for( i = 0; i < msg_content_size ; i++ )
           out_buffer[i+3] = msg_content_byte[i] ;

        //---�]�w�Y�ɶǰe send_type = 100
        out_buffer[msg_content_size + 3 ] = 101;
        out_buffer[msg_content_size + 4 ] = 0 ; // ='\0'

        //---�]�w�����ɶ�
        for( i = 0; i < send_time_size ; i++ )
           out_buffer[msg_content_size + 5 + i ] = send_time_byte[i] ;

        //----�e�X�T��
        this.dout.write(out_buffer);

        //---Ū return code
        ret_code = this.din.readByte();
       
        //---Ū return message
        this.din.read(ret_content,0,128);
        this.ret_message = new String(ret_content);
        this.ret_message = this.ret_message.trim();
        return ret_code ;

    } catch( UnknownHostException eu) {
         this.ret_message = "Cannot find the host!";
         return 70 ;
    } catch( IOException ex) {
         this.ret_message = " Socket Error: " + ex.getMessage();
         return 71 ;
    }
  }//end of function


  //�d�ߤ�r²�T���ǰe���G
  public int query_message(String sms_tel, String messageid) {
    //---�]�w�e�X�T���T����buffer
    byte out_buffer[] = new byte[258]; //�ǰe���׬�258

    //----�]�w������buffer
    byte ret_code = 99;
    byte ret_content[] = new byte[128];

    try {
        int i ;
        //----�M�� buffer
        for( i=0 ; i < 258 ; i++ ) out_buffer[i] = 0 ;
        for( i=0 ; i < 128 ; i++ ) ret_content[i] = 0 ;
      
        //---�]�w�ǰe�T�������e 01:�Y�ɶǰe
        String msg_content = sms_tel.trim() + "\0" + messageid.trim() + "\0" ;
        byte msg_content_byte[] = msg_content.getBytes();
        int msg_content_size = msg_content_byte.length;

        //---�]�w�e�X�T���� buffer
        out_buffer[0] = 0; //interface_type default=0
        out_buffer[1] = 2; //msg_type=2,�d�ߤ�r²�T�ǰe���G
        out_buffer[2] = (byte)(msg_content_size);
	//�]�wmsg_content ���e "�T�����e"
        for( i = 0; i < msg_content_size ; i++ )
              out_buffer[i+3] = msg_content_byte[i] ;

        //----�e�X�T��
        this.dout.write(out_buffer);

        //---Ū return code
        ret_code = this.din.readByte();
       
        //---Ū return message
        this.din.read(ret_content,0,128);
        this.ret_message = new String(ret_content);
        this.ret_message = this.ret_message.trim();
        return ret_code ;

    } catch( UnknownHostException eu) {
         this.ret_message = "Cannot find the host!";
         return 70 ;
    } catch( IOException ex) {
         this.ret_message = " Socket Error: " + ex.getMessage();
         return 71 ;
    }
    
  }//end of function

  public String get_message() {

     return ret_message;
  }


  //�D�禡 - �ϥνd��
  public static void main(String[] args) throws Exception {

  try {
      String server  = "203.66.172.131"; //Socket to Air Gateway IP
      int port	     = 8000;            //Socket to Air Gateway Port

      if(args.length<4){
         System.out.println("Use: java sock2air id passwd tel url message");
         System.out.println(" Ex: java sock2air test test123 0910123xxx HiNet²�T!");
         return;
      }
      String user    = args[0]; //�b��
      String passwd  = args[1]; //�K�X
      String tel     = args[2]; //������X
      String message = new String(args[3].getBytes(),"big5"); //²�T���e

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
      ret_code=mysms.send_message(tel,message);
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

      //�����s�u
      mysms.close_conn();

  }catch (Exception e)  {

      System.out.println("I/O Exception : " + e);
   }
 }

}//end of class
