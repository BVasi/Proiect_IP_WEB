package com.project.ip.controller;

import com.project.ip.constants.Constants;
import com.project.ip.model.AccessType;
import com.project.ip.model.User;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

@Controller
public class SignUpController
{
    @GetMapping(Constants.EndPoints.SIGN_UP)
    public String getSignUp(Model model, HttpSession session)
    {
        User user = (User)session.getAttribute(Constants.Attributes.LOGGED_IN_USER);
        if ((user != null) && (user.getAccessType() == AccessType.ADMIN))
        {
            model.addAttribute(Constants.Attributes.IS_ADMIN, true);
        }
        model.addAttribute(Constants.Attributes.USER, new User());
        return Constants.EndPoints.SIGN_UP;
    }

    @PostMapping(Constants.EndPoints.SIGN_UP)
    public String postSignUp(Model model, User user)
    {
        if (user.getAccessType() == null)
        {
            user.setAccessType(AccessType.PACIENT);
        }
        try
        {
            ResponseEntity<Void> response = restTemplate.postForEntity(Constants.RemoteServerAddresses.CLOUD_SERVER +
                            Constants.RemoteServerEndPoints.ADD_USER,
                    user, Void.class);
            return Constants.Redirects.LOGIN;
        } catch (HttpClientErrorException.Conflict error)
        {
            model.addAttribute(Constants.Attributes.POP_UP_MESSAGE, Constants.ErrorMessages.SIGN_UP_ERROR);
            return Constants.EndPoints.SIGN_UP;
        }
    }

    @Autowired
    private RestTemplate restTemplate;
}