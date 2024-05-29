package com.project.ip.controller;

import com.project.ip.constants.Constants;
import com.project.ip.model.AccessType;
import com.project.ip.model.User;
import com.project.ip.model.UserContext;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Map;
import java.util.Objects;

@Controller
public class StuffMenuController
{
    @GetMapping(Constants.EndPoints.STUFF_MENU)
    public String getStuffMenu(Model model, HttpSession session)
    {
        User user = (User)session.getAttribute(Constants.Attributes.LOGGED_IN_USER);
        if (user == null)
        {
            return Constants.Redirects.LOGIN;
        }
        if (user.getAccessType() == AccessType.PACIENT)
        {
            model.addAttribute(Constants.Attributes.POP_UP_MESSAGE, Constants.ErrorMessages.NOT_STUFF_ERROR);
            return Constants.Redirects.LOGIN;
        }

        model.addAttribute(Constants.Attributes.PACIENT_LIST, getPacientsFromRemoteServer(user.getId()));
        return Constants.EndPoints.STUFF_MENU;
    }

    private List<UserContext> getPacientsFromRemoteServer(final Integer userId)
    {
        try
        {
            ResponseEntity<List<User>> response = restTemplate.exchange(
                    Constants.RemoteServerAddresses.CLOUD_SERVER + Constants.RemoteServerEndPoints.GET_PACIENTS,
                    HttpMethod.POST,
                    new HttpEntity<>(Map.of(Constants.RequestParameters.ID, userId)),
                    new ParameterizedTypeReference<>() {}
            );
            return Objects.requireNonNull(response.getBody()).
                    stream().map(user -> (UserContext)user).toList();
        } catch (HttpClientErrorException.NotFound exception)
        {
            return null;
        }
    }

    @Autowired
    private RestTemplate restTemplate;
}