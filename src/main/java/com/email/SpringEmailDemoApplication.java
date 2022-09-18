package com.email;

import com.email.service.EmailSenderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import javax.mail.MessagingException;
import java.util.Random;

@SpringBootApplication
public class SpringEmailDemoApplication {

    @Autowired
    private EmailSenderService emailSenderService;
    private final int randomInt = new Random().nextInt(1000);

    public static void main(String[] args) {
        SpringApplication.run(SpringEmailDemoApplication.class, args);
    }

    // Below methods are just for base testing purpose, and are not triggered on web application

//    @EventListener(ApplicationReadyEvent.class)
    public void sendMailWithoutAttachment(){

        try {
            emailSenderService.sendEmail("harshal.hs03@outlook.com", null, null,
                    "Test Email Subject Number "+randomInt,
                    "Test Email Body text msg Number"+randomInt);
        } catch (MessagingException e) {
            System.out.println("Email not sent due to some internal error");
            e.printStackTrace();
        }
    }

//    @EventListener(ApplicationReadyEvent.class)     // This will trigger the email as soon as the application is ready
    public void sendMailWithAttachment() throws MessagingException {

        String attachmentPathFromLocal = "C:\\Users\\harsh\\Downloads\\sample_pdf.pdf";
        String attachmentFromRepo1 = "src/main/resources/sample_pdf.pdf";
        String attachmentFromRepo2 = "src/main/resources/TheDarkKnight.jpg";

        emailSenderService.sendEmail("harshal.hs03@outlook.com", null, null,
                "Test Email With Attachment Subject Line Number "+randomInt,
                "Test Email With Attachment Body text msg Number "+randomInt,
                new String[]{attachmentFromRepo1, attachmentFromRepo2, attachmentPathFromLocal});
    }
}
