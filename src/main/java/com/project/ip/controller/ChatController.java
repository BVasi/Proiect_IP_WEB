package com.project.ip.controller;

import com.project.ip.constants.Constants;
import com.project.ip.model.Chat;
import com.project.ip.model.User;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@Controller
public class ChatController
{
    @GetMapping(Constants.EndPoints.CHAT + "/{chatId}")
    public String getChat(@PathVariable Integer chatId, Model model, HttpSession session)
    {
        User user = (User)session.getAttribute(Constants.Attributes.LOGGED_IN_USER);
        if (user == null)
        {
            return Constants.Redirects.LOGIN;
        }
        model.addAttribute(Constants.Attributes.LOGGED_IN_USER_ID, user.getId());
        Chat chat = getChatFromRemoteServer(user.getId(), chatId);
        if (chat == null)
        {
            model.addAttribute(Constants.Attributes.POP_UP_MESSAGE, Constants.ErrorMessages.RETRIEVING_CHAT_ERROR);
            return Constants.Redirects.YOUR_CHATS;
        }

        model.addAttribute(Constants.Attributes.CHAT, chat);
        model.addAttribute(Constants.Attributes.CHAT_ID, chatId);
        model.addAttribute(Constants.Attributes.LOGGED_IN_USER_TYPE, user.getAccessType());
        return Constants.EndPoints.CHAT;
    }

    @PostMapping(Constants.EndPoints.CHAT)
    public String postChat(Model model, HttpSession session,
                           @RequestParam(Constants.RequestParameters.ID) Integer otherUserId)
    {
        User user = (User)session.getAttribute(Constants.Attributes.LOGGED_IN_USER);
        if (user == null)
        {
            return Constants.Redirects.LOGIN;
        }
        final Integer newChatId = addChatToRemoteServer(user.getId(), otherUserId);
        if (newChatId == null)
        {
            model.addAttribute(Constants.Attributes.POP_UP_MESSAGE, Constants.ErrorMessages.CANNOT_CREATE_CHAT_ERROR);
            return Constants.Redirects.YOUR_CHATS;
        }

        return Constants.Redirects.CHAT + "/" + newChatId;
    }

    private Integer addChatToRemoteServer(final Integer thisUserId, final Integer otherUserId)
    {
        try
        {
            ResponseEntity<Map<String, Integer>> responseEntity = restTemplate.exchange(
                    Constants.RemoteServerAddresses.CLOUD_SERVER + Constants.RemoteServerEndPoints.ADD_CHAT,
                    HttpMethod.POST,
                    new HttpEntity<>(generateRequestBodyForAddChat(thisUserId, otherUserId)),
                    new ParameterizedTypeReference<>() {});

            return Objects.requireNonNull(responseEntity.getBody()).get(Constants.RequestParameters.ID);
        } catch (HttpClientErrorException error)
        {
            return null;
        }
    }

    private Chat getChatFromRemoteServer(final Integer userId, final Integer chatId)
    {
        try
        {
            return restTemplate.postForObject(Constants.RemoteServerAddresses.CLOUD_SERVER +
                            Constants.RemoteServerEndPoints.GET_CHAT,
                    generateRequestBodyForChat(userId, chatId),
                    Chat.class);
        } catch (HttpClientErrorException.NotFound exception)
        {
            return null;
        }
    }

    private Map<String, Integer> generateRequestBodyForChat(final Integer userId, final Integer chatId)
    {
        Map<String, Integer> requestBody = new HashMap<>();
        requestBody.put(Constants.RequestParameters.ID, userId);
        requestBody.put(Constants.RequestParameters.CHAT_ID, chatId);
        return requestBody;
    }

    private Map<String, Integer> generateRequestBodyForAddChat(final Integer thisUserId, final Integer otherUserId)
    {
        Map<String, Integer> requestBody = new HashMap<>();
        requestBody.put(Constants.RequestParameters.ID, thisUserId);
        requestBody.put(Constants.RequestParameters.OTHER_USER_ID, otherUserId);
        return requestBody;
    }

    @Autowired
    private RestTemplate restTemplate;
}