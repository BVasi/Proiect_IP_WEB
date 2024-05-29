package com.project.ip.controller;

import com.project.ip.constants.Constants;
import com.project.ip.model.*;
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
import java.util.List;
import java.util.Map;

@Controller
public class AlarmController
{
    @GetMapping(Constants.EndPoints.ALARMS + "/{userId}")
    public String getAlarms(Model model, HttpSession session,
                            @PathVariable Integer userId)
    {
        User user = (User)session.getAttribute(Constants.Attributes.LOGGED_IN_USER);
        if (user == null)
        {
            return Constants.Redirects.LOGIN;
        }

        model.addAttribute(Constants.Attributes.ALARMS, getAlarmsFromRemoteServer(userId));
        return Constants.EndPoints.ALARMS;
    }

    @PostMapping(Constants.EndPoints.ALARMS)
    public String postAlarms(Model model, HttpSession session,
                             @RequestParam(Constants.RequestParameters.ID) Integer alarmId,
                             @RequestParam(Constants.RequestParameters.IS_RESOLVED) Boolean isResolved)
    {
        User user = (User)session.getAttribute(Constants.Attributes.LOGGED_IN_USER);
        if (user == null)
        {
            return Constants.Redirects.LOGIN;
        }
        if (!updatedAlarmOnRemoteServer(alarmId, isResolved))
        {
            model.addAttribute(Constants.Attributes.POP_UP_MESSAGE, Constants.ErrorMessages.RESOLVING_ALARM_ERROR);
        }

        return Constants.EndPoints.ALARMS;
    }

    private List<Alarm> getAlarmsFromRemoteServer(final Integer userId)
    {
        try
        {
            ResponseEntity<List<Alarm>> response = restTemplate.exchange(
                    Constants.RemoteServerAddresses.CLOUD_SERVER + Constants.RemoteServerEndPoints.GET_ALARMS,
                    HttpMethod.POST,
                    new HttpEntity<>(Map.of(Constants.RequestParameters.ID, userId)),
                    new ParameterizedTypeReference<>() {}
            );
            return response.getBody();
        } catch (HttpClientErrorException.NotFound exception)
        {
            return null;
        }
    }

    private Boolean updatedAlarmOnRemoteServer(final Integer alarmId, final Boolean isResolved)
    {
        try
        {
            Map<String, Object> request = new HashMap<>();
            request.put(Constants.RequestParameters.ID, alarmId);
            request.put(Constants.RequestParameters.IS_RESOLVED, isResolved);
            restTemplate.postForEntity(Constants.RemoteServerAddresses.CLOUD_SERVER +
                            Constants.RemoteServerEndPoints.RESOLVE_ALARM,
                    request, Void.class);
            return true;
        } catch (HttpClientErrorException.NotFound exception)
        {
            return false;
        }
    }

    @Autowired
    private RestTemplate restTemplate;
}
