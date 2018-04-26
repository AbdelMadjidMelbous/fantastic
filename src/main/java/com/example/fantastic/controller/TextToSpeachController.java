package com.example.fantastic.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class TextToSpeachController {
    @RequestMapping(value = "/textToSpeach", method = RequestMethod.GET)
    public String converter() {
        return "Enfant/textToSpeach";
    }
    }
