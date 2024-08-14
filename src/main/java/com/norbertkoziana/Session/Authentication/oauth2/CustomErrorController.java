package com.norbertkoziana.Session.Authentication.oauth2;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
@RestController
public class CustomErrorController implements ErrorController {

    @GetMapping("/oauth2/error")
    public String handleError(HttpServletRequest request) {
        String message = (String) request.getSession().getAttribute("error.message");
        if (message == null) {
            message = "Unknown error";
        }
        request.getSession().removeAttribute("error.message");
        return message;
    }

}

