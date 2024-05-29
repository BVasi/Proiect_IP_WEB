package com.project.ip.controller;

import com.project.ip.constants.Constants;
import com.project.ip.model.Chat;
import com.project.ip.model.User;
import com.project.ip.model.UserContext;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@Controller
public class YourChatsController
{
    @GetMapping(Constants.EndPoints.YOUR_CHATS)
    public String getYourChats(Model model, HttpSession session)
    {
        User user = (User)session.getAttribute(Constants.Attributes.LOGGED_IN_USER);
        if (user == null)
        {
            return Constants.Redirects.LOGIN;
        }
        model.addAttribute(Constants.Attributes.LOGGED_IN_USER_ID, user.getId());
        model.addAttribute(Constants.Attributes.CHAT_HISTORY, getChatHistoryFromRemoteServer(user.getId()));
        List<UserContext> foundUsers = (List<UserContext>)session.getAttribute(Constants.Attributes.FOUND_USERS);
        Boolean hasDisplayedUsers = (Boolean)session.getAttribute(Constants.Attributes.HAS_DISPLAYED_USERS);
        if (foundUsers != null)
        {
            if (hasDisplayedUsers == null || !hasDisplayedUsers) //could be refactored
            {
                session.setAttribute(Constants.Attributes.HAS_DISPLAYED_USERS, true);
            }
            else
            {
                session.setAttribute(Constants.Attributes.HAS_DISPLAYED_USERS, false);
                model.addAttribute(Constants.Attributes.FOUND_USERS, foundUsers);
                session.removeAttribute(Constants.Attributes.FOUND_USERS);
            }
        }

        return Constants.EndPoints.YOUR_CHATS;
    }

    @PostMapping(Constants.EndPoints.YOUR_CHATS)
    public String postYourChats(HttpSession session, @RequestParam(Constants.RequestParameters.NAME) String searchedUsersName)

    {
        User user = (User)session.getAttribute(Constants.Attributes.LOGGED_IN_USER);
        if (user == null)
        {
            return Constants.Redirects.LOGIN;
        }
        session.setAttribute(Constants.Attributes.FOUND_USERS, getUsersThatMatchNameFromRemoteServer(searchedUsersName));

        return Constants.Redirects.YOUR_CHATS;
    }


    private List<Chat> getChatHistoryFromRemoteServer(final Integer userId)
    {
        try
        {
            ResponseEntity<List<Chat>> response = restTemplate.exchange(Constants.RemoteServerAddresses.CLOUD_SERVER +
                            Constants.RemoteServerEndPoints.GET_CHAT_HISTORY,
                    HttpMethod.POST,
                    new HttpEntity<>(generateRequestBodyForChatHistory(userId)),
                    new ParameterizedTypeReference<>() {}
            );
            return response.getBody();
        } catch (HttpClientErrorException.NotFound exception)
        {
            return null;
        }
    }

    private List<UserContext> getUsersThatMatchNameFromRemoteServer(final String name)
    {
        try
        {
            ResponseEntity<List<User>> response = restTemplate.exchange(
                    Constants.RemoteServerAddresses.CLOUD_SERVER + Constants.RemoteServerEndPoints.SEARCH_USER,
                    HttpMethod.POST,
                    new HttpEntity<>(Map.of(Constants.RequestParameters.NAME, name)),
                    new ParameterizedTypeReference<>() {}
            );
            return Objects.requireNonNull(response.getBody()).
                    stream().map(user -> (UserContext)user).toList();
        } catch (HttpClientErrorException.NotFound exception)
        {
            return null;
        }
    }

    private Map<String, Integer> generateRequestBodyForChatHistory(final Integer userId)
    {
        Map<String, Integer> requestBody = new HashMap<>();
        requestBody.put(Constants.RequestParameters.ID, userId);
        return requestBody;
    }

    @Autowired
    private RestTemplate restTemplate;
}