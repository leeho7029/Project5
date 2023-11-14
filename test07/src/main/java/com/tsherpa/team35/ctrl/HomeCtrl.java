package com.tsherpa.team35.ctrl;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeCtrl {

    @GetMapping( "/")
    public String home(Model model){
        return "index";
    }

}