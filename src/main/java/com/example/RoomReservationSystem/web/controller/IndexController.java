package com.example.RoomReservationSystem.web.controller;

import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import java.util.Locale;

@Controller
public class IndexController {

    @GetMapping("/")
    public String indexPage(HttpServletRequest request){

        Locale currentLocale = request.getLocale();
        String countryCode = currentLocale.getCountry();
        String CountryName = currentLocale.getDisplayName();

        String langCode = currentLocale.getLanguage();
        String langName = currentLocale.getDisplayLanguage();
        
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || authentication instanceof AnonymousAuthenticationToken) {
            return "login";
        }
        return "overview_calendar";
    }

    @GetMapping("/login")
    public String login() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || authentication instanceof AnonymousAuthenticationToken) {
            return "login";
        }
        return "redirect:/";
    }

    @GetMapping("/logout")
    public String logout() {
        return "login";
    }

}
