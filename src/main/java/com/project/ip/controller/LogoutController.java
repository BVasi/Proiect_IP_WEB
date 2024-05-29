package com.project.ip.controller;

import com.project.ip.constants.Constants;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class LogoutController
{
    @GetMapping(Constants.EndPoints.LOGOUT)
    public String getLogout(HttpSession session)
    {
        session.invalidate();
        return Constants.Redirects.LOGIN;
    }
}