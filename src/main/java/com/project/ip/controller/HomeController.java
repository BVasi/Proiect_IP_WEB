package com.project.ip.controller;

import com.project.ip.constants.Constants;
import com.project.ip.model.User;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController
{
    @GetMapping(Constants.EndPoints.HOME)
    public String getHome(Model model, HttpSession session)
    {
        User user = (User)session.getAttribute(Constants.Attributes.LOGGED_IN_USER);
        if (user == null)
        {
            return Constants.Redirects.LOGIN;
        }

        model.addAttribute(Constants.Attributes.USER_ID, user.getId());
        model.addAttribute(Constants.Attributes.LOGGED_IN_USER_TYPE, user.getAccessType());
        return Constants.EndPoints.HOME;
    }
}