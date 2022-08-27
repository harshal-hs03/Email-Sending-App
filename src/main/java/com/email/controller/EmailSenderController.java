package com.email.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class EmailSenderController {

    @ResponseBody
    @GetMapping("/test")
    public String test(){
        return "Response body is working fine";
    }

}
