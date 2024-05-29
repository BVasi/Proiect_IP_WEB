package com.project.ip.controller;

import com.project.ip.constants.Constants;
import com.project.ip.model.AccessType;
import com.project.ip.model.SensorSettings;
import com.project.ip.model.User;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

@Controller
public class SensorSettingController
{
    @GetMapping(Constants.EndPoints.SENSOR_SETTINGS + "/{userId}")
    public String getSensorSettings(Model model, HttpSession session,
                                    @PathVariable Integer userId)
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

        model.addAttribute(Constants.Attributes.USER_ID, userId);
        model.addAttribute(Constants.Attributes.SENSORS_SETTINGS, getCurrentSensorSettingsFromRemoteServer(userId));
        return Constants.EndPoints.SENSOR_SETTINGS;
    }

    @PostMapping(Constants.EndPoints.SENSOR_SETTINGS + "/{userId}")
    public String postSensorSettings(@ModelAttribute(Constants.Attributes.SENSORS_SETTINGS)
                                         SensorSettings newSensorsSettings, Model model,
                                     HttpSession session, @PathVariable Integer userId)
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
        if (!updateSensorSettingsOnRemoteServer(userId, newSensorsSettings))
        {
            model.addAttribute(Constants.Attributes.POP_UP_MESSAGE, Constants.ErrorMessages.UPDATE_SENSOR_SETTINGS_ERROR);
            return Constants.EndPoints.SENSOR_SETTINGS;
        }

        model.addAttribute(Constants.Attributes.POP_UP_MESSAGE, Constants.Messages.SENSOR_SETTINGS_UPDATE_SUCCESS);
        return Constants.EndPoints.SENSOR_SETTINGS;
    }

    private SensorSettings getCurrentSensorSettingsFromRemoteServer(final Integer userId)
    {
        try
        {
            return restTemplate.postForObject(Constants.RemoteServerAddresses.CLOUD_SERVER +
                            Constants.RemoteServerEndPoints.GET_SENSORS_SETTINGS,
                    Map.of(Constants.RequestParameters.ID, userId),
                    SensorSettings.class);
        } catch (HttpClientErrorException.NotFound exception)
        {
            return null;
        }
    }

    private Boolean updateSensorSettingsOnRemoteServer(final Integer userId, final SensorSettings newSensorsSettings)
    {
        try
        {
            ResponseEntity<Void> response = restTemplate.postForEntity(Constants.RemoteServerAddresses.CLOUD_SERVER +
                            Constants.RemoteServerEndPoints.UPDATE_SENSORS_SETTINGS,
                    generateRequestBodyForUpdateSensorSettings(userId, newSensorsSettings),
                    Void.class);
            return true;
        } catch (HttpClientErrorException.NotFound exception)
        {
            return false;
        }
    }

    private Map<String, Object> generateRequestBodyForUpdateSensorSettings(final Integer userId, final SensorSettings newSensorsSettings)
    {
        Map<String, Object> request = new HashMap<>();
        request.put(Constants.RequestParameters.ID, userId);
        request.put(Constants.RequestParameters.NEW_SENSOR_SETTINGS, newSensorsSettings);
        return request;
    }

    @Autowired
    private RestTemplate restTemplate;
}