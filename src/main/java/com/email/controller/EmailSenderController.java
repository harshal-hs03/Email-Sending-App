package com.email.controller;

import com.email.service.EmailSenderService;
import com.email.util.Mappings;
import com.email.util.ViewNames;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.mail.MessagingException;

@Controller
public class EmailSenderController {

    // == fields ==
    private final EmailSenderService emailSenderService;

    // == constructor ==
    @Autowired
    public EmailSenderController(EmailSenderService emailSenderService) {
        this.emailSenderService = emailSenderService;
    }

    @ResponseBody
    @GetMapping("/test")
    public String test(){
        return "Response body is working fine";
    }

    // == request methods ==
    @GetMapping(Mappings.COMPOSE_EMAIL)
    public String composeEmail(Model model) {
//        model.addAttribute(AttributeNames.MAIN_MESSAGE, gameService.getMainMessage());
//        model.addAttribute(AttributeNames.RESULT_MESSAGE, gameService.getResultMessage());
//        log.info("model= {}", model);
//
//        if(gameService.isGameOver()) {
//            return ViewNames.GAME_OVER;
//        }

        return ViewNames.COMPOSE_EMAIL;
    }

    @PostMapping(Mappings.COMPOSE_EMAIL)
    public String sendEmailMsg(@RequestParam String toEmail,
                               @RequestParam String subject,
                               @RequestParam String emailBody) {
        System.out.println("toEmail object returned from front end = "+toEmail);
        System.out.println("subject object returned from front end = "+subject);
        System.out.println("emailBody object returned from front end = "+emailBody);

        try {
            this.emailSenderService.sendEmail(toEmail, subject, emailBody);
        } catch (MessagingException e) {
            e.printStackTrace();
        }

        return Mappings.REDIRECT_COMPOSE_EMAIL;
    }


}
