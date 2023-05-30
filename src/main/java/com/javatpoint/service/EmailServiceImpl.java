/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.javatpoint.service;

import java.util.Properties;
import javax.mail.Address;
import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.Multipart;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.mail.internet.MimeUtility;

/**
 *
 * @author usuario
 */
public class EmailServiceImpl 
        implements 
            EmailService 
{
    
    public void sendMessage( String dest, String subject, String body )
    {
//        Properties props = new Properties();
//        /** Parâmetros de conexão com servidor Gmail */
//        props.put("mail.smtp.host", "smtp.gmail.com");
//        props.put("mail.smtp.socketFactory.port", "465");
//        props.put("mail.smtp.socketFactory.class", 
//        "javax.net.ssl.SSLSocketFactory");
//        props.put("mail.smtp.auth", "true");
//        props.put("mail.smtp.port", "465");
// 
//        Session session = Session.getDefaultInstance(props,
//          new javax.mail.Authenticator() {
//               protected javax.mail.PasswordAuthentication getPasswordAuthentication() 
//               {
//                     return new PasswordAuthentication("chsmscs@gmail.com", 
//                     "Vv14D0000");
//               }
//          });
        Properties props = new Properties();
        /** Parâmetros de conexão com servidor Gmail */
        props.put("mail.smtp.host", "mail.tw4r.com");
        props.put("mail.smtp.socketFactory.port", "465");
        props.put("mail.smtp.socketFactory.class", 
        "javax.net.ssl.SSLSocketFactory");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.port", "465");
 
//        Session session = Session.getDefaultInstance(props,
//          new javax.mail.Authenticator() {
//               protected javax.mail.PasswordAuthentication getPasswordAuthentication() 
//               {
//                     return new PasswordAuthentication("chsmscs@gmail.com", 
//                     "Vv14D0000");
//               }
//          });
        Session session = Session.getDefaultInstance(props,
          new javax.mail.Authenticator() {
               protected javax.mail.PasswordAuthentication getPasswordAuthentication() 
               {
                     return new PasswordAuthentication("contato@tw4r.com", 
                     "twcontato");
               }
          });
 
        /** Ativa Debug para sessão */
        session.setDebug(true);

        try 
        {

          Message message = new MimeMessage(session);
          message.setFrom(new InternetAddress("contato@tw4r.com")); 
          
          Address[] toUser = InternetAddress.parse( dest );  

          message.setRecipients(Message.RecipientType.TO, toUser);
          
         message.setSubject(MimeUtility.encodeText(subject, "UTF-8", "B"));
          
          
         Multipart multipart = new MimeMultipart();
         
         BodyPart part1 = new MimeBodyPart();
         part1.setContent( body, "text/html; charset=UTF-8" );
         
         multipart.addBodyPart(part1);
         
         message.setContent(multipart);
          
//          message.setSubject(new String(subject.getBytes("utf-8"), "utf-8"));
//          message.setText(new String(body.getBytes("utf-8"), "utf-8"));
                    
          //String charset = "ISO-8859-1";
                 
          //subject = MimeUtility.encodeText( subject, charset, null );
          //body = MimeUtility.encodeText( body, charset, null );

//          MimeBodyPart textPart = new MimeBodyPart();
//
//		textPart.setHeader("Content-Type", "text/html; charset=\"iso-8859-1\"");
//		textPart.setContent(mail.getCorpo(), "text/html; charset=iso-8859-1");
//		textPart.setHeader("Content-Transfer-Encoding", "quoted-printable");
          
          //message.setSubject(subject);                  
          //message.setText( body );
          
          //message.setContent( body, "text/html; charset=iso-8859-1");  
          
          //message.setHeader("Content-Type", "text/plain; charset=" + charset);
          //Transport.send(msg);
          
//          message.setSubject( subject );//Assunto
//          message.setText( body );
          
          //message.addHeader("Content-Type", "text/plain; charadd=UTF-8");
          
          
//           MimeBodyPart messageBodyPart = new MimeBodyPart();
//
//        // Fill the message
//        messageBodyPart.setText( body, "UTF-8","html");
//
//        Multipart multipart = new MimeMultipart();
//        multipart.addBodyPart(messageBodyPart);
//
//        // Put parts in message
//        message.setContent(multipart);
          
          
          /**Método para enviar a mensagem criada*/
          Transport.send(message);

        }
        catch( Exception e )
        {
            System.out.println( e.getMessage() );
        }
    }
    
}
