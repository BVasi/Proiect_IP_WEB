package com.project.ip.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.ip.constants.Constants;
import com.project.ip.model.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.socket.messaging.SessionConnectEvent;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

import java.util.HashMap;
import java.util.Map;

@Controller
public class ChatWebSocket
{
    @EventListener
    public void handleSessionConnected(SessionConnectEvent event)
    {
        SimpMessageHeaderAccessor accessor = SimpMessageHeaderAccessor.wrap(event.getMessage());
        Integer userId = (Integer)accessor.getSessionAttributes().get(Constants.Attributes.LOGGED_IN_USER_ID);
        if (userId == null)
        {
            System.out.println(Constants.Logs.WEBSOCKET_CONNECTING_FAILED);
            return;
        }
        System.out.println(Constants.Logs.WEBSOCKET_CONNECTING_SUCCESS + userId);
    }

    @EventListener
    public void handleSessionDisconnected(SessionDisconnectEvent event)
    {
        SimpMessageHeaderAccessor accessor = SimpMessageHeaderAccessor.wrap(event.getMessage());
        Integer userId = (Integer)accessor.getSessionAttributes().get(Constants.Attributes.LOGGED_IN_USER_ID);
        if (userId == null)
        {
            System.out.println(Constants.Logs.WEBSOCKET_DISCONNECTING_FAILED);
            return;
        }
        System.out.println(Constants.Logs.WEBSOCKET_DISCONNECTING_SUCCESS + userId);
    }

    @MessageMapping(Constants.EndPoints.SEND_MESSAGE)
    public void handleMessage(@Payload String messageContent,
                              SimpMessageHeaderAccessor accessor) //to do: refactor
    {
        try
        {
            ObjectMapper objectMapper = new ObjectMapper();
            Map messageMap = objectMapper.readValue(messageContent, Map.class);
            String content = (String)messageMap.get(Constants.Attributes.CONTENT);
            Integer chatId = (Integer)messageMap.get(Constants.Attributes.CHAT_ID);
            Integer userId = (Integer)accessor.getSessionAttributes().get(Constants.Attributes.LOGGED_IN_USER_ID);
            ResponseEntity<String> responseEntity = restTemplate.postForEntity(
                    Constants.RemoteServerAddresses.CLOUD_SERVER +
                            Constants.RemoteServerEndPoints.ADD_MESSAGE,
                    generateRequestBodyForAddMessage(chatId, new Message(userId, content)),
                    String.class
            );
            messagingTemplate.convertAndSend(Constants.EndPoints.NEW_MESSAGE + chatId, generateUiMessage(content, userId));
        } catch (HttpClientErrorException.Conflict error)
        {
            System.out.println(Constants.Logs.ADD_MESSAGE_ERROR);
        } catch (JsonProcessingException e)
        {
            throw new RuntimeException(e);
        }
    }

    private Map<String, Object> generateRequestBodyForAddMessage(final Integer chatId, final Message message)
    {
        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put(Constants.RequestParameters.CHAT_ID, chatId);
        requestBody.put(Constants.RequestParameters.MESSAGE_TO_ADD, message);
        return requestBody;
    }

    private Map<String, Object> generateUiMessage(final String content, final Integer userId)
    {
        Map<String, Object> message = new HashMap<>();
        message.put(Constants.Attributes.CONTENT, content);
        message.put(Constants.Attributes.SENDER_ID, userId);
        return message;
    }

    @Autowired
    private RestTemplate restTemplate;
    @Autowired
    private SimpMessagingTemplate messagingTemplate;
}