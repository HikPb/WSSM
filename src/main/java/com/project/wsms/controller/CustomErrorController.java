package com.project.wsms.controller;

import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RequestMapping;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.http.HttpServletRequest;

public class CustomErrorController implements ErrorController{
    
    @RequestMapping("/error")
    public String handleError(HttpServletRequest request) {
        final Object status = request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE);
        if(status != null && Integer.valueOf(status.toString()) == HttpStatus.NOT_FOUND.value()){
            return "error-404";
        }
        return "error";
    }
}
