package com.project.ip.controller;

import com.project.ip.constants.Constants;
import com.project.ip.model.User;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.client.RestTemplate;

import java.util.Objects;

@Controller
public class LoginController
{
    @GetMapping({Constants.EndPoints.DEFAULT, Constants.EndPoints.LOGIN})
    public String getLogin(Model model, HttpSession session)
    {
        User user = (User)session.getAttribute(Constants.Attributes.LOGGED_IN_USER);
        if (user == null)
        {
            model.addAttribute(Constants.Attributes.USER, new User());
            return Constants.EndPoints.LOGIN;
        }
        return Constants.Redirects.HOME;
    }

    @PostMapping(Constants.EndPoints.LOGIN)
    public String postLogin(@ModelAttribute(Constants.Attributes.USER) User user, Model model, HttpSession session)
    {
        user = restTemplate.postForObject(Constants.RemoteServerAddresses.CLOUD_SERVER +
                        Constants.RemoteServerEndPoints.LOGIN,
                        user, User.class);
        assert user != null;
        if (Objects.equals(user.getId(), Constants.Errors.DUMMY_ID))
        {
            model.addAttribute(Constants.Attributes.POP_UP_MESSAGE, Constants.ErrorMessages.LOGIN_ERROR);
            return Constants.EndPoints.LOGIN;
        }

        session.setAttribute(Constants.Attributes.LOGGED_IN_USER, user);
        return Constants.Redirects.HOME;
    }

    @Autowired
    private RestTemplate restTemplate;
}