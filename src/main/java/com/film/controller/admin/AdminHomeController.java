package com.film.controller.admin;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/admin")
public class AdminHomeController {

    @RequestMapping("/index")
    public String getIndex() {
        return "/admin/index";
    }

    @RequestMapping("/home")
    public String getHome() {
        return "/admin/home";
    }

}
