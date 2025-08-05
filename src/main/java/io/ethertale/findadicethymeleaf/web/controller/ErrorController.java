package io.ethertale.findadicethymeleaf.web.controller;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class ErrorController implements org.springframework.boot.web.servlet.error.ErrorController {

    @RequestMapping("/error")
    public String handleError(HttpServletRequest request, HttpServletResponse response) {
        Object status = request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE);
        Integer statusCode = Integer.valueOf(status.toString());

        switch (statusCode) {
            case 403, 404:
                response.setStatus(HttpStatus.NOT_FOUND.value());
                return "status-404";
            case 500:
                response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
                return "status-500";
        }
        return "error";
    }
}
