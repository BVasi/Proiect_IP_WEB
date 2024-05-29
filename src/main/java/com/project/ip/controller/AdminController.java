package com.project.ip.controller;

import com.project.ip.constants.Constants;
import com.project.ip.model.AccessType;
import com.project.ip.model.Alarm;
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
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Map;
import java.util.Objects;

@Controller
public class AdminController
{
    @GetMapping(Constants.EndPoints.ADMIN)
    public String getAdmin(Model model, HttpSession session)
    {
        User user = (User)session.getAttribute(Constants.Attributes.LOGGED_IN_USER);
        if (user == null)
        {
            return Constants.Redirects.LOGIN;
        }
        if (user.getAccessType() != AccessType.ADMIN)
        {
            model.addAttribute(Constants.Attributes.POP_UP_MESSAGE, Constants.ErrorMessages.NOT_STUFF_ERROR);
            return Constants.Redirects.LOGIN;
        }

        model.addAttribute(Constants.Attributes.USER_LIST, getAllUsersFromRemoteServer(user));
        return Constants.EndPoints.ADMIN;
    }

    @PostMapping(Constants.EndPoints.ADMIN)
    public String postAdmin(Model model, HttpSession session,
                            @RequestBody Map<String, String> requestParameters)
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
        if (requestParameters.get(Constants.RequestParameters.ACCESS_TYPE) == null)
        {
            deleteUserFromRemoteServer(Integer.parseInt(requestParameters.get(Constants.RequestParameters.ID)));
            return Constants.Redirects.ADMIN;
        }

        changeUserAccessTypeOnRemoteServer(Integer.parseInt(requestParameters.get(Constants.RequestParameters.ID)),
                AccessType.valueOf(requestParameters.get(Constants.RequestParameters.ACCESS_TYPE)));
        return Constants.Redirects.ADMIN;
    }

    private List<UserContext> getAllUsersFromRemoteServer(final User loggedInUser)
    {
        try
        {
            ResponseEntity<List<User>> response = restTemplate.exchange(
                    Constants.RemoteServerAddresses.CLOUD_SERVER + Constants.RemoteServerEndPoints.GET_ALL_USERS,
                    HttpMethod.POST,
                    new HttpEntity<>(loggedInUser),
                    new ParameterizedTypeReference<>() {}
            );
            return Objects.requireNonNull(response.getBody()).
                    stream().map(user -> (UserContext)user).toList();
        } catch (HttpClientErrorException.Forbidden exception)
        {
            return null;
        }
    }

    private void deleteUserFromRemoteServer(final Integer userId)
    {
        restTemplate.postForEntity(Constants.RemoteServerAddresses.CLOUD_SERVER + Constants.RemoteServerEndPoints.DELETE_USER,
                Map.of(Constants.RequestParameters.ID, userId),
                void.class);
    }

    private void changeUserAccessTypeOnRemoteServer(final Integer userId, final AccessType newAccessType)
    {
        restTemplate.postForEntity(Constants.RemoteServerAddresses.CLOUD_SERVER + Constants.RemoteServerEndPoints.CHANGE_USER_ACCESS_TYPE,
                Map.of(Constants.RequestParameters.ID, userId, Constants.RequestParameters.ACCESS_TYPE, newAccessType),
                void.class);
    }

    @Autowired
    private RestTemplate restTemplate;
}
