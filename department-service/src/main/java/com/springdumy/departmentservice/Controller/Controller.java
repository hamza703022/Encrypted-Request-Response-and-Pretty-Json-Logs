package com.springdumy.departmentservice.Controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/hello")
public class Controller {



    @RequestMapping("/world")
    public String helloWorld() {
        return "Hello World";
    }
}
