package com.email.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.File;

@Service
public class EmailSenderService {

    @Autowired
    private JavaMailSender javaMailSender;

    @Value("$spring.mail.username")     // Reading the property username from application.yml file
    private String fromEmail;

    public void sendEmail(String toEmail,
                          String subject,
                          String bodyText) throws MessagingException {
        sendEmail(toEmail, subject, bodyText, null);
    }

    public void sendEmail(String toEmail,
                          String subject,
                          String bodyText,
                          String[] attachments) throws MessagingException {
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, true);

        mimeMessageHelper.setFrom(fromEmail);
        mimeMessageHelper.setTo(toEmail);
        mimeMessageHelper.setSubject(subject);
        mimeMessageHelper.setText(bodyText);

        boolean attachmentFlag = attachments != null && attachments.length > 0;
        if(attachmentFlag){
            for(String attachment : attachments){
                FileSystemResource fileSystemResource = new FileSystemResource(new File(attachment));
                mimeMessageHelper.addAttachment(fileSystemResource.getFilename(), fileSystemResource);
            }
        }

        javaMailSender.send(mimeMessage);
        String resultMsg = (attachmentFlag) ? "with attachment " : "";
        System.out.println("Email "+ resultMsg +"sent successfully");
    }
}