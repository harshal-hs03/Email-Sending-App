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
import java.util.ArrayList;
import java.util.List;

@Service
public class EmailSenderService {

    @Autowired
    private JavaMailSender javaMailSender;

    @Value("${spring.mail.username}")     // Reading the property username from application.yml file
    private String fromEmail;

    private boolean attachmentsEmptyFlag = true;

    private static final String UPLOADED_FILES_DIR = System.getProperty("user.dir") + "/src/main/resources/uploads";

    public String getFromEmail() {
        return fromEmail;
    }

    public void sendEmail(String toEmail,
                          String cc,
                          String bcc,
                          String subject,
                          String bodyText) throws MessagingException {
        sendEmail(toEmail,cc, bcc, subject, bodyText, null);
    }

    public void sendEmail(String toEmail,
                          String cc,
                          String bcc,
                          String subject,
                          String bodyText,
                          String[] attachments) throws MessagingException {
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, true);

        mimeMessageHelper.setFrom(fromEmail);
        mimeMessageHelper.setTo(toEmail);
        mimeMessageHelper.setSubject(subject);
        mimeMessageHelper.setText(bodyText);

        if(cc!=null && cc.length() > 0)
            mimeMessageHelper.setCc(cc);
        if(bcc!=null && bcc.length() > 0)
            mimeMessageHelper.setBcc(bcc);

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

    public void clearAttachments(){
        File f= new File(UPLOADED_FILES_DIR);
        String[] files = f.list();
        if(files != null) {
            for (String file : files) {
                File fileToDelete = new File(UPLOADED_FILES_DIR + "/" + file);
                if (fileToDelete.delete())
                    System.out.println("Successfully deleted " + file);
                else
                    System.out.println("Unable to delete " + file);
            }
        }
    }

    public List<String> getNamesOfUploadedFiles(){
        List<String> listToReturn = new ArrayList<>();
        File f= new File(UPLOADED_FILES_DIR);
        String[] files = f.list();
        if(files != null) {
            for (String file : files) {
                File fileToDelete = new File(UPLOADED_FILES_DIR + "/" + file);
                listToReturn.add(fileToDelete.getName());
            }
        }
        return listToReturn;
    }

    public int getSizeOfUploadedAttachments(){
        File f = new File(UPLOADED_FILES_DIR);
        String[] files = f.list();
        return (files != null) ? files.length : 0;
    }

    public boolean getAttachmentsEmptyFlag() {
        return this.attachmentsEmptyFlag;
    }

    public void setAttachmentsEmptyFlag(boolean attachmentsEmptyFlag) {
        this.attachmentsEmptyFlag = attachmentsEmptyFlag;
    }
}
