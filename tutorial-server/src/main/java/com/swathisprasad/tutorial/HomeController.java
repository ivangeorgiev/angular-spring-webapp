package com.swathisprasad.tutorial;

import org.springframework.stereotype.Controller;
//import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.ui.Model;


@Controller
public class HomeController {
       @GetMapping("/home")
       public String home(Model model) {
            return "forward:/index.html";
       }

       @GetMapping("/")
       public String index() {
            return "forward:/home";
       }

       @GetMapping("/vinod")
       public String vinod() {
            return "forward:/index.html";
    }
   
}