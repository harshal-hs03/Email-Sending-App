package com.email.controller;

import com.email.service.EmailSenderService;
import com.email.util.AttributeNames;
import com.email.util.Mappings;
import com.email.util.ViewNames;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.mail.MessagingException;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Map;

@Controller
public class EmailSenderController {

    // == fields ==
    private final EmailSenderService emailSenderService;

    private static final String UPLOAD_DIR_RELATIVE = "/src/main/resources/uploads";
    private static final String UPLOAD_DIRECTORY = System.getProperty("user.dir") + UPLOAD_DIR_RELATIVE;

    // == constructor ==
    @Autowired
    public EmailSenderController(EmailSenderService emailSenderService) {
        this.emailSenderService = emailSenderService;
    }

    // == request methods ==
    @ResponseBody
    @GetMapping("/test")
    public String test(){
        return "Response body is working fine";
    }

    @GetMapping(Mappings.HOME)
    public String home(){
        return ViewNames.HOME;
    }

    @GetMapping(Mappings.COMPOSE_EMAIL)
    public String composeEmail(Model model, OAuth2AuthenticationToken oAuth2AuthenticationToken) {
        Map<String, Object> attributes = oAuth2AuthenticationToken.getPrincipal().getAttributes();
        System.out.println(attributes.get("email").toString());
        model.addAttribute(AttributeNames.FROM_EMAIL, emailSenderService.getFromEmail());
        model.addAttribute(AttributeNames.SIZE_OF_UPLOADED_DOCS, this.emailSenderService.getSizeOfUploadedAttachments());
        return ViewNames.COMPOSE_EMAIL;
    }

    @PostMapping(Mappings.COMPOSE_EMAIL)
    public String sendEmailMsg(@RequestParam String toEmail,
                               @RequestParam String subject,
                               @RequestParam String emailBody) {
        System.out.println("Objects returned from front end = toEmail = "+toEmail+
                            " subject = "+subject+
                            " emailBody = "+emailBody);

        File directory = new File(UPLOAD_DIRECTORY);
        String[] files = directory.list();

        // Adding the attachment list uploaded in the folder "src/main/resources/uploads"
        String[] attachmentArr = (files != null) ? Arrays.stream(files).map(s -> UPLOAD_DIRECTORY + "/" + s).toArray(String[]::new) : null;

        try {
            this.emailSenderService.sendEmail(toEmail, subject, emailBody, attachmentArr);
        } catch (MessagingException e) {
            e.printStackTrace();
        } finally {
            this.emailSenderService.clearAttachments();
            this.emailSenderService.setAttachmentsEmptyFlag(true);
        }

        return Mappings.REDIRECT_COMPOSE_EMAIL;
    }

    @GetMapping(Mappings.UPLOAD_FILES) public String displayUploadForm(Model model) {
        model.addAttribute(AttributeNames.LIST_OF_UPLOADED_DOCS, this.emailSenderService.getNamesOfUploadedFiles());
        model.addAttribute(AttributeNames.NO_DOC_PRESENT_FLAG, this.emailSenderService.getAttachmentsEmptyFlag());
        return ViewNames.UPLOAD_FILES;
    }

    @PostMapping(Mappings.UPLOAD_ATTACHMENTS)
    public String uploadFile(Model model, @RequestParam("file") MultipartFile file) throws IOException {
        if (file.isEmpty())
            model.addAttribute(AttributeNames.NO_FILE_MSG, "Please choose a file to upload!");
        else {
            StringBuilder fileNames = new StringBuilder();
            Path fileNameAndPath = Paths.get(UPLOAD_DIRECTORY, file.getOriginalFilename());
            fileNames.append(file.getOriginalFilename());
            Files.write(fileNameAndPath, file.getBytes());
            this.emailSenderService.setAttachmentsEmptyFlag(false);
        }
        model.addAttribute(AttributeNames.LIST_OF_UPLOADED_DOCS, this.emailSenderService.getNamesOfUploadedFiles());
        model.addAttribute(AttributeNames.NO_DOC_PRESENT_FLAG, this.emailSenderService.getAttachmentsEmptyFlag());
        return ViewNames.UPLOAD_FILES;
    }

    @GetMapping(Mappings.CLEAR_ATTACHMENTS) public String clearAttachments() {
        this.emailSenderService.clearAttachments();
        this.emailSenderService.setAttachmentsEmptyFlag(true);
        return Mappings.REDIRECT_UPLOAD_FILES;
    }
}
