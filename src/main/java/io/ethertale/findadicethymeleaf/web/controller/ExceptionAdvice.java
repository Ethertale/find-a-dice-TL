package io.ethertale.findadicethymeleaf.web.controller;

import io.ethertale.findadicethymeleaf.exceptions.*;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@ControllerAdvice
public class ExceptionAdvice {

    @ExceptionHandler(LoginProfileWrongCredentials.class)
    public String handleLoginProfileWrongCredentials(RedirectAttributes redirectAttributes) {
        redirectAttributes.addFlashAttribute("loginProfileWrongCredentials", "Username or Password is incorrect!");
        return "redirect:/login";
    }
    @ExceptionHandler(RegisterEmailNotValid.class)
    public String handleRegisterEmailNotValid(RedirectAttributes redirectAttributes) {
        redirectAttributes.addFlashAttribute("registerEmailNotValid", "Email is not valid!");
        return "redirect:/register";
    }
    @ExceptionHandler(RegisterPasswordNotInCharRange.class)
    public String handleRegisterPasswordNotInCharRange(RedirectAttributes redirectAttributes) {
        redirectAttributes.addFlashAttribute("registerPasswordNotInCharRange", "Password must be between 8 and 32 characters!");
        return "redirect:/register";
    }
    @ExceptionHandler(RegisterUsernameNotInCharRange.class)
    public String handleRegisterUsernameNotInCharRange(RedirectAttributes redirectAttributes) {
        redirectAttributes.addFlashAttribute("registerUsernameNotInCharRange", "Username must be between 5 and 16 characters!");
        return "redirect:/register";
    }
    @ExceptionHandler(GroupDoesNotExistException.class)
    public String handleGroupDoesNotExist(RedirectAttributes redirectAttributes) {
        redirectAttributes.addFlashAttribute("groupDoesNotExist", "Group does not exist!");
        return "redirect:/groups";
    }
    @ExceptionHandler(GroupHeroAlreadyInGroupException.class)
    public String handleGroupHeroAlreadyInGroup(RedirectAttributes redirectAttributes) {
        redirectAttributes.addFlashAttribute("groupHeroAlreadyInGroup", "Hero already in group!");
        return "redirect:/groups";
    }

}
