package com.project.ip.controller;

import com.project.ip.constants.Constants;
import com.project.ip.model.Allergy;
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

import java.util.HashMap;
import java.util.Map;

@Controller
public class AddAllergyController
{
    @GetMapping(Constants.EndPoints.ADD_ALLERGY)
    public String getAddAllergy(Model model, HttpSession session)
    {
        User user = (User)session.getAttribute(Constants.Attributes.LOGGED_IN_USER);
        if (user == null)
        {
            return Constants.Redirects.LOGIN;
        }

        model.addAttribute(Constants.Attributes.ALLERGY, new Allergy());
        return Constants.EndPoints.ADD_ALLERGY;
    }

    @PostMapping(Constants.EndPoints.ADD_ALLERGY)
    public String postAddStrategy(Allergy allergy, Model model, HttpSession session)
    {
        try
        {
            User user = (User)session.getAttribute(Constants.Attributes.LOGGED_IN_USER);
            if (user == null)
            {
                return Constants.Redirects.LOGIN;
            }
            ResponseEntity<Void> response = restTemplate.postForEntity(Constants.RemoteServerAddresses.CLOUD_SERVER +
                            Constants.RemoteServerEndPoints.ADD_ALLERGY,
                    generateRequestBodyForAddingAlarm(user.getId(), allergy), Void.class);
            return Constants.Redirects.HOME;
        } catch (HttpClientErrorException.Conflict error)
        {
            model.addAttribute(Constants.Attributes.POP_UP_MESSAGE, Constants.ErrorMessages.ADD_ALLERGY_ERROR);
            return Constants.EndPoints.ADD_ALLERGY;
        }
    }

    private Map<String, Object> generateRequestBodyForAddingAlarm(final Integer userId, final Allergy allergy)
    {
        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put(Constants.RequestParameters.ID, userId);
        requestBody.put(Constants.RequestParameters.ALLERGY, allergy);
        return requestBody;
    }

    @Autowired
    private RestTemplate restTemplate;
}