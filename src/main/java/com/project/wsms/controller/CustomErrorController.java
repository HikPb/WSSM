package com.project.wsms.controller;

import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.http.HttpServletRequest;

@Controller
public class CustomErrorController implements ErrorController{
    
    @RequestMapping("/error")
    public String handleError(HttpServletRequest request) {
        final Object status = request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE);
        System.out.println(status);
        if(status != null && Integer.valueOf(status.toString()) == HttpStatus.NOT_FOUND.value()){
            return "error-404";
        }
        if(status!=null && Integer.valueOf(status.toString()) == HttpStatus.UNAUTHORIZED.value()){
            return "error-404";
        }
        return "error";
    }

    @RequestMapping("/error/500")
    public String handleError500() {
        return "error";
    }

    @RequestMapping("/error/401")
    public String handleError401() {
        return "error-404";
    }

    @RequestMapping("/error/404")
    public String handleError404() {
        return "error-404";
    }

}
