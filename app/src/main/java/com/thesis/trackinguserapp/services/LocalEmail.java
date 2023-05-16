package com.thesis.trackinguserapp.services;

import android.content.Context;
import android.util.Log;

import com.thesis.trackinguserapp.common.Constants;

import java.util.Properties;

import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class LocalEmail {
    Context mContext;

    public LocalEmail(Context mContext) {
        this.mContext = mContext;
    }


    public void sendEmail(String emailTo, String subject, String msg) {
        try {
            Properties properties = System.getProperties();
            properties.put("mail.smtp.host", Constants.emailHost);
            properties.put("mail.smtp.socketFactory.port", Constants.emailPort);
            properties.put("mail.smtp.socketFactory.class", Constants.emailClass);
            properties.put("mail.smtp.auth", Constants.emailAuth);


            Session mailSession = Session.getInstance(properties, new Authenticator() {
                @Override
                protected PasswordAuthentication getPasswordAuthentication() {
                    return new PasswordAuthentication(Constants.emailAdd, Constants.emailPass);
                }
            });

            MimeMessage message = new MimeMessage(mailSession);

            message.addRecipient(Message.RecipientType.TO, new InternetAddress(emailTo));
            message.setSubject(subject);
            message.setText(msg);

            Thread thread = new Thread(() -> {
                try {
                    Transport.send(message);
                } catch (MessagingException e) {
                    Log.e("MessagingException >>", e.getLocalizedMessage());
                }
            });
//
            thread.start();


        } catch (AddressException e) {
            Log.e("AddressException >>", e.getLocalizedMessage());
        } catch (MessagingException e) {
            Log.e("MessagingException >>", e.getLocalizedMessage());
        }
    }
}
